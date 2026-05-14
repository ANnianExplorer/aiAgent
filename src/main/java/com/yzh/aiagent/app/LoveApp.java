package com.yzh.aiagent.app;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.yzh.aiagent.constant.CommonConstant;
import com.yzh.aiagent.domain.ChatUsage;
import com.yzh.aiagent.service.ChatUsageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 聊天应用
 *
 * @author yzh
 * @since 2026/5/14
 **/
@Component
@Slf4j
public class LoveApp {

    @Resource
    @Qualifier("doChatClient")
    private ChatClient doChatClient;

    @Resource
    private ChatUsageService chatUsageService;

    /**
     * 聊天
     *
     * @param message 消息
     * @param chatId  会话id
     * @return 响应
     */
    public String doChat(String message, String chatId) {
        // 消息响应
        ChatResponse response =
                doChatClient.prompt()
                        .user(message)
                        .advisors(
                                spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                        )
                        .call()
                        .chatResponse();
        log.info("response: {}", response);
        // 存入Token
        ChatUsage chatUsage = new ChatUsage();
        chatUsage.setConversation_id(chatId);
        chatUsage.setResponse_id(response.getMetadata().getId());
        chatUsage.setInput_tokens(response.getMetadata().getUsage().getPromptTokens());
        chatUsage.setOutput_tokens(response.getMetadata().getUsage().getCompletionTokens());
        chatUsage.setTotal_tokens(response.getMetadata().getUsage().getTotalTokens());
        chatUsageService.save(chatUsage);
        return response.getResult().getOutput().getText();
    }

    /**
     * 回忆陪伴报告
     */
    record LoveReport(String title, List<String> suggestions) {
    }

    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = doChatClient
                .prompt()
                //.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT) // 启用结构化输出，是原生结构化输出，不是JSON结构化输出
                .system(CommonConstant.SYSTEM_PROMPT + "每次对话后都要生成回忆结果，标题为{用户名}的回忆报告，内容为建议列表")
                .user(message)
                .advisors(
                        spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }
}