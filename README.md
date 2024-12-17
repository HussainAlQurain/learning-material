# SkillHub

SkillHub is a robust online learning platform that empowers authors to create and manage courses, lessons, and sections, while enabling learners to enroll, track their progress, and engage deeply with course content. Built with Spring Boot and leveraging modern technologies, SkillHub aims to deliver a seamless and scalable educational experience.

## Table of Contents
- Features
- Technologies Used
- Architecture
- API Documentation
- Installation
- Usage
- Contributing
- License
- Contact

## Features
### User Management:

- **Registration & Authentication**: Secure user registration and login with role-based access control.
- **Roles**: Support for multiple roles such as Admin, Author, Student, Collaborator, and more.
### Course Management:

- **Create & Manage Courses**: Authors can create, update, and delete courses with rich content including images and videos.
- **Categorization**: Organize courses into categories for better discoverability.
### Lesson & Section Management:

- **Structured Learning**: Each course comprises multiple lessons, which are further divided into sections supporting various content types like text, images, videos, audio, PDFs, and quizzes.
### Progress Tracking:

- **User Progress**: Learners can track their progress through courses, lessons, and sections with statuses like Not Started, In Progress, and Completed.
### Collaborator Roles:

- **Team Management**: Assign different roles to users within a course, such as Collaborators or Reviewers, to facilitate teamwork and content management.
### Comprehensive API Documentation:

- **Swagger Integration**: Interactive API documentation using Swagger (Springdoc-OpenAPI) for easy exploration and testing of endpoints.

## Technologies Used
### Backend:

- Spring Boot
- Spring Data JPA
- Spring Security
- Springdoc-OpenAPI
- Lombok
- Hibernate
### Database:

- PostgreSQL (or any preferred relational database)
### Others:

- Maven (or Gradle)
- Java 21

## Architecture
### SkillHub follows a layered architecture, ensuring separation of concerns and scalability:

- **Controllers**: Handle incoming HTTP requests and delegate processing to services.
- **Services**: Contain business logic and interact with repositories.
- **Repositories**: Interface with the database using Spring Data JPA.
- **Entities**: Represent the data models mapped to database tables.
- **DTOs**: Data Transfer Objects used for API communication, ensuring a clear contract between the backend and frontend.
- **Security**: Implemented using Spring Security for authentication and authorization.

## API Documentation
### SkillHub provides comprehensive API documentation using Swagger UI, allowing developers to explore and test API endpoints interactively.

- **Swagger UI**: http://localhost:8080/swagger-ui.html (Ensure your application is running and replace localhost:8080 with your server address if different)

## Installation
### Prerequisites
- **Java 17 or higher**: Download Java
- **Maven**: Download Maven (or Gradle if preferred)
- **PostgreSQL**: Download PostgreSQL
- **Git**: Download Git

### Steps
1. Clone the Repository:

```bash
git clone https://github.com/yourusername/skillhub.git
cd skillhub
```
2. Create a database (you can use docker with postgres image)
3. Update configurations by editing application.properties file.
   src/main/resources/ to include your database credentials:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/skillhub_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
4. Build & run the Project:
```bash
./mvnw clean install
./mvnw spring-boot:run
```
For Linux you might want to run chmod +x mvnw first.
## Usage

### Authentication
- **Register**: Create a new user account by sending a POST request to /api/auth/register with necessary details.
- **Login**: Authenticate by sending a POST request to /api/auth/login to receive a JWT token.

### API Endpoints
#### Here are some of the key API endpoints available in SkillHub:

### Author Management
- **Register Author**: POST /api/auth/register
- **Login**: POST /api/auth/login
- **Get Author Profile**: GET /api/auth/me
### Course Management
- **Create Course**: POST /api/courses
- **Get Course by ID**: GET /api/courses/{id}
- **Update Course**: PUT /api/courses/{id}
- **Delete Course**: DELETE /api/courses/{id}
- **Get All Courses**: GET /api/courses
### Lesson Management
- **Create Lesson**: POST /api/lessons
- **Get Lesson by ID**: GET /api/lessons/{id}
- **Update Lesson**: PUT /api/lessons/{id}
- **Delete Lesson**: DELETE /api/lessons/{id}
- **Get All Lessons**: GET /api/lessons
### Section Management
- **Create Section**: POST /api/sections
- **Get Section by ID**: GET /api/sections/{id}
- **Update Section**: PUT /api/sections/{id}
- **Delete Section**: DELETE /api/sections/{id}
- **Get All Sections**: GET /api/sections
### Progress Tracking
- **Create Progress Record**: POST /api/progress
- **Get Progress by ID**: GET /api/progress/{id}
- **Update Progress**: PUT /api/progress/{id}
- **Delete Progress**: DELETE /api/progress/{id}
- **Get All Progress Records**: GET /api/progress
### Course Collaborators
- **Add Collaborator to Course**: POST /api/course-users
- **Get CourseUser by ID**: GET /api/course-users/{id}
- **Update CourseUser**: PUT /api/course-users/{id}
- **Delete CourseUser**: DELETE /api/course-users/{id}
- **Get All CourseUsers**: GET /api/course-users


### Example Requests
#### Register a New Author
```JSON
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "phone": "0987654321",
  "password": "securePassword123",
  "role": "ROLE_AUTHOR"
}
```

## Contact
### For any inquiries, please contact:
- **Name**: Hussain AlQurain
- **Email**: hussain.qurain@outlook.com

