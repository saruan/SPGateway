package com.kbds.portal.framework.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

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
 *     Ver 1.0      2020-05-26    	       구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
@EnableJpaAuditing
public class CommonConfiguration {

  @PersistenceContext
  private EntityManager entityManager;


  /**
   * 데이터 형변환을 위한 ModelMapper 등록
   * 
   * @return  ModelMapper
   */
  @Bean
  public ModelMapper modelMapper() {

    return new ModelMapper();
  }

  /**
   * 유효성 체크를 위한 Validation Bean 등록
   * 
   * @param autowireCapableBeanFactory
   * @return  Validator
   */
  @Bean
  Validator validator(AutowireCapableBeanFactory autowireCapableBeanFactory) {

    ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
        .constraintValidatorFactory(
            new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
        .buildValidatorFactory();

    return validatorFactory.getValidator();
  }

  /**
   * QueryDSL Bean 등록
   *
   * @return  JPAQueryFactory
   */
  @Bean
  public JPAQueryFactory jpaQueryFactory() {

    return new JPAQueryFactory(entityManager);
  }
}
