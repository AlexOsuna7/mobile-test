# Ktor API Backend

This is a Kotlin-based REST API built with Ktor. It provides endpoints to generate and validate seeds used for QR codes.

## Features

- Generate a unique seed with a 10-minute expiration (`GET /seed`)
- Validate if a given seed is valid, expired, or missing (`GET /validate?seed=...`)
- Simple in-memory seed storage with expiration handling
- JSON responses using Kotlinx Serialization
- Basic logging to console for seed generation and validation

## Requirements

- JDK 11 or higher
- Gradle 7.x
- Internet connection to download dependencies

## Getting Started

### Running the server

1. Clone the repository and navigate to the `ktor-api` directory
2. Run the server using Gradle: ./gradlew run or ./gradlew :ktor-api:run 

## Available Endpoints

GET /seed
Generates a new random seed and returns JSON:

GET /validate?seed={seed}
Validates the seed provided as a query parameter. Returns:

HTTP 200 + { "valid": true } if valid

HTTP 400 + { "valid": false, "reason": "Missing seed" } if no seed param

HTTP 404 + { "valid": false, "reason": "Seed not found" } if unknown seed

HTTP 410 + { "valid": false, "reason": "Seed expired" } if expired

## Configuration
The server runs by default on port 8080 and binds to all interfaces (0.0.0.0).

Seed expiration is set to 10 minutes but can be adjusted in code.

## Logging
The server prints logs to the console showing:

Seed generation events

Seed validation attempts and results