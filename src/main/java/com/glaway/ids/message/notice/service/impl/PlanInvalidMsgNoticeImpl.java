package com.glaway.ids.message.notice.service.impl;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.message.notice.service.PlanInvalidMsgNoticeI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;




/**
 * 发送完工消息中间过度类
 * 
 * @author wqb
 * @version 2016年6月15日
 * @see PlanInvalidMsgNoticeImpl
 * @since
 */
@Service("planInvalidMsgNotice")
@Transactional(propagation = Propagation.REQUIRED) 
public class PlanInvalidMsgNoticeImpl extends CommonServiceImpl implements PlanInvalidMsgNoticeI {

    @Override
    public String getAllMessage(String msg, TSUserDto user) {
        // TODO Auto-generated method stub
        String allMsg = msg;
        
        return allMsg;
    }

}
