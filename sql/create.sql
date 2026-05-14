-- 创建数据库
CREATE DATABASE IF NOT EXISTS aiagent_message
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 切换数据库
USE aiagent_message;

-- AI会话消息表
CREATE TABLE IF NOT EXISTS chat_message
(
    -- 主键ID
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',

    -- 会话ID
    conversation_id VARCHAR(64) NOT NULL COMMENT '会话ID',

    -- 消息顺序
    msg_index INT NOT NULL COMMENT '消息顺序，从0开始',

    -- 消息角色
    role VARCHAR(32) NOT NULL COMMENT '消息角色：system/user/assistant/tool/function',

    -- 消息内容
    content LONGTEXT NOT NULL COMMENT '消息内容',

    -- 扩展元数据
    metadata JSON DEFAULT NULL COMMENT '扩展元数据(JSON)',

    -- 重要性评分（长期记忆）
    importance_score FLOAT DEFAULT 0 COMMENT '重要性评分',

    -- 创建时间
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 会话索引
    KEY idx_conv_id (conversation_id),

    -- 会话顺序索引
    KEY idx_conv_order (conversation_id, msg_index),

    -- 重要性索引
    KEY idx_importance (importance_score)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='AI Agent 会话记忆表';

CREATE TABLE chat_usage
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    conversation_id VARCHAR(64),

    response_id VARCHAR(128),

    input_tokens INT,
    output_tokens INT,
    total_tokens INT,

    finish_reason VARCHAR(32),

    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);