DROP TRIGGER IF EXISTS after_users_insert;
DROP TRIGGER IF EXISTS after_medications_update;
DROP TRIGGER IF EXISTS after_procedures_insert;

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
DROP TABLE IF EXISTS users;

CREATE TABLE `users` (
	`user_id` INT PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(50) UNIQUE NOT NULL,
	`salt` VARCHAR(30),
	`hash` VARBINARY(256),
    `firstname` VARCHAR(50) NOT NULL,
	`lastname` VARCHAR(50) NOT NULL,
	-- format: (000) 000-0000
	`phone` VARCHAR(10) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
    -- (only for admins and doctors)
    `salary` FLOAT,
    -- (only for patients) patient's notes and family doctor
    `notes` VARCHAR(255),
    `type` ENUM('Admin','Doctor','Patient') NOT NULL
);

CREATE TABLE `family_doctors` (
	`user_id` INT,
    `doctor_id` INT,
	FOREIGN KEY (`user_id`)
		REFERENCES `users`(`user_id`),
	FOREIGN KEY (`doctor_id`)
		REFERENCES `users`(`user_id`)
);

CREATE TABLE `appointments` (
	`appointment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `patient_id` INT,
    `doctor_id` INT,
    -- format: 'YYYY-MM-DD HH:MM:SS'
    `date` DATETIME NOT NULL,
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
	`time` DATETIME NOT NULL DEFAULT NOW(),
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_raise` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`time` DATETIME NOT NULL DEFAULT NOW(),
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_procedures` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`time` DATETIME NOT NULL DEFAULT NOW(),
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `log_prescriptions` (
	`log_id` INT PRIMARY KEY AUTO_INCREMENT,
	`time` DATETIME NOT NULL DEFAULT NOW(),
    `entry` VARCHAR(255) NOT NULL
);

CREATE TABLE `notifications` (
	`notification_id` INT PRIMARY KEY AUTO_INCREMENT,
    `time` DATETIME NOT NULL DEFAULT NOW(),
    `entry` VARCHAR(255) NOT NULL
);

DELIMITER //
CREATE TRIGGER after_users_insert AFTER INSERT ON users FOR EACH ROW
BEGIN
	INSERT INTO `log_create_user` VALUES (NULL, NOW(), CONCAT("User ", NEW.username, " created"));
END //

CREATE TRIGGER after_medications_update AFTER UPDATE ON medications FOR EACH ROW
BEGIN
	IF NEW.stock < 6 THEN
		INSERT INTO notifications (entry) VALUES (CONCAT("Medication ", NEW.medication_name, " stock is below 5"));
	END IF;
END //

CREATE TRIGGER after_prescriptions_insert AFTER INSERT ON prescriptions FOR EACH ROW
BEGIN
	INSERT INTO log_procedures VALUES (null, NOW(), CONCAT("Prescription for "));
END //

CREATE TRIGGER after_procedures_insert AFTER INSERT ON procedures FOR EACH ROW
BEGIN
	INSERT INTO log_procedures VALUES (null, NOW(), CONCAT("Procedure on "));
END //
DELIMITER ;

-- CREATE INDEX `idx_book__genre` ON `book` (`genre`);

-- INSERT INTO book VALUES
-- (null,'','2008-02-01',0),
-- (null, '', '3952-02-12', 0);

-- default user with admin privileges.
INSERT INTO users VALUES (null, 'root', null, null, 'root', 'user', '51471NUX<3', 'root@root.com', null, null, 'Admin');

COMMIT;
