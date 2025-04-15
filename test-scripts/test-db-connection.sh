#!/bin/bash

# Test script for database connection
# This script tests the connection to the PostgreSQL database

# Set text colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Database connection parameters
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="liveasy"
DB_USER="arya"
DB_PASSWORD="Arya@9966509079"

# Function to print colored messages
print_message() {
  local color=$1
  local message=$2
  echo -e "${color}${message}${NC}"
}

# Function to check if PostgreSQL is installed
check_postgres_installed() {
  print_message "$YELLOW" "Checking if PostgreSQL is installed..."
  
  if command -v psql &> /dev/null; then
    print_message "$GREEN" "PostgreSQL is installed."
    return 0
  else
    print_message "$RED" "PostgreSQL is not installed. Please install PostgreSQL first."
    return 1
  fi
}

# Function to check if the database exists
check_database_exists() {
  print_message "$YELLOW" "Checking if database '$DB_NAME' exists..."
  
  if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw "$DB_NAME"; then
    print_message "$GREEN" "Database '$DB_NAME' exists."
    return 0
  else
    print_message "$RED" "Database '$DB_NAME' does not exist."
    return 1
  fi
}

# Function to check if the user has access to the database
check_user_access() {
  print_message "$YELLOW" "Checking if user '$DB_USER' has access to database '$DB_NAME'..."
  
  if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1" &> /dev/null; then
    print_message "$GREEN" "User '$DB_USER' has access to database '$DB_NAME'."
    return 0
  else
    print_message "$RED" "User '$DB_USER' does not have access to database '$DB_NAME'."
    return 1
  fi
}

# Function to check if the required tables exist
check_tables_exist() {
  print_message "$YELLOW" "Checking if required tables exist..."
  
  local tables=("loads" "bookings")
  local all_tables_exist=true
  
  for table in "${tables[@]}"; do
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT to_regclass('public.$table')" | grep -q "$table"; then
      print_message "$GREEN" "Table '$table' exists."
    else
      print_message "$RED" "Table '$table' does not exist."
      all_tables_exist=false
    fi
  done
  
  if [ "$all_tables_exist" = true ]; then
    return 0
  else
    return 1
  fi
}

# Function to check the database schema
check_database_schema() {
  print_message "$YELLOW" "Checking database schema..."
  
  # Check loads table schema
  print_message "$YELLOW" "Checking 'loads' table schema..."
  local loads_schema=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "\d loads" 2>&1)
  
  if [[ "$loads_schema" == *"id"* && "$loads_schema" == *"shipper_id"* && "$loads_schema" == *"product_type"* && "$loads_schema" == *"truck_type"* ]]; then
    print_message "$GREEN" "'loads' table schema is valid."
  else
    print_message "$RED" "'loads' table schema is invalid or table does not exist."
    print_message "$YELLOW" "Schema: $loads_schema"
    return 1
  fi
  
  # Check bookings table schema
  print_message "$YELLOW" "Checking 'bookings' table schema..."
  local bookings_schema=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "\d bookings" 2>&1)
  
  if [[ "$bookings_schema" == *"id"* && "$bookings_schema" == *"load_id"* && "$bookings_schema" == *"transporter_id"* && "$bookings_schema" == *"proposed_rate"* ]]; then
    print_message "$GREEN" "'bookings' table schema is valid."
  else
    print_message "$RED" "'bookings' table schema is invalid or table does not exist."
    print_message "$YELLOW" "Schema: $bookings_schema"
    return 1
  fi
  
  return 0
}

# Main test function
run_db_tests() {
  print_message "$YELLOW" "Starting database connection tests..."
  
  # Check if PostgreSQL is installed
  if ! check_postgres_installed; then
    return 1
  fi
  
  # Check if the database exists
  if ! check_database_exists; then
    print_message "$YELLOW" "Creating database '$DB_NAME'..."
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -c "CREATE DATABASE $DB_NAME;" &> /dev/null; then
      print_message "$GREEN" "Database '$DB_NAME' created successfully."
    else
      print_message "$RED" "Failed to create database '$DB_NAME'."
      return 1
    fi
  fi
  
  # Check if the user has access to the database
  if ! check_user_access; then
    return 1
  fi
  
  # Note: We don't check tables here because they will be created by the application
  # when it starts with spring.jpa.hibernate.ddl-auto=update
  
  print_message "$GREEN" "All database connection tests completed successfully!"
  return 0
}

# Run the tests
run_db_tests
exit $?
