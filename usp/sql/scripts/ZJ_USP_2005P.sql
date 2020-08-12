ALTER TABLE usp_uas.sys_right ADD right_code varchar(20) DEFAULT NULL COMMENT '权限码' after right_name;
ALTER TABLE usp_uas.sys_right ADD has_extra_corp varchar(1) NOT NULL DEFAULT 'N' COMMENT '其他租户企业，租户性质的补充' after cloud_manager;

CREATE TABLE `sys_right_extra_corp` (
  `right_id` bigint(20) NOT NULL COMMENT '权限编号',
  `corp_id` bigint(20) NOT NULL COMMENT '企业ID',
   PRIMARY KEY (`right_id`,`corp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限额外租户表';