#!/bin/bash

# Test script for Load API
# This script tests the CRUD operations for the Load API

# Set the base URL
BASE_URL="http://localhost:8080"

# Set text colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored messages
print_message() {
  local color=$1
  local message=$2
  echo -e "${color}${message}${NC}"
}

# Function to check if the application is running
check_app_running() {
  print_message "$YELLOW" "Checking if the application is running..."

  if curl -s "$BASE_URL/load" > /dev/null; then
    print_message "$GREEN" "Application is running."
    return 0
  else
    print_message "$RED" "Application is not running. Please start the application first."
    return 1
  fi
}

# Function to create a load
create_load() {
  print_message "$YELLOW" "Creating a new load..."

  local response=$(curl -s -X POST "$BASE_URL/load" \
    -H "Content-Type: application/json" \
    -d '{
      "shipperId": "shipper123",
      "facility": {
        "loadingPoint": "Delhi",
        "unloadingPoint": "Mumbai",
        "loadingDate": "2023-06-01T10:00:00",
        "unloadingDate": "2023-06-03T18:00:00"
      },
      "productType": "Electronics",
      "truckType": "Open",
      "noOfTrucks": 2,
      "weight": 1500.0,
      "comment": "Handle with care"
    }')

  # Extract the load ID from the response
  local load_id=$(echo "$response" | grep -o '"id":"[^"]*' | cut -d'"' -f4)

  if [ -n "$load_id" ]; then
    print_message "$GREEN" "Load created successfully with ID: $load_id"
    echo "$load_id"
  else
    print_message "$RED" "Failed to create load. Response: $response"
    return 1
  fi
}

# Function to get all loads
get_all_loads() {
  print_message "$YELLOW" "Getting all loads..."

  local response=$(curl -s -X GET "$BASE_URL/load")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Loads retrieved successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to retrieve loads."
    return 1
  fi
}

# Function to get a load by ID
get_load_by_id() {
  local load_id=$1

  local response=$(curl -s -X GET "$BASE_URL/load/$load_id")

  if [[ "$response" == *"$load_id"* ]]; then
    print_message "$GREEN" "Load retrieved successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to retrieve load. Response: $response"
    return 1
  fi
}

# Function to update a load
update_load() {
  local load_id=$1
  print_message "$YELLOW" "Updating load with ID: $load_id..."

  local response=$(curl -s -X PUT "$BASE_URL/load/$load_id" \
    -H "Content-Type: application/json" \
    -d '{
      "shipperId": "shipper123",
      "facility": {
        "loadingPoint": "Chennai",
        "unloadingPoint": "Bangalore",
        "loadingDate": "2023-06-05T10:00:00",
        "unloadingDate": "2023-06-07T18:00:00"
      },
      "productType": "Furniture",
      "truckType": "Closed",
      "noOfTrucks": 1,
      "weight": 800.0,
      "comment": "Updated load"
    }')

  if [[ "$response" == *"$load_id"* ]]; then
    print_message "$GREEN" "Load updated successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to update load. Response: $response"
    return 1
  fi
}

# Function to delete a load
delete_load() {
  local load_id=$1
  print_message "$YELLOW" "Deleting load with ID: $load_id..."

  local http_code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/load/$load_id")

  if [ "$http_code" -eq 204 ]; then
    print_message "$GREEN" "Load deleted successfully."
    return 0
  else
    print_message "$RED" "Failed to delete load. HTTP code: $http_code"
    return 1
  fi
}

# Function to filter loads by shipper ID
filter_loads_by_shipper() {
  local shipper_id=$1
  print_message "$YELLOW" "Filtering loads by shipper ID: $shipper_id..."

  local response=$(curl -s -X GET "$BASE_URL/load?shipperId=$shipper_id")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Loads filtered successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to filter loads."
    return 1
  fi
}

# Function to filter loads by truck type
filter_loads_by_truck_type() {
  local truck_type=$1
  print_message "$YELLOW" "Filtering loads by truck type: $truck_type..."

  local response=$(curl -s -X GET "$BASE_URL/load?truckType=$truck_type")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Loads filtered successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to filter loads."
    return 1
  fi
}

# Main test function
run_load_tests() {
  print_message "$YELLOW" "Starting Load API tests..."

  # Check if the application is running
  if ! check_app_running; then
    return 1
  fi

  # Create a load
  print_message "$YELLOW" "Creating a new load..."
  local response=$(curl -s -X POST "$BASE_URL/load" \
    -H "Content-Type: application/json" \
    -d '{
      "shipperId": "shipper123",
      "facility": {
        "loadingPoint": "Delhi",
        "unloadingPoint": "Mumbai",
        "loadingDate": "2023-06-01T10:00:00",
        "unloadingDate": "2023-06-03T18:00:00"
      },
      "productType": "Electronics",
      "truckType": "Open",
      "noOfTrucks": 2,
      "weight": 1500.0,
      "comment": "Handle with care"
    }')

  # Extract the load ID from the response
  local load_id=$(echo "$response" | grep -o '"id":"[^"]*' | cut -d'"' -f4)

  if [ -n "$load_id" ]; then
    print_message "$GREEN" "Load created successfully with ID: $load_id"
  else
    print_message "$RED" "Failed to create load. Response: $response"
    return 1
  fi

  # Get all loads
  if ! get_all_loads; then
    return 1
  fi

  # Get load by ID
  print_message "$YELLOW" "Getting load by ID: $load_id"
  if ! get_load_by_id "$load_id"; then
    return 1
  fi

  # Update load
  if ! update_load "$load_id"; then
    return 1
  fi

  # Filter loads by shipper ID
  if ! filter_loads_by_shipper "shipper123"; then
    return 1
  fi

  # Filter loads by truck type
  if ! filter_loads_by_truck_type "Closed"; then
    return 1
  fi

  # Delete load
  if ! delete_load "$load_id"; then
    return 1
  fi

  print_message "$GREEN" "All Load API tests completed successfully!"
  return 0
}

# Run the tests
run_load_tests
exit $?
