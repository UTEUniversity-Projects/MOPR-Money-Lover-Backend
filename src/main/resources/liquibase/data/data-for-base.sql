-- Dumping data for table db_money_lover.db_money_lover_group: ~5 rows (approximately)
INSERT INTO `db_money_lover_group` (`id`, `description`, `is_system_group`, `kind`, `name`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`) VALUES
	(8274172085239808, 'Role for super admin', b'1', 1, ' ROLE SUPER ADMIN', 'super_admin', '2025-03-10 15:08:32', 'super_admin', '2025-03-10 15:08:32', 1),
	(8274172436414464, 'Role for admin', b'1', 2, ' ROLE ADMIN', 'super_admin', '2025-03-10 15:08:34', 'super_admin', '2025-03-10 15:08:34', 1),
	(8274172862824448, 'Role for manager', b'1', 3, ' ROLE MANAGER', 'super_admin', '2025-03-10 15:08:47', 'super_admin', '2025-03-10 15:08:47', 1),
	(8274173363322880, 'Role for internal user', b'1', 4, ' ROLE INTERNAL', 'super_admin', '2025-03-10 15:09:02', 'super_admin', '2025-03-10 15:09:02', 1),
	(8274173680582656, 'Role for user', b'1', 5, ' ROLE USER', 'super_admin', '2025-03-10 15:09:12', 'super_admin', '2025-03-10 15:09:12', 1);

-- Dumping data for table db_money_lover.db_money_lover_permission: ~22 rows (approximately)
INSERT INTO `db_money_lover_permission` (`id`, `action`, `code`, `description`, `name`, `name_group`, `show_menu`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`) VALUES
	(8286933868337983, '/api/v1/group/list', 'GRO_LIS', 'List group', 'List group', 'Group', b'0', 'super_admin', '2025-03-10 03:21:57', 'super_admin', '2025-03-10 03:21:57', 1),
	(8286938957348864, '/api/v1/group/get', 'GRO_GET', 'Get group', 'Get group', 'Group', b'0', 'super_admin', '2025-03-10 03:23:43', 'super_admin', '2025-03-10 03:23:43', 1),
	(8286942226737843, '/api/v1/group/create', 'GRO_CRE', 'Create group', 'Create group', 'Group', b'0', 'super_admin', '2025-03-10 03:24:00', 'super_admin', '2025-03-10 03:24:00', 1),
	(8286942415224832, '/api/v1/group/update', 'GRO_UPD', 'Update group', 'Update group', 'Group', b'0', 'super_admin', '2025-03-10 03:24:25', 'super_admin', '2025-03-10 03:24:25', 1),
	(8286942975426560, '/api/v1/group/add-permission', 'GRO_PER_ADD', 'Group add permissions', 'Group add permissions', 'Group', b'0', 'super_admin', '2025-03-10 03:24:25', 'super_admin', '2025-03-10 03:24:25', 1),
	(8286943786663936, '/api/v1/group/remove-permission', 'GRO_PER_REM', 'Group remove permissions', 'Group remove permissions', 'Group', b'0', 'super_admin', '2025-03-10 03:24:25', 'super_admin', '2025-03-10 03:24:25', 1),
	(8286944394838016, '/api/v1/group/delete', 'GRO_DEL', 'Delete group', 'Delete group', 'Group', b'0', 'super_admin', '2025-03-10 03:24:43', 'super_admin', '2025-03-10 03:24:43', 1),
	(8286946225651712, '/api/v1/permission/list', 'PER_LIS', 'List permission', 'List permission', 'Permission', b'0', 'super_admin', '2025-03-10 03:25:39', 'super_admin', '2025-03-10 03:25:39', 1),
	(8286946657402880, '/api/v1/permission/get', 'PER_GET', 'Get permission', 'Get permission', 'Permission', b'0', 'super_admin', '2025-03-10 03:25:52', 'super_admin', '2025-03-10 03:25:52', 1),
	(8286947141156864, '/api/v1/permission/create', 'PER_CRE', 'Create permission', 'Create permission', 'Permission', b'0', 'super_admin', '2025-03-10 03:26:07', 'super_admin', '2025-03-10 03:26:07', 1),
	(8286947649257472, '/api/v1/permission/update', 'PER_UPD', 'Update permission', 'Update permission', 'Permission', b'0', 'super_admin', '2025-03-10 03:26:23', 'super_admin', '2025-03-10 03:26:23', 1),
	(8286948333322240, '/api/v1/permission/delete', 'PER_DEL', 'Delete permission', 'Delete permission', 'Permission', b'0', 'super_admin', '2025-03-10 03:26:43', 'super_admin', '2025-03-10 03:26:43', 1),
	(8286950156173312, '/api/v1/user/list', 'USE_LIS', 'List user', 'List user', 'User', b'0', 'super_admin', '2025-03-10 03:27:39', 'super_admin', '2025-03-10 03:27:39', 1),
	(8286950515507200, '/api/v1/user/get', 'USE_GET', 'Get user', 'Get user', 'User', b'0', 'super_admin', '2025-03-10 03:27:50', 'super_admin', '2025-03-10 03:27:50', 1),
	(8286951655669760, '/api/v1/user/update', 'USE_UPD', 'Update user', 'Update user', 'User', b'0', 'super_admin', '2025-03-10 03:28:25', 'super_admin', '2025-03-10 03:28:25', 1),
	(8286952140406784, '/api/v1/user/delete', 'USE_DEL', 'Delete user', 'Delete user', 'User', b'0', 'super_admin', '2025-03-10 03:28:40', 'super_admin', '2025-03-10 03:28:40', 1);

