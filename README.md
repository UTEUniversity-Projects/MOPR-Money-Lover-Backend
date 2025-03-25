# Money Lover Application
> Developed by **Lê Hồng Phúc**.
> © 2025 All rights reserved.

## 🚀 Technologies Used

### 🏗 **Build & Dependency Management**
- **Java 17**
- **Maven** (Dependency Management & Build Tool)

### 📦 **Frameworks & Libraries**
#### 🔹 **Spring Boot & Spring Cloud**
- `spring-boot-starter-web` → RESTful API development
- `spring-boot-starter-data-jpa` → ORM & database interaction
- `spring-boot-starter-validation` → Input validation
- `spring-boot-starter-security` → Application security
- `spring-boot-starter-mail` → Email handling
- `spring-cloud-starter-openfeign` → Inter-service communication

#### 🔹 **OAuth2 & JWT (Authentication & Authorization)**
- `spring-boot-starter-oauth2-authorization-server` → OAuth2 authorization server
- `spring-boot-starter-oauth2-client` → OAuth2 client setup
- `spring-boot-starter-oauth2-resource-server` → Secure APIs using OAuth2
- `io.jsonwebtoken:jjwt` → JWT token handling
- `com.nimbusds:nimbus-jose-jwt` → JWT & JOSE processing

#### 🔹 **Database & ORM**
- `mysql-connector-java` → MySQL database connection
- `spring-boot-starter-data-jpa` → JPA/Hibernate for ORM
- `liquibase-core` → Database schema versioning
- `liquibase-hibernate5` → Hibernate integration with Liquibase

#### 🔹 **Code Generation & Utilities**
- `lombok` → Reduce boilerplate code
- `mapstruct` → DTO ↔ Entity mapping

#### 🔹 **API Documentation**
- `springdoc-openapi-starter-webmvc-ui` → Auto-generate API documentation

#### 🔹 **Testing**
- `spring-boot-starter-test` → JUnit, Mockito, and AssertJ for testing

---

## ⚙️ Configuration (`application.yml`)
- **Server Port:** `server.port`
- **OAuth2 Authorization Server** configuration
- **Email SMTP** settings

---

## 📌 How to Run in Development Environment
```sh
# Clone the repository
git clone git@github.com:UTEUniversity-Projects/Money-Lover-Backend.git
cd Money-Lover-Backend

# Build & Run
mvn clean install
mvn spring-boot:run
```

### 📝 Notes
- Ensure **MySQL is running** and properly configured.
- Modify `application.yml` for your database and email settings.

---

## 🔐 User Registration Process

### 1️⃣ Request Registration
User sends a request to register an account.

#### 📌 Endpoint:
```http
POST http://localhost:8080/api/request-register
```

#### 📤 Request:
```json
{
  "email": "<string>",
  "username": "<string>",
  "password": "<string>",
  "recaptchaResponse": "<string>"
}
```

#### 📥 Headers:
```http
Content-Type: application/json
Accept: application/json
Authorization: No Auth
```

#### ✅ Success Response:
```json
{
  "result": "<boolean>",
  "code": "<string>",
  "data": {
    "token": "<string>",
    "kind": "<integer>"
  },
  "message": "<string>"
}
```

---

### 2️⃣ Verify OTP & Complete Registration
FE sends the received token and OTP code to confirm registration.

#### 📌 Endpoint:
```http
POST http://localhost:8080/api/register
```

#### 📤 Request:
```json
{
  "otp": "<string>",
  "token": "<string>"
}
```

#### 📥 Headers:
```http
Content-Type: application/json
Accept: application/json
Authorization: No Auth
```

#### ✅ Success Response:
```json
{
  "result": "<boolean>",
  "code": "<string>",
  "data": "<string>",
  "message": "<string>"
}
```

---

### 3️⃣ Resend OTP
User can request to resend the OTP code.

#### 📌 Endpoint:
```http
POST http://localhost:8080/api/resend-otp
```

#### 📤 Request:
```json
{
  "token": "<string>"
}
```
- `token`: this is token received in the first step.

#### 📥 Headers:
```http
Content-Type: application/json
Accept: application/json
Authorization: No Auth
```

#### ✅ Success Response:
```json
{
  "result": "<boolean>",
  "code": "<string>",
  "data": "<string>",
  "message": "<string>"
}
```

---

### 📝 Notes:
- You can only request to resend the OTP within the **token's lifetime**. 
- If the token expires, you are required to **go back** to the previous step to retrieve the information and **receive a new token**.

---

## 🔑 How to Get reCAPTCHA v3 Token

To obtain a reCAPTCHA v3 token, follow these steps:

### 1️⃣ Run the JavaScript Code
Create an HTML file and copy the following code. This will generate a simple webpage where you can retrieve a reCAPTCHA v3 token.

```html
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test reCAPTCHA v3</title>
    <script src="https://www.google.com/recaptcha/api.js?render=SITE_KEY"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            background-color: #f4f4f4;
        }
        h1 {
            color: #333;
        }
        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition: background 0.3s;
        }
        button:hover {
            background-color: #0056b3;
        }
        #tokenDisplay {
            margin-top: 20px;
            padding: 10px;
            background: #fff;
            border-radius: 5px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            max-width: 80%;
            word-break: break-word;
            text-align: left;
        }
    </style>
</head>
<body>
    <h1>Test reCAPTCHA v3</h1>
    <button id="submitBtn">Get Token</button>
    <p id="tokenDisplay">Token will appear here ...</p>

    <script>
        document.getElementById("submitBtn").addEventListener("click", function() {
            grecaptcha.ready(function() {
                grecaptcha.execute('SITE_KEY', { action: 'submit' }).then(function(token) {
                    document.getElementById("tokenDisplay").innerText = token;
                    console.log("reCAPTCHA Token:", token);
                });
            });
        });
    </script>
</body>
</html>
```
- Replace `SITE_KEY` with actual **Site Key** from [Google reCAPTCHA Admin Console](https://www.google.com/recaptcha/admin).

### 2️⃣ Run the HTML File
- Save the file as `recaptcha_test.html`.
- Open the file in a web browser.

### 3️⃣ Get the reCAPTCHA v3 Token
- Click the **“Get Token”** button.
- The generated reCAPTCHA token will appear on the screen.
- You can also check the browser console (`F12` → Console) to see the token.

---

### ⏳ Lifespan of reCAPTCHA v3 Token
- The token is **valid for one-time use only** and must be sent to the server immediately for verification.
- After **2 minutes**, if the token is not used, a new token must be generated.
- If an expired token is used, Google will reject the authentication with an appropriate error.

---

## 📚 References
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [OAuth2 Authorization Server](https://spring.io/projects/spring-authorization-server)
