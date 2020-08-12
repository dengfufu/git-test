-- 系统权限表增加字段
ALTER TABLE usp_uas.sys_right ADD has_scope varchar(1) DEFAULT 'N' NOT NULL COMMENT '是范围权限 Y=是 N=否';
ALTER TABLE usp_uas.sys_right CHANGE has_scope has_scope varchar(1) DEFAULT 'N' NOT NULL COMMENT '是范围权限 Y=是 N=否' AFTER sys_type;

-- 创建范围权限类型表
CREATE TABLE usp_uas.sys_right_scope_type (
	right_id BIGINT NOT NULL COMMENT '权限ID',
	scope_type SMALLINT DEFAULT 0 NOT NULL COMMENT '范围权限类型，1=服务网点',
	PRIMARY KEY (right_id, scope_type)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_bin
COMMENT='范围权限类型表';

-- 创建角色范围权限表
CREATE TABLE usp_uas.sys_role_right_scope (
	id BIGINT NOT NULL COMMENT '主键',
	role_id BIGINT DEFAULT 0 NOT NULL COMMENT '角色编号',
	right_id BIGINT DEFAULT 0 NOT NULL COMMENT '权限编号',
	scope_type SMALLINT DEFAULT 0 NOT NULL COMMENT '范围类型，1=服务网点',
	has_all_scope varchar(1) DEFAULT 'N' NOT NULL COMMENT '是否有全部权限',
	has_own_scope varchar(1) DEFAULT 'N' NOT NULL COMMENT '是否有所在组织权限',
	has_own_lower_scope varchar(1) DEFAULT 'N' NOT NULL COMMENT '是否有所在组织的下级权限',
	CONSTRAINT PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_bin
COMMENT='角色范围权限表';


-- 服务网点表增加字段
ALTER TABLE usp_anyfix.service_branch ADD upper_branch_id BIGINT DEFAULT 0 NOT NULL COMMENT '直接上级网点编号';
ALTER TABLE usp_anyfix.service_branch CHANGE upper_branch_id upper_branch_id BIGINT DEFAULT 0 NOT NULL COMMENT '直接上级网点编号' AFTER branch_id;

-- 新建服务网点上级表
CREATE TABLE usp_anyfix.service_branch_upper (
	branch_id BIGINT NOT NULL COMMENT '网点编号',
	upper_branch_id BIGINT NOT NULL COMMENT '上级网点编号',
	PRIMARY KEY (branch_id,upper_branch_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_bin
COMMENT='服务网点上级表';


-- 新增鉴权
INSERT INTO usp_uas.sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES 
(1236928981222277121,1,'/api/anyfix/service-branch/tree','POST',153,'')
,(1236929048926732289,1,'/api/anyfix/service-branch/upper/tree','POST',153,''),
(1236949466282704898,1,'/api/anyfix/service-branch/upper/list','POST',153,''),
(1237266194560630786,1,'/api/anyfix/service-branch/lower/query','POST',153,''),
(1237305956344446978,1,'/api/anyfix/service-branch/same/query','POST',153,''),
(1237654044858904578,1,'/api/uas/sys-right-scope-type/type/list','GET',8,''),
(1238070735833022465,1,'/api/uas/sys-role-right-scope/list','POST',32,'')
;

ALTER TABLE usp_anyfix.work_request ADD is_supplement varchar(1) DEFAULT 'N' NULL COMMENT '是否补单';
-- 工单处理表工单状态增加注释
ALTER TABLE usp_anyfix.work_deal MODIFY COLUMN work_status int(11) DEFAULT 0 NOT NULL COMMENT '工单状态，10=待提单 20=待分配 30=待派单 40=待接单 50=待签到 60=待服务 70=服务中 80=待评价 90=已完成 100=已退单 110=已撤单';

-- 新增鉴权
INSERT INTO usp_uas.sys_right_url (id,right_type,uri,path_method,right_id,description) VALUES
(1236907085286391809,0,'/api/anyfix/work-finish/engineer/supplement','POST',183,'补建工单');


-- 插入atm需要的oauth信息
INSERT INTO usp_auth.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, corp_id, create_time, update_time)
VALUES('zjft-atm', NULL, '$2a$10$w/SGT1z6fSnonWsClbsj0u4Ytlz/QhblyUomhbURmP5bYH3jvBMGu', 'all', 'authorization_code,password,refresh_token,implicit,client_credentials', '', NULL, 86400, 108000, '{}', 'true', 1229327791726825475, NULL, NULL);

-- 修改权限点名称
UPDATE usp_uas.sys_right set right_name='委托商确认工单' WHERE right_id=231;
UPDATE usp_uas.sys_right set right_name='批量确认通过' WHERE right_id=232;
UPDATE usp_uas.sys_right set right_name='确认工单' WHERE right_id=233;
-- 新增权限
INSERT INTO usp_uas.sys_right (right_id,right_name,parent_id,app_id,sys_type,service_demander,service_provider,device_user,cloud_manager) VALUES
(316,'服务商审核工单',174,10003,0,'N','Y','N','N'),
(317,'批量审核通过',316,10003,0,'N','Y','N','N'),
(318,'审核工单',316,10003,0,'N','Y','N','N'),
(319, '工单导出', 182, 10003, 0, 'Y', 'Y', 'N', 'N');

-- 新增鉴权
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description) VALUES
(1236893818768957441, 0, '/api/anyfix/work-request/service/check/query', 'POST', 316, ''),
(1236893962650361857, 0, '/api/anyfix/work-deal/service/batchCheck', 'POST', 317, ''),
(1236894069366038529, 0, '/api/anyfix/work-deal/service/check', 'POST', 318, ''),
(1238016978041987074, 0, '/api/anyfix/work-finish/update/locale/finish', 'POST', 197, ''),
(1238103234434850817, 0, '/api/anyfix/work-excel/export', 'POST', 319, '');

-- 修改权限点
update usp_uas.sys_right set right_name = '查询工单', has_scope = 'Y' where right_id = 179;
update usp_uas.sys_right set has_scope = 'N', service_demander = 'N', service_provider = 'N', device_user = 'N' where right_id = 180;

-- 新增权限范围类型
INSERT INTO usp_uas.sys_right_scope_type (right_id,scope_type) VALUES
(179,1);

-- 新增角色范围
INSERT INTO usp_uas.sys_role_right_scope (id,role_id,right_id,scope_type,has_all_scope,has_own_scope,has_own_lower_scope) VALUES
(1238734645552648193,1229327791739408400,179,1,'Y','N','N')
,(1238734757272129538,1229327791739408401,179,1,'Y','N','N')
,(1238734793057931265,1229327791739408402,179,1,'Y','N','N')
,(1238734850561839105,1229327791739408403,179,1,'N','Y','N')
,(1238735336039944194,1231537686774579201,179,1,'N','Y','Y')
,(1238735382412169218,1231537750424555522,179,1,'N','Y','Y')
,(1238735494349754369,1233050995826905089,179,1,'Y','N','N')
,(1238736278785265666,1229327791739408406,179,1,'Y','N','N')
,(1238736316718551042,1229327791739408407,179,1,'Y','N','N')
;
-- 新增 派单主管 查询工单权限
INSERT INTO usp_uas.sys_role_right (role_id,right_id) VALUES
(1229327791739408403,179);