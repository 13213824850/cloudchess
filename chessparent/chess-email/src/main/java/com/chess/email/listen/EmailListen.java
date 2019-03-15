package com.chess.email.listen;

import java.util.Map;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chess.common.vo.ActionCode;
import com.chess.email.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailListen {
	
	@Autowired
	private EmailService emailService;
	@Value("${chess.register.url}")
	private String register_url;
	
	@RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chess.email.verify.queue"),
            exchange = @Exchange(name = "chess.email.exchange", type = ExchangeTypes.TOPIC),
            key = "email.verify.code"
    ))
	public void listenVerify(ActionCode actionCode) {
        if (actionCode == null) {
            return;
        }
        String toMail = actionCode.getUserName();
        String code = actionCode.getCode();
        String subject = "中国象棋在线对战邮箱账号激活";
        String url = register_url +  "?userName="+toMail+"&&code="+code; 
        String content = "<h3><a href=\""+ url+"\">请点击此处激活账号</a></h3>";
        log.info(content);
        try {
        	emailService.sendHtmlMail(toMail, subject, content);
		} catch (Exception e) {
			log.warn("{}发送失败",actionCode.getUserName());
		}
      
    }
}
