-- Dumping data for table db_base.oauth2_registered_client: ~5 rows (approximately)
INSERT INTO `oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`, `client_secret_expires_at`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `post_logout_redirect_uris`, `scopes`, `client_settings`, `token_settings`) VALUES
	('aa6de5a0-61a1-44ba-911c-6eec251200b0', 'super_admin', '2025-03-10T03:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('b1284a3a-d525-48fa-a52c-ddd4aca84a89', 'admin', '2025-03-10T03:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('c420383c-860f-4144-a75a-75086035fdb7', 'manager', '2025-03-10T03:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('c421ad30-ca04-4d0f-8f18-e7953c42da5c', 'internal', '2025-03-10T03:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}'),
	('2f1e6400-9632-4e36-9c05-af642db77b3d', 'user', '2025-03-10T03:18:18', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', NULL, NULL, 'client_secret_basic', 'authorization_code,refresh_token', '/api/oauth2/callback', NULL, 'openid,profile,email', '{"@class": "java.util.LinkedHashMap", "requireProofKey": false, "requireAuthorizationConsent": false}', '{"@class": "java.util.LinkedHashMap", "accessTokenTimeToLive": null, "refreshTokenTimeToLive": null}');

-- Dumping data for table db_base.db_base_group: ~5 rows (approximately)
INSERT INTO `db_base_group` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `description`, `kind`, `name`, `is_system_group`) VALUES
	(8274172085239808, 'super_admin', '2025-03-10T15:08:32', 'super_admin', '2025-03-10T15:08:32', 1, 'Role for super admin', 1, ' ROLE SUPER ADMIN', b'1'),
	(8274172436414464, 'super_admin', '2025-03-10T15:08:34', 'super_admin', '2025-03-10T15:08:34', 1, 'Role for admin', 2, ' ROLE ADMIN', b'1'),
	(8274172862824448, 'super_admin', '2025-03-10T15:08:47', 'super_admin', '2025-03-10T15:08:47', 1, 'Role for manager', 3, ' ROLE MANAGER', b'1'),
	(8274173363322880, 'super_admin', '2025-03-10T15:09:02', 'super_admin', '2025-03-10T15:09:02', 1, 'Role for internal user', 4, ' ROLE INTERNAL', b'1'),
	(8274173680582656, 'super_admin', '2025-03-10T15:09:12', 'super_admin', '2025-03-10T15:09:12', 1, 'Role for user', 5, ' ROLE USER', b'1');

-- Dumping data for table db_base.db_base_permission: ~22 rows (approximately)
INSERT INTO `db_base_permission` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `action`, `code`, `description`, `name`, `name_group`, `show_menu`) VALUES
	(8274175207243776, 'super_admin', '2025-03-10T15:09:59', 'super_admin', '2025-03-10T15:09:59', 1, '/api/v1/account/list', 'ACC_LIS', 'List account', 'List account', 'Account', b'0'),
	(8274175633620992, 'super_admin', '2025-03-10T15:10:12', 'super_admin', '2025-03-10T15:10:12', 1, '/api/v1/account/get', 'ACC_GET', 'Get account', 'Get account', 'Account', b'0'),
	(8274176145981440, 'super_admin', '2025-03-10T15:10:27', 'super_admin', '2025-03-10T15:10:27', 1, '/api/v1/account/create', 'ACC_CRE', 'Create account', 'Create account', 'Account', b'0'),
	(8274176672759808, 'super_admin', '2025-03-10T15:10:43', 'super_admin', '2025-03-10T15:10:43', 1, '/api/v1/account/update', 'ACC_UPD', 'Update account', 'Update account', 'Account', b'0'),
	(8274177328185344, 'super_admin', '2025-03-10T15:11:03', 'super_admin', '2025-03-10T15:11:03', 1, '/api/v1/account/delete', 'ACC_DEL', 'Delete account', 'Delete account', 'Account', b'0'),
	(8286938957348864, 'super_admin', '2025-03-10T03:21:57', 'super_admin', '2025-03-10T03:21:57', 1, '/api/v1/group/list', 'GRO_LIS', 'List group', 'List group', 'Group', b'0'),
	(8286942415224832, 'super_admin', '2025-03-10T03:23:43', 'super_admin', '2025-03-10T03:23:43', 1, '/api/v1/group/get', 'GRO_GET', 'Get group', 'Get group', 'Group', b'0'),
	(8286942975426560, 'super_admin', '2025-03-10T03:24:00', 'super_admin', '2025-03-10T03:24:00', 1, '/api/v1/group/create', 'GRO_CRE', 'Create group', 'Create group', 'Group', b'0'),
	(8286943786663936, 'super_admin', '2025-03-10T03:24:25', 'super_admin', '2025-03-10T03:24:25', 1, '/api/v1/group/update', 'GRO_UPD', 'Update group', 'Update group', 'Group', b'0'),
	(8286933868337983, 'super_admin', '2025-03-10T03:24:25', 'super_admin', '2025-03-10T03:24:25', 1, '/api/v1/group/add-permission', 'GRO_PER_ADD', 'Group add permissions', 'Group add permissions', 'Group', b'0'),
	(8286942226737843, 'super_admin', '2025-03-10T03:24:25', 'super_admin', '2025-03-10T03:24:25', 1, '/api/v1/group/remove-permission', 'GRO_PER_REM', 'Group remove permissions', 'Group remove permissions', 'Group', b'0'),
	(8286944394838016, 'super_admin', '2025-03-10T03:24:43', 'super_admin', '2025-03-10T03:24:43', 1, '/api/v1/group/delete', 'GRO_DEL', 'Delete group', 'Delete group', 'Group', b'0'),
	(8286946225651712, 'super_admin', '2025-03-10T03:25:39', 'super_admin', '2025-03-10T03:25:39', 1, '/api/v1/permission/list', 'PER_LIS', 'List permission', 'List permission', 'Permission', b'0'),
	(8286946657402880, 'super_admin', '2025-03-10T03:25:52', 'super_admin', '2025-03-10T03:25:52', 1, '/api/v1/permission/get', 'PER_GET', 'Get permission', 'Get permission', 'Permission', b'0'),
	(8286947141156864, 'super_admin', '2025-03-10T03:26:07', 'super_admin', '2025-03-10T03:26:07', 1, '/api/v1/permission/create', 'PER_CRE', 'Create permission', 'Create permission', 'Permission', b'0'),
	(8286947649257472, 'super_admin', '2025-03-10T03:26:23', 'super_admin', '2025-03-10T03:26:23', 1, '/api/v1/permission/update', 'PER_UPD', 'Update permission', 'Update permission', 'Permission', b'0'),
	(8286948333322240, 'super_admin', '2025-03-10T03:26:43', 'super_admin', '2025-03-10T03:26:43', 1, '/api/v1/permission/delete', 'PER_DEL', 'Delete permission', 'Delete permission', 'Permission', b'0'),
	(8286950156173312, 'super_admin', '2025-03-10T03:27:39', 'super_admin', '2025-03-10T03:27:39', 1, '/api/v1/user/list', 'USE_LIS', 'List user', 'List user', 'User', b'0'),
	(8286950515507200, 'super_admin', '2025-03-10T03:27:50', 'super_admin', '2025-03-10T03:27:50', 1, '/api/v1/user/get', 'USE_GET', 'Get user', 'Get user', 'User', b'0'),
	(8286951021084672, 'super_admin', '2025-03-10T03:28:05', 'super_admin', '2025-03-10T03:28:05', 1, '/api/v1/user/create', 'USE_CRE', 'Create user', 'Create user', 'User', b'0'),
	(8286951655669760, 'super_admin', '2025-03-10T03:28:25', 'super_admin', '2025-03-10T03:28:25', 1, '/api/v1/user/update', 'USE_UPD', 'Update user', 'Update user', 'User', b'0'),
	(8286952140406784, 'super_admin', '2025-03-10T03:28:40', 'super_admin', '2025-03-10T03:28:40', 1, '/api/v1/user/delete', 'USE_DEL', 'Delete user', 'Delete user', 'User', b'0');

