--  新增用户自定义配置
CREATE TABLE `uas_user_defined_setting` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `setting_key` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '自定义配置Key',
  `setting_value` varchar(10000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '自定义配置项值',
  PRIMARY KEY (`userid`,`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户自定义配置'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;

-- 增加相关权限
INSERT INTO usp_uas.sys_right (right_id, right_name, right_code, parent_id, app_id, sys_type, has_scope, service_demander, service_demander_common, service_provider, service_provider_common, device_user, device_user_common, cloud_manager, cloud_manager_common, has_extra_corp) VALUES(410, '用户自定义配置', NULL, 54, 10002, 0, 'N', 'Y', 'Y', 'Y', 'Y', 'Y', 'N', 'Y', 'Y', 'N');

-- 增加 url 鉴权配置
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description) VALUES(1260127683361275906, 1, '/api/uas/user-defined-setting/list', 'GET', 410, '获取用户自定义配置');
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description) VALUES(1260127683361275907, 1, '/api/uas/user-defined-setting/merge', 'POST', 410, '更新用户自定义配置');

