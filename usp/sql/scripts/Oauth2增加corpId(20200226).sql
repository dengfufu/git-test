-- nacos中usp-gateway.yml也要更新,增加url权限过滤
-- 修改oauth表
ALTER TABLE `usp_auth.oauth_client_details` ADD COLUMN `corp_id` bigint(20) COMMENT '企业(租户)ID' AFTER `autoapprove`;

-- 插入hr需要的oauth信息
INSERT INTO usp_auth.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, corp_id, create_time, update_time)
VALUES ('zjft-hr',NULL,'$2a$10$w/SGT1z6fSnonWsClbsj0u4Ytlz/QhblyUomhbURmP5bYH3jvBMGu','all','authorization_code,password,refresh_token,implicit,client_credentials','',NULL,18000,28800,'{}','true',1219133519689580551,NULL,NULL);
