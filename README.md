# Liveasy - Load & Booking Management System

A simple backend system built with Spring Boot and PostgreSQL for managing loads and bookings in the logistics domain.

## Overview

This project implements a RESTful API for managing loads and bookings in a logistics system. It includes comprehensive shell scripts for testing all API endpoints and database connectivity.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- PostgreSQL 12 or higher

## Database Setup

1. Create a PostgreSQL database:

```sql
CREATE DATABASE liveasy;
CREATE USER arya WITH PASSWORD 'Arya@9966509079';
GRANT ALL PRIVILEGES ON DATABASE liveasy TO arya;
```

2. The database connection is configured in `src/main/resources/application.properties`

## Running the Application

```bash
# Build and run the application
mvn spring-boot:run
```

The application will start on port 8080 by default. You can access it at http://localhost:8080.

## API Endpoints

### Load Management
- POST /load - Create a new load
- GET /load - Get all loads
- GET /load/{loadId} - Get load by ID
- PUT /load/{loadId} - Update a load
- DELETE /load/{loadId} - Delete a load

### Booking Management
- POST /booking - Create a new booking
- GET /booking - Get all bookings
- GET /booking/{bookingId} - Get booking by ID
- PUT /booking/{bookingId} - Update a booking
- DELETE /booking/{bookingId} - Delete a booking
- PATCH /booking/{bookingId}/status - Update booking status

## Testing

The project includes comprehensive shell scripts for testing the application:

### Test Scripts

```bash
# Navigate to the test-scripts directory
cd test-scripts

# Make scripts executable
chmod +x *.sh

# Run all tests
./run-all-tests.sh

# Or run individual tests
./test-db-connection.sh  # Test database connectivity
./test-load-api.sh       # Test Load API endpoints
./test-booking-api.sh    # Test Booking API endpoints
```

These scripts provide thorough testing of:
- Database connection and schema validation
- All Load API endpoints (CRUD operations and filtering)
- All Booking API endpoints (CRUD operations, status updates, and filtering)

The test scripts use color-coded output for easy interpretation of test results and include detailed error reporting.

## Project Structure

```
├── src/main/java/com/delivery/liveasy/
│   ├── controller/       # REST controllers
│   ├── dto/              # Data Transfer Objects
│   ├── exception/        # Custom exceptions and error handling
│   ├── model/            # Entity models
│   ├── repository/       # Data access layer
│   ├── service/          # Business logic
│   └── LiveasyApplication.java
├── src/main/resources/
│   └── application.properties
└── test-scripts/         # Shell scripts for testing
```
