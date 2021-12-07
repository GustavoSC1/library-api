# Library API
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/GustavoSC1/library-api/blob/main/LICENSE)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/GustavoSC1/library-api/maven)
[![codecov](https://codecov.io/gh/GustavoSC1/library-api/branch/main/graph/badge.svg?token=POCBL2KT2P)](https://codecov.io/gh/GustavoSC1/library-api)

API: https://gustavo-library-api.herokuapp.com/

Documentação: https://gustavo-library-api.herokuapp.com/swagger-ui.html

Library API é uma aplicação back-end construída com Java + Spring + H2 durante o curso **Design de API's RestFul com Spring Boot, TDD e o novo JUnit5**.

A aplicação consiste em um serviço de biblioteca que permite o cadastro, edição, exclusão e consulta de livros, e também permite cadastrar, devolver e consultar um empréstimo de livro. 

## Modelo conceitual
![Modelo Conceitual](https://ik.imagekit.io/gustavosc/library-api/Modelo_conceitual_Uu4mjrQ2Z.PNG?updatedAt=1638875435577)

## Tecnologias utilizadas
### Back end
- [Java](https://www.oracle.com/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2](https://www.h2database.com/html/main.html)
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://site.mockito.org/)
- [Swagger](https://swagger.io/)
- [Jacoco](https://www.jacoco.org/jacoco/trunk/doc/mission.html)

## Como executar o projeto

### Back end
Pré-requisitos: Java 11

```bash
# clonar repositório
git clone https://github.com/GustavoSC1/library-api.git

# entrar na pasta do projeto library api
cd library-api

# executar o projeto
./mvnw spring-boot:run
```

## Autor

Gustavo da Silva Cruz

https://www.linkedin.com/in/gustavo-silva-cruz-20b128bb/
