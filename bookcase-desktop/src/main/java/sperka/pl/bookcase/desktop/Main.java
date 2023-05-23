package sperka.pl.bookcase.desktop;

import feign.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = {"sperka.online.bookcase.desktop.client"})
@ComponentScan(basePackages = {"sperka.online.bookcase.desktop.controller.*", "sperka.online.bookcase.desktop.service.*", "sperka.online.bookcase.desktop.repository"})
public class Main {
    public static void main(String[] args) {
        App.main(args);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
