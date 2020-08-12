-- 修改oauth表
ALTER TABLE `usp_auth.oauth_client_details` ADD COLUMN `corp_id` bigint(20) COMMENT '企业(租户)ID' AFTER `autoapprove`;

-- 插入hr需要的oauth信息
INSERT INTO usp_auth.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, corp_id, create_time, update_time)
VALUES ('zjft',NULL,'$2a$10$NkZojsg.OLFGJ7dmyhHY9.U4MZPzfidIr9ShzmPml9xzmqk/3Odz2','all','authorization_code,password,refresh_token,implicit,client_credentials','',NULL,18000,28800,'{}','true',1219133519689580551,NULL,NULL);

-- 新增权限
INSERT INTO sys_right (right_id,right_name,parent_id,app_id,sys_type,service_demander,service_provider,device_user,cloud_manager) VALUES
(310,'查询金融设备工单',175,10003,0,'N','Y','N','N')
,(311,' 查看金融设备工单详情',175,10003,0,'N','Y','N','N');
INSERT INTO sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES
(1231973675530383361,1,'/api/anyfix/work-request/query/atmcase','POST',310,'')
,(1231973980158488577,1,'/api/anyfix/work-request/detail/atmcase','POST',311,'');

-- 新增消息模板
INSERT INTO `push_template` VALUES (7,10001,'退单提醒','您有一个工单[${workCode}]被退回','<div style=\"line-height: 30px;margin-top: 10px;\"><span style=\"display:inline-block;width:25%;color:#999999;font-size:14px;\">工单号</span><span style=\"font-size:15px;color:#444444;font-weight:500;\">${workCode}</span></div><div style=\"line-height: 30px;margin-top: 10px;\"><span style=\"display:inline-block;width:25%;color:#999999;font-size:14px;\">委托单号</span><span style=\"font-size:15px;color:#444444;font-weight:500;\">${checkWorkCode}</span></div><div style=\"line-height: 30px;margin-top: 10px;\"><span style=\"display:inline-block;width: 25%;color:#999999;font-size:14px;\">客户名称</span><span style=\"font-size:15px;color:#444444;font-weight:500;\">${customCorpName}</span></div><div style=\"line-height: 30px;margin-top: 10px;\"><span style=\"display:inline-block;width: 25%;color:#999999;font-size:14px;\">设备</span><span style=\"font-size:15px;color:#444444;font-weight:500;\">${smallClassName}</span></div>',23,'2019-08-12 17:30:28','1');
