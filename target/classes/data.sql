-- MySQL dump 10.13  Distrib 8.0.33, for macos12.6 (arm64)
--
-- Host: localhost    Database: fci_regulation_bias
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `FCISpecieTypeGroup`
--

LOCK TABLES `FCISpecieTypeGroup` WRITE;
/*!40000 ALTER TABLE `FCISpecieTypeGroup` DISABLE KEYS */;
INSERT INTO `FCISpecieTypeGroup` VALUES (1,1,_binary '','Equity species to invest in local market','Equity'),(2,100,_binary '','Public bonds to invest in Argentina','Bond'),(3,1,_binary '','Foreing Equities to invest from Argentina','Cedears'),(4,1,_binary '\0','Represents available cash for withdrawals','Cash');
/*!40000 ALTER TABLE `FCISpecieTypeGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieTypeGroup_SEQ`
--

LOCK TABLES `FCISpecieTypeGroup_SEQ` WRITE;
/*!40000 ALTER TABLE `FCISpecieTypeGroup_SEQ` DISABLE KEYS */;
INSERT INTO `FCISpecieTypeGroup_SEQ` VALUES (101);
/*!40000 ALTER TABLE `FCISpecieTypeGroup_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieType`
--

LOCK TABLES `FCISpecieType` WRITE;
/*!40000 ALTER TABLE `FCISpecieType` DISABLE KEYS */;
INSERT INTO `FCISpecieType` VALUES (1,0,_binary '','Comprehends all bank type equities','(E) Banks'),(2,0,_binary '','Comprehends all energy companies type equities','(E) Energies'),(3,0,_binary '','Comprehends all agriculture companies type equities','(E) Agricultures'),(4,0,_binary '','Comprehends all telecomunication companies type equities','(E) Telcos'),(5,0,_binary '','Comprehends all telecomunication companies type equities','(E) Industries'),(6,0,_binary '','Comprehends all agriculture companies type equities','(E) Generals'),(7,0,_binary '','Comprehends all national public bonds','(B) National'),(8,0,_binary '','Comprehends all provincial bonds','(B) Provincial'),(9,0,_binary '','Comprehends all foreing banks','(C) CBanks'),(10,0,_binary '','Comprehends all technological companies','(C) CTechnological'),(11,0,_binary '\0','Represents available cash for withdrawals','(C) Cash');
/*!40000 ALTER TABLE `FCISpecieType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieType_SEQ`
--

LOCK TABLES `FCISpecieType_SEQ` WRITE;
/*!40000 ALTER TABLE `FCISpecieType_SEQ` DISABLE KEYS */;
INSERT INTO `FCISpecieType_SEQ` VALUES (101);
/*!40000 ALTER TABLE `FCISpecieType_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIRegulation`
--

LOCK TABLES `FCIRegulation` WRITE;
/*!40000 ALTER TABLE `FCIRegulation` DISABLE KEYS */;
INSERT INTO `FCIRegulation` VALUES (1,'2024-05-22 10:09:51.165000','Comprehends a mix of Equities, Bunds and Cash to allow retrievals','Gama Mix Rent FCI','ADR610'),(2,'2024-05-22 10:09:54.168000','Comprehends operable Cedears for foreing companies','Sigma Cedear Rent FCI','SGM515');
/*!40000 ALTER TABLE `FCIRegulation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIRegulation_SEQ`
--

LOCK TABLES `FCIRegulation_SEQ` WRITE;
/*!40000 ALTER TABLE `FCIRegulation_SEQ` DISABLE KEYS */;
INSERT INTO `FCIRegulation_SEQ` VALUES (101);
/*!40000 ALTER TABLE `FCIRegulation_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Advices`
--

LOCK TABLES `Advices` WRITE;
/*!40000 ALTER TABLE `Advices` DISABLE KEYS */;
/*!40000 ALTER TABLE `Advices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Advices_SEQ`
--

LOCK TABLES `Advices_SEQ` WRITE;
/*!40000 ALTER TABLE `Advices_SEQ` DISABLE KEYS */;
INSERT INTO `Advices_SEQ` VALUES (1);
/*!40000 ALTER TABLE `Advices_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `advisor_criteria_parameter`
--

LOCK TABLES `advisor_criteria_parameter` WRITE;
/*!40000 ALTER TABLE `advisor_criteria_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `advisor_criteria_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `advisor_criteria_parameter_SEQ`
--

LOCK TABLES `advisor_criteria_parameter_SEQ` WRITE;
/*!40000 ALTER TABLE `advisor_criteria_parameter_SEQ` DISABLE KEYS */;
INSERT INTO `advisor_criteria_parameter_SEQ` VALUES (1);
/*!40000 ALTER TABLE `advisor_criteria_parameter_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Biases`
--

LOCK TABLES `Biases` WRITE;
/*!40000 ALTER TABLE `Biases` DISABLE KEYS */;
/*!40000 ALTER TABLE `Biases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Biases_SEQ`
--

LOCK TABLES `Biases_SEQ` WRITE;
/*!40000 ALTER TABLE `Biases_SEQ` DISABLE KEYS */;
INSERT INTO `Biases_SEQ` VALUES (1);
/*!40000 ALTER TABLE `Biases_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fci_specie_type_by_group`
--

LOCK TABLES `fci_specie_type_by_group` WRITE;
/*!40000 ALTER TABLE `fci_specie_type_by_group` DISABLE KEYS */;
INSERT INTO `fci_specie_type_by_group` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(2,7),(2,8),(3,9),(3,10),(4,11);
/*!40000 ALTER TABLE `fci_specie_type_by_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIComposition`
--

LOCK TABLES `FCIComposition` WRITE;
/*!40000 ALTER TABLE `FCIComposition` DISABLE KEYS */;
INSERT INTO `FCIComposition` VALUES (1,1,1,20),(1,2,2,20),(1,3,3,10),(1,4,5,20),(1,5,6,10),(1,6,7,10),(1,7,11,10),(2,8,9,40),(2,9,10,50),(2,10,11,10);
/*!40000 ALTER TABLE `FCIComposition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIComposition_SEQ`
--

LOCK TABLES `FCIComposition_SEQ` WRITE;
/*!40000 ALTER TABLE `FCIComposition_SEQ` DISABLE KEYS */;
INSERT INTO `FCIComposition_SEQ` VALUES (101);
/*!40000 ALTER TABLE `FCIComposition_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIConfiguration`
--

LOCK TABLES `FCIConfiguration` WRITE;
/*!40000 ALTER TABLE `FCIConfiguration` DISABLE KEYS */;
INSERT INTO `FCIConfiguration` VALUES (1,'algorithm_key','AQIDBAUGBwgJCgsMDQ4PEA=='),(2,'secret','2QiZ+hSJQBorx0ANbWZliQ==');
/*!40000 ALTER TABLE `FCIConfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIConfiguration_SEQ`
--

LOCK TABLES `FCIConfiguration_SEQ` WRITE;
/*!40000 ALTER TABLE `FCIConfiguration_SEQ` DISABLE KEYS */;
INSERT INTO `FCIConfiguration_SEQ` VALUES (101);
/*!40000 ALTER TABLE `FCIConfiguration_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIPosition`
--

LOCK TABLES `FCIPosition` WRITE;
/*!40000 ALTER TABLE `FCIPosition` DISABLE KEYS */;
/*!40000 ALTER TABLE `FCIPosition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCIPosition_SEQ`
--

LOCK TABLES `FCIPosition_SEQ` WRITE;
/*!40000 ALTER TABLE `FCIPosition_SEQ` DISABLE KEYS */;
INSERT INTO `FCIPosition_SEQ` VALUES (1);
/*!40000 ALTER TABLE `FCIPosition_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieToSpecieType`
--

LOCK TABLES `FCISpecieToSpecieType` WRITE;
/*!40000 ALTER TABLE `FCISpecieToSpecieType` DISABLE KEYS */;
INSERT INTO `FCISpecieToSpecieType` VALUES (5,1,'ALUA'),(5,2,'BBAR'),(6,3,'BMA'),(6,4,'BYMA'),(5,5,'CEPU'),(5,6,'COME'),(3,7,'CRES'),(3,8,'CRE3W'),(2,9,'EDN'),(1,10,'GGAL'),(5,11,'LOMA'),(3,12,'MIRG'),(2,13,'PAMP'),(5,14,'SUPV'),(4,15,'TECO2'),(2,16,'TGNO4'),(2,17,'TGSU2'),(2,18,'TRAN'),(2,19,'TXAR'),(2,20,'VALO'),(2,21,'YPDF'),(3,22,'AGRO'),(5,23,'ALAAF'),(5,24,'AUSO'),(1,25,'BHIP'),(2,26,'BOLT'),(1,27,'BPAT'),(3,28,'CADO'),(3,29,'CAPX'),(3,30,'CARC'),(2,31,'CECO2'),(5,32,'CELU'),(1,33,'CGPA2'),(2,34,'CRFE3W'),(5,35,'CTIO'),(4,36,'CVH'),(2,37,'DGCU2'),(2,38,'DOME'),(2,39,'DYCA'),(2,40,'FERR'),(2,41,'FIPL'),(2,42,'GAMI'),(2,43,'GARO'),(2,44,'GBAN'),(2,45,'GCDI'),(1,46,'GCLA'),(1,47,'GGALB'),(1,48,'GGALD'),(2,49,'GRIM'),(2,50,'HARG'),(2,51,'HAVA'),(2,52,'INTR'),(4,53,'INVJ'),(2,54,'IRS2W'),(3,55,'IRSA'),(5,56,'LEDE'),(2,57,'LONG'),(2,58,'METR'),(2,59,'MOLA'),(3,60,'MOLI'),(3,61,'MOLI5'),(3,62,'MORI'),(2,63,'MTR'),(2,64,'OEST'),(2,65,'PAMPD'),(2,66,'PATA'),(4,67,'PNIZF'),(2,68,'POLL'),(2,69,'REGE'),(2,70,'RICH'),(5,71,'RIGO'),(5,72,'ROSE'),(5,73,'SAMI'),(2,74,'SEMI'),(2,75,'TXARD'),(2,76,'VALOD'),(2,77,'YPFB'),(2,78,'YPFD'),(2,79,'YPFDD'),(7,80,'CAC2O'),(7,81,'TVPA'),(7,82,'TVPY'),(7,83,'YCA60'),(7,84,'YMCHO'),(7,85,'YMCIO'),(7,86,'GD29D'),(7,87,'GD30'),(7,88,'GD38D'),(7,89,'GD38D'),(7,90,'TVPA'),(7,91,'TVPY'),(7,92,'YCA60'),(7,93,'YMCHO'),(7,94,'YMCIO'),(7,95,'GD29D'),(7,96,'GD30'),(7,97,'GD38D'),(7,98,'CAC2O'),(7,99,'TVPA'),(7,100,'TVPY'),(7,101,'YCA60'),(7,102,'YMCHO'),(7,103,'YMCIO'),(7,104,'GD29D'),(7,105,'GD30'),(7,106,'GD38D'),(7,107,'CAC2O'),(7,108,'TVPA'),(7,109,'TVPY'),(7,110,'YCA60'),(7,111,'YMCHO'),(7,112,'YMCIO'),(7,113,'GD29D'),(7,114,'GD30'),(7,115,'GD38D'),(7,116,'AL35D'),(7,117,'RCCJO'),(7,118,'BPO27'),(7,119,'CUAP'),(7,120,'TVPE'),(7,121,'TX25'),(7,122,'YMCJO'),(7,123,'GD35'),(7,124,'GD46'),(7,125,'AL30'),(7,126,'TDG24'),(7,127,'TV25'),(7,128,'AL41D'),(7,129,'GNCXO'),(7,130,'TX24'),(7,131,'GD29'),(7,132,'AE38D'),(7,133,'AL29'),(7,134,'RUC5O'),(7,135,'TDJ24'),(7,136,'BB37D'),(7,137,'CO26'),(7,138,'CS34O'),(7,139,'PARP'),(7,140,'TLC5O'),(7,141,'DIP0'),(7,142,'GD41D'),(7,143,'AL29D'),(7,144,'T2V4'),(7,145,'CS38O'),(7,146,'T6X4'),(7,147,'TDA24'),(7,148,'CO26D'),(7,149,'DICP'),(7,150,'PBA25'),(7,151,'T2X4'),(7,152,'TVPP'),(7,153,'AL35'),(7,154,'MGC9O'),(7,155,'AL41'),(7,156,'BA37D'),(7,157,'CP17O'),(7,158,'CRCEO'),(7,159,'MTCGO'),(7,160,'TC25P'),(7,161,'TDF24'),(7,162,'TO26'),(7,163,'TX26'),(7,164,'TX28'),(7,165,'GD35D'),(7,166,'T2X5'),(7,167,'T5X4'),(7,168,'PAP0'),(7,169,'TLC1O'),(7,170,'GN34O'),(7,171,'GD30D'),(7,172,'GD38'),(7,173,'GD41'),(7,174,'AE38'),(7,175,'AL30D'),(7,176,'T3X4'),(7,177,'DNC2O'),(7,178,'TDE25'),(7,179,'BDC24'),(7,180,'BDC28'),(7,181,'PNDCO'),(7,182,'PR13'),(7,183,'TV24'),(7,184,'YPCUO'),(7,185,'GD46D'),(7,186,'IRCFO'),(7,187,'YCA60'),(8,188,'T4X4'),(8,189,'PAY0'),(8,190,'TVY0'),(8,191,'VSC3O'),(11,192,'Cash'),(10,193,'ADBE'),(10,194,'ADBED'),(9,195,'ABBV');
/*!40000 ALTER TABLE `FCISpecieToSpecieType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieToSpecieType_SEQ`
--

LOCK TABLES `FCISpecieToSpecieType_SEQ` WRITE;
/*!40000 ALTER TABLE `FCISpecieToSpecieType_SEQ` DISABLE KEYS */;
INSERT INTO `FCISpecieToSpecieType_SEQ` VALUES (251);
/*!40000 ALTER TABLE `FCISpecieToSpecieType_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieToSpecieTypePosition`
--

LOCK TABLES `FCISpecieToSpecieTypePosition` WRITE;
/*!40000 ALTER TABLE `FCISpecieToSpecieTypePosition` DISABLE KEYS */;
/*!40000 ALTER TABLE `FCISpecieToSpecieTypePosition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `FCISpecieToSpecieTypePosition_SEQ`
--

LOCK TABLES `FCISpecieToSpecieTypePosition_SEQ` WRITE;
/*!40000 ALTER TABLE `FCISpecieToSpecieTypePosition_SEQ` DISABLE KEYS */;
INSERT INTO `FCISpecieToSpecieTypePosition_SEQ` VALUES (1);
/*!40000 ALTER TABLE `FCISpecieToSpecieTypePosition_SEQ` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Reports`
--

LOCK TABLES `Reports` WRITE;
/*!40000 ALTER TABLE `Reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Reports_SEQ`
--

LOCK TABLES `Reports_SEQ` WRITE;
/*!40000 ALTER TABLE `Reports_SEQ` DISABLE KEYS */;
INSERT INTO `Reports_SEQ` VALUES (1);
/*!40000 ALTER TABLE `Reports_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ReportTypes`
--

LOCK TABLES `ReportTypes` WRITE;
/*!40000 ALTER TABLE `ReportTypes` DISABLE KEYS */;
INSERT INTO `ReportTypes` VALUES (1,'','','FCI Position Overview');
/*!40000 ALTER TABLE `ReportTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ReportTypes_SEQ`
--

LOCK TABLES `ReportTypes_SEQ` WRITE;
/*!40000 ALTER TABLE `ReportTypes_SEQ` DISABLE KEYS */;
INSERT INTO `ReportTypes_SEQ` VALUES (51);
/*!40000 ALTER TABLE `ReportTypes_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Retrievals`
--

LOCK TABLES `Retrievals` WRITE;
/*!40000 ALTER TABLE `Retrievals` DISABLE KEYS */;
/*!40000 ALTER TABLE `Retrievals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Retrievals_SEQ`
--

LOCK TABLES `Retrievals_SEQ` WRITE;
/*!40000 ALTER TABLE `Retrievals_SEQ` DISABLE KEYS */;
INSERT INTO `Retrievals_SEQ` VALUES (1);
/*!40000 ALTER TABLE `Retrievals_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Statistic`
--

LOCK TABLES `Statistic` WRITE;
/*!40000 ALTER TABLE `Statistic` DISABLE KEYS */;
/*!40000 ALTER TABLE `Statistic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Statistic_SEQ`
--

LOCK TABLES `Statistic_SEQ` WRITE;
/*!40000 ALTER TABLE `Statistic_SEQ` DISABLE KEYS */;
INSERT INTO `Statistic_SEQ` VALUES (1);
/*!40000 ALTER TABLE `Statistic_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (_binary '',1,NULL,NULL,'$2a$10$K6MFo0h1i.wtwE4uaBP.M.hSXbl7vJy43r8EUzfIY6MtdIu2LC9Mm',NULL,'pga');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_SEQ`
--

LOCK TABLES `User_SEQ` WRITE;
/*!40000 ALTER TABLE `User_SEQ` DISABLE KEYS */;
INSERT INTO `User_SEQ` VALUES (51);
/*!40000 ALTER TABLE `User_SEQ` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-22 10:15:17
