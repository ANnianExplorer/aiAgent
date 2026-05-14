package com.yzh.aiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.aiagent.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yzh
* @description 针对表【chat_message(AI Agent 会话记忆表)】的数据库操作Mapper
* @createDate 2026-05-14 17:35:54
* @Entity com.yzh.aiagent.domain.ChatMessage
*/
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

}




