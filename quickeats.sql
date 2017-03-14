-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 09, 2017 at 10:32 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `quickeats`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE IF NOT EXISTS `addresses` (
  `address_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `address_lat` double NOT NULL,
  `address_long` double NOT NULL,
  `formatted_address` text NOT NULL,
  `nickname` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`address_id`, `user_id`, `address_lat`, `address_long`, `formatted_address`, `nickname`) VALUES
(1, 9, 42.55858522, 9.22252552, '7th Street Leopard', 'Home'),
(2, 9, 43.5222214, 7.22255, '5TH STREET FOX AVENUE', 'WORK');

-- --------------------------------------------------------

--
-- Table structure for table `agentearnings`
--

CREATE TABLE IF NOT EXISTS `agentearnings` (
`id` int(11) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `order_amount` float NOT NULL,
  `agent_earning` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `delivery_agents`
--

CREATE TABLE IF NOT EXISTS `delivery_agents` (
  `agent_id` int(11) NOT NULL,
  `firstname` text NOT NULL,
  `lastname` text NOT NULL,
  `current_lat` decimal(10,5) NOT NULL,
  `current_long` decimal(10,5) NOT NULL,
  `rating` decimal(10,5) NOT NULL,
  `phoneno` varchar(20) NOT NULL,
  `account_status` varchar(20) NOT NULL,
  `password` varchar(80) NOT NULL,
  `email` text NOT NULL,
  `delivery_earning` float NOT NULL,
  `total_delivery_earning` double NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `delivery_agents`
--

INSERT INTO `delivery_agents` (`agent_id`, `firstname`, `lastname`, `current_lat`, `current_long`, `rating`, `phoneno`, `account_status`, `password`, `email`, `delivery_earning`, `total_delivery_earning`) VALUES
(1, 'Jeff', 'Dunhalm', '-1.00000', '20.00000', '5.00000', '+142142142142', '', '21232f297a57a5a743894a0e4a801fc3', '', 0, 0),
(2, 'Humy', 'Wine', '4.20000', '5.20000', '4.20000', '+235235235235', '', 'c822c1b63853ed273b89687ac505f9fa', '', 0, 0),
(3, 'Boniface', 'Macharia', '-4.05000', '39.66700', '0.00000', '+2547001414141', 'Active', '12329f3e8ce6f4c0d59c6fb1a7bab387', 'bonificial@usdn.com', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `itemid` int(11) NOT NULL,
  `item_name` text NOT NULL,
  `restaurant_id` text NOT NULL,
  `item_photo` text NOT NULL,
  `item_price_per_unit` double NOT NULL,
  `item_packaging` text NOT NULL,
  `item_units` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
`OrderID` int(11) NOT NULL,
  `OrderAmount` decimal(10,2) NOT NULL,
  `orderStatus` varchar(20) NOT NULL,
  `UserID` int(11) NOT NULL,
  `DateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `restaurant_id` int(11) NOT NULL,
  `delivery_agent_id` int(11) NOT NULL,
  `payment_status` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`OrderID`, `OrderAmount`, `orderStatus`, `UserID`, `DateTime`, `restaurant_id`, `delivery_agent_id`, `payment_status`) VALUES
(1, '500.34', 'Processing', 5, '2017-03-02 07:00:00', 2, 2, '');

-- --------------------------------------------------------

--
-- Table structure for table `restaurants`
--

CREATE TABLE IF NOT EXISTS `restaurants` (
`restaurant_id` int(11) NOT NULL,
  `restaurant_name` text NOT NULL,
  `Address_lat` decimal(10,5) NOT NULL,
  `Address_lng` decimal(10,5) NOT NULL,
  `formatted_address` text NOT NULL,
  `rating` decimal(10,5) NOT NULL,
  `username` text NOT NULL,
  `email` text NOT NULL,
  `account_status` text NOT NULL,
  `password` text NOT NULL,
  `accepting_orders` text NOT NULL,
  `time_open` time NOT NULL,
  `time_close` time NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`restaurant_id`, `restaurant_name`, `Address_lat`, `Address_lng`, `formatted_address`, `rating`, `username`, `email`, `account_status`, `password`, `accepting_orders`, `time_open`, `time_close`) VALUES
