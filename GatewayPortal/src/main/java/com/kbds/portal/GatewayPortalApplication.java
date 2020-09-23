package com.kbds.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableFeignClients
@EnableAsync
@EnableCaching
public class GatewayPortalApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayPortalApplication.class, args);
  }

}
