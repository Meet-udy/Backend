package com.api.meetudy.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_DEBUG = "mail.smtp.debug";
    private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.timeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.debug}")
    private boolean debug;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.starttls.enable}")
    private boolean startTlsEnable;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put(MAIL_SMTP_AUTH, auth);
        props.put(MAIL_DEBUG, debug);
        props.put(MAIL_CONNECTION_TIMEOUT, timeout);
        props.put(MAIL_SMTP_STARTTLS_ENABLE, startTlsEnable);

        javaMailSender.setJavaMailProperties(props);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

}