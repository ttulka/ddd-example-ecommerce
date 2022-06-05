# DDD Example Project in Java: eCommerce (Modulith)

*Disclaimer:* This is an alternative branch, for more info please check out [master](https://github.com/ttulka/ddd-example-ecommerce).

The purpose of this project is to provide a sample implementation of an e-commerce product following **Domain-Driven Design (DDD)** and **Service-Oriented Architecture (SOA)** principles.

Programming language is Java with heavy use of Spring framework.

```sh
# build
mvn clean install

# run 
mvn spring-boot:run -f application/pom.xml

# open in browser http://localhost:8080
```

## About the Branch

In this branch, all services are cut into separate Maven modules and brought together again in a "modulithic" application later.

Every service has multiple Maven modules for a layer (technical cut).

Although the final artifact (the application) is a physically monolithic deployment unit, logical separation by feature/component into services is projected into the code structure. 

In a real-world scenario there would be a separate Git repository for each domain or service.
