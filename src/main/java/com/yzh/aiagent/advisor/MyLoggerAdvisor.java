package com.yzh.aiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 自定义日志 Advisor
 * 打印 info 级别日志、只输出单次用户提示词和 AI 回复的文本
 * @author yzh
 * @since 2026/5/14
 */

/**
 * 在 1.0.0 版本中，这些接口已被替换：
 * CallAroundAdvisor → CallAdvisor，
 * StreamAroundAdvisor → StreamAdvisor，
 * CallAroundAdvisorChain → CallAdvisorChain 和 StreamAroundAdvisorChain → StreamAdvisorChain。
 * AdvisedRequest → ChatClientRequest 和 AdivsedResponse → ChatClientResponse。
 */
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logRequest(chatClientRequest);

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        logResponse(chatClientResponse);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
                                                 StreamAdvisorChain streamAdvisorChain) {
        logRequest(chatClientRequest);

        Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);

        return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponses, this::logResponse);
    }

    private void logRequest(ChatClientRequest request) {
        List<Message> messages = request.prompt().getInstructions();

        for (Message message : messages) {

            log.debug(
                    "[{}]: {}",
                    message.getMessageType(),
                    message.getText()
            );
        }
    }

    private void logResponse(ChatClientResponse chatClientResponse) {
        log.debug("response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
    }

}