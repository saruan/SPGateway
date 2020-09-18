package com.kbds.auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  File  Name     : CommonConfig
 *  Description    : 기타 Bean 등록 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-18             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Configuration
public class CommonConfig {

  /**
   * 데이터 형변환을 위한 ModelMapper 등록
   *
   * @return  ModelMapper
   */
  @Bean
  public ModelMapper modelMapper() {

    return new ModelMapper();
  }
}
