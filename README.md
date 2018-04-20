# URL Shortener task
Demo url shotener application.
This application implemented on Spring Boot framework and it is designed to receive a short link to the chosen URL.

## Approach
The app implemented using Spring Boot framework with the following dependencies: 
1) MySQL as DB. 
Exactly if we are going to store billions of rows and we don’t need to use relationships between objects – a NoSQL key-value store like Dynamo or Cassandra is a better choice.
But according this task I decided to use DB that meets the following requirements: widely used mature technology with very fast index lookup.
2) JPA for accessing, persisting, and managing data between Java objects/classes and a relational database. 
3) Thymeleaf for htmp templates implementation

## Requirements

```
java 8

maven 3.2.3

MySQL
 ```

## Tutorial

```bash
# create test database in MySQL
# execute in MySQL cli
create database test;

# configure MySQL credentials
# in src/main/resources/application.properties provide your MySQL creentials instead of 'root':
spring.datasource.username = root
spring.datasource.password = root

# run application
mvn spring-boot:run

# run tests
mvn test
```
