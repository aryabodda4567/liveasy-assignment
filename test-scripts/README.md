# API Test Scripts

This directory contains shell scripts for testing the Liveasy API endpoints.

## Overview

These scripts test the functionality of the Load and Booking APIs, as well as the database connection. They use `curl` to make HTTP requests to the API endpoints and validate the responses.

## Prerequisites

- Bash shell
- curl
- PostgreSQL client (psql)
- The Liveasy application must be running on http://localhost:8080

## Scripts

### 1. test-db-connection.sh

Tests the connection to the PostgreSQL database:
- Checks if PostgreSQL is installed
- Checks if the database exists
- Checks if the user has access to the database
- Checks if the required tables exist
- Checks the database schema

### 2. test-load-api.sh

Tests the Load API endpoints:
- Create a new load
- Get all loads
- Get a load by ID
- Update a load
- Filter loads by shipper ID
- Filter loads by truck type
- Delete a load

### 3. test-booking-api.sh

Tests the Booking API endpoints:
- Create a new booking
- Get all bookings
- Get a booking by ID
- Update a booking
- Update booking status
- Filter bookings by load ID
- Filter bookings by transporter ID
- Delete a booking

### 4. run-all-tests.sh

Master script that runs all the tests in sequence:
- Makes the test scripts executable
- Checks if the application is running
- Runs the database connection tests
- Runs the Load API tests
- Runs the Booking API tests
- Prints a summary of the test results

## Usage

1. Make the scripts executable:
   ```bash
   chmod +x *.sh
   ```

2. Start the Liveasy application:
   ```bash
   cd ..
   mvn spring-boot:run
   ```

3. Run all tests:
   ```bash
   ./run-all-tests.sh
   ```

   Or run individual tests:
   ```bash
   ./test-db-connection.sh
   ./test-load-api.sh
   ./test-booking-api.sh
   ```

## Test Output

The scripts provide colored output to indicate success or failure:
- Green: Success
- Red: Failure
- Yellow: Information/Warning

Each test step is logged, and a summary is provided at the end of the run-all-tests.sh script.

## Troubleshooting

If the tests fail, check the following:
1. Is the application running on http://localhost:8080?
2. Is the PostgreSQL database running and accessible?
3. Are the database credentials correct in test-db-connection.sh?
4. Do you have curl installed?
5. Do you have psql installed?