-- Dumping data for table db_base.db_base_account: ~ rows (approximately)
INSERT INTO `db_base_account` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `avatar_path`, `email`, `is_super_admin`, `password`, `phone`, `username`, `group_id`) VALUES
	(8274182432555008, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '/image/avatar/super_admin', 'super_admin@gmail.com', b'1', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0123456789', 'super_admin', 8274172085239808),
	(8274185907273728, 'super_admin', '2025-03-10T15:16:25', 'super_admin', '2025-03-10T15:16:25', 1, '/image/avatar/admin', 'admin@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0987654321', 'admin', 8274172436414464),
	(8274188362824639, 'super_admin', '2025-03-10T15:16:25', 'super_admin', '2025-03-10T15:16:25', 1, '/image/avatar/admin', 'manager@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0538227546', 'manager', 8274172862824448),
	(8274180735929377, 'super_admin', '2025-03-10T15:16:25', 'super_admin', '2025-03-10T15:16:25', 1, '/image/avatar/admin', 'internal@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0735289218', 'internal', 8274173363322880),
	(8274185907263427, 'super_admin', '2025-03-10T15:17:25', 'super_admin', '2025-03-10T15:17:25', 1, '/image/avatar/user', 'user@gmail.com', b'0', '$2a$12$mp9n9/fCtOI5hDDuUx3uMOYYepGUwlXzY5A1F4x5pLiz9Q8uhRguW', '0246813579', 'user', 8274173680582656);

-- Dumping data for table db_base.db_base_user: ~5 rows (approximately)
INSERT INTO `db_base_user` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `birthday`, `gender`) VALUES
	(8274182432555008, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '2000-03-25T00:00:00', 1),
	(8274185907273728, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '2000-11-25T00:00:00', 1),
	(8274188362824639, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '2000-06-10T00:00:00', 1),
	(8274180735929377, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '2000-05-15T00:00:00', 1),
	(8274185907263427, 'super_admin', '2025-03-10T15:15:25', 'super_admin', '2025-03-10T15:15:25', 1, '2000-11-09T00:00:00', 1);

