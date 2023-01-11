package com.rocky.bistro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class BistroApplication {

    public static void main(String[] args) {
        SpringApplication.run(BistroApplication.class);
        log.info("BistroApplication run ...");
    }
}
