package com.yzh.aiagent.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 自定义重新阅读的Advisor
 *
 * @author yzh
 * @since 2026/5/14
 **/
public class ReReadingAdvisor implements BaseAdvisor {

    /**
	 * 默认的模板
	 */
	private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
			{re2_input_query}
			Read the question again: {re2_input_query}
			""";

    /**
     * 重新阅读的模板
     */
	private final String re2AdviseTemplate;

    /**
     * 顺序
     */
	private int order = 0;

    /**
     * 默认构造函数
     */
	public ReReadingAdvisor() {
		this(DEFAULT_RE2_ADVISE_TEMPLATE);
	}

    /**
     * 构造函数
     *
     * @param re2AdviseTemplate 模板
     */
	public ReReadingAdvisor(String re2AdviseTemplate) {
		this.re2AdviseTemplate = re2AdviseTemplate;
	}

    /**
     * 拦截器
     * 该 before方法通过应用重读技术来增强用户的输入查询
     *
     * @param chatClientRequest 请求
     * @param advisorChain 链
     * @return 请求
     */
	@Override
	public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
		String augmentedUserText = PromptTemplate.builder()
			.template(this.re2AdviseTemplate)
			.variables(Map.of("re2_input_query", chatClientRequest.prompt().getUserMessage().getText()))
			.build()
			.render();

		return chatClientRequest.mutate()
			.prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
			.build();
	}

    /**
     * 拦截器
     *
     * @param chatClientResponse 响应
     * @param advisorChain 链
     * @return 响应
     */
	@Override
	public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
		return chatClientResponse;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

    /**
     * 设置顺序
     *
     * @param order 顺序
     * @return this
     */
	public ReReadingAdvisor withOrder(int order) {
		this.order = order;
		return this;
	}

}