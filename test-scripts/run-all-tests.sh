#!/bin/bash

# Master script to run all tests

# Set text colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored messages
print_message() {
  local color=$1
  local message=$2
  echo -e "${color}${message}${NC}"
}

# Function to print a separator
print_separator() {
  echo -e "${BLUE}=========================================${NC}"
}

# Function to check if the application is running
check_app_running() {
  print_message "$YELLOW" "Checking if the application is running..."
  
  if curl -s "http://localhost:8080/load" > /dev/null; then
    print_message "$GREEN" "Application is running."
    return 0
  else
    print_message "$RED" "Application is not running. Please start the application first."
    return 1
  fi
}

# Function to make scripts executable
make_scripts_executable() {
  print_message "$YELLOW" "Making test scripts executable..."
  
  chmod +x test-db-connection.sh
  chmod +x test-load-api.sh
  chmod +x test-booking-api.sh
  
  print_message "$GREEN" "Test scripts are now executable."
}

# Main function to run all tests
run_all_tests() {
  print_message "$YELLOW" "Starting all tests..."
  
  # Make scripts executable
  make_scripts_executable
  
  # Check if the application is running
  if ! check_app_running; then
    print_message "$YELLOW" "Would you like to start the application? (y/n)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
      print_message "$YELLOW" "Starting the application..."
      cd ..
      mvn spring-boot:run &
      app_pid=$!
      print_message "$YELLOW" "Waiting for the application to start..."
      sleep 30
      cd test-scripts
    else
      print_message "$RED" "Tests aborted. Please start the application manually and run the tests again."
      return 1
    fi
  fi
  
  # Run database connection tests
  print_separator
  print_message "$YELLOW" "Running database connection tests..."
  ./test-db-connection.sh
  db_result=$?
  
  if [ $db_result -ne 0 ]; then
    print_message "$RED" "Database connection tests failed. Aborting further tests."
    return 1
  fi
  
  # Run Load API tests
  print_separator
  print_message "$YELLOW" "Running Load API tests..."
  ./test-load-api.sh
  load_result=$?
  
  # Run Booking API tests
  print_separator
  print_message "$YELLOW" "Running Booking API tests..."
  ./test-booking-api.sh
  booking_result=$?
  
  # Print summary
  print_separator
  print_message "$YELLOW" "Test Summary:"
  
  if [ $db_result -eq 0 ]; then
    print_message "$GREEN" "✓ Database Connection Tests: PASSED"
  else
    print_message "$RED" "✗ Database Connection Tests: FAILED"
  fi
  
  if [ $load_result -eq 0 ]; then
    print_message "$GREEN" "✓ Load API Tests: PASSED"
  else
    print_message "$RED" "✗ Load API Tests: FAILED"
  fi
  
  if [ $booking_result -eq 0 ]; then
    print_message "$GREEN" "✓ Booking API Tests: PASSED"
  else
    print_message "$RED" "✗ Booking API Tests: FAILED"
  fi
  
  # Overall result
  if [ $db_result -eq 0 ] && [ $load_result -eq 0 ] && [ $booking_result -eq 0 ]; then
    print_message "$GREEN" "All tests passed successfully!"
    return 0
  else
    print_message "$RED" "Some tests failed. Please check the logs for details."
    return 1
  fi
  
  # Kill the application if we started it
  if [ -n "$app_pid" ]; then
    print_message "$YELLOW" "Stopping the application..."
    kill $app_pid
  fi
}

# Run all tests
run_all_tests
exit $?
