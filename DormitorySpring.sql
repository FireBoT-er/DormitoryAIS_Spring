-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: dormitorydb
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `check_ins_outs`
--

DROP TABLE IF EXISTS `check_ins_outs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `check_ins_outs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `room_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2terhvocgnwdo9qvy43abjuaa` (`room_id`),
  KEY `FK9hcyrhk06e0v0nl1b4ndpht4u` (`student_id`),
  CONSTRAINT `FK2terhvocgnwdo9qvy43abjuaa` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `FK9hcyrhk06e0v0nl1b4ndpht4u` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `check_ins_outs`
--

LOCK TABLES `check_ins_outs` WRITE;
/*!40000 ALTER TABLE `check_ins_outs` DISABLE KEYS */;
INSERT INTO `check_ins_outs` VALUES (1,'2019-08-31','2023-08-31',2,1),(2,'2017-09-04','2021-09-04',5,2),(3,'2016-05-13','2020-03-20',3,3),(4,'2018-08-29','2022-08-29',5,4),(5,'2018-08-29','2022-08-29',5,5),(6,'2018-08-29','2022-08-29',5,6),(7,'2019-09-03','2023-09-03',3,7),(8,'2021-12-09','2025-12-09',2,8),(9,'2018-08-29','2022-08-29',5,9),(10,'2019-09-02','2023-09-02',3,10),(11,'2016-09-05','2022-09-05',1,11),(12,'2020-08-31','2024-08-31',3,12);
/*!40000 ALTER TABLE `check_ins_outs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cleanings`
--

DROP TABLE IF EXISTS `cleanings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cleanings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cleaned` varchar(500) DEFAULT NULL,
  `datec` date NOT NULL,
  `timec` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cleanings`
--

LOCK TABLES `cleanings` WRITE;
/*!40000 ALTER TABLE `cleanings` DISABLE KEYS */;
INSERT INTO `cleanings` VALUES (1,'Кухни третьего этажа','2021-12-08','11:34:00'),(2,'Туалеты и душевые третьего этажа','2021-12-08','11:35:00'),(3,'Коридоры третьего этажа','2021-12-08','11:36:00');
/*!40000 ALTER TABLE `cleanings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cleanings_employees`
--

DROP TABLE IF EXISTS `cleanings_employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cleanings_employees` (
  `cleanings_id` bigint NOT NULL,
  `employees_id` bigint NOT NULL,
  KEY `FK8vt87opkhjmeg6vfxub4xv3qp` (`employees_id`),
  KEY `FKm5icji904g63xkc8j18y7fgdd` (`cleanings_id`),
  CONSTRAINT `FK8vt87opkhjmeg6vfxub4xv3qp` FOREIGN KEY (`employees_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `FKm5icji904g63xkc8j18y7fgdd` FOREIGN KEY (`cleanings_id`) REFERENCES `cleanings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cleanings_employees`
--

LOCK TABLES `cleanings_employees` WRITE;
/*!40000 ALTER TABLE `cleanings_employees` DISABLE KEYS */;
INSERT INTO `cleanings_employees` VALUES (3,7),(3,2),(2,7),(1,2);
/*!40000 ALTER TABLE `cleanings_employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `emp_position` varchar(50) DEFAULT NULL,
  `is_working_now` bit(1) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `patronymic` varchar(30) DEFAULT NULL,
  `photo_file_name` varchar(255) DEFAULT NULL,
  `surname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Комендант',_binary '','Мирон','Иванович','1','Селиванов'),(2,'Уборщица',_binary '','Мия','Александровна','2','Калинина'),(3,'Охранник',_binary '\0','Артур','Иванович','3','Шубин'),(4,'Паспортистка',_binary '','Марьям','Максимовна','4','Колесова'),(5,'Воспитатель',_binary '\0','Агата','Даниловна','5','Кондратьева'),(6,'Воспитатель',_binary '','Ева','Владимировна','6','Грачёва'),(7,'Уборщица',_binary '','Анна','Данииловна','7','Громова'),(8,'Охранник',_binary '','Арсений','Давидович','8','Кузнецов'),(9,'Охранник',_binary '','Никита','Святославович','9','Антонов'),(10,'Воспитатель',_binary '','Евангелина','Ивановна','10','Яковлева'),(11,'Уборщица',_binary '','Алёна','Егоровна','11','Полякова');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees_cleanings`
--

DROP TABLE IF EXISTS `employees_cleanings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees_cleanings` (
  `employees_id` bigint NOT NULL,
  `cleanings_id` bigint NOT NULL,
  KEY `FK6nek5yw26w84xryw4dmx4xbbo` (`cleanings_id`),
  KEY `FKlnlyesaibagndofuiylqlck8d` (`employees_id`),
  CONSTRAINT `FK6nek5yw26w84xryw4dmx4xbbo` FOREIGN KEY (`cleanings_id`) REFERENCES `cleanings` (`id`),
  CONSTRAINT `FKlnlyesaibagndofuiylqlck8d` FOREIGN KEY (`employees_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees_cleanings`
--

LOCK TABLES `employees_cleanings` WRITE;
/*!40000 ALTER TABLE `employees_cleanings` DISABLE KEYS */;
INSERT INTO `employees_cleanings` VALUES (7,3),(7,2),(2,3),(2,1);
/*!40000 ALTER TABLE `employees_cleanings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `inv_count` int NOT NULL,
  `inv_type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,1000,'Подушка'),(2,1000,'Одеяло'),(3,3000,'Наволочка'),(4,3000,'Простыня'),(5,3000,'Пододеяльник'),(6,1000,'Матрас'),(7,10,'Лопата');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issued_inventory`
--

DROP TABLE IF EXISTS `issued_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issued_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `iss_count` int NOT NULL,
  `issue_date` date NOT NULL,
  `turn_in_date` date NOT NULL,
  `inventory_item_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkw1289ec2q3iibh1nlwuefbst` (`inventory_item_id`),
  KEY `FKa1rba88tcwi75axf7fp14uetf` (`student_id`),
  CONSTRAINT `FKa1rba88tcwi75axf7fp14uetf` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKkw1289ec2q3iibh1nlwuefbst` FOREIGN KEY (`inventory_item_id`) REFERENCES `inventory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issued_inventory`
--

LOCK TABLES `issued_inventory` WRITE;
/*!40000 ALTER TABLE `issued_inventory` DISABLE KEYS */;
INSERT INTO `issued_inventory` VALUES (1,1,'2019-08-31','2023-08-31',6,1),(2,1,'2019-08-31','2023-08-31',3,1),(3,1,'2019-08-31','2023-08-31',2,1),(4,1,'2019-08-31','2023-08-31',5,1),(5,1,'2019-08-31','2023-08-31',1,1),(6,1,'2019-08-31','2023-08-31',4,1),(7,1,'2017-09-04','2021-09-04',6,2),(8,1,'2017-09-04','2021-09-04',2,2),(9,1,'2017-09-04','2017-09-04',1,2),(10,1,'2018-08-29','2022-08-29',6,5),(11,1,'2018-08-29','2022-08-29',2,5),(12,1,'2018-08-29','2022-08-29',1,5);
/*!40000 ALTER TABLE `issued_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `beds` int NOT NULL,
  `room_number` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,2,105),(2,2,101),(3,3,102),(4,2,103),(5,4,104);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birthday` date NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `patronymic` varchar(30) DEFAULT NULL,
  `photo_file_name` varchar(255) DEFAULT NULL,
  `record_book_number` varchar(10) DEFAULT NULL,
  `sex` bit(1) NOT NULL,
  `surname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'2001-08-10','Ксения','Егоровна','1','ЧСВ-119-01',_binary '\0','Исаева'),(2,'1999-01-22','Кирилл','Максимович','2','ИБД-117-05',_binary '','Царёв'),(3,'1993-12-10','Думгай','Иванович','3','ОБП-900-00',_binary '','Беседкин'),(4,'2000-04-24','Артём','Арсеньевич','4','ГЛД-118-03',_binary '','Авдеев'),(5,'2000-02-21','Платон','Даниилович','5','ЧУА-118-01',_binary '','Коновалов'),(6,'2000-03-22','Андрей','Русланович','6','ВОА-118-02',_binary '','Котов'),(7,'2001-05-14','Ева','Артёмовна','7','МРХ-219-14',_binary '\0','Крючкова'),(8,'2002-05-03','Мария','Евгеньевна','8','ЧПМ-120-02',_binary '\0','Докторская'),(9,'2000-05-25','Всеволод','Владиславович','9','СМР-118-04',_binary '','Вавилов'),(10,'2001-03-09','Анастасия','Дмитриевна','10','ОГО-119-04',_binary '\0','Попова'),(11,'1998-11-20','Тамерлан','Филиппович','11','ГРН-116-22',_binary '','Соколов'),(12,'2002-12-07','Карина','Максимовна','12','АМФ-120-08',_binary '\0','Никулина');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_violations`
--

DROP TABLE IF EXISTS `students_violations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_violations` (
  `students_id` bigint NOT NULL,
  `violations_id` bigint NOT NULL,
  KEY `FK114n5yluhb2ehttgpa2t4fbtp` (`violations_id`),
  KEY `FKbfviycpc8y0r6eexbf6abgtqu` (`students_id`),
  CONSTRAINT `FK114n5yluhb2ehttgpa2t4fbtp` FOREIGN KEY (`violations_id`) REFERENCES `violations` (`id`),
  CONSTRAINT `FKbfviycpc8y0r6eexbf6abgtqu` FOREIGN KEY (`students_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_violations`
--

LOCK TABLES `students_violations` WRITE;
/*!40000 ALTER TABLE `students_violations` DISABLE KEYS */;
INSERT INTO `students_violations` VALUES (2,2),(3,3),(1,1),(4,4),(5,4),(6,4),(9,4);
/*!40000 ALTER TABLE `students_violations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `violations`
--

DROP TABLE IF EXISTS `violations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `violations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_vl` date NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `punishment` varchar(500) DEFAULT NULL,
  `time_vl` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `violations`
--

LOCK TABLES `violations` WRITE;
/*!40000 ALTER TABLE `violations` DISABLE KEYS */;
INSERT INTO `violations` VALUES (1,'2021-12-08','Шум после полуночи','Выговор','00:30:00'),(2,'2020-07-01','Драка в коридоре общежития','Вызов полиции','11:58:00'),(3,'2020-03-20','Использование бензопилы в помещениях общежития для борьбы с \"демонами\"','Выселение из общежития, вызов психиатрической скорой помощи','20:02:00'),(4,'2020-06-06','Распитие спиртных напитков на территории общежития','Дисциплинарная беседа с предупреждением о выселении','18:06:00');
/*!40000 ALTER TABLE `violations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `violations_students`
--

DROP TABLE IF EXISTS `violations_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `violations_students` (
  `violations_id` bigint NOT NULL,
  `students_id` bigint NOT NULL,
  KEY `FK1htywro9xlhnpnv71mpa5rvf0` (`students_id`),
  KEY `FK3w916ykp2rkwex1o0f1x4dorv` (`violations_id`),
  CONSTRAINT `FK1htywro9xlhnpnv71mpa5rvf0` FOREIGN KEY (`students_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FK3w916ykp2rkwex1o0f1x4dorv` FOREIGN KEY (`violations_id`) REFERENCES `violations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `violations_students`
--

LOCK TABLES `violations_students` WRITE;
/*!40000 ALTER TABLE `violations_students` DISABLE KEYS */;
INSERT INTO `violations_students` VALUES (1,1),(2,2),(3,3),(4,4),(4,5),(4,6),(4,9);
/*!40000 ALTER TABLE `violations_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitors`
--

DROP TABLE IF EXISTS `visitors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_vs` date NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `patronymic` varchar(30) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(30) DEFAULT NULL,
  `time_vs` time NOT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK574h1johu61d65wwf1m6v8ff6` (`student_id`),
  CONSTRAINT `FK574h1johu61d65wwf1m6v8ff6` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitors`
--

LOCK TABLES `visitors` WRITE;
/*!40000 ALTER TABLE `visitors` DISABLE KEYS */;
INSERT INTO `visitors` VALUES (1,'2021-12-08','Даниил','Максимович','89516042551','Морозов','22:00:00',1),(2,'2020-02-21','Сергей','Ярославович','89927282950','Львов','13:47:00',2),(3,'2021-04-28','Полина','Егоровна','89710369372','Сергеева','13:40:00',11),(4,'2021-10-04','Галатея','Дмитриевна','89217134509','Степанова','18:22:00',8),(5,'2021-12-08','Тимофей','Макарович','89755788978','Жаров','22:00:00',1);
/*!40000 ALTER TABLE `visitors` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-26  3:00:58
