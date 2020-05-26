package com.kbds.serviceapi.framework.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <pre>
 *  Class Name     : CommonConfiguration.java
 *  Description    : 공통 설정 부분
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-26    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
public class CommonConfiguration {

  // ModelMapper Bean 등록
  @Bean
  public ModelMapper modelMapper() {

    return new ModelMapper();
  }
}
