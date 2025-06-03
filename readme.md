# Gardrops Image Upload Project

This project is a simple microservice application for handling image uploads and processing for Gardrops. It consists of two parts: `ImageUploadApi` (upload) and `ImageProcessingApi` (processing).

## What We Used

*   **Spring Boot:** An easy way to build web applications with Java.
*   **Kotlin & Java:** The languages we used in the project, they talk to each other.
*   **Maven:** To build and run the project.
*   **Redis:** A fast database for storing temporary data (sessions, rate limits).
*   **RestTemplate:** For services to communicate with each other.

## How to Set Up and Run

1.  Download the project to your computer.
2.  Make sure you have Maven and Docker installed on your computer.
3.  Start Redis with Docker:
    ```bash
    docker run --name gardrops-redis -p 6379:6379 -d redis
    ```
4.  Navigate into the `ImageUploadApi` and `ImageProcessingApi` folders and build the projects:
    ```bash
    cd ImageUploadApi
    mvn clean install
    cd ../ImageProcessingApi
    mvn clean install
    ```
5.  Set the API key in the terminal (optional but recommended):
    ```bash
    export INTERNAL_API_KEY=write-a-secure-key-here
    ```
6.  First, start `ImageProcessingApi`:
    ```bash
    cd ImageProcessingApi
    mvn spring-boot:run
    ```
7.  **Open another terminal**, navigate to the `ImageUploadApi` folder, and start it:
    ```bash
    cd ImageUploadApi
    mvn spring-boot:run
    ```

The services should now be running. `ImageUploadApi` runs on 8080, and `ImageProcessingApi` runs on 8081 (intended for use only by `ImageUploadApi`).

## API Endpoints

Check the @Gardrops.postman_collection.json
