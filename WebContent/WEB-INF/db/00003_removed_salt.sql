ALTER TABLE customer
DROP COLUMN salt;

ALTER TABLE customer 
CHANGE COLUMN password password CHAR(80) NOT NULL DEFAULT '' ;
