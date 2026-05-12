package com.yzh.aiagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.swing.*;

@SpringBootApplication
@Slf4j
public class AiAgentApplication {

    public static void main(String[] args) {
        //SpringApplication.run(AiAgentApplication.class, args);
        SpringApplication application = new SpringApplication(AiAgentApplication.class);
        ConfigurableEnvironment env = application.run(args).getEnvironment();
        log.info("success start!");
        log.info("knife4j API URL: \thttp://localhost:{}/api/doc.html", env.getProperty("server.port"));
    }

}
