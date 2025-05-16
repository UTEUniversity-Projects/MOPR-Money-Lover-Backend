# Money Lover API
> A comprehensive personal finance management API system built with Spring Boot.

## 🌟 Overview
Money Lover API is a robust backend service that powers a personal finance tracking application. It allows users to monitor expenses, track budgets, analyze spending patterns, and manage multiple wallets and currencies.

## 🚀 Key Features
- User authentication with OAuth2 and JWT tokens
- Account management with group-based permissions
- Multi-wallet tracking with different currencies
- Categorization for income and expenses
- Budget planning and monitoring
- Expense tracking with detailed reporting
- Statistics and data analysis for spending patterns
- Tag-based filtering for expenses
- Event-based expense tracking
- Reminders for recurring expenses or payments
- File management for receipts and documentation
- Notification system for budget alerts

## 🏗️ Technical Stack

### Core Technologies
- **Java 17**
- **Spring Boot 3.2.12**
- **Maven** for dependency management and build

### Frameworks & Libraries
#### Spring Ecosystem
- **Spring Web** for RESTful API development
- **Spring Data JPA** with Hibernate for ORM
- **Spring Security** with OAuth2 for authentication
- **Spring Validation** for input validation
- **Spring Mail** for email notifications
- **Spring Cloud OpenFeign** for service communication
- **Spring Data Redis** for caching and session management

#### Authentication & Security
- **Spring OAuth2 Authorization Server**
- **Spring OAuth2 Client**
- **Spring OAuth2 Resource Server**
- **JJWT** for JWT token handling
- **Nimbus JOSE+JWT** for JWT and JOSE processing

#### Database & Storage
- **MySQL** for primary data storage
- **Redis** for caching and session management
- **Liquibase** for database migration and schema versioning

#### Cloud Services
- **Cloudinary** for file storage and management
- **Azure Web Apps** for deployment

#### Developer Tools
- **Lombok** for reducing boilerplate code
- **MapStruct** for DTO-Entity mapping
- **SpringDoc OpenAPI** for API documentation

## 📂 Project Structure

The project follows a standard Spring Boot architecture with the following key components:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Data access layer
- **Models**: Domain entities, DTOs, and forms
- **Security**: Authentication and authorization
- **Configuration**: Application and bean configuration
- **Utilities**: Helper classes

## 🔄 Key Workflows

### 🔐 User Registration Process

#### 1. Request Registration
```http
POST /api/request-register
```
- Validates reCAPTCHA
- Sends OTP to user's email
- Returns token for verification

#### 2. Verify OTP & Complete Registration
```http
POST /api/register
```
- Verifies OTP and token
- Creates user account
- Initializes default data (categories, wallet)

#### 3. Resend OTP (if needed)
```http
POST /api/resend-otp
```
- Resends OTP to registered email

### 💰 Expense Management

- Create, read, update, and delete expenses
- Categorize expenses
- Tag expenses for better organization
- Associate expenses with events
- Attach receipts as images
- Track expenses across different wallets

### 📊 Budget Planning

- Create budgets for specific categories
- Set spending limits for different time periods
- Monitor budget usage
- Receive notifications for budget thresholds

### 📈 Statistics and Analysis

- View spending patterns by category
- Analyze expenses over different time periods
- Compare income vs expense
- Generate detailed reports

## 🛡️ Security Features

- OAuth2 with JWT for authentication
- Role-based access control
- Permission-based authorization
- Secure password management
- OTP for sensitive operations
- reCAPTCHA integration for bot protection

## 🛠️ Development Setup

### Prerequisites
- JDK 17
- Maven
- MySQL
- Redis

### Configuration
Create a `.env` file based on the provided `.env.example`:
```properties
# Spring Server Url
SPRING_SERVER_URL=http://localhost:8080

# Spring Security Default User
SECURITY_USER_NAME=
SECURITY_USER_PASSWORD=
SECURITY_REDIRECT_URI=

# Spring Datasource
DATASOURCE_URL=
DATASOURCE_NAME=
DATASOURCE_PASSWORD=

# Spring Mail
MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=
MAIL_PROTOCOL=

# Srping Security RSA
SECURITY_PRIVATE_KEY=
SECURITY_PUBLIC_KEY=
```

### Building the Project
```bash
# Clone the repository
git clone https://github.com/your-username/money-lover-api.git
cd money-lover-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Using Docker
```bash
# Build and run with Docker Compose
docker-compose -f docker-compose.dev.yml up -d

# Check logs
docker-compose logs -f money-lover-api-dev
```

## 🚢 Deployment

The project is configured for deployment to Azure Web Apps:

```bash
# Using the provided GitHub Action workflow
git push origin dev  # Triggers the CI/CD pipeline
```

## 📦 Database Schema

The application uses several key entities:
- User / Account
- Wallet
- Category
- Bill (Expense/Income)
- Budget
- Tag
- Event
- Notification
- File

## 📞 API Endpoints

The API is documented using SpringDoc OpenAPI, available at:
```
http://localhost:8080/swagger-ui.html
```

Key endpoints include:
- Authentication: `/api/login`, `/api/register`
- User: `/api/v1/user/*`
- Wallet: `/api/v1/wallet/*`
- Category: `/api/v1/category/*`
- Bill: `/api/v1/bill/*`
- Budget: `/api/v1/budget/*`
- Tag: `/api/v1/tag/*`
- Event: `/api/v1/event/*`
- Statistics: `/api/v1/statistics/*`

## 👨‍💻 Developed by
Lê Hồng Phúc © 2025 All rights reserved.
