package com.yzh.aiagent.app;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoveAgentApp {

    @Resource
    @Qualifier("loveAgent")
    private ReactAgent loveAgent;

    /**
     * 聊天
     *
     * @param message  用户消息
     * @param threadId 会话ID
     * @return AI响应
     */
    public String doChat(String message, String threadId) throws GraphRunnerException {

        /**
         * 核心：
         * threadId = 会话记忆钥匙
         */
        RunnableConfig config = RunnableConfig.builder()
                .threadId(threadId)
                .build();

        // Agent调用
        AssistantMessage response = loveAgent.call(message, config);

        String result = response.getText();

        log.info("AI响应: {}", result);

        return result;
    }
}