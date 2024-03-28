# Интеграционное тестирование
Для REST API эндпоинтов supplier service были разработаны интеграционные тесты с помощью библиотеку Testcontainers.

Покрываются основные сценарии использования приложения и обрабатываются возможные ошибки.
## Инструкция по запуску интеграционных тестов
- Склонировать репозиторий
```
git clone https://github.com/lordphiluren/microservices-task
```
- Перейти в папку с supplier
- Запустить приложение
```
./gradlew test
```
- Страница с документацией для Supplier будет доступна по ссылке: http://localhost:8080/swagger-ui/index.html
## Используемые технологии
- Java
- Gradle
- Spring Boot, Spring Data JPA
- PostgreSQL
- Liquibase
- Swagger
- Docker - Dockerfile, Docker Compose
- Testcontainers