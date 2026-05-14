package com.yzh.aiagent.app;

import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAgentTest {

    @Resource
    private LoveAgentApp loveAgentApp;

    @Test
    void testMemory() throws GraphRunnerException {

        /**
         * 同一个threadId
         * 才属于同一个会话
         */
        String threadId = UUID.randomUUID().toString();

        // 第一轮
        String answer1 = loveAgentApp.doChat(
                        "你好，我叫小杨",
                        threadId
                );

        System.out.println(answer1);

        // 第二轮
        String answer2 = loveAgentApp.doChat(
                        "我喜欢的人叫小码",
                        threadId
                );

        System.out.println(answer2);

        // 第三轮
        String answer3 = loveAgentApp.doChat(
                        "我喜欢的人叫什么？",
                        threadId
                );

        System.out.println(answer3);

        Assertions.assertNotNull(answer3);
    }
}