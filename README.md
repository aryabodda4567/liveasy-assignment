# Liveasy - Load & Booking Management System

A simple backend system built with Spring Boot and PostgreSQL for managing loads and bookings in the logistics domain.

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
