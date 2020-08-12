-- 企业注册表
ALTER TABLE usp_uas.uas_corp_reg ADD clientid varchar(32) DEFAULT '' NOT NULL COMMENT '应用标识';
-- 企业操作记录表
ALTER TABLE usp_uas.uas_corp_oper ADD clientid varchar(32) DEFAULT '' NOT NULL COMMENT '应用标识';
ALTER TABLE usp_uas.uas_corp_oper CHANGE clientid clientid varchar(32) DEFAULT '' NOT NULL COMMENT '应用标识' AFTER appid;
ALTER TABLE usp_uas.uas_corp_oper MODIFY COLUMN appid int(11) DEFAULT 0 NOT NULL COMMENT '应用ID';


-- 人员操作记录表
ALTER TABLE usp_uas.uas_user_oper ADD clientid varchar(32) DEFAULT '' NOT NULL COMMENT '应用标识';
ALTER TABLE usp_uas.uas_user_oper MODIFY COLUMN appid int(11) DEFAULT 0 NOT NULL COMMENT '应用ID';

-- 人员应用表
ALTER TABLE usp_uas.uas_user_app ADD clientid varchar(32) DEFAULT '' NOT NULL COMMENT '应用标识';
ALTER TABLE usp_uas.uas_user_app CHANGE clientid clientid varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NOT NULL COMMENT '应用标识' AFTER userid;
ALTER TABLE usp_uas.uas_user_app MODIFY COLUMN appid int(11) DEFAULT 0 NOT NULL COMMENT '应用ID';
UPDATE usp_uas.uas_user_app SET clientid='usp-mobile';
ALTER TABLE usp_uas.uas_user_app DROP PRIMARY KEY;
ALTER TABLE usp_uas.uas_user_app ADD CONSTRAINT uas_user_app_PK PRIMARY KEY (userid,clientid);
ALTER TABLE usp_uas.uas_user_app MODIFY COLUMN clientid varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用标识';


-- 新增消息模板
INSERT INTO usp_msg.push_template (tplid,appid,tplname,title,content,adduserid,addtime,enabled) VALUES 
(6,10000,'加入企业','您收到一个加入企业申请',
'<div style="line-height: 30px;margin-top: 10px;"><span style="display:inline-block;width:25%;color:#999999;font-size:14px;">姓名</span><span style="font-size:15px;color:#444444;font-weight:500;">${userName}</span></div><div style="line-height: 30px;margin-top: 10px;"><span style="display:inline-block;width: 25%;color:#999999;font-size:14px;">企业名称</span><span style="font-size:15px;color:#444444;font-weight:500;">${corpName}</span></div>',23,CURRENT_TIME,'1');
