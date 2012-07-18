CREATE SCHEMA `dalga` DEFAULT CHARACTER SET utf8 COLLATE utf8_turkish_ci;

USE `dalga`;

CREATE  TABLE `dalga`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(45) NOT NULL ,
  `passwd` VARCHAR(45) NOT NULL ,
  `avatar` VARCHAR(120) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`Topic` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `userid` INT NOT NULL ,
  `date` DATETIME NULL ,
  `title` VARCHAR(120) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Topic_User` (`userid` ASC) ,
  CONSTRAINT `fk_Topic_User`
    FOREIGN KEY (`userid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`Post` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `userid` INT NOT NULL ,
  `topicid` INT NOT NULL ,
  `date` DATETIME NOT NULL ,
  `post` TEXT NOT NULL ,
  `reply` INT NULL DEFAULT -1 ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Post_Topic` (`topicid` ASC) ,
  INDEX `fk_Post_User` (`userid` ASC) ,
  CONSTRAINT `fk_Post_Topic`
    FOREIGN KEY (`topicid` )
    REFERENCES `dalga`.`Topic` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Post_User`
    FOREIGN KEY (`userid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`Tag` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`PrivateMessage` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `toid` INT NOT NULL ,
  `fromid` INT NOT NULL ,
  `date` DATETIME NOT NULL ,
  `message` TEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_PrivateMessage_To` (`toid` ASC) ,
  INDEX `fk_PrivateMessage_From` (`fromid` ASC) ,
  CONSTRAINT `fk_PrivateMessage_To`
    FOREIGN KEY (`toid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PrivateMessage_From`
    FOREIGN KEY (`fromid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`LoginLog` (
  `userid` INT NOT NULL ,
  `logindate` DATETIME NOT NULL ,
  `ip` VARCHAR(45) NOT NULL ,
  INDEX `date` (`logindate` ASC) ,
  INDEX `fk_LoginLog_User` (`userid` ASC) ,
  CONSTRAINT `fk_LoginLog_User`
    FOREIGN KEY (`userid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`TopicTags` (
  `topicid` INT NOT NULL ,
  `tagid` INT NOT NULL ,
  INDEX `fk_TopicTags_Topic` (`topicid` ASC) ,
  INDEX `fk_TopicTags_Tag` (`tagid` ASC) ,
  CONSTRAINT `fk_TopicTags_Topic`
    FOREIGN KEY (`topicid` )
    REFERENCES `dalga`.`Topic` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TopicTags_Tag`
    FOREIGN KEY (`tagid` )
    REFERENCES `dalga`.`Tag` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE  TABLE `dalga`.`TopicUsers` (
  `userid` INT NOT NULL ,
  `topicid` INT NOT NULL ,
  INDEX `fk_TopicUsers_Topic` (`topicid` ASC) ,
  INDEX `fk_TopicUsers_User` (`userid` ASC) ,
  CONSTRAINT `fk_TopicUsers_Topic`
    FOREIGN KEY (`topicid` )
    REFERENCES `dalga`.`Topic` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TopicUsers_User`
    FOREIGN KEY (`userid` )
    REFERENCES `dalga`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


INSERT INTO `User` VALUES (4, 'Kadir Anil Turgut', 'kanilturgut@gmail.com', '12345', NULL);
INSERT INTO `User` VALUES (3, 'Batuhan Bardak', 'bbardak37@gmail.com', '12345', NULL);
INSERT INTO `User` VALUES (2, 'Umut Ozan Yildirim', 'uoy1991@gmail.com', '12345', NULL);
INSERT INTO `User` VALUES (1, 'Mustafa Simav', 'mustafa1991@gmail.com', '12345', NULL);
INSERT INTO `TopicUsers` VALUES (4, 4);
INSERT INTO `TopicUsers` VALUES (4, 3);
INSERT INTO `TopicUsers` VALUES (4, 2);
INSERT INTO `TopicUsers` VALUES (4, 1);
INSERT INTO `TopicUsers` VALUES (3, 4);
INSERT INTO `TopicUsers` VALUES (3, 3);
INSERT INTO `TopicUsers` VALUES (3, 2);
INSERT INTO `TopicUsers` VALUES (3, 1);
INSERT INTO `TopicUsers` VALUES (2, 3);
INSERT INTO `TopicUsers` VALUES (2, 2);
INSERT INTO `TopicUsers` VALUES (2, 1);
INSERT INTO `TopicUsers` VALUES (2, 1);
INSERT INTO `TopicUsers` VALUES (1, 4);
INSERT INTO `TopicUsers` VALUES (1, 4);
INSERT INTO `TopicUsers` VALUES (1, 3);
INSERT INTO `TopicUsers` VALUES (1, 2);
INSERT INTO `TopicUsers` VALUES (1, 1);
INSERT INTO `TopicTags` VALUES (4, 8);
INSERT INTO `TopicTags` VALUES (4, 2);
INSERT INTO `TopicTags` VALUES (4, 1);
INSERT INTO `TopicTags` VALUES (3, 5);
INSERT INTO `TopicTags` VALUES (3, 4);
INSERT INTO `TopicTags` VALUES (3, 1);
INSERT INTO `TopicTags` VALUES (2, 7);
INSERT INTO `TopicTags` VALUES (2, 3);
INSERT INTO `TopicTags` VALUES (2, 2);
INSERT INTO `TopicTags` VALUES (2, 1);
INSERT INTO `TopicTags` VALUES (1, 6);
INSERT INTO `TopicTags` VALUES (1, 2);
INSERT INTO `TopicTags` VALUES (1, 1);
INSERT INTO `Topic` VALUES (4, 4, '2012-09-02 12:00:00', 'Yapacak is var mi gencler :D');
INSERT INTO `Topic` VALUES (3, 3, '2012-08-02 15:00:00', 'SQL cok kolay abi..');
INSERT INTO `Topic` VALUES (2, 2, '2012-08-02 12:01:02', 'Arayuz nasil olmus beyler..');
INSERT INTO `Topic` VALUES (1, 1, '2012-07-02 12:00:00', 'JSON kullanmak muhtesem beyler..');
INSERT INTO `Tag` VALUES (8, 'help');
INSERT INTO `Tag` VALUES (7, 'arayuz');
INSERT INTO `Tag` VALUES (6, 'json');
INSERT INTO `Tag` VALUES (5, 'mysql');
INSERT INTO `Tag` VALUES (4, 'sql');
INSERT INTO `Tag` VALUES (3, 'swing');
INSERT INTO `Tag` VALUES (2, 'java');
INSERT INTO `Tag` VALUES (1, 'bil372');
INSERT INTO `PrivateMessage` VALUES (2, 4, 2, '2012-07-01 13:13:43', 'tamamdir calisiyor');
INSERT INTO `PrivateMessage` VALUES (1, 2, 4, '2012-07-01 13:13:12', 'deneme mesajidir..');
INSERT INTO `Post` VALUES (22, 4, 4, '2012-07-02 16:07:30', 'Yaptim zaten onu baskan', 20);
INSERT INTO `Post` VALUES (21, 4, 2, '2012-07-02 15:07:00', 'Yap artik birseyler..', 18);
INSERT INTO `Post` VALUES (20, 4, 1, '2012-07-02 14:05:00', 'Veritabanini doldur abi', 18);
INSERT INTO `Post` VALUES (19, 4, 3, '2012-07-02 14:04:00', 'Git bir su al gel..', 18);
INSERT INTO `Post` VALUES (18, 4, 4, '2012-07-02 13:01:00', 'Evet beyler varmidir yapilcak bir is ?', NULL);
INSERT INTO `Post` VALUES (17, 3, 4, '2012-07-01 12:07:30', 'Sorgulara bakacakmis hoca', 15);
INSERT INTO `Post` VALUES (16, 3, 4, '2012-07-01 12:07:00', 'Ben bile yazmaya basladim..zor degil yani', 13);
INSERT INTO `Post` VALUES (15, 3, 2, '2012-07-01 12:05:00', 'Neye bakacakmis hoca ?', 14);
INSERT INTO `Post` VALUES (14, 3, 1, '2012-07-01 12:04:00', 'Onlar olsun, hoca bakicam demisti', 13);
INSERT INTO `Post` VALUES (13, 3, 3, '2012-07-01 12:01:00', 'Aslinda kolay ama ek isleri yoruyor', NULL);
INSERT INTO `Post` VALUES (12, 2, 3, '2012-06-29 12:04:00', 'Bir Alex degil !', 7);
INSERT INTO `Post` VALUES (11, 2, 4, '2012-06-29 12:03:30', 'Guzel guzel', 7);
INSERT INTO `Post` VALUES (10, 2, 4, '2012-06-29 12:03:00', 'O odunsu swing gorunusu yok en azindan', 8);
INSERT INTO `Post` VALUES (9, 2, 2, '2012-06-29 12:02:30', 'Ya iste guzel olsun diye swt dedim', 8);
INSERT INTO `Post` VALUES (8, 2, 1, '2012-06-29 12:02:00', 'Bastan dedik swing kullan diye', 7);
INSERT INTO `Post` VALUES (7, 2, 2, '2012-06-29 12:01:00', 'Arayuz hos oldu ', NULL);
INSERT INTO `Post` VALUES (6, 1, 4, '2012-06-20 12:04:00', 'Eskiden JSON mu vardi beyler :D', 1);
INSERT INTO `Post` VALUES (5, 1, 4, '2012-06-19 12:03:30', 'Degil mi abi ya..', 4);
INSERT INTO `Post` VALUES (4, 1, 3, '2012-06-19 12:03:00', 'JSON nedir abi, is cikiyo bosuna :D', 1);
INSERT INTO `Post` VALUES (3, 1, 1, '2012-07-19 12:02:30', 'return edecegi deger beli hersey belli mis mis', 2);
INSERT INTO `Post` VALUES (2, 1, 2, '2012-06-18 12:02:00', 'Formati hos aslinda', 1);
INSERT INTO `Post` VALUES (1, 1, 1, '2012-06-18 12:01:00', 'JSON kullanmak muhtesem beyler', NULL);
INSERT INTO `LoginLog` VALUES (4, '2012-07-06 18:12:10', '127.168.50.16');
INSERT INTO `LoginLog` VALUES (1, '2012-07-05 17:12:10', '127.218.11.12');
INSERT INTO `LoginLog` VALUES (2, '2012-07-01 15:12:10', '117.168.10.1');
INSERT INTO `LoginLog` VALUES (4, '2012-07-06 18:12:10', '127.168.50.16');
INSERT INTO `LoginLog` VALUES (1, '2012-07-05 17:12:10', '127.218.11.12');
INSERT INTO `LoginLog` VALUES (2, '2012-07-01 15:12:10', '117.168.10.1');
INSERT INTO `LoginLog` VALUES (2, '2012-06-01 18:12:10', '127.168.50.16');
INSERT INTO `LoginLog` VALUES (4, '2012-06-01 17:12:10', '127.218.11.12');
INSERT INTO `LoginLog` VALUES (3, '2012-06-01 15:12:10', '117.168.10.1');
INSERT INTO `LoginLog` VALUES (1, '2012-06-01 13:12:10', '127.168.10.1');
