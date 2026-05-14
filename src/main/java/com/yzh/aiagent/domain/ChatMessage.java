package com.yzh.aiagent.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * AI Agent 会话记忆表
 * @TableName chat_message
 */
@TableName(value ="chat_message")
@Data
public class ChatMessage implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    private String conversation_id;

    /**
     * 消息顺序，从0开始
     */
    private Integer msg_index;

    /**
     * 消息角色：system/user/assistant/tool/function
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 扩展元数据(JSON)
     */
    private Object metadata;

    /**
     * 重要性评分
     */
    private Double importance_score;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    private static final long serialVersionUID = 1L;

}