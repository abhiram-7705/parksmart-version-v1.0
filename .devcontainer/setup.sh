#!/bin/bash
echo "Setting up ParkSmart..."

# Wait for MySQL to be ready
echo "Waiting for MySQL..."
until mysqladmin ping -h localhost -u root -ptiger --silent; do
    sleep 2
done
echo "MySQL is ready!"

# Create the database
mysql -u root -ptiger -e "CREATE DATABASE IF NOT EXISTS parksmartdb;"
echo "Database parksmartdb created!"

# Install Angular dependencies
cd parksmart-ng
npm install
echo "Angular dependencies installed!"

echo "Setup complete! Now start the apps:"
echo "Terminal 1: cd ParkSmart_Backend/ParkSmart && ./mvnw spring-boot:run -Dspring-boot.run.profiles=codespaces"
echo "Terminal 2: cd parksmart-ng && ng serve --host 0.0.0.0 --port 4200 --disable-host-check"
