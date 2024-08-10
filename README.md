# Task Management System

## Описание

Task Management System — это приложение для управления задачами, разработанное с использованием Java 17+, Spring Boot и Spring Security. Система позволяет создавать, редактировать, удалять и просматривать задачи, а также управлять комментариями к задачам. Аутентификация и авторизация пользователей осуществляется с помощью JWT токенов. API полностью документировано с помощью OpenAPI и Swagger.

## Требования

- Java 17+
- Spring Boot
- PostgreSQL или MySQL
- Docker и Docker Compose
- Maven или Gradle

## Установка и Запуск

1. Убедитесь, что на вашем компьютере установлен Docker.

2. Файл конфигурации Docker Compose для запуска базы данных находится в [`docker-compose.yml`](docker-compose.yml). Этот файл содержит все необходимые настройки для развертывания среды разработки.

3. Откройте терминал и перейдите в корневую директорию вашего проекта, где находится файл `docker-compose.yml`.

4. Выполните команду:

    docker compose up

    Эта команда создаст и запустит контейнеры для базы данных и приложения.

5. После запуска контейнеров, приложение будет доступно по адресу: `http://localhost:8080`. Swagger UI для документации API доступен по адресу: `http://localhost:8080/swagger-ui.html`.

6. Для остановки и удаления контейнеров используйте команду:

    docker compose down

Эти шаги помогут вам настроить и запустить вашу среду разработки с помощью Docker и Docker Compose.
