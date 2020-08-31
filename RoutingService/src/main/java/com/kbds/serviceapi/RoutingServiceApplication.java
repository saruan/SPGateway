package com.kbds.serviceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableFeignClients
@EnableAsync
@EnableCaching
public class RoutingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RoutingServiceApplication.class, args);
  }

}