(1, 'MacDonald', '5.20000', '4.60000', '5th Street, Fox,Nevada', '3.50000', 'macdonalds', 'macdonalds@yahoo.com', 'Active', '21232f297a57a5a743894a0e4a801fc3', '0', '00:00:00', '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `userid` int(11) NOT NULL,
  `firstname` text NOT NULL,
  `lastname` text NOT NULL,
  `email` text NOT NULL,
  `password` varchar(10) NOT NULL,
  `address_lat` decimal(10,5) NOT NULL,
  `address_lng` decimal(10,5) NOT NULL,
  `formatted_address` text NOT NULL,
  `phoneno` varchar(20) NOT NULL,
  `account_status` varchar(20) NOT NULL,
  `refferal_code` varchar(1000) DEFAULT NULL,
  `discount` double NOT NULL DEFAULT '0',
  `reffered_by` int(11) NOT NULL,
  `no_of_refferals` int(11) NOT NULL
) ENGINE = InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userid`, `firstname`, `lastname`, `email`, `password`, `address_lat`, `address_lng`, `formatted_address`, `phoneno`, `account_status`, `refferal_code`, `discount`, `reffered_by`, `no_of_refferals`) VALUES
(8, 'Gilb', 'Hum', 'gilb3um@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-4.05000', '39.66700', '', '+25411123552', 'Active', '81e4aa8de7', 0, 0, 0),
(9, 'Gilb', 'Hum', 'gilb3rum@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-4.05000', '39.66700', '', '+25411123552', 'Active', '1db1c5db86', 0, 0, 0),
(10, 'Gilb', 'Hum', 'giihm@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-4.05000', '39.66700', '', '+25411123552', 'Active', 'a22d387bda', 0, 0, 0),
(11, 'theord', 'gabe', 'thennneee@yahoo.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+25871423621', 'Active', 'b3bda8839e', 0, 0, 0),
(12, 'Gilb', 'Hum', 'giihm@gomail.com', '21232f297a57a5a743894a0e4a801fc3', '-4.05000', '39.66700', '', '+25411123552', 'Active', '8f5bffcbc6', 0, 0, 0),
(13, 'theord', 'gabe', 'thwde@yahoo.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+25871423621', 'Active', '64c2707514', 0, 0, 0),
(14, 'theord', 'gabe', 'thfdwde@yahoo.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+25871423621', 'Active', 'b0d5f375e2', 0, 435436, 0),
(15, 'Boniface ', 'Macharia', 'bonificial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '0.00000', '0.00000', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', '64b3d1c15b', 0, 0, 0),
(16, 'Boniface ', 'Macharia', 'bonifitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', 'c7ef66b257', 0, 0, 0),
(17, 'Boniface ', 'Macharia', 'bognifitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', '2449781000', 0, 810000, 0),
(18, 'Boniface ', 'Macharia', 'bognissfitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', '32d2cc9cf4', 0, 12, 0),
(19, 'Boniface ', 'Macharia', 'bogwnissfitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', 'a26f1f1112', 0, 12, 0),
(20, 'Boniface ', 'Macharia', 'botgwnissfitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', 'a3bb79035d', 0, 12, 0),
(21, 'Boniface ', 'Macharia', 'botgwnissfsitcial.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', '2c33484371', 0, 12, 0),
(22, 'Boniface ', 'Macharia', 'botl.items@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', '68d1832672', 0, 12, 0),
(23, 'Boniface', 'bbdfd', 'xfdv@gmail.com', '21232f297a57a5a743894a0e4a801fc3', '-1.29200', '36.82200', 'Ufanisi House, Nairobi, Kenya', '+254700618822', 'Active', 'cb4a6cdd8c', 0, 8, 0),
(24, '', '', '', 'd41d8cd98f00b204e9800998ecf8427e', '0.00000', '0.00000', '', '', 'Active', '944ce93b72', 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
 ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `agentearnings`
--
ALTER TABLE `agentearnings`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `delivery_agents`
--
ALTER TABLE `delivery_agents`
 ADD PRIMARY KEY (`agent_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
 ADD PRIMARY KEY (`OrderID`);

--
-- Indexes for table `restaurants`
--
ALTER TABLE `restaurants`
 ADD PRIMARY KEY (`restaurant_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`userid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agentearnings`
--
ALTER TABLE `agentearnings`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `delivery_agents`
--
ALTER TABLE `delivery_agents`
MODIFY `agent_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `restaurants`
--
ALTER TABLE `restaurants`
MODIFY `restaurant_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=25;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
