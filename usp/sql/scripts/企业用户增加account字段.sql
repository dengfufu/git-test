-- 租户信息增加第三方用户验证接口
ALTER TABLE usp_uas.sys_tenant ADD apply_check_api varchar(256) COMMENT '第三方用户验证接口' after cloud_manager;

-- 企业用户id
ALTER TABLE usp_uas.uas_corp_apply ADD account varchar(20) COMMENT '企业员工账号' after applynote;
ALTER TABLE usp_uas.uas_corp_user ADD account varchar(20) COMMENT '企业员工账号' after corpid;
