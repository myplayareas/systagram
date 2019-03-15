-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: localhost    Database: dbsysweb
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (1,'Nota de teste');
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cep` varchar(8) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKemsnreyk6g37uoja1ngeog5sp` (`user_id`),
  CONSTRAINT `FKemsnreyk6g37uoja1ngeog5sp` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'Teste','teste','tesTe',0,0,'Armando Soares','PI',1),(2,'Teste','teste','tesTe',0,0,'Maria','PI',2),(3,'Teste','teste','tesTe',0,0,'Francisco','PI',3),(4,'Teste','teste','tesTe',0,0,'Joao','PI',4),(5,'Teste','teste','tesTe',0,0,'Carlos','PI',8),(6,'Teste','teste','tesTe',0,0,'Capitulina','PI',16),(7,'Teste','teste','tesTe',0,0,'Foca','PI',17);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_notes`
--

DROP TABLE IF EXISTS `person_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_notes` (
  `person_id` bigint(20) NOT NULL,
  `notes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5w4m7upgc8nj3wggg91wdd47q` (`notes_id`),
  KEY `FKl367sdtn19xmyeiibpg82nr5v` (`person_id`),
  CONSTRAINT `FKkqxxpw4c6mc3fstk6f4gc6r5d` FOREIGN KEY (`notes_id`) REFERENCES `notes` (`id`),
  CONSTRAINT `FKl367sdtn19xmyeiibpg82nr5v` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_notes`
--

LOCK TABLES `person_notes` WRITE;
/*!40000 ALTER TABLE `person_notes` DISABLE KEYS */;
INSERT INTO `person_notes` VALUES (1,1);
/*!40000 ALTER TABLE `person_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'USER'),(4,'GUEST');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `person_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `id` (`id`),
  KEY `FKd21kkcigxa21xuby5i3va9ncs` (`person_id`),
  CONSTRAINT `FKd21kkcigxa21xuby5i3va9ncs` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('ana','$2a$10$xEyUW9wNFDWcjdvo4orYgeWYWIEUTR0KweCM7jXbfytEDwsuMXyTK',1,15,'ana@gmail.com',NULL),('armando','$2a$10$G21Awy3udH2acuuY3IqTPeJRT5QnuUGCVMydgLLZbjLMjFVJa9O12',1,1,'armando@ufpi.edu.br',1),('brenda','$2a$10$XGgI46HrmD8X4Du.vi2oKeh/8ZFDdrLuK8g9gBjReyfight3RoIBK',1,21,'brenda@gmail.com',NULL),('capitu','$2a$10$oEaZHj67GPXkR/khMmdgjeMITD7L8dz80IDPtjzKK6kwT2rt2cbGW',1,16,'capitu@gmail.com',NULL),('carlos','$2a$10$ZxkTB./ZdO92fVoGRkwY4OUEk6uxUTnfB/iRSg1iqzBemMARGVpnu',1,8,'carlos@gmail.com',NULL),('foca','$2a$10$3gJxHPj.g5gp2jIz.UhpQup4sc8jE2voGs5M3aj1SzDRcvWX5pneq',1,17,'foca@gmail.cofm',NULL),('Francisco','$2a$10$vKnTJGs54wwOlDXB6QsAxOO3RxK.OSSq8uJLZKjxJ7kd7lg.jhVG2',1,3,'francisco@uol.com.br',3),('joao','$2a$10$hRJ0e4Ix3O4GKvryi3h3.urzxFwvOvuxlsbLKKJKPCrx8VJfqeNSO',1,4,'joao@gmail.com',4),('jose','$2a$10$eDKIyxHjiQfQqrjKO9MWnu8spMCmanXWneP72ypm0ShnmxasLfNgO',1,14,'jn@gma.com',NULL),('josesilva','$2a$10$4rvNGVW4ExPT/NEO//U2BeI4UWRKnsUwN0E5MeQR5qXPBCnl54Y3a',1,18,'jose@gmail.com',NULL),('jota','$2a$10$JNwqetWQWIINWs6QWr0bm.Qe/XTIUNeK2pvYVZdiOc18cAZFzBVv2',1,11,'jotaq@gm.com',NULL),('maria','$2a$10$e2lpDJNq6FTovXESm/2vfuGKFItZuSanv.VikJ/N16Y8aPR010IgO',1,2,'maria@gmail.com',2),('tes','$2a$10$TdQU8ETpFfYdEUDKxx/TmOkbSwPXGwVP2kon1e74b8ieqCy3qjbDW',1,13,'tersaliaamaral@gmail.com',NULL),('teste','$2a$10$fE60f6LN5pt.Kz8Avv9/g.I.OrRhh1N8UdfGe.DzN8eRoZSml/Swe',1,24,'teste@gmail.com',NULL),('teste2','$2a$10$XaNeT7uE5Juk23aUYqy/7.QMzHtO/bFhKheQBTE4ANQXwww75Rv7.',1,23,'teste2@gmail.com',NULL),('tica','$2a$10$1A7DQ.MoAnGmt9wNhb/o7.BWdMPVlpiGw6Y4/kPPfNDo1F8IjCi26',1,20,'tica@gmail.com',NULL),('vera','$2a$10$U3BspMHOLefjDFzGmyIYo.Woti72slfkPxEsm57V/HxmHDfTbGpsi',1,9,'vera@gmail.com',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles` (
  `users_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FK15d410tj6juko0sq9k4km60xq` (`roles_id`),
  KEY `FKml90kef4w2jy7oxyqv742tsfc` (`users_id`),
  CONSTRAINT `FK15d410tj6juko0sq9k4km60xq` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKml90kef4w2jy7oxyqv742tsfc` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,1),(1,2),(2,2),(4,2),(9,2),(11,2),(13,2),(18,2),(20,2),(21,2),(23,2),(8,2),(14,2),(3,2),(16,2),(24,2),(15,2);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-15 19:06:17
