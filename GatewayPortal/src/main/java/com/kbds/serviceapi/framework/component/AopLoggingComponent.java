package com.kbds.serviceapi.framework.component;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kbds.serviceapi.common.utils.CommonUtils;

/**
 *
 * <pre>
 *  Class Name     : AopLoggingComponent.java
 *  Description    : Logging 공통 설정용 AOP 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-24     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Aspect
@Component
public class AopLoggingComponent {

  @Autowired
  private HttpServletRequest request;

  /**
   * controller 패키지 하위의 메소드가 실행 될 때 기본적으로 로깅에 필요한 값들을 설정한다.
   * 
   * @param pjp
   * @throws Throwable
   */
  @Around("execution(* com.kbds.serviceapi.apis.controller.*.*(..)) || execution(* com.kbds.serviceapi.framework.exception.ServiceExceptionHandler.*(..))")
  public Object setCommonLogParams(ProceedingJoinPoint pjp) throws Throwable {

    CommonUtils.setCommonLog(request.getRequestURI(), "1");

    return pjp.proceed();
  }
}
