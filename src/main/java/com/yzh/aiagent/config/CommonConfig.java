package com.yzh.aiagent.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.yzh.aiagent.advisor.MyLoggerAdvisor;
import com.yzh.aiagent.advisor.ReReadingAdvisor;
import com.yzh.aiagent.chatmemory.FileBasedChatMemoryRepository;
import com.yzh.aiagent.chatmemory.MysqlChatMemoryRepository;
import com.yzh.aiagent.constant.CommonConstant;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用配置
 *
 * @author yzh
 * @since 2026/5/14
 **/
@Configuration
public class CommonConfig {

    @Resource
    private MysqlChatMemoryRepository chatMemoryRepository;

    /**
     * 会话记忆功能，这里使用的是内存会话记忆
     *
     * @return 会话记忆
     */
    @Bean
    public ChatMemory chatMemory() {
        // 基于内存的对话记忆
        // 那个最大记忆条数现在直接是在创建ChatMemory的时候设置
        return MessageWindowChatMemory.builder()
                // 自定义持久化
                .chatMemoryRepository(new FileBasedChatMemoryRepository("chat-memory"))
                // 基于内存的持久化
                //.chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    @Bean
    public ChatMemory chatMemoryToMysql() {
        // 基于内存的对话记忆
        // 那个最大记忆条数现在直接是在创建ChatMemory的时候设置
        return MessageWindowChatMemory.builder()
                // 自定义持久化
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }
    @Bean
    public ChatClient doChatClient(DashScopeChatModel dashscopeChatModel, @Qualifier("chatMemoryToMysql") ChatMemory mysqlChatMemory) {
        return ChatClient.builder(dashscopeChatModel)
                .defaultSystem(CommonConstant.SYSTEM_PROMPT)
                .defaultAdvisors(/*new SimpleLoggerAdvisor()*/ // 添加默认的Advisor,记录日志
                        new MyLoggerAdvisor() // 添加自定义的Advisor
                        // 自定义推理增强 Advisor，可按需开启
                        //,new ReReadingAdvisor()
                        , MessageChatMemoryAdvisor.builder(mysqlChatMemory).build())
                .build();
    }
}