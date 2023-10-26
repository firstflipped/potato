package com.flipped.potato.gateway;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

/**
 * 网关启动类
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2023-10-23 11:05:33
 */
@SpringBootApplication
public class PotatoGateway {

    public static final Logger logger = LoggerFactory.getLogger(PotatoGateway.class);

    public static void main(String[] args) {

        SpringApplication.run(PotatoGateway.class, args);

    }


    @Component
    public static class PotatoCommandLineRunner implements CommandLineRunner {

        @Resource
        private Environment environment;

        @Override
        public void run(String... args) {
            // 程序实参
            logger.info("Command line arguments: {}", Arrays.toString(args));

            // JVM 虚拟机参数
            List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            logger.info("Command line options: {}", inputArguments);

            // 最终参数
            String port = environment.getProperty("server.port");
            logger.info("started on port: {}", port);

        }
    }


}