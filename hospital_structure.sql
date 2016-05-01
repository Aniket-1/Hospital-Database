DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS log_prescriptions;
DROP TABLE IF EXISTS log_procedures;
DROP TABLE IF EXISTS log_create_user;
DROP TABLE IF EXISTS log_raise;

DROP TABLE IF EXISTS invoices;

DROP TABLE IF EXISTS procedures_patients;
DROP TABLE IF EXISTS procedures_doctors;
DROP TABLE IF EXISTS procedures;

DROP TABLE IF EXISTS prescriptions_medications;
DROP TABLE IF EXISTS medications;
DROP TABLE IF EXISTS prescriptions;

DROP TABLE IF EXISTS appointments;

DROP TABLE IF EXISTS family_doctors;
-- DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS `users` (
	`user_id` INT PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(50) UNIQUE NOT NULL,
	`salt` VARCHAR(30),
	`hash` VARBINARY(256),
    `firstname` VARCHAR(50) NOT NULL,
	`lastname` VARCHAR(50) NOT NULL,
	-- format: 1234567890
	`phone` VARCHAR(10) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
    -- (only for admins and doctors)
    `salary` FLOAT,
    -- (only for patients) patient's notes
    `notes` VARCHAR(255),
    `type` ENUM('Admin', 'Doctor', 'Patient') NOT NULL
);

CREATE TABLE `family_doctors` (
	`patient_id` INT,
    `doctor_id` INT,
	FOREIGN KEY (`patient_id`)
		REFERENCES `users`(`user_id`),
	FOREIGN KEY (`doctor_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `appointments` (
	`appointment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `patient_id` INT,
    `doctor_id` INT,
    `date` DATE NOT NULL,
    `notes` VARCHAR(255),
    FOREIGN KEY (`patient_id`)
		REFERENCES `users`(`user_id`),
    FOREIGN KEY (`doctor_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `prescriptions` (
	`prescription_id` INT PRIMARY KEY AUTO_INCREMENT,
	`patient_id` INT,
    FOREIGN KEY (`patient_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `medications` (
	`medication_id` INT PRIMARY KEY AUTO_INCREMENT,
    `medication_name` VARCHAR(255) NOT NULL,
    `cost` FLOAT NOT NULL,
    `stock` INT NOT NULL
);

CREATE TABLE `prescriptions_medications` (
    `prescription_id` INT,
    `medication_id` INT,
    FOREIGN KEY (`prescription_id`)
		REFERENCES `prescriptions`(`prescription_id`),
    FOREIGN KEY (`medication_id`)
		REFERENCES `medications`(`medication_id`)
);

CREATE TABLE `procedures` (
	`procedure_id` INT PRIMARY KEY AUTO_INCREMENT,
    `procedure_name` VARCHAR(255) NOT NULL,
    `cost` FLOAT NOT NULL
);

CREATE TABLE `procedures_doctors` (
	`procedure_id` INT,
	`doctor_id` INT,
    FOREIGN KEY (`procedure_id`)
		REFERENCES `procedures`(`procedure_id`),
    FOREIGN KEY (`doctor_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `procedures_patients` (
	`procedure_id` INT,
	`patient_id` INT,
    FOREIGN KEY (`procedure_id`)
		REFERENCES `procedures`(`procedure_id`),
    FOREIGN KEY (`patient_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `invoices` (
	`invoice_id` INT PRIMARY KEY AUTO_INCREMENT,
    `patient_id` INT,
    `invoice_amount` INT NOT NULL,
    FOREIGN KEY (`patient_id`)
		REFERENCES `users`(`user_id`)
);

-- Log tables
CREATE TABLE `log_create_user` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_raise` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_procedures` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_prescriptions` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `notifications` (
	`notification_id` INT PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entry` VARCHAR(255) NOT NULL
);

-- CREATE INDEX `idx_table_name` ON `table` (`column`);

-- default user with admin privileges.
-- INSERT INTO users VALUES (null, 'root', null, null, 'root', 'user', '5141234567', 'root@root.com', null, null, 'Admin');

COMMIT;
