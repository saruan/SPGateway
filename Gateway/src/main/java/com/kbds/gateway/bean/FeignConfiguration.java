package com.kbds.gateway.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 *
 * <pre>
 *  Class Name     : FeignConfiguration.java
 *  Description    : Webflux에서 Feign Decode/Encode를 설정해주기 위한 클래스
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
  public Decoder decoder() {

    return new JacksonDecoder();
  }

  @Bean
  public Encoder encoder() {

    return new JacksonEncoder();
  }
}
