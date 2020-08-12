create schema usp-pay default character set utf8mb4 collate utf8mb4_general_ci;

DROP TABLE IF EXISTS `account_info`;
CREATE TABLE account_info(
    id BIGINT NOT NULL   COMMENT '账户id 开通钱包，用户表和企业表增加关联(pay_account_id)' ,
    account_type varchar(10) NOT NULL   COMMENT '账户类型 c:个人 e:企业 p:平台' ,
    user_id BIGINT    COMMENT '用户ID 用户ID，个人时非空' ,
    corp_id BIGINT    COMMENT '企业ID 企业ID，企业时非空' ,
    status smallint NOT NULL   COMMENT '账户状态 账户状态 10:激活 20:冻结' ,
    create_time DATETIME NOT NULL   COMMENT '账户开通时间' ,
    balance DECIMAL(14,2) NOT NULL  DEFAULT 0.00 COMMENT '余额 余额 = 可用余额+冻结金额' ,
    available_amount DECIMAL(14,2) NOT NULL  DEFAULT 0.00 COMMENT '可用余额' ,
    frozen_amount DECIMAL(14,2) NOT NULL  DEFAULT 0.00 COMMENT '冻结金额 使用场景有： 1.提现与资金到账期间冻结金额' ,
    total_income DECIMAL(14,2) NOT NULL  DEFAULT 0.00 COMMENT '总收入' ,
    total_expend DECIMAL(14,2) NOT NULL  DEFAULT 0.00 COMMENT '总支出' ,
    PRIMARY KEY (id)
) COMMENT = '账户表';

DROP TABLE IF EXISTS `account_trace`;
CREATE TABLE account_trace(
    id BIGINT NOT NULL   COMMENT '流水ID' ,
    account_id BIGINT NOT NULL   COMMENT '账户ID' ,
    amount DECIMAL(14,2) NOT NULL   COMMENT '余额' ,
    balance DECIMAL(14,2) NOT NULL   COMMENT '账户余额' ,
    direction smallint NOT NULL   COMMENT '资金方向 10:收入  20:支出' ,
    time DATETIME NOT NULL   COMMENT '时间' ,
    trace_type INT NOT NULL   COMMENT '流水类型 100000:支付(平台)消费;100001:余额消费;200000:提现;300000:退款;900000:退支付手续费(平台收入);900001:支付手续费(平台支出);900010:平台费用(平台收入);900011:退平台费用(平台支出);900020:入驻费用(平台收入);900021:退入驻费用(平台支出);900030:营销活动(平台支出)' ,
    apply_id BIGINT NOT NULL   COMMENT '申请ID 支付申请ID/退款申请ID/提现申请ID' ,
    apply_source smallint NOT NULL   COMMENT '申请来源 100：支付；200：提现 ；300：退款' ,
    apply_name varchar(200) NOT NULL   COMMENT '申请名称 支付类申请描述如：委托商结算单- SD20200516000005；充值-100；提现-100；退款-10' ,
    PRIMARY KEY (id)
) COMMENT = '账户流水表 ';

