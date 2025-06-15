-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 15, 2025 at 11:06 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `warehouse`
--

-- --------------------------------------------------------

--
-- Table structure for table `funds`
--

CREATE TABLE `funds` (
  `fundsID` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `fundsLimit` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `funds`
--

INSERT INTO `funds` (`fundsID`, `userID`, `balance`, `fundsLimit`) VALUES
(15, 24, 99600000, 999999999),
(16, 25, 422001000, 999999999);

-- --------------------------------------------------------

--
-- Table structure for table `manufacturers`
--

CREATE TABLE `manufacturers` (
  `manufacturerID` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `nationalityID` int(11) DEFAULT NULL,
  `manufacturerType` enum('Land','Air') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `manufacturers`
--

INSERT INTO `manufacturers` (`manufacturerID`, `name`, `nationalityID`, `manufacturerType`) VALUES
(1, 'General Dynamics Land Systems', 33, 'Land'),
(2, 'BAE Systems Inc.', 33, 'Land'),
(3, 'Lockheed Martin', 33, 'Air'),
(4, 'Boeing Defense', 33, 'Air'),
(5, 'BAE Systems Land UK', 32, 'Land'),
(6, 'Vickers Defence Systems', 32, 'Land'),
(7, 'BAE Systems Air', 32, 'Air'),
(8, 'Rolls-Royce Defence', 32, 'Air'),
(9, 'Nexter Systems', 12, 'Land'),
(10, 'Arquus', 12, 'Land'),
(11, 'Dassault Aviation', 12, 'Air'),
(12, 'Safran Aircraft Engines', 12, 'Air'),
(13, 'Krauss-Maffei Wegmann', 2, 'Land'),
(14, 'Rheinmetall AG', 2, 'Land'),
(15, 'Airbus Defence Germany', 2, 'Air'),
(16, 'MTU Aero Engines', 2, 'Air');

-- --------------------------------------------------------

--
-- Table structure for table `nationality`
--

CREATE TABLE `nationality` (
  `nationalityID` int(11) NOT NULL,
  `country` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nationality`
--

INSERT INTO `nationality` (`nationalityID`, `country`) VALUES
(1, 'The Trader'),
(2, 'Germany'),
(3, 'Albania'),
(4, 'Belgium'),
(5, 'Bulgaria'),
(6, 'Canada'),
(7, 'Croatia'),
(8, 'Czech Republic'),
(9, 'Denmark'),
(10, 'Estonia'),
(11, 'Finland'),
(12, 'France'),
(13, 'Greece'),
(14, 'Hungary'),
(15, 'Iceland'),
(16, 'Italy'),
(17, 'Latvia'),
(18, 'Lithuania'),
(19, 'Luxembourg'),
(20, 'Montenegro'),
(21, 'Netherlands'),
(22, 'North Macedonia'),
(23, 'Norway'),
(24, 'Poland'),
(25, 'Portugal'),
(26, 'Romania'),
(27, 'Slovakia'),
(28, 'Slovenia'),
(29, 'Spain'),
(30, 'Sweden'),
(31, 'Turkey'),
(32, 'United Kingdom'),
(33, 'United States');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL,
  `productID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `orderQuantity` int(11) DEFAULT NULL,
  `orderPrice` double DEFAULT NULL,
  `paymentDate` datetime DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderID`, `productID`, `userID`, `orderQuantity`, `orderPrice`, `paymentDate`, `deliveryDate`) VALUES
(11, 26, 24, 1, 400000, '2025-06-15 21:22:04', '2025-07-15 21:22:04'),
(12, 5, 25, 1, 78000000, '2025-06-15 21:22:37', '2025-07-15 21:22:37');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `productID` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `productType` enum('Land','Air') DEFAULT NULL,
  `pricePerUnit` double DEFAULT NULL,
  `idManufacturer` int(11) DEFAULT NULL,
  `inStock` int(11) DEFAULT NULL,
  `productDescription` varchar(200) DEFAULT NULL,
  `photoName` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`productID`, `name`, `productType`, `pricePerUnit`, `idManufacturer`, `inStock`, `productDescription`, `photoName`) VALUES
(1, 'M1A2 Abrams', 'Land', 8900000, 1, 50, 'Main battle tank with advanced armor and fire control.', 'm1a2.jpg'),
(2, 'Stryker ICV', 'Land', 4700000, 1, 20, '8x8 wheeled armored infantry carrier.', 'stryker.jpg'),
(3, 'M109A7 Paladin', 'Land', 3100000, 2, 15, '155mm self-propelled howitzer.', 'paladin.jpg'),
(4, 'Bradley IFV', 'Land', 3600000, 2, 30, 'Infantry fighting vehicle with autocannon and TOW launcher.', 'bradley.jpg'),
(5, 'F-35A Lightning II', 'Air', 78000000, 3, 9, '5th-gen stealth multirole fighter.', 'f35.jpg'),
(6, 'F-22 Raptor', 'Air', 150000000, 3, 29, 'Air superiority stealth fighter.', 'f22.jpg'),
(7, 'F/A-18E Super Hornet', 'Air', 70000000, 4, 20, 'Carrier-based strike fighter.', 'superhornet.jpg'),
(8, 'AH-64 Apache', 'Air', 35000000, 4, 30, 'Attack helicopter with Hellfire missiles.', 'apache.jpg'),
(9, 'Challenger 2', 'Land', 8600000, 5, 30, 'British main battle tank.', 'challenger2.jpg'),
(10, 'CV90 Armadillo', 'Land', 5500000, 5, 70, 'Armored combat support vehicle.', 'cv90.jpg'),
(11, 'Vickers Mk.7', 'Land', 4200000, 6, 30, 'Legacy main battle tank.', 'vickersmk7.jpg'),
(12, 'Vickers Warrior IFV', 'Land', 3800000, 6, 50, 'Tracked infantry fighting vehicle.', 'warrior.jpg'),
(13, 'Eurofighter Typhoon', 'Air', 90000000, 7, 30, 'Multinational multirole fighter jet.', 'typhoon.jpg'),
(14, 'Hawk T2 Trainer', 'Air', 21000000, 7, 40, 'Advanced jet trainer aircraft.', 'hawk.jpg'),
(15, 'EJ200 Engine', 'Air', 6000000, 8, 30, 'Jet engine for Eurofighter Typhoon.', 'ej200.jpg'),
(16, 'Adour Engine', 'Air', 4500000, 8, 60, 'Engine for Hawk trainer jets.', 'adour.jpg'),
(17, 'Leclerc MBT', 'Land', 9300000, 9, 20, 'French main battle tank.', 'leclerc.jpg'),
(18, 'CAESAR Howitzer', 'Land', 4800000, 9, 30, '155mm truck-mounted artillery.', 'caesar.jpg'),
(19, 'VBL Light Vehicle', 'Land', 110000, 10, 25, 'Light 4x4 armored scout vehicle.', 'vbl.jpg'),
(20, 'Sherpa Light', 'Land', 300000, 10, 20, 'Armored tactical vehicle.', 'sherpa.jpg'),
(21, 'Rafale C', 'Air', 85000000, 11, 30, 'Multirole fighter jet.', 'rafale.jpg'),
(22, 'Falcon 8X', 'Air', 62000000, 11, 10, 'Long-range business jet (military config).', 'falcon8x.jpg'),
(23, 'M88 Engine', 'Air', 5000000, 12, 15, 'Turbofan engine for Rafale.', 'm88.jpg'),
(24, 'TP400-D6', 'Air', 7200000, 12, 10, 'Turboprop engine for A400M.', 'tp400.jpg'),
(25, 'Leopard 2A7+', 'Land', 9800000, 13, 19, 'Advanced MBT with improved armor.', 'leopard2.jpg'),
(26, 'Dingo 2', 'Land', 400000, 13, 36, 'Armored infantry mobility vehicle.', 'dingo.jpg'),
(27, 'PzH 2000', 'Land', 4700000, 14, 14, 'Self-propelled 155mm artillery.', 'pzh2000.jpg'),
(28, 'Lynx KF41', 'Land', 6200000, 14, 20, 'Modular infantry fighting vehicle.', 'lynx.jpg'),
(29, 'Eurofighter Typhoon', 'Air', 90000000, 15, 29, 'Multirole fighter jet.', 'typhoon_de.jpg'),
(30, 'A400M Atlas', 'Air', 175000000, 15, 10, 'Military transport aircraft.', 'a400m.jpg'),
(31, 'MTU EJ200', 'Air', 6000000, 16, 14, 'Eurofighter jet engine (with Rolls-Royce).', 'mtu_ej200.jpg'),
(32, 'MTU TP400', 'Air', 7200000, 16, 10, 'Prop engine for A400M.', 'mtu_tp400.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `loginHash` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `creationDate` date NOT NULL,
  `nationalityID` int(11) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `loginHash`, `password`, `creationDate`, `nationalityID`, `isAdmin`) VALUES
(1, 'Admin', 'admin', '2025-05-20', 1, 1),
(24, 'UserPL', 'Informatyka12345!', '2025-06-15', 24, 0),
(25, 'UserGER', 'Informatyka12345!', '2025-06-15', 2, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `funds`
--
ALTER TABLE `funds`
  ADD PRIMARY KEY (`fundsID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `manufacturers`
--
ALTER TABLE `manufacturers`
  ADD PRIMARY KEY (`manufacturerID`),
  ADD KEY `nationalityID` (`nationalityID`);

--
-- Indexes for table `nationality`
--
ALTER TABLE `nationality`
  ADD PRIMARY KEY (`nationalityID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderID`),
  ADD KEY `userID` (`userID`),
  ADD KEY `productID` (`productID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productID`),
  ADD KEY `idManufacturer` (`idManufacturer`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`),
  ADD KEY `nationalityID` (`nationalityID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `funds`
--
ALTER TABLE `funds`
  MODIFY `fundsID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `manufacturers`
--
ALTER TABLE `manufacturers`
  MODIFY `manufacturerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `nationality`
--
ALTER TABLE `nationality`
  MODIFY `nationalityID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `productID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `funds`
--
ALTER TABLE `funds`
  ADD CONSTRAINT `funds_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `manufacturers`
--
ALTER TABLE `manufacturers`
  ADD CONSTRAINT `manufacturers_ibfk_1` FOREIGN KEY (`nationalityID`) REFERENCES `nationality` (`nationalityID`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `products` (`productID`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`idManufacturer`) REFERENCES `manufacturers` (`manufacturerID`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`nationalityID`) REFERENCES `nationality` (`nationalityID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
