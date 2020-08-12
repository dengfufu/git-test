-- 新增访问uri
INSERT INTO usp_uas.sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES
(1234299244054663170,0,'/api/anyfix/work-excel/template','GET',183,'下载工单模板'),
(1234299609902829570,0,'/api/anyfix/work-excel/import','POST',183,'导入工单');

-- 服务完成表新增出发时间
alter table usp_anyfix.work_deal add ( go_time datetime comment '出发时间' );

-- 工单请求表新增重提单工单号
ALTER TABLE usp_anyfix.work_request ADD resubmit_work_id BIGINT DEFAULT 0 NOT NULL COMMENT '重新提单关联号';

-- 文件表，部分字段增加默认值
ALTER TABLE usp_file.file_info MODIFY COLUMN adduserid bigint(20) DEFAULT 0 NOT NULL COMMENT '上传用户ID';
ALTER TABLE usp_file.file_info MODIFY COLUMN `size` bigint(20) DEFAULT 0 NOT NULL COMMENT '文件大小';
ALTER TABLE usp_file.file_info MODIFY COLUMN appid int(11) DEFAULT 0 NOT NULL COMMENT '应用ID';


-- 设备档案服务表新增工单号字段
ALTER TABLE usp_device.device_service ADD work_id BIGINT DEFAULT 0 NOT NULL COMMENT '工单ID，通过工单自动生成的档案';
-- 工程师签到表增加是否协同字段
alter table usp_anyfix.work_sign add COLUMN together varchar(1) NOT NULL DEFAULT 'N' comment '是否为协同工程师，Y=是，N=否' AFTER deviation;
-- 设备档案表增加维保合同号字段
alter table usp_device.device_info add COLUMN cont_no varchar(30) NOT NULL DEFAULT '' comment '维保合同号' AFTER warranty_status;

-- 新增权限
INSERT INTO usp_uas.sys_right (right_id,right_name,parent_id,app_id,sys_type,service_demander,service_provider,device_user,cloud_manager) VALUES
(313,'编辑工单',182,10003,0,'Y','Y','N','N')
;
-- 新增金融设备工单下拉框数据源权限
INSERT INTO usp_uas.sys_right (right_id,right_name,parent_id,app_id,sys_type,service_demander,service_provider,device_user,cloud_manager) VALUES
(315,'金融设备工单下拉框数据源',310,10003,0,'N','Y','N','N');

INSERT INTO usp_uas.sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES
(1233701204813381633,1,'/api/anyfix/work-request/findAtmCaseOption','POST',315,'');
INSERT INTO usp_uas.sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES
(1235043108067106817,0,'/api/anyfix/work-request/update','POST',313,'')
;
INSERT INTO usp_uas.sys_right (right_id, right_name, parent_id, app_id, sys_type, service_demander, service_provider, device_user, cloud_manager)
VALUES(314, '修改协同工程师', 182, 10003, 0, 'N', 'Y', 'N', 'N');
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description)
VALUES(1235089137063198722, 0, '/api/anyfix/work-deal/update/togetherEngineers', 'POST', 314, '');
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description)
VALUES(1235200084582912002, 0, '/api/device/device-excel/template', 'GET', 271, '下载导入模板');
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description)
VALUES(1235200229445783553, 0, '/api/device/device-excel/import', 'POST', 271, '导入设备档案');

-- 新增zjft-oa访问新一代云服务平台认证，插入需要的oauth信息
INSERT INTO usp_auth.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, corp_id, create_time, update_time)
VALUES('zjft-oa', NULL, '$2a$10$w/SGT1z6fSnonWsClbsj0u4Ytlz/QhblyUomhbURmP5bYH3jvBMGu', 'all', 'authorization_code,password,refresh_token,implicit,client_credentials', '', NULL, 18000, 28800, '{}', 'true', 1229327791726825475, NULL, NULL);


ALTER TABLE usp_anyfix.work_deal ADD recall_time DATETIME NULL COMMENT '撤单时间';
ALTER TABLE usp_anyfix.work_deal ADD recall_staff BIGINT DEFAULT 0 NULL COMMENT '撤单人';
ALTER TABLE usp_anyfix.work_deal ADD return_time DATETIME NULL COMMENT '退单时间';
ALTER TABLE usp_anyfix.work_deal ADD return_staff BIGINT DEFAULT 0 NULL COMMENT '退单人';



-- usp_uas;
CREATE TABLE `uas_corp_user_trace` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `corp_id` bigint(20) NOT NULL COMMENT '企业ID',
  `operate` smallint(6) NOT NULL COMMENT '操作:1-加入,2-离开',
  `operator` bigint(20) COMMENT '操作人',
  `client_id` varchar(32) NOT NULL COMMENT '操作客户端',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业员工跟踪表';

-- usp-anyfix
CREATE TABLE `service_branch_user_trace` (
  `branch_id` bigint(20) NOT NULL COMMENT '网点编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `operate` smallint(6) NOT NULL COMMENT '操作:1-加入,2-离开',
  `operator` bigint(20) COMMENT '操作人',
  `client_id` varchar(32) NOT NULL COMMENT '操作客户端',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务网点人员跟踪表';

CREATE TABLE `device_branch_user_trace` (
  `branch_id` bigint(20) NOT NULL COMMENT '网点编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `operate` smallint(6) NOT NULL COMMENT '操作:1-加入,2-离开',
  `operator` bigint(20) COMMENT '操作人',
  `client_id` varchar(32) NOT NULL COMMENT '操作客户端',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备网点人员跟踪表';