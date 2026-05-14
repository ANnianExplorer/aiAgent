package com.yzh.aiagent.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会话token使用情况
 * @TableName chat_usage
 */
@TableName(value ="chat_usage")
@Data
public class ChatUsage implements Serializable {
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
     * 响应ID
     */
    private String response_id;

    /**
     * 输入Token
     */
    private Integer input_tokens;

    /**
     * 输出Token
     */
    private Integer output_tokens;

    /**
     * 总Token
     */
    private Integer total_tokens;

    /**
     * 结束原因
     */
    private String finish_reason;

    /**
     * 创建时间
     */
    private Date create_time;

    private static final long serialVersionUID = 1L;

}