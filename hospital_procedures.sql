DROP PROCEDURE IF EXISTS raise_salary;
DROP PROCEDURE IF EXISTS book_appointment;
DROP PROCEDURE IF EXISTS cancel_appointment;
DROP PROCEDURE IF EXISTS get_prescriptions;
DROP PROCEDURE IF EXISTS get_procedures;
DROP PROCEDURE IF EXISTS get_invoices;
DROP PROCEDURE IF EXISTS get_family_doctor;
DROP PROCEDURE IF EXISTS get_specialists;
DROP PROCEDURE IF EXISTS prescribe_medication;
DROP PROCEDURE IF EXISTS perform_procedure;

DELIMITER //

/* Raises a given doctor's salary by the given amount */
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

/* Books an appointment for a given patient with a given doctor, on a given date */
CREATE PROCEDURE book_appointment(IN patient INT, IN doctor INT, IN appointment_date DATE, IN note VARCHAR(255))
BEGIN
	IF appointment_date < CURDATE()
    THEN 
		SIGNAL SQLSTATE '45000'
        SET message_text = "Cannot create appointment in the past";
	END IF;
    
	INSERT INTO appointments (patient_id, doctor_id, date, notes)
		VALUES (patient, doctor, appointment_date, note);
END //

/* Cancel a given appointment */
CREATE PROCEDURE cancel_appointment(IN appointment INT)
BEGIN
	DELETE FROM appointments WHERE appointment_id=appointment;
END //

/* Gets all prescriptions of a given patient */
CREATE PROCEDURE get_prescriptions(IN patient INT)
BEGIN
	SELECT prescription_id, medication_name
		FROM prescriptions
		INNER JOIN medications ON medications.medication_id=prescriptions.medication_id
		WHERE patient_id=patient
		ORDER BY prescription_id;
END //
 
/* Gets procedures of a given patient */
CREATE PROCEDURE get_procedures(IN patient INT)
BEGIN
	SELECT procedures.procedure_id, procedure_name FROM procedures
		INNER JOIN procedures_patients ON procedures.procedure_id=procedures_patients.procedure_id
		INNER JOIN users ON patient_id=user_id
		WHERE patient_id=patient
        ORDER BY procedure_id DESC;
END //

/* Gets all invoices of a given patient */
CREATE PROCEDURE get_invoices(IN patient INT)
BEGIN
	SELECT invoice_id, invoice_amount
		FROM invoices
        WHERE patient_id=patient
        ORDER BY invoice_id DESC;
END//

/* Gets family doctor of given patient */
CREATE PROCEDURE get_family_doctor(IN patient INT)
BEGIN
	SELECT doctor_id, firstname, lastname
		FROM family_doctors
		INNER JOIN users ON user_id=doctor_id
		WHERE patient_id=patient;
END //

/* Gets all specialists */
CREATE PROCEDURE get_specialists()
BEGIN
	SELECT doctor_id, firstname, lastname, procedure_name
		FROM users
        INNER JOIN procedures_doctors ON user_id=doctor_id
        INNER JOIN procedures ON procedures_doctors.procedure_id=procedures.procedure_id;
END //

/* Prescribes a given medication to a given patient */
CREATE PROCEDURE prescribe_medication(IN patient INT, IN medication INT, IN doctor INT)
BEGIN
	DECLARE medication_stock INT;
    DECLARE medication_cost FLOAT;
    
    DECLARE p_name VARCHAR(255);
    DECLARE d_name VARCHAR(255);
    DECLARE m_name VARCHAR(255);
    
    -- Get medication info
    SELECT medication_name, cost, stock INTO m_name, medication_cost, medication_stock
		FROM medications
        WHERE medication_id=medication;
	
    IF medication_stock = 0
    THEN
		SIGNAL SQLSTATE '45000'
		SET message_text = "There is no medication left";
	END IF;
    
    -- Update stock
    UPDATE medications SET stock = (medication_stock - 1)
		WHERE medication_id=medication;
	
    -- Prescribe medication
    INSERT INTO prescriptions VALUES (null, patient, medication);
    
    -- Create invoice
    INSERT INTO invoices VALUES (null, patient, medication_cost);
    
    -- Get patient/doctor info
    SELECT CONCAT(firstname, " ", lastname) INTO p_name
		FROM users
        WHERE user_id=patient;
        
	SELECT CONCAT(firstname, " ", lastname) INTO d_name
		FROM users
        WHERE user_id=doctor;

    -- Create log
    INSERT INTO logs_admin (entry) VALUES (CONCAT(m_name, " given to ", p_name, " by ", d_name));
END //

/* Performs a given procedure on a given patient */
CREATE PROCEDURE perform_procedure(IN patient INT, IN procedureID INT, IN doctor INT)
BEGIN
	DECLARE procedure_cost INT;
	
    DECLARE p_name VARCHAR(255);
    DECLARE d_name VARCHAR(255);
    DECLARE pr_name VARCHAR(255);
    
    SELECT procedure_name, cost INTO pr_name, procedure_cost
		FROM procedures
        WHERE procedure_id=procedureID;
	
	INSERT INTO procedures_patients VALUES (procedureID, patient);
    
    -- Create invoice
    INSERT INTO invoices VALUES (null, patient, procedure_cost);
    
    -- Get patient/doctor info
    SELECT CONCAT(firstname, " ", lastname) INTO p_name
		FROM users
        WHERE user_id=patient;
        
	SELECT CONCAT(firstname, " ", lastname) INTO d_name
		FROM users
        WHERE user_id=doctor;
        
    -- Create log
    INSERT INTO logs_admin (entry) VALUES (CONCAT(pr_name, " performed on ", p_name, " by ", d_name));
END //

