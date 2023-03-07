DROP TABLE IF EXISTS esm_employee_data;

CREATE TABLE esm_employee_data(
emp_id VARCHAR(50) PRIMARY KEY NOT NULL,
emp_login VARCHAR(50) UNIQUE NOT NULL,
emp_name VARCHAR(75)  NOT NULL,
emp_salary REAL NOT NULL);
