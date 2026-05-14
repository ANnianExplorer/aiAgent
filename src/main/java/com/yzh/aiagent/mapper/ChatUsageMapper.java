package com.yzh.aiagent.mapper;

import com.yzh.aiagent.domain.ChatUsage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yzh
* @description 针对表【chat_usage】的数据库操作Mapper
* @createDate 2026-05-14 19:16:31
* @Entity com.yzh.aiagent.domain.ChatUsage
*/
@Mapper
public interface ChatUsageMapper extends BaseMapper<ChatUsage> {

}