-- Dumping data for table db_base.db_base_permission_group: ~58 rows (approximately)
INSERT INTO `db_base_permission_group` (`group_id`, `permission_id`) VALUES
	(8274172085239808, 8274175207243776),
	(8274172085239808, 8274175633620992),
	(8274172085239808, 8274176145981440),
	(8274172085239808, 8274176672759808),
	(8274172085239808, 8274177328185344),
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
	(8274172085239808, 8286951021084672),
	(8274172085239808, 8286951655669760),
	(8274172085239808, 8286952140406784),
    (8274172436414464, 8274175207243776),
	(8274172436414464, 8274175633620992),
	(8274172436414464, 8274176145981440),
	(8274172436414464, 8274176672759808),
	(8274172436414464, 8274177328185344),
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
	(8274172436414464, 8286951021084672),
	(8274172436414464, 8286951655669760),
	(8274172436414464, 8286952140406784),
    (8274172862824448, 8274175207243776),
   	(8274172862824448, 8274175633620992),
   	(8274172862824448, 8274176145981440),
   	(8274172862824448, 8274176672759808),
   	(8274172862824448, 8286938957348864),
   	(8274172862824448, 8286942415224832),
   	(8274172862824448, 8286946225651712),
   	(8274172862824448, 8286946657402880),
   	(8274172862824448, 8286950156173312),
   	(8274172862824448, 8286950515507200),
   	(8274172862824448, 8286951021084672),
   	(8274172862824448, 8286951655669760),
    (8274173363322880, 8274175207243776),
    (8274173363322880, 8274175633620992),
    (8274173363322880, 8286938957348864),
    (8274173363322880, 8286942415224832),
    (8274173363322880, 8286946225651712),
    (8274173363322880, 8286946657402880),
    (8274173363322880, 8286950156173312),
    (8274173363322880, 8286950515507200);
