package com.yzh.aiagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzh.aiagent.domain.ChatMessage;
import com.yzh.aiagent.service.ChatMessageService;
import com.yzh.aiagent.mapper.ChatMessageMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author yzh
* @description 针对表【chat_message(AI Agent 会话记忆表)】的数据库操作Service实现
* @createDate 2026-05-14 17:35:54
*/
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * 查询会话ID
     *
     * @return 会话ID列表
     */
    @Override
    public List<String> findConversationIds() {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ChatMessage::getConversation_id).groupBy(ChatMessage::getConversation_id);
        return this.baseMapper
                .selectList(wrapper)
                .stream()
                .map(ChatMessage::getConversation_id)
                .distinct()
                .toList();
    }

    /**
     * 查询会话消息
     *
     * @param conversationId 会话ID
     * @return 会话消息列表
     */
    @Override
    public List<ChatMessage> findByConversationId(String conversationId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getConversation_id, conversationId)
                .orderByAsc(ChatMessage::getMsg_index);
        return this.getBaseMapper()
                .selectList(wrapper);
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getConversation_id, conversationId);
        this.getBaseMapper().delete(wrapper);
    }
    /**
     * 将会话消息列表转换为Message列表
     *
     * @param chatMessages 会话消息列表
     * @return Message列表
     */
    @Override
    public Message convertToMessage(ChatMessage chatMessage) {
        String role = chatMessage.getRole();
        Map<String, Object> metadata = parseMetadata(chatMessage.getMetadata());
        return switch (role) {
            case "user" ->
                    UserMessage.builder()
                            .text(chatMessage.getContent())
                            .metadata(metadata)
                            .build();

            case "assistant" ->
                    AssistantMessage.builder()
                            .content(chatMessage.getContent())
                            .properties(metadata)
                            .build();

            case "system" ->
                    SystemMessage.builder()
                            .text(chatMessage.getContent())
                            .metadata(metadata)
                            .build();

            default ->
                    null;
        };
    }

    /**
     * 将 Message 列表转换为会话消息列表
     *
     * @param messages Message列表
     * @return 会话消息列表
     */
    @Override
    public ChatMessage convertToChatMessage(String conversationId, int msgIndex, Message message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversation_id(conversationId);
        chatMessage.setMsg_index(msgIndex);
        chatMessage.setRole(
                message.getMessageType()
                        .name()
                        .toLowerCase()
        );
        chatMessage.setContent(message.getText());
        Map<String, Object> metadata = message.getMetadata();
        // metadata序列化
        try {
            chatMessage.setMetadata(OBJECT_MAPPER.writeValueAsString(message.getMetadata()));
        } catch (JsonProcessingException e) {
            chatMessage.setMetadata("{}");
            throw new RuntimeException(e);
        }
        return chatMessage;
    }

    /**
     * 解析metadata
     */
    private static Map<String, Object> parseMetadata(Object metadataJson) {
        if (metadataJson == null) {
            return new HashMap<>();
        }
        try {
            return OBJECT_MAPPER.readValue(metadataJson.toString(), Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}




