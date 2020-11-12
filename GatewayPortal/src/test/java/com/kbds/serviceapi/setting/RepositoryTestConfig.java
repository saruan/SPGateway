package com.kbds.serviceapi.setting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 *  File  Name     : R
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-10          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@TestConfiguration
public class RepositoryTestConfig {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory(){

    return new JPAQueryFactory(entityManager);
  }

  @Bean
  public ModelMapper modelMapper(){

    return new ModelMapper();
  }
}
