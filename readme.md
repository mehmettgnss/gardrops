# Gardrops Image Upload Project

This project is a simple microservice application for handling image uploads and processing for Gardrops. It consists of two parts: `ImageUploadApi` (upload) and `ImageProcessingApi` (processing).

## What I Used

*   **Spring Boot** 
*   **Kotlin & Java** (Kotlin for the entities)
*   **Maven**
*   **Redis** 

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

5.  First, start `ImageProcessingApi` and then `ImageUploadApi`



The services should now be running. `ImageUploadApi` runs on 8080, and `ImageProcessingApi` runs on 8081 (intended for use only by `ImageUploadApi`).

## API Endpoints

Check the @Gardrops.postman_collection.json
