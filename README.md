# rest-spring-boot-java-security-jwt-jdbc

This application was created following, partially:

[https://octoperf.com/blog/2018/03/08/securing-rest-api-spring-security/#publicuserscontroller](https://octoperf.com/blog/2018/03/08/securing-rest-api-spring-security)

The idea of the service is to have:

- a relational database that has the users
- endpoint that provides token
- all other endpoints are token required to access secure information

## Tech details

Build on:

- language: Java 11+
- framework: spring-boot (+ security and liquibase for database migrations)
- database: MySQL

## Resources / Endpoints

| Method | Resource | Token secured | Description  |
| ---    |:------- |:-----:|:-----|
|POST| /users | false | Save new user |
|GET| /me | true | get logged user information |
|POST| /login | false | Endpoint that will accept user/pwd and will return the token that will be have an expiration date |
|GET| /logout | true | Invalidates the token, stored on the databse |

## Database model

- `users`: contains user details, such as first name, username and password (encoded)
- `user_authentication_log`: Has the log, with the expiration date, of the token provided on the `/login` endpoint, and whether they have been invalidates from `/logout` 
