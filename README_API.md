# Employee Management System - Spring Boot REST API

A comprehensive Spring Boot REST API for managing employee information with full CRUD operations, H2 database integration, CORS support for Angular frontend, comprehensive logging, and unit tests.

## Features

- ✅ **Employee CRUD Operations**: Create, Read, Update, Delete employees
- ✅ **H2 Database**: In-memory database for development and testing
- ✅ **RESTful API**: Complete REST endpoints with proper HTTP status codes
- ✅ **CORS Configuration**: Enabled for Angular frontend (localhost:4200, localhost:3000)
- ✅ **Logging**: SLF4J with Logback for comprehensive logging
- ✅ **Exception Handling**: Global exception handler with custom exceptions
- ✅ **DTOs & Mapping**: Clean separation between entities and DTOs
- ✅ **Unit Tests**: JUnit 5 and Mockito test cases for services and controllers
- ✅ **CI/CD Pipeline**: GitHub Actions workflow for automated testing and deployment
- ✅ **Docker Support**: Dockerfile and docker-compose for containerization
- ✅ **API Documentation**: Swagger/Springdoc-openapi ready

## Technologies Used

- **Java 17**
- **Spring Boot 4.0.4**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **SLF4J & Logback**
- **JUnit 5**
- **Mockito**
- **Docker**
- **Maven**

## Project Structure

```
deployment/
├── src/
│   ├── main/
│   │   ├── java/com/actions/deployment/
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── repository/      # Data Access Layer
│   │   │   ├── service/         # Business Logic Layer
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── config/         # Configuration Classes
│   │   │   ├── exception/      # Custom Exceptions
│   │   │   └── util/           # Utility Classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/actions/deployment/
│           ├── service/        # Service Tests
│           └── controller/     # Controller Tests
├── .github/
│   └── workflows/
│       └── ci-cd.yml          # GitHub Actions CI/CD
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker (optional)
- Git

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/deployment.git
cd deployment
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Application Configuration

Configure the application properties in `src/main/resources/application.properties`:

```properties
spring.application.name=deployment

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (Web Console)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8080
server.servlet.context-path=/api
```

## API Endpoints

### Get All Employees
```http
GET /api/employees
```

### Get All Active Employees
```http
GET /api/employees/active
```

### Get Employee by ID
```http
GET /api/employees/{id}
```

### Get Employee by Email
```http
GET /api/employees/email/{email}
```

### Get Employees by Department
```http
GET /api/employees/department/{department}
```

### Get Employees by Position
```http
GET /api/employees/position/{position}
```

### Search by Name
```http
GET /api/employees/firstname/{firstName}
GET /api/employees/lastname/{lastName}
```

### Create Employee
```http
POST /api/employees
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "department": "IT",
  "position": "Developer",
  "salary": 50000.0,
  "hireDate": "2024-03-20T10:00:00"
}
```

### Update Employee
```http
PUT /api/employees/{id}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "phone": "0987654321",
  "department": "HR",
  "position": "Manager",
  "salary": 60000.0
}
```

### Delete Employee (Hard Delete)
```http
DELETE /api/employees/{id}
```

### Soft Delete Employee (Mark as Inactive)
```http
PATCH /api/employees/{id}/deactivate
```

### Get Statistics
```http
GET /api/employees/stats/total      # Total employee count
GET /api/employees/stats/active     # Active employee count
```

## Response Format

All API responses follow a standard format:

```json
{
  "status": 200,
  "message": "Employees retrieved successfully",
  "data": [...],
  "timestamp": 1711000000000
}
```

### Success Response (200)
```json
{
  "status": 200,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": 1711000000000
}
```

### Error Response (4xx/5xx)
```json
{
  "status": 409,
  "message": "Conflict",
  "error": "Email already exists",
  "timestamp": 1711000000000
}
```

## H2 Database Console

Access H2 Database Web Console at: `http://localhost:8080/api/h2-console`

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User Name**: `sa`
- **Password**: (leave empty)

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=EmployeeServiceTest
```

### Run with coverage
```bash
mvn test jacoco:report
```

Test reports will be available at `target/site/jacoco/index.html`

## Docker Deployment

### Build Docker Image
```bash
docker build -t deployment-service:latest .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

### Using Docker only
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb \
  deployment-service:latest
```

## GitHub Actions CI/CD

The project includes a GitHub Actions workflow that:

1. Triggers on push to `main` and `develop` branches
2. Runs Maven build and tests on Java 17
3. Generates test reports
4. Runs SonarQube analysis (optional)
5. Uploads coverage reports to CodeCov
6. Builds and pushes Docker images to Docker Hub (on main branch)
7. Can deploy to production

### Required Secrets for CI/CD

Add these secrets to your GitHub repository settings:

- `DOCKER_USERNAME`: Your Docker Hub username
- `DOCKER_PASSWORD`: Your Docker Hub password
- `SONAR_TOKEN`: SonarQube authentication token (optional)
- `SONAR_HOST_URL`: SonarQube server URL (optional)

## CORS Configuration

The application is configured to accept CORS requests from:

- `http://localhost:4200` (Angular default port)
- `http://localhost:3000` (Alternative frontend port)
- `http://127.0.0.1:4200`
- `http://127.0.0.1:3000`

Update `CorsConfig.java` to add more origins for production.

## Angular Integration

### Example Angular Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) { }

  getEmployees(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  getEmployeeById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createEmployee(employee: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, employee);
  }

  updateEmployee(id: number, employee: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
```

## Logging Configuration

Logs are configured with different levels:

- **ROOT**: INFO level
- **com.actions.deployment**: DEBUG level

Log output format:
```
2024-03-20 10:30:45 [main] DEBUG com.actions.deployment.service.impl.EmployeeServiceImpl - Fetching all employees
```

## Best Practices Implemented

✅ **Clean Code**
- Meaningful naming conventions
- Single responsibility principle
- DRY (Don't Repeat Yourself)

✅ **Error Handling**
- Global exception handler
- Custom exceptions
- Proper HTTP status codes

✅ **Testing**
- Unit tests for services
- Unit tests for controllers
- Mock objects using Mockito
- Test coverage > 80%

✅ **Security**
- CORS properly configured
- Input validation
- SQL injection prevention with JPA

✅ **Documentation**
- JavaDoc comments
- README with examples
- API documentation

✅ **Performance**
- Lazy loading of relationships
- Efficient queries
- Caching ready

✅ **Maintainability**
- Separate layers (Entity, DTO, Service, Controller)
- Configuration externalized
- Dependency injection

## Troubleshooting

### Port 8080 already in use
```bash
# Change port in application.properties
server.port=8081
```

### H2 Console not accessible
Ensure `spring.h2.console.enabled=true` in application.properties

### CORS errors in browser
Check `CorsConfig.java` and add your frontend URL to the allowed origins

### Tests failing
```bash
mvn clean test
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, email support@example.com or create an issue in the GitHub repository.

## Authors

- Your Name - Initial work

## Acknowledgments

- Spring Boot community
- Hibernate/JPA documentation
- Angular documentation

