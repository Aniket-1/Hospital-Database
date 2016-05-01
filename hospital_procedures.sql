DROP PROCEDURE IF EXISTS get_procedures;
DROP PROCEDURE IF EXISTS get_prescriptions;
DROP PROCEDURE IF EXISTS raise_salary;

DELIMITER //

-- PROCEDURES
CREATE PROCEDURE raise_salary(IN doctor INT, IN raise FLOAT)
BEGIN
	DECLARE current_salary FLOAT;
    DECLARE new_salary FLOAT DEFAULT current_salary;
    
    SELECT salary INTO current_salary
		FROM users
        WHERE user_id=doctor;

	SET new_salary = current_salary + raise;
    
    UPDATE users SET salary=new_salary
		WHERE user_id=doctor;
END //

CREATE PROCEDURE get_prescriptions(IN patient INT)
BEGIN
	SELECT prescriptions.prescription_id, medication_name
		FROM prescriptions
		INNER JOIN prescriptions_medications ON prescriptions.prescription_id=prescriptions_medications.prescription_id
		INNER JOIN medications ON medications.medication_id=prescriptions_medications.medication_id
		WHERE patient_id=patient
		ORDER BY prescriptions.prescription_id;
END //

CREATE PROCEDURE get_procedures(IN patient INT)
BEGIN
	SELECT procedure_name FROM procedures
		INNER JOIN procedures_patients ON procedures.procedure_id=procedures_patients.procedure_id
		INNER JOIN users ON procedures_patients.patient_id=user_id
		WHERE patient_id=patient;
END //
