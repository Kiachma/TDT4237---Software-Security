CREATE TABLE booklist (
  booklist_id int(10) unsigned NOT NULL AUTO_INCREMENT,
  customer_id int(10) unsigned NOT NULL,
  title varchar(60) NOT NULL,
  description varchar(200) DEFAULT NULL,
  PRIMARY KEY (booklist_id),
  KEY fk_booklist_1_idx (customer_id),
  CONSTRAINT fk_booklist_1 FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


CREATE TABLE booklist_x_book (
  booklist_id int(10) unsigned NOT NULL,
  book_title_id int(10) unsigned NOT NULL,
  KEY fk_booklist_x_book_1_idx (booklist_id),
  KEY fk_booklist_x_book_2_idx (book_title_id),
  CONSTRAINT fk_booklist_x_book_1 FOREIGN KEY (booklist_id) REFERENCES booklist (booklist_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_booklist_x_book_2 FOREIGN KEY (book_title_id) REFERENCES title (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE captchacount (
    email VARCHAR(60) NOT NULL,
    count INT);

CREATE TABLE review (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  rating INT NOT NULL,
  review VARCHAR(500) NULL,
  customer INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_review_1_idx (customer ASC),
  CONSTRAINT fk_review_1
    FOREIGN KEY (customer)
    REFERENCES customer (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE reviewVote (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  vote TINYINT(1) NOT NULL,
  review INT(10) UNSIGNED NOT NULL,
  customer INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_reviewVote_1_idx (review ASC),
  INDEX fk_reviewVote_2_idx (customer ASC),
  CONSTRAINT fk_reviewVote_1
    FOREIGN KEY (review)
    REFERENCES review (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_reviewVote_2
    FOREIGN KEY (customer)
    REFERENCES customer (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE review 
ADD COLUMN book INT(10) UNSIGNED NOT NULL AFTER customer,
ADD INDEX fk_review_2_idx (book ASC);
ALTER TABLE review 
ADD CONSTRAINT fk_review_2
  FOREIGN KEY (book)
  REFERENCES book (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
