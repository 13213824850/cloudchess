package com.chess.email.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	@Autowired
	private JavaMailSender sender;
	@Value("${spring.mail.username}")
	private String fromMail;
	public void sendHtmlMail(String toMail, String subject, String content) {
		MimeMessage mimeMessage = sender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(toMail);
			mimeMessageHelper.setFrom(fromMail);
			mimeMessageHelper.setText(content, true);
			mimeMessageHelper.setSubject(subject);
			sender.send(mimeMessage);
		} catch (MessagingException e) {
			log.warn("发送给" + toMail + "html send mail error subject：" + subject);
			e.printStackTrace();
		}
	}
}
