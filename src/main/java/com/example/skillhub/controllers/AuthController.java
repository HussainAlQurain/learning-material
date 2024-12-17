package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.CurrentUser;
import com.example.skillhub.domain.dto.AuthResponse;
import com.example.skillhub.domain.dto.AuthorProfileResponse;
import com.example.skillhub.domain.dto.LoginRequest;
import com.example.skillhub.domain.dto.RegisterRequest;
import com.example.skillhub.services.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthorService authorService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthorService authorService,
                          AuthenticationManager authenticationManager,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authorService = authorService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Register a new user", description = "Registers a new author with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email is already in use",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Register request containing user details", required = true) RegisterRequest registerRequest) {
        if (authorService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("Email is already in use"));
        }

        Author author = Author.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole()) // Ensure role is prefixed with "ROLE_"
                .status("active")
                .rating(null) // Set default rating if needed
                .createdAt(new java.util.Date())
                .build();

        authorService.createAuthor(author);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("User registered successfully"));
    }

    @Operation(summary = "Login user", description = "Authenticates a user with email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login request containing email and password", required = true) LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Session is automatically created
            return ResponseEntity.ok(new AuthResponse("Login successful"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid email or password"));
        }
    }

    @Operation(summary = "Logout user", description = "Logs out the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
    })
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logoutUser(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        // Invalidate session if needed
        request.getSession().invalidate();
        return ResponseEntity.ok(new AuthResponse("Logout successful"));
    }

    @Operation(summary = "Get current user profile", description = "Retrieves the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<AuthorProfileResponse> getCurrentAuthor(
            @Parameter(hidden = true) @CurrentUser Author author) {
        if (author == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthorProfileResponse profile = new AuthorProfileResponse(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getPhone(),
                author.getRole(),
                author.getStatus(),
                author.getRating(),
                author.getCreatedAt()
        );
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Update current user profile", description = "Updates the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found after update",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PutMapping("/me")
    public ResponseEntity<AuthorProfileResponse> updateCurrentAuthor(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update details for the author", required = true) Author updateDetails,
            @Parameter(hidden = true) @CurrentUser Author author) {
        if (author == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        authorService.partialUpdate(author.getId(), updateDetails);

        // Fetch updated author
        Author updatedAuthor = authorService.getAuthorById(author.getId())
                .orElseThrow(() -> new com.example.skillhub.exceptions.ResourceNotFoundException("Author not found after update"));

        AuthorProfileResponse profile = new AuthorProfileResponse(
                updatedAuthor.getId(),
                updatedAuthor.getName(),
                updatedAuthor.getEmail(),
                updatedAuthor.getPhone(),
                updatedAuthor.getRole(),
                updatedAuthor.getStatus(),
                updatedAuthor.getRating(),
                updatedAuthor.getCreatedAt()
        );
        return ResponseEntity.ok(profile);
    }

}
