-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 13, 2012 at 07:15 PM
-- Server version: 5.5.22
-- PHP Version: 5.3.10-1ubuntu3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dalga`
--

-- --------------------------------------------------------

--
-- Table structure for table `LoginLog`
--

CREATE TABLE IF NOT EXISTS `LoginLog` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `ip` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Post`
--

CREATE TABLE IF NOT EXISTS `Post` (
  `id` int(11) NOT NULL,
  `topicid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `post` text COLLATE utf8_turkish_ci NOT NULL,
  `reply` int(11) DEFAULT NULL,
  KEY `topicid` (`topicid`,`userid`),
  KEY `IND2` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PrivateMessage`
--

CREATE TABLE IF NOT EXISTS `PrivateMessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toid` int(11) NOT NULL,
  `fromid` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `message` text COLLATE utf8_turkish_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `toid` (`toid`,`fromid`),
  KEY `IND3` (`fromid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Tag`
--

CREATE TABLE IF NOT EXISTS `Tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Topic`
--

CREATE TABLE IF NOT EXISTS `Topic` (
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `title` varchar(120) COLLATE utf8_turkish_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IND1` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TopicTags`
--

CREATE TABLE IF NOT EXISTS `TopicTags` (
  `topicid` int(11) NOT NULL,
  `tagid` int(11) NOT NULL,
  KEY `topicid` (`topicid`,`tagid`),
  KEY `IND 4` (`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TopicUsers`
--

CREATE TABLE IF NOT EXISTS `TopicUsers` (
  `userid` int(11) NOT NULL,
  `topicid` int(11) NOT NULL,
  KEY `userid` (`userid`,`topicid`),
  KEY `IND5` (`topicid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  `email` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  `passwd` varchar(45) COLLATE utf8_turkish_ci NOT NULL,
  `avatar` varchar(120) COLLATE utf8_turkish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `LoginLog`
--
ALTER TABLE `LoginLog`
  ADD CONSTRAINT `LoginLog_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Post`
--
ALTER TABLE `Post`
  ADD CONSTRAINT `Post_ibfk_1` FOREIGN KEY (`topicid`) REFERENCES `Topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Post_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `PrivateMessage`
--
ALTER TABLE `PrivateMessage`
  ADD CONSTRAINT `PrivateMessage_ibfk_2` FOREIGN KEY (`fromid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `PrivateMessage_ibfk_1` FOREIGN KEY (`toid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Topic`
--
ALTER TABLE `Topic`
  ADD CONSTRAINT `Topic_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `TopicTags`
--
ALTER TABLE `TopicTags`
  ADD CONSTRAINT `TopicTags_ibfk_2` FOREIGN KEY (`tagid`) REFERENCES `Tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `TopicTags_ibfk_1` FOREIGN KEY (`topicid`) REFERENCES `Topic` (`id`);

--
-- Constraints for table `TopicUsers`
--
ALTER TABLE `TopicUsers`
  ADD CONSTRAINT `TopicUsers_ibfk_2` FOREIGN KEY (`topicid`) REFERENCES `Topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `TopicUsers_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
