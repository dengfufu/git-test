-- 插入oa需要的oauth信息
INSERT INTO usp_auth.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, corp_id, create_time, update_time)
VALUES('zjft-oa', NULL, '$2a$10$w/SGT1z6fSnonWsClbsj0u4Ytlz/QhblyUomhbURmP5bYH3jvBMGu', 'all', 'authorization_code,password,refresh_token,implicit,client_credentials', '', NULL, 18000, 28800, '{}', 'true', 1229327791726825475, NULL, NULL);
