package com.yzh.aiagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzh.aiagent.domain.ChatMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
* @author yzh
* @description 针对表【chat_message(AI Agent 会话记忆表)】的数据库操作Service
* @createDate 2026-05-14 17:35:54
*/
public interface ChatMessageService extends IService<ChatMessage> {
    /**
     * 查询所有会话ID
     */
    List<String> findConversationIds();

    /**
     * 查询会话消息
     */
    List<ChatMessage> findByConversationId(@Param("conversationId") String conversationId);

    /**
     * 删除会话
     */
    void deleteByConversationId(@Param("conversationId") String conversationId);
    /**
     * 消息格式转换
     */
    Message convertToMessage(ChatMessage chatMessage);
    ChatMessage convertToChatMessage(String conversationId, int msgIndex, Message message);
}
