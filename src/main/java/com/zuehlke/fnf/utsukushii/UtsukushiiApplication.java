package com.zuehlke.fnf.utsukushii;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Configuration
@Import(UtsukushiiContext.class)
public class UtsukushiiApplication {

    private static final Logger log = LoggerFactory.getLogger(UtsukushiiApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(UtsukushiiApplication.class);
        Environment env = app.run(args).getEnvironment();
        logNetworkInfo(env);
    }

    private static void logNetworkInfo(Environment env) {
        String address = null;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        log.info("Access URL:\n----------------------------------------------------------\n\t" +
                        " \thttp://{}:{}\n----------------------------------------------------------",
                address,
                env.getProperty("server.port"));

    }
}
