package com.evgeniy.spring.springapplication;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class TestMailConfig {

    @Bean
    public GreenMail greenMail() {
        ServerSetup smtpSetup = new ServerSetup(3025, null, "smtp");
        GreenMail greenMail = new GreenMail(smtpSetup);
        greenMail.setUser("test@example.com", "username", "password");
        greenMail.start();
        return greenMail;
    }

}