-- Dumping data for table db_money_lover.db_money_lover_account: ~5 rows (approximately)
INSERT INTO `db_money_lover_account` (`id`, `avatar_path`, `email`, `is_super_admin`, `password`, `phone`, `username`, `group_id`, `verified`,`created_by`, `created_date`, `modified_by`, `modified_date`, `status`) VALUES
	(8274180735929377, '/image/avatar/super_admin', 'super_admin@gmail.com', b'1', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0123456789', 'super_admin', 8274172085239808, b'1', 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1),
	(8274182432555008, '/image/avatar/admin', 'admin@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0987654321', 'admin', 8274172436414464, b'1', 'super_admin', '2025-03-10 15:16:25', 'super_admin', '2025-03-10 15:16:25', 1),
	(8274185907263427, '/image/avatar/manager', 'manager@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0538227546', 'manager', 8274172862824448, b'1', 'super_admin', '2025-03-10 15:16:25', 'super_admin', '2025-03-10 15:16:25', 1),
	(8274185907273728, '/image/avatar/internal', 'internal@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0735289218', 'internal', 8274173363322880, b'1', 'super_admin', '2025-03-10 15:16:25', 'super_admin', '2025-03-10 15:16:25', 1),
	(8274188362824639, '/image/avatar/user', 'user@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0246813579', 'user', 8274173680582656, b'1', 'super_admin', '2025-03-10 15:17:25', 'super_admin', '2025-03-10 15:17:25', 1);

-- Dumping data for table db_money_lover.db_money_lover_user: ~5 rows (approximately)
INSERT INTO `db_money_lover_user` (`id`, `birthday`, `full_name`, `gender`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`) VALUES
	(8274180735929377, '2000-03-25 00:00:00', 'Super Admin', 1, 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1),
	(8274182432555008, '2000-11-25 00:00:00', 'Admin', 1, 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1),
	(8274185907263427, '2000-06-10 00:00:00', 'Manager', 1, 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1),
	(8274185907273728, '2000-05-15 00:00:00', 'Internal', 1, 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1),
	(8274188362824639, '2000-11-09 00:00:00', 'User', 1, 'super_admin', '2025-03-10 15:15:25', 'super_admin', '2025-03-10 15:15:25', 1);

-- Dumping data for table db_money_lover.oauth2_registered_client: ~5 rows (approximately)
INSERT INTO `oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`, `client_secret_expires_at`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `post_logout_redirect_uris`, `scopes`, `client_settings`, `token_settings`) VALUES
	('2f1e6400-9632-4e36-9c05-af642db77b3d', 'super_admin', '2025-03-09 20:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('aa6de5a0-61a1-44ba-911c-6eec251200b0', 'admin', '2025-03-09 20:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('b1284a3a-d525-48fa-a52c-ddd4aca84a89', 'manager', '2025-03-09 20:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('c420383c-860f-4144-a75a-75086035fdb7', 'internal', '2025-03-09 20:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('c421ad30-ca04-4d0f-8f18-e7953c42da5c', 'user', '2025-03-09 20:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}');

-- Dumping data for table db_money_lover.db_money_lover_permission_group: ~60 rows (approximately)
INSERT INTO `db_money_lover_permission_group` (`group_id`, `permission_id`) VALUES
	(8274172085239808, 8286938957348864),
	(8274172085239808, 8286942415224832),
	(8274172085239808, 8286942975426560),
	(8274172085239808, 8286943786663936),
	(8274172085239808, 8286933868337983),
	(8274172085239808, 8286942226737843),
	(8274172085239808, 8286944394838016),
	(8274172085239808, 8286946225651712),
	(8274172085239808, 8286946657402880),
	(8274172085239808, 8286947141156864),
	(8274172085239808, 8286947649257472),
	(8274172085239808, 8286948333322240),
	(8274172085239808, 8286950156173312),
	(8274172085239808, 8286950515507200),
	(8274172085239808, 8286951655669760),
	(8274172085239808, 8286952140406784),
	(8274172436414464, 8286938957348864),
	(8274172436414464, 8286942415224832),
	(8274172436414464, 8286942975426560),
	(8274172436414464, 8286943786663936),
	(8274172436414464, 8286946225651712),
	(8274172436414464, 8286946657402880),
	(8274172436414464, 8286947141156864),
	(8274172436414464, 8286947649257472),
	(8274172436414464, 8286950156173312),
	(8274172436414464, 8286950515507200),
	(8274172436414464, 8286951655669760),
	(8274172436414464, 8286952140406784),
	(8274172862824448, 8286938957348864),
	(8274172862824448, 8286942415224832),
	(8274172862824448, 8286946225651712),
	(8274172862824448, 8286946657402880),
	(8274172862824448, 8286950156173312),
	(8274172862824448, 8286950515507200),
	(8274172862824448, 8286951655669760),
	(8274173363322880, 8286938957348864),
	(8274173363322880, 8286942415224832),
	(8274173363322880, 8286946225651712),
	(8274173363322880, 8286946657402880),
	(8274173363322880, 8286950156173312),
	(8274173363322880, 8286950515507200);
