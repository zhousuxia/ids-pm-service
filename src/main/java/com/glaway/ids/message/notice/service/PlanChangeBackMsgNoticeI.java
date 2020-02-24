package com.glaway.ids.message.notice.service;




import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;


/**
 * 发送完工消息中间过度类
 * 
 * @author wqb
 * @version 2016年6月15日
 * @see PlanChangeBackMsgNoticeI
 * @since
 */
public interface PlanChangeBackMsgNoticeI extends CommonService {

    /**
     * 获取所有的，变更的，发送消息的信息
     *
     * @param msg
     * @param user
     * @return
     */
    String getAllMessage(String msg, TSUserDto user);

}
