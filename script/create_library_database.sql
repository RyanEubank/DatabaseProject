DROP SCHEMA IF EXISTS Library;
CREATE SCHEMA Library;

DROP TABLE IF EXISTS Library.Book_Authors;
DROP TABLE IF EXISTS Library.Authors;
DROP TABLE IF EXISTS Library.Fines;
DROP TABLE IF EXISTS Library.Book_Loans;
DROP TABLE IF EXISTS Library.Book;
DROP TABLE IF EXISTS Library.Borrower;

CREATE TABLE Library.Book(
	isbn		CHAR(17) CHARACTER SET UTF8MB4,
	title		VARCHAR(200) CHARACTER SET UTF8MB4	NOT NULL,
	CONSTRAINT pk_book PRIMARY KEY (isbn)
);

CREATE TABLE Library.Authors(
	author_id	INT	AUTO_INCREMENT	NOT NULL,
	name		VARCHAR(50) CHARACTER SET UTF8MB4	NOT NULL,
	CONSTRAINT pk_author PRIMARY KEY (author_id)

	-- Cannot have a unique key for author in case 2 authors 
	-- have the same name, this will result in multiple entries 
	-- for authors who write multiple books but no way of knowing 
	-- from csv whether duplicate is actually the same person
);

CREATE TABLE Library.Book_Authors(
	author_id	INT,
	isbn		CHAR(17) CHARACTER SET UTF8MB4,
	CONSTRAINT pk_book_author PRIMARY KEY (author_id, isbn),
	CONSTRAINT fk_authorid_authors FOREIGN KEY (author_id) REFERENCES Library.Authors(author_id),
	CONSTRAINT fk_isbn_bookauthors FOREIGN KEY (isbn) REFERENCES Library.Book(isbn)
);

CREATE TABLE Library.Borrower(
	card_id		INT	AUTO_INCREMENT,
	ssn		CHAR(11) CHARACTER SET UTF8MB4	NOT NULL,
	bname		VARCHAR(50) CHARACTER SET UTF8MB4	NOT NULL,
	address		VARCHAR(100) CHARACTER SET UTF8MB4,
	phone		CHAR(14) CHARACTER SET UTF8MB4,
	CONSTRAINT pk_borrower PRIMARY KEY (card_id),
	CONSTRAINT uk_borrower_ssn UNIQUE KEY (ssn),
	CONSTRAINT uk_borrower_name_addr_phone UNIQUE KEY (bname, address, phone)
);

CREATE TABLE Library.Book_Loans(
	loan_id		INT	AUTO_INCREMENT,
	isbn		CHAR(17) CHARACTER SET UTF8MB4	NOT NULL,
	card_id		INT		NOT NULL,
	date_out	DATE		NOT NULL,
	due_date	DATE		NOT NULL,
	date_in		DATE,
	CONSTRAINT pk_book_loans PRIMARY KEY (loan_id),
	CONSTRAINT uk_loans UNIQUE KEY (isbn),
	CONSTRAINT fk_isbn_bookloans FOREIGN KEY (isbn) REFERENCES Library.Book(isbn),
	CONSTRAINT fk_cardid_borrower FOREIGN KEY (card_id) REFERENCES Library.Borrower(card_id)
);

CREATE TABLE Library.Fines(
	loan_id		INT,
	fine_amt	DECIMAL(10, 2)	NOT NULL,
	paid		BOOL,
	CONSTRAINT pk_fines PRIMARY KEY (loan_id),
	CONSTRAINT fk_loanid_book_loans FOREIGN KEY (loan_id) REFERENCES Library.Book_Loans(loan_id)
);


DROP TRIGGER IF EXISTS Library.Loan_Maximum;
DELIMITER $$
CREATE TRIGGER Library.Loan_Maximum BEFORE INSERT ON Library.Book_Loans FOR EACH ROW 
BEGIN
	IF NEW.card_id IN (
		SELECT temp.card_id FROM (SELECT bl.card_id, COUNT(*) as num_loans FROM Library.Book_Loans as bl
		GROUP BY bl.card_id HAVING num_loans >= 3) AS temp
	) THEN SET NEW.card_id = NULL;
	-- intentionally violate not null constraint to deny insert.
	END IF;
END$$
DELIMITER ;