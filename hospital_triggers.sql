DROP TRIGGER IF EXISTS after_users_insert;
DROP TRIGGER IF EXISTS after_medications_update;
DROP TRIGGER IF EXISTS after_prescriptions_insert;
DROP TRIGGER IF EXISTS after_procedures_patients_insert;
DROP TRIGGER IF EXISTS after_users_update;

DELIMITER //
-- COMPLETED
CREATE TRIGGER after_users_insert AFTER INSERT ON users FOR EACH ROW
BEGIN
	INSERT INTO `log_create_user` (entry) VALUES (CONCAT("User '", NEW.username, "' created"));
END //

-- COMPLETED
CREATE TRIGGER after_medications_update AFTER UPDATE ON medications FOR EACH ROW
BEGIN
	IF NEW.stock < 6 THEN
		INSERT INTO notifications (entry) VALUES (CONCAT("Medication ", NEW.medication_name, " stock is ", NEW.stock));
	END IF;
END //

CREATE TRIGGER after_prescriptions_insert AFTER INSERT ON prescriptions FOR EACH ROW
BEGIN
	INSERT INTO log_prescriptions (entry) VALUES (CONCAT("Prescription for "));
END //

CREATE TRIGGER after_procedures_patients_insert AFTER INSERT ON procedures_patients FOR EACH ROW
BEGIN
	DECLARE procedure_cost FLOAT;
    
    SELECT cost INTO procedure_cost
		FROM procedures
        WHERE procedure_id=NEW.procedure_id;
	
    INSERT INTO invoices VALUES (null, NEW.patient_id, procedure_cost);
END //

-- COMPLETED
CREATE TRIGGER after_users_update AFTER UPDATE ON users FOR EACH ROW
BEGIN
	-- only if a raise was issued
	IF OLD.salary <> NEW.salary
    THEN
		INSERT INTO log_raise (entry) VALUES (CONCAT("Salary of doctor with id ", NEW.user_id, " changed from ", OLD.salary, " to ", NEW.salary));
    END IF;
END //
