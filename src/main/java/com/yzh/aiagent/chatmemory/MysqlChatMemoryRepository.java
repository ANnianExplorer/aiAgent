package com.yzh.aiagent.chatmemory;

import com.yzh.aiagent.domain.ChatMessage;
import com.yzh.aiagent.service.ChatMessageService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MySQL持久化ChatMemoryRepository
 *
 * @author yzh
 * @since 2026/5/14
 */
@Component
public class MysqlChatMemoryRepository implements ChatMemoryRepository {

    @Resource
    private ChatMessageService chatMessageService;

    @Override
    public List<String> findConversationIds() {
        return chatMessageService.findConversationIds();
    }

    /**
     * 查询会话消息
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        return chatMessageService.findByConversationId(conversationId)
                .stream()
                .map(chatMessageService::convertToMessage)
                .toList();
    }

    /**
     * 保存会话消息
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        // 删除旧快照
        chatMessageService.deleteByConversationId(
                conversationId
        );
        int msgIndex = 0;
        for (Message message : messages) {
            ChatMessage chatMessage = chatMessageService.convertToChatMessage(conversationId, msgIndex++, message);
            chatMessageService.save(chatMessage);
        }
    }
    /**
     * 删除会话
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        chatMessageService.deleteByConversationId(conversationId);
    }

}