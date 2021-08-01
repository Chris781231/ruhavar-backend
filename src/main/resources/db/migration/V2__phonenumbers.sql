CREATE TABLE `phonenumbers` (
                             `id` bigint(20) AUTO_INCREMENT,
                             `type` varchar(255) not null COLLATE utf8mb4_hungarian_ci,
                             `number` varchar(255) not null COLLATE utf8mb4_hungarian_ci,
                             `customers_id` bigint(20) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;