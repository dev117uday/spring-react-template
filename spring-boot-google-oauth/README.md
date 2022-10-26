# Spring Boot Google OAuth Template

All code written in this repo is me hacking together things i know from the internet. If you find this cord weird, yes it is, because it works for me and if the repo is public, still in use.

## Configuration

- create a file in `application.yml` in `src/main/resources` folder

```s
mkdir src/main/resources 
touch src/main/resources/application.yml
```

- Paste the following with correct info

```yml
spring:
  datasource:
    url: jdbc:postgresql://<hostt>:<port>/<db>
    username: <user>
    password: <password>

  jpa:
   hibernate.ddl-auto: update
   show-sql: true
   properties:
     hibernate:
       dialect: org.hibernate.dialect.PostgreSQLDialect

app:
  cors:
    allowedOrigins: <frontend url>

  jwt:
    secret_key: <jwt secret key>


server:
  error:
    include-message: always
```
