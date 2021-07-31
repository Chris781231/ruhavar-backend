CREATE TABLE `customers` (
                             `id` bigint(20) AUTO_INCREMENT,
                             `address` varchar(255) COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
                             `city` varchar(255) COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
                             `email` varchar(255) COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
                             `name` varchar(255) COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
                             `phone_number` varchar(255) COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;
