-- Dumping data for table db_money_lover.db_money_lover_file: ~6 rows (approximately)
INSERT INTO `db_money_lover_file` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `scope`, `file_name`, `file_size`, `file_type`, `file_url`, `is_system_file`, `public_id`) VALUES
	(8372381648158720, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '001-united-states.png', 1268, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616416/xjujm0tjkjkhrgie8dfq.png', b'1', 'xjujm0tjkjkhrgie8dfq'),
	(8372381650550784, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '002-united-kingdom.png', 1751, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616417/cwk5s20glr3zjf3l4tf5.png', b'1', 'cwk5s20glr3zjf3l4tf5'),
	(8372381650649088, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '003-china.png', 621, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616418/uzwxa9rv0jaq15bwqhbn.png', b'1', 'uzwxa9rv0jaq15bwqhbn'),
	(8372381650649089, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '004-japan.png', 366, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616419/dkrvtmd7ysdyh3zfipih.png', b'1', 'dkrvtmd7ysdyh3zfipih'),
	(8372381650747392, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '005-european-union.png', 861, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616421/avcxkoyvwubppn8u8tpg.png', b'1', 'avcxkoyvwubppn8u8tpg'),
	(8372381650780160, 'super_admin', '2025-04-14 07:40:21', 'super_admin', '2025-04-14 07:40:21', 1, 'international_flags', '006-vietnam.png', 616, 'IMAGE', 'https://res.cloudinary.com/dnnpea3fw/image/upload/v1744616422/syup4tjojmiwaccz1p38.png', b'1', 'syup4tjojmiwaccz1p38');

-- Dumping data for table db_money_lover.db_money_lover_currency: ~6 rows (approximately)
INSERT INTO `db_money_lover_currency` (`id`, `created_by`, `created_date`, `modified_by`, `modified_date`, `status`, `code`, `name`, `icon_id`) VALUES
	(8372360012201984, 'super_admin', '2025-04-14 07:29:21', 'super_admin', '2025-04-14 07:29:21', 1, 'USD', 'United States Dollar', 8372381648158720),
	(8372363257348096, 'super_admin', '2025-04-14 07:31:00', 'super_admin', '2025-04-14 07:31:00', 1, 'GBP', 'Pound', 8372381650550784),
	(8372364763234304, 'super_admin', '2025-04-14 07:31:46', 'super_admin', '2025-04-14 07:31:46', 1, 'YUA', 'Yuan Renminbi', 8372381650649088),
	(8372366127136768, 'super_admin', '2025-04-14 07:32:28', 'super_admin', '2025-04-14 07:32:28', 1, 'JPY', 'Yen', 8372381650649089),
	(8372370320850944, 'super_admin', '2025-04-14 07:34:36', 'super_admin', '2025-04-14 07:34:36', 1, 'EUR', 'Euro', 8372381650747392),
	(8372371040731136, 'super_admin', '2025-04-14 07:34:58', 'super_admin', '2025-04-14 07:34:58', 1, 'VND', 'Vietnam Dong', 8372381650780160);
