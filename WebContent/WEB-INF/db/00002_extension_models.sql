CREATE TABLE `bookList` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(60) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `bookListItem` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `bookListId` INT NOT NULL,
  `bookId` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bookListItem_1_idx` (`bookId` ASC),
  INDEX `fk_bookListItem_2_idx` (`bookListId` ASC),
  CONSTRAINT `fk_bookListItem_1`
    FOREIGN KEY (`bookId`)
    REFERENCES `book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookListItem_2`
    FOREIGN KEY (`bookListId`)
    REFERENCES `bookList` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `review` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `rating` INT NOT NULL,
  `review` VARCHAR(500) NULL,
  `customer` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_review_1_idx` (`customer` ASC),
  CONSTRAINT `fk_review_1`
    FOREIGN KEY (`customer`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `reviewVote` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `vote` TINYINT(1) NOT NULL,
  `review` INT(10) UNSIGNED NOT NULL,
  `customer` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_reviewVote_1_idx` (`review` ASC),
  INDEX `fk_reviewVote_2_idx` (`customer` ASC),
  CONSTRAINT `fk_reviewVote_1`
    FOREIGN KEY (`review`)
    REFERENCES `review` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviewVote_2`
    FOREIGN KEY (`customer`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `review` 
ADD COLUMN `book` INT(10) UNSIGNED NOT NULL AFTER `customer`,
ADD INDEX `fk_review_2_idx` (`book` ASC);
ALTER TABLE `review` 
ADD CONSTRAINT `fk_review_2`
  FOREIGN KEY (`book`)
  REFERENCES `book` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
