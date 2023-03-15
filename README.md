# be_esm_app
Backend source code of MVP Employee Salary Management Application

## About the application
ESM application is a backend Java Spring Boot application developed in order to help the HR department to manage the employees' salaries. This application helps to expose REST based web services. Using this application we can perform below functionalities.
1) Add new employee data
2) Update employee data
3) Delete employee data
4) View employee data
5) Search for employee data using employee id
6) Display all employee records in dashboard. Filter using salary. Sort by ID, Login, Name or Salary fields in ascending or descending order
7) Upload employees' data using CSV file

## Tecnologies / Frameworks/ Libraries
1) Java
2) Spring Boot
3) Spring Data
4) H2 database
5) Lombok
6) Opencsv

## Installation
1) Download or check out source code for be_esm_app
	git clone https://github.com/kavyasreedharan/be_esm_app.git
2) Import as maven project in your ide (if you plan to run the application from your local machine and IDE)
3) Build backend application using below maven command from \be_esm_app\esmapp folder location
	mvn clean install
4) Start your application from IDE or using below command from location - be_esm_app\esmapp\target
	java -jar esmapp-0.0.1-SNAPSHOT.jar
   Note: Application is configured to start in 8080 port. If this port is already in use, kindly change the port number in application.properties file.
	server.port = 9090
	
## Note
Test data for file upload is available in below location
	be_esm_app\esmapp\src\test\resources\
	
# DB Schema
	CREATE TABLE esm_employee_data(
	id VARCHAR(50) PRIMARY KEY NOT NULL,
	login VARCHAR(50) UNIQUE NOT NULL,
	name VARCHAR(75)  NOT NULL,
	salary REAL NOT NULL);



