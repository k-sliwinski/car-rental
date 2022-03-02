DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS verification_tokens;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS companies;

CREATE TABLE users (
    id BIGSERIAL NOT NULL PRIMARY KEY,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	email VARCHAR(150),
	password VARCHAR(250),
	role VARCHAR(20),
	enabled VARCHAR(10) NOT NULL
);
CREATE TABLE verification_tokens (
    id BIGSERIAL NOT NULL PRIMARY KEY,
	token VARCHAR(150),
	user_id INT,
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE TABLE companies (
    id BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(50),
	country VARCHAR(50),
	city VARCHAR(50),
	street_address VARCHAR(100)
);
CREATE TABLE cars (
    id BIGSERIAL NOT NULL PRIMARY KEY,
	brand VARCHAR(50),
	model VARCHAR(50),
	year VARCHAR(4),
	color VARCHAR(50),
	daily_fee INT,
	rent_date DATE,
	available VARCHAR(10) NOT NULL,
	company_id INT NOT NULL,
	user_id INT,
	FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users (id)
);