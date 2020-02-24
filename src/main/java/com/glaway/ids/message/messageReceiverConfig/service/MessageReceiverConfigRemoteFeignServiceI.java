package com.glaway.ids.message.messageReceiverConfig.service;


import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.message.messageReceiverConfig.fallback.MessageReceiverConfigRemoteFeignServiceCallBack;
import com.glaway.ids.message.vo.MessageReceiverConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 消息配置业务接口
 * 
 * @author Administrator
 * @version 2020年1月4日 10:04:09
 * @see MessageReceiverConfigRemoteFeignServiceI
 * @since
 */

@FeignClient(value = FeignConstants.ID_COMMON_SERVICE,fallback = MessageReceiverConfigRemoteFeignServiceCallBack.class)
public interface MessageReceiverConfigRemoteFeignServiceI
{
    @RequestMapping(value = FeignConstants.IDS_COMMON_FEIGN_SERVICE+"/feign/messageReceiverConfigRestController/getMessageList.do")
    List<MessageReceiverConfigDto> getMessageList();

}
