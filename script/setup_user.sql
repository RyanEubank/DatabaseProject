DROP SCHEMA IF EXISTS Library;
CREATE SCHEMA Library;

DROP USER IF EXISTS 'librarian'@'localhost';
CREATE USER 'librarian'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON library.* TO 'librarian'@'localhost';
FLUSH PRIVILEGES;