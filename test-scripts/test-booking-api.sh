#!/bin/bash

# Test script for Booking API
# This script tests the CRUD operations for the Booking API

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

  if curl -s "$BASE_URL/booking" > /dev/null; then
    print_message "$GREEN" "Application is running."
    return 0
  else
    print_message "$RED" "Application is not running. Please start the application first."
    return 1
  fi
}

# Function to create a load (needed for booking tests)
create_load() {
  print_message "$YELLOW" "Creating a new load for booking tests..."

  local response=$(curl -s -X POST "$BASE_URL/load" \
    -H "Content-Type: application/json" \
    -d '{
      "shipperId": "shipper456",
      "facility": {
        "loadingPoint": "Hyderabad",
        "unloadingPoint": "Pune",
        "loadingDate": "2023-07-01T10:00:00",
        "unloadingDate": "2023-07-03T18:00:00"
      },
      "productType": "Chemicals",
      "truckType": "Tanker",
      "noOfTrucks": 1,
      "weight": 2000.0,
      "comment": "Hazardous material"
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

# Function to create a booking
create_booking() {
  local load_id=$1
  print_message "$YELLOW" "Creating a new booking for load ID: $load_id..."

  local json_payload='{"loadId":"'$load_id'","transporterId":"transporter123","proposedRate":25000.0,"comment":"Can deliver early"}'
  local response=$(curl -s -X POST "$BASE_URL/booking" \
    -H "Content-Type: application/json" \
    -d "$json_payload")

  # Extract the booking ID from the response
  local booking_id=$(echo "$response" | grep -o '"id":"[^"]*' | cut -d'"' -f4)

  if [ -n "$booking_id" ]; then
    print_message "$GREEN" "Booking created successfully with ID: $booking_id"
    echo "$booking_id"
  else
    print_message "$RED" "Failed to create booking. Response: $response"
    return 1
  fi
}

# Function to get all bookings
get_all_bookings() {
  print_message "$YELLOW" "Getting all bookings..."

  local response=$(curl -s -X GET "$BASE_URL/booking")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Bookings retrieved successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to retrieve bookings."
    return 1
  fi
}

# Function to get a booking by ID
get_booking_by_id() {
  local booking_id=$1

  local response=$(curl -s -X GET "$BASE_URL/booking/$booking_id")

  if [[ "$response" == *"$booking_id"* ]]; then
    print_message "$GREEN" "Booking retrieved successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to retrieve booking. Response: $response"
    return 1
  fi
}

# Function to update a booking
update_booking() {
  local booking_id=$1
  local load_id=$2
  print_message "$YELLOW" "Updating booking with ID: $booking_id..."

  local json_payload='{"loadId":"'$load_id'","transporterId":"transporter456","proposedRate":30000.0,"comment":"Updated booking"}'
  local response=$(curl -s -X PUT "$BASE_URL/booking/$booking_id" \
    -H "Content-Type: application/json" \
    -d "$json_payload")

  if [[ "$response" == *"$booking_id"* ]]; then
    print_message "$GREEN" "Booking updated successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to update booking. Response: $response"
    return 1
  fi
}

# Function to update booking status
update_booking_status() {
  local booking_id=$1
  local status=$2
  print_message "$YELLOW" "Updating booking status to $status for booking ID: $booking_id..."

  local response=$(curl -s -X PATCH "$BASE_URL/booking/$booking_id/status?status=$status")

  if [[ "$response" == *"$booking_id"* && "$response" == *"$status"* ]]; then
    print_message "$GREEN" "Booking status updated successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to update booking status. Response: $response"
    return 1
  fi
}

# Function to filter bookings by load ID
filter_bookings_by_load() {
  local load_id=$1
  print_message "$YELLOW" "Filtering bookings by load ID: $load_id..."

  local response=$(curl -s -X GET "$BASE_URL/booking?loadId=$load_id")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Bookings filtered successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to filter bookings."
    return 1
  fi
}

# Function to filter bookings by transporter ID
filter_bookings_by_transporter() {
  local transporter_id=$1
  print_message "$YELLOW" "Filtering bookings by transporter ID: $transporter_id..."

  local response=$(curl -s -X GET "$BASE_URL/booking?transporterId=$transporter_id")

  if [ -n "$response" ]; then
    print_message "$GREEN" "Bookings filtered successfully."
    echo "$response"
  else
    print_message "$RED" "Failed to filter bookings."
    return 1
  fi
}

# Function to delete a booking
delete_booking() {
  local booking_id=$1
  print_message "$YELLOW" "Deleting booking with ID: $booking_id..."

  local http_code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/booking/$booking_id")

  if [ "$http_code" -eq 204 ]; then
    print_message "$GREEN" "Booking deleted successfully."
    return 0
  else
    print_message "$RED" "Failed to delete booking. HTTP code: $http_code"
    return 1
  fi
}

# Main test function
run_booking_tests() {
  print_message "$YELLOW" "Starting Booking API tests..."

  # Check if the application is running
  if ! check_app_running; then
    return 1
  fi

  # Create a load for booking tests
  print_message "$YELLOW" "Creating a new load for booking tests..."
  local response=$(curl -s -X POST "$BASE_URL/load" \
    -H "Content-Type: application/json" \
    -d '{
      "shipperId": "shipper456",
      "facility": {
        "loadingPoint": "Hyderabad",
        "unloadingPoint": "Pune",
        "loadingDate": "2023-07-01T10:00:00",
        "unloadingDate": "2023-07-03T18:00:00"
      },
      "productType": "Chemicals",
      "truckType": "Tanker",
      "noOfTrucks": 1,
      "weight": 2000.0,
      "comment": "Hazardous material"
    }')

  # Extract the load ID from the response
  local load_id=$(echo "$response" | grep -o '"id":"[^"]*' | cut -d'"' -f4)

  if [ -n "$load_id" ]; then
    print_message "$GREEN" "Load created successfully with ID: $load_id"
  else
    print_message "$RED" "Failed to create load. Response: $response"
    return 1
  fi

  # Create a booking
  print_message "$YELLOW" "Creating a new booking..."
  local json_payload='{"loadId":"'$load_id'","transporterId":"transporter123","proposedRate":25000.0,"comment":"Can deliver early"}'
  local response=$(curl -s -X POST "$BASE_URL/booking" \
    -H "Content-Type: application/json" \
    -d "$json_payload")

  # Extract the booking ID from the response
  local booking_id=$(echo "$response" | grep -o '"id":"[^"]*' | cut -d'"' -f4)

  if [ -n "$booking_id" ]; then
    print_message "$GREEN" "Booking created successfully with ID: $booking_id"
  else
    print_message "$RED" "Failed to create booking. Response: $response"
    return 1
  fi

  # Get all bookings
  if ! get_all_bookings; then
    return 1
  fi

  # Get booking by ID
  print_message "$YELLOW" "Getting booking by ID: $booking_id"
  if ! get_booking_by_id "$booking_id"; then
    return 1
  fi

  # Update booking
  if ! update_booking "$booking_id" "$load_id"; then
    return 1
  fi

  # Update booking status
  if ! update_booking_status "$booking_id" "ACCEPTED"; then
    return 1
  fi

  # Filter bookings by load ID
  if ! filter_bookings_by_load "$load_id"; then
    return 1
  fi

  # Filter bookings by transporter ID
  if ! filter_bookings_by_transporter "transporter456"; then
    return 1
  fi

  # Delete booking
  if ! delete_booking "$booking_id"; then
    return 1
  fi

  print_message "$GREEN" "All Booking API tests completed successfully!"
  return 0
}

# Run the tests
run_booking_tests
exit $?
