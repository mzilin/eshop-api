# E-commerce Shop API Service


## Introduction

The `E-commerce Shop API Service` provides a set of RESTful endpoints for managing products and shopping carts. This API enables users to perform CRUD (Create, Read, Update, Delete) operations on products and manage shopping carts, including adding items and checking out.

## API Endpoints
### 1. Product Management
- Create a new product `POST http://127.0.0.1:8080/products`
- List all products `GET http://127.0.0.1:8080/products`
- List one product `GET http://127.0.0.1:8080/products/:id`
- Delete existing product `DELETE http://127.0.0.1:8080/products/:id`

### 2. Shopping Cart Management
- Create a shopping cart `POST http://127.0.0.1:8080/carts`
- List all shopping carts `GET http://127.0.0.1:8080/carts`
- Modify a shopping cart `PUT http://127.0.0.1:8080/carts/:id`
- Checkout a shopping cart `POST http://127.0.0.1:8080/carts/:id/checkout`

## Technologies Used
This API service utilises a comprehensive suite of technologies and dependencies, ensuring robust and scalable functionality:

- **Spring Boot** `3.2.5`:
  - **Data JPA**: Simplifies database integration by managing relational data access in a more object-oriented manner using Java Persistence API.
  - **Validation**: Ensures that incoming data meets the application's expectations, crucial for maintaining data integrity and proper functioning.
  - **Web**: Enables building web-based applications and services, supporting RESTful endpoints and traditional MVC setups.

- **Database Integration**:
  - **Flyway**: Manages and tracks database migrations, ensuring that database schema changes are version-controlled and consistently applied across various environments.
  - **H2 Database**: An in-memory database used primarily for development and testing, offering fast and lightweight performance. (For production, it is best to replace it with PostgreSQL)

- **Java** `JDK 17`: Essential for secure, portable, high-performance software development.

- **Lombok**: Reduces boilerplate in Java code significantly, automating the generation of getters, setters, constructors, and other common methods.

- **Testing**:
  - **Mockito Core** `5.3.1`: Provides essential mocking capabilities for unit testing, thereby facilitating thorough and effective test cases.


### Dependency Management

- **Gradle**: A powerful build automation tool that streamlines the compilation, testing, and deployment processes for software projects.


## Requirements

To successfully set up and run the application, ensure you have the following installed:

- [Java JDK 17](https://www.oracle.com/uk/java/technologies/downloads/#java17)
- [Gradle](https://gradle.org/)


## Installation

Follow these steps to get the API Service up and running:

1. Navigate into the app's directory
```shell
cd eshop-api
```

2. Clean and build the service

```shell
./gradlew clean build
```

3. Start the service

```shell
./gradlew bootRun
```


## Testing

Ensure the application is working as expected by executing the unit tests:

```shell
./gradlew clean test
```


## Contact

For any questions or clarifications about the project, please reach out to the project developer via [www.mariuszilinskas.com/contact](https://www.mariuszilinskas.com/contact).

Marius Zilinskas

------