DROP TABLE IF EXISTS `payment_apply`;
CREATE TABLE payment_apply(
    id bigint(8) NOT NULL   COMMENT '支付申请ID' ,
    payee_account_id bigint(8) NOT NULL   COMMENT '入账账户ID' ,
    payer_account_id bigint(8) NOT NULL   COMMENT '出账账户ID 使用账户余额交易，则非空' ,
    order_id bigint(8) NOT NULL   COMMENT '商品订单ID' ,
    order_type INT NOT NULL   COMMENT '订单类型 100000:支付(平台)消费;100001:余额消费' ,
    order_source smallint NOT NULL   COMMENT '支付订单来源 100: 委托商结算单;101: 客户结算单;102: 工程师结算单;200: 充值订单' ,
    order_name varchar(200) NOT NULL   COMMENT '商品名称' ,
    order_detail varchar(5000)    COMMENT '商品明细 用于保存支付订单商品信息快照' ,
    order_amount DECIMAL(14,2) NOT NULL   COMMENT '商品订单金额' ,
    apply_user bigint(8) NOT NULL   COMMENT '申请人' ,
    apply_time DATETIME NOT NULL   COMMENT '申请时间' ,
    order_period smallint    COMMENT '支付订单有效期 单位：分钟。平台默认为5分钟' ,
    expire_time DATETIME    COMMENT '支付订单过期时间' ,
    finish_time DATETIME    COMMENT '支付订单完成时间' ,
    end_time DATETIME    COMMENT '支付订单结束时间 支付完成后更新，到期后不得发起退款申请。结束时间 = 支付完成时间+平台退款期限' ,
    status smallint NOT NULL   COMMENT '订单状态 100:订单创建 101:订单取消 200:支付中(一般对应支付平台订单已创建，未支付) 300:支付成功 301:支付失败' ,
    cancel_reason varchar(500)    COMMENT '取消原因 订单取消原因 主动取消：用户输入；超时取消：订单超时取消' ,
    pay_way smallint NOT NULL   COMMENT '支付方式 10:支付宝 20:微信 30:余额' ,
    plat_fee_rate float   DEFAULT 0 COMMENT '平台服务费率' ,
    plat_fee_amount DECIMAL(14,2)   DEFAULT 0 COMMENT '平台服务费用' ,
    pay_fee_rate float   DEFAULT 0.006 COMMENT '支付平台费率 支付平台的费率，一般，支付宝：0.6% 微信：0.6%' ,
    pay_fee_amount DECIMAL(14,2)    COMMENT '支付平台手续费 支付成功，通过支付平台获取数据' ,
    channel_type smallint    COMMENT '支付渠道 暂只支持：10:支付宝网页扫码支付11:支付宝 App支付20:微信网页扫码支付21:微信App支付30:钱包余额支付' ,
    trade_no varchar(64)    COMMENT '交易号 支付平台返回的交易号，用于通知，对账等' ,
    is_refund smallint   DEFAULT 0 COMMENT '是否退款 1: 是 0: 否' ,
    refund_id BIGINT    COMMENT '退款申请号' ,
    refund_amount DECIMAL(14,2)   DEFAULT 0 COMMENT '退款金额' ,
    check_status smallint   DEFAULT 10 COMMENT '对账状态 第三方平台资金类交易，需对账。10: 未对账20: 对账成功21: 对账失败' ,
    PRIMARY KEY (id)
) COMMENT = '支付申请表 ';

DROP TABLE IF EXISTS `refund_apply`;
CREATE TABLE refund_apply(
    id BIGINT NOT NULL   COMMENT '退款申请ID' ,
    pay_id BIGINT NOT NULL   COMMENT '原支付订单ID' ,
    refund_amount DECIMAL(14,2) NOT NULL   COMMENT '退款金额' ,
    apply_user BIGINT NOT NULL   COMMENT '申请人' ,
    apply_time DATETIME NOT NULL   COMMENT '申请时间' ,
    apply_reason varchar(500) NOT NULL   COMMENT '退款原因' ,
    approve_user BIGINT    COMMENT '审核人' ,
    approve_time DATETIME    COMMENT '审核时间' ,
    approve_result smallint    COMMENT '审核结果 1:通过；0:不通过' ,
    approve_note varchar(500)    COMMENT '审核意见' ,
    request_period smallint   DEFAULT 3 COMMENT '有效期' ,
    expire_time DATETIME    COMMENT '过期时间' ,
    status smallint NOT NULL   COMMENT '状态 100:申请创建101:申请取消；200:审核通过；201:审核不通过；300:退款中；400:退款成功；401:退款失败' ,
    refund_way smallint NOT NULL   COMMENT '原交易支付方式 10:支付宝 20:微信 30:余额' ,
    refund_trade_no varchar(64)    COMMENT '退款交易号 第三方支付：支付平台返回；钱包余额为空' ,
    refund_time DATETIME    COMMENT '完成时间' ,
    check_status smallint    COMMENT '对账状态 第三方平台资金类交易，需对账；10: 未对账；20: 对账成功；21: 对账失败' ,
    PRIMARY KEY (id)
) COMMENT = '退款申请表 ';

DROP TABLE IF EXISTS `trade_confirm`;
CREATE TABLE trade_confirm(
    id BIGINT NOT NULL   COMMENT 'id' ,
    trade_type smallint NOT NULL   COMMENT '交易类型 10: 支付 20：退款 30：提现(转账)' ,
    trade_apply_id bigint(8) NOT NULL   COMMENT '申请ID 支付申请，退款申请，提现申请 ID' ,
    trade_status smallint NOT NULL   COMMENT '交易状态 支付交易：200-支付中(一般对应支付平台订单已创建，未支付)  300-支付成功 301-支付失败  退款交易：300-退款中 400-退款成功 401-退款失败  提现交易： 200-提现处理中 300-提现成功 301-提现失败' ,
    freq varchar(10) NOT NULL   COMMENT '频率 m:分钟，比如每分钟确认支付状态 h:小时，每小时确认退款信息 d:天，每天转账状态' ,
    PRIMARY KEY (id)
) COMMENT = '交易状态确认表 ';

DROP TABLE IF EXISTS `payment_notify`;
CREATE TABLE payment_notify(
    id BIGINT    COMMENT '通知ID' ,
    pay_id BIGINT    COMMENT '支付申请ID' ,
    app_id BIGINT    COMMENT '应用ID 业务/应用ID，用于查找应用公钥信息，对通知信息进行加密' ,
    notify_type smallint    COMMENT '通知类型 100:订单创建；101:订单取消；200:支付中；300:支付成功；301:支付失败' ,
    notify_url varchar(500)    COMMENT '通知URL 对应状态下的通知URL，用于通知应用/业务方进行后续处理' ,
    last_notify_time DATETIME    COMMENT '最后通知时间 最后一次通知时间：1.成功通知时间2.达上限次数的时间' ,
    try_times smallint   DEFAULT 0 COMMENT '第几次尝试' ,
    limit_times smallint   DEFAULT 5 COMMENT '最多尝试次数' ,
    order_name varchar(200)    COMMENT '商品名称' ,
    notified_time DATETIME    COMMENT '通知送达时间' ,
    status VARCHAR(32)    COMMENT '通知状态 100: 成功；200: 失败'
) COMMENT = '支付通知表 ';

DROP TABLE IF EXISTS `account_withdraw_info`;
CREATE TABLE account_withdraw_info(
    account_id BIGINT NOT NULL   COMMENT '提现账户' ,
    withdraw_type smallint NOT NULL   COMMENT '提现账号类型 1:支付宝2: 微信3: 银行卡' ,
    alipay_account_id varchar(20)    COMMENT '收款方支付宝账号ID' ,
    alipay_account_name varchar(200)    COMMENT '收款方支付宝账号姓名' ,
    weixin_account_id varchar(20)    COMMENT '收款方微信账号ID' ,
    weixin_account_name varchar(200)    COMMENT '收款方微信账号姓名' ,
    bank_account_card varchar(20)    COMMENT '银行账户卡号' ,
    bank_account_name varchar(200)    COMMENT '银行账户名称' ,
    bank_account_type varchar(10)    COMMENT '银行账户类型 c:个人; e:企业' ,
    bank_name varchar(100)    COMMENT '银行名称 如果为企业，必填' ,
    bank_province varchar(20)    COMMENT '开户行省份 如果为企业，必填' ,
    bank_city varchar(20)    COMMENT '开户行城市 如果为企业，必填' ,
    bank_branch varchar(50)    COMMENT '开户行支行 如果为企业，必填' ,
    is_default smallint    COMMENT '是否默认提现账户 1:是 0:否'
) COMMENT = '账号提现信息表 ';

DROP TABLE IF EXISTS `withdraw_apply`;
CREATE TABLE withdraw_apply(
    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '提现申请ID' ,
    amount DECIMAL(14,2) NOT NULL   COMMENT '提现金额' ,
    account_id BIGINT NOT NULL   COMMENT '提现账户' ,
    account_type varchar(10) NOT NULL   COMMENT '账户类型 c:个人；e:企业；不同类型，限额不同' ,
    apply_user BIGINT NOT NULL   COMMENT '申请人' ,
    apply_time DATETIME NOT NULL   COMMENT '申请时间' ,
    approve_user BIGINT    COMMENT '审核人 平台原因或者是账户原因，需要审核，则不为空' ,
    approve_time DATETIME    COMMENT '审核时间 平台原因或者是账户原因，需要审核，则不为空' ,
    approve_result smallint    COMMENT '审核结果 1:通过；0:不通过' ,
    approve_note varchar(500)    COMMENT '审核意见' ,
    status smallint NOT NULL   COMMENT '状态 100:创建申请；101:取消申请；200:提现处理中；300:提现成功；301:提现失败' ,
    withdraw_type smallint    COMMENT '收款方账号类型 1:支付宝2: 微信3: 银行卡' ,
    alipay_account_id varchar(50)    COMMENT '收款方支付宝账号ID' ,
    alipay_account_name varchar(200)    COMMENT '收款方支付宝账号姓名' ,
    weixin_account_id varchar(20)    COMMENT '收款方微信账号ID' ,
    weixin_account_name varchar(200)    COMMENT '收款方微信账号姓名' ,
    bank_account_card varchar(20)    COMMENT '银行账户卡号' ,
    bank_account_name varchar(200)    COMMENT '银行账户名称' ,
    bank_account_type varchar(10)    COMMENT '银行账户类型 c:个人; e:企业' ,
    bank_name varchar(100)    COMMENT '银行名称 如果为企业，必填' ,
    bank_province varchar(20)    COMMENT '开户行省份 如果为企业，必填' ,
    bank_city varchar(20)    COMMENT '开户行城市 如果为企业，必填' ,
    bank_branch varchar(50)    COMMENT '开户行支行 如果为企业，必填' ,
    transfer_trade_no varchar(64)    COMMENT '转账交易号 第三方支付：支付平台返回' ,
    check_status smallint    COMMENT '对账状态 第三方平台资金类交易，需对账；10: 未对账；20: 对账成功；21: 对账失败' ,
    PRIMARY KEY (id)
) COMMENT = '提现申请表 ';

DROP TABLE IF EXISTS `recharge_order`;
CREATE TABLE recharge_order(
    id BIGINT NOT NULL   COMMENT '充值订单ID' ,
    amount DECIMAL(14,2) NOT NULL   COMMENT '充值金额' ,
    account_id BIGINT NOT NULL   COMMENT '充值账户' ,
    account_type varchar(10) NOT NULL   COMMENT '账户类型 c:个人; e:企业' ,
    status smallint NOT NULL   COMMENT '订单状态 100:订单创建; 101:订单取消; 200:支付中; 300:支付成功; 301:支付失败' ,
    pay_id BIGINT NOT NULL   COMMENT '支付订单号' ,
    PRIMARY KEY (id)
) COMMENT = '充值订单表 ';

-- 初始化平台钱包
INSERT INTO usp_pay.account_info
(id,account_type,user_id,corp_id,status,create_time,balance,available_amount,frozen_amount,total_income,total_expend) VALUES
(1,'p',NULL,1229327791726825476,10,'2020-06-03 18:00:00.0',0.00,0.00,0.00,0.00,0.00);

-- 修改权限码长度
ALTER TABLE usp_uas.sys_right MODIFY COLUMN right_code varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL NULL COMMENT '权限码';

-- 权限数据
INSERT INTO usp_uas.sys_right (right_id,right_name,right_code,parent_id,app_id,sys_type,has_scope,service_demander,service_demander_common,service_provider,service_provider_common,device_user,device_user_common,cloud_manager,cloud_manager_common,has_extra_corp) VALUES
(70000,'支付系统','PAY',0,10007,0,'N','Y','N','Y','N','Y','N','Y','N','N')
,(70001,'在线支付','PAY_ONLINE',70000,10007,0,'N','Y','N','N','N','N','N','N','N','N')
,(70002,'企业账户查看','PAY_CORP_ACCOUNT_VIEW',70000,10007,0,'N','Y','N','Y','N','Y','N','Y','N','N')
,(70003,'企业账单查看','PAY_CORP_APPLY_VIEW',70000,10007,0,'N','Y','N','Y','N','Y','N','Y','N','N')
;

-- gateway yml security.ignore.httpUrls 增加 /api/pay/alipay/notify
INSERT INTO usp_uas.sys_right_url (id, right_type, uri, path_method, right_id, description) VALUES
(1275227828534693890,0,'/api/pay/alipay/pc-pay','POST',70001,'支付宝-电脑支付')
,(1275004529959555073,0,'/api/pay/account-info/*','POST',70002,'开通企业钱包，查看企业钱包等')
,(1275227640067837953,0,'/api/pay/account-trace/query','POST',70002,'查看账户流水')
,(1275228543185375233,0,'/api/pay/payment-apply/*','POST',70003,'支付申请相关')
;

