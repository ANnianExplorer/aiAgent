package com.yzh.aiagent.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.yzh.aiagent.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ReactAgent配置
 *
 * @author yzh
 * @since 2026/5/14
 **/

@Configuration
public class AgentConfig {

    /**
     * ReactAgent
     */
    @Bean
    public ReactAgent loveAgent(DashScopeChatModel chatModel) {

        return ReactAgent.builder()
                // Agent名称
                .name("love_agent")
                // 大模型
                .model(chatModel)
                // 系统提示词
                .systemPrompt(CommonConstant.SYSTEM_PROMPT)
                /**
                 * 核心：
                 * 开启多轮会话记忆
                 */
                .saver(new MemorySaver())
                .build();
    }
}