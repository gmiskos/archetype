-- archetype.`user` definition

CREATE TABLE `user` (
    `id` bigint(20) NOT NULL,
    `email` varchar(255) DEFAULT NULL,
    `enabled` bit(1) NOT NULL,
    `first_name` varchar(255) DEFAULT NULL,
    `last_name` varchar(255) DEFAULT NULL,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    `token_expired` bit(1) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- archetype.`role` definition

CREATE TABLE `role` (
    `id` bigint(20) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- archetype.privilege definition

CREATE TABLE `privilege` (
    `id` bigint(20) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- archetype.users_roles definition

CREATE TABLE `users_roles` (
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL,
    KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
    KEY `FKgd3iendaoyh04b95ykqise6qh` (`user_id`),
    CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- archetype.roles_privileges definition

CREATE TABLE `roles_privileges` (
    `role_id` bigint(20) NOT NULL,
    `privilege_id` bigint(20) NOT NULL,
    KEY `FK5yjwxw2gvfyu76j3rgqwo685u` (`privilege_id`),
    KEY `FK9h2vewsqh8luhfq71xokh4who` (`role_id`),
    CONSTRAINT `FK5yjwxw2gvfyu76j3rgqwo685u` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`),
    CONSTRAINT `FK9h2vewsqh8luhfq71xokh4who` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;