DROP TABLE IF EXISTS usp_uas.uas_protocol_def;
CREATE TABLE usp_uas.uas_protocol_def(
    id INT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,
    name VARCHAR(128) NOT NULL   COMMENT '协议名称' ,
    url VARCHAR(128) NOT NULL   COMMENT '协议地址' ,
    module VARCHAR(20) NOT NULL   COMMENT '所属模块' ,
    update_date DATETIME NOT NULL   COMMENT '更新日期' ,
    PRIMARY KEY (id)
) COMMENT = '协议定义';

DROP TABLE IF EXISTS usp_uas.uas_protocol_sign;
CREATE TABLE usp_uas.uas_protocol_sign(
    id INT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,
    protocol_id INT NOT NULL   COMMENT '协议id' ,
    user_id BIGINT  COMMENT '用户id' ,
    corp_id BIGINT   COMMENT '企业id' ,
    operator BIGINT   COMMENT '操作员ID，企业签订时不为空' ,
    sign_date DATETIME NOT NULL   COMMENT '签订日期' ,
    PRIMARY KEY (id)
) COMMENT = '签订协议情况，包括企业和个人';


INSERT INTO usp_uas.uas_protocol_def(id, name, url, `module`, update_date) VALUES
(1, '用户协议','/uas/protocol/user-protocol.html', '', '2020-03-10'),
(2, '隐私协议','/uas/protocol/privacy.html', '', '2020-03-10'),
(3, '软件许可协议', '/uas/protocol/software-agreement.html', '', '2020-03-10'),
(4, '账户与交易安全协议', '/uas/protocol/payment.html', 'payment', '2020-03-10');
