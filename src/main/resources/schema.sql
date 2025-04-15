-- This script will be executed by Spring Boot during application startup
-- It will create the database schema if it doesn't exist

-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS public;

-- Set the search path to the public schema
SET search_path TO public;
