package com.glaway.ids.message.messageReceiverConfig.fallback;

import com.glaway.ids.message.messageReceiverConfig.service.MessageReceiverConfigRemoteFeignServiceI;
import com.glaway.ids.message.vo.MessageReceiverConfigDto;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageReceiverConfigRemoteFeignServiceCallBack implements FallbackFactory<MessageReceiverConfigRemoteFeignServiceI> {

	@Override
	public MessageReceiverConfigRemoteFeignServiceI create(Throwable throwable) {
		return new MessageReceiverConfigRemoteFeignServiceI(){

			@Override
			public List<MessageReceiverConfigDto> getMessageList() {
				return null;
			}
		};
	}
}

