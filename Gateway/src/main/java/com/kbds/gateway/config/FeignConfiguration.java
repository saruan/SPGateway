package com.kbds.gateway.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <pre>
 *  Class Name     : FeignConfiguration.java
 *  Description    : Feign 관련 추가 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
public class FeignConfiguration {

  @Bean
  public Encoder feignFormEncoder() {
    return new JacksonEncoder();
  }

  @Bean
  public Decoder feignFormDecoder() {
    return new JacksonDecoder();
  }
}
