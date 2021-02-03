package com.kbds.serviceapi.framework.listener;

import com.kbds.serviceapi.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * <pre>
 *  File  Name     : InitListener
 *  Description    : 구동 이후 초기 이벤트
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-27          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@Configuration
public class InitListener {

  private final  ApplicationContext applicationContext;

  /**
   * Constructor Injection
   * @param applicationContext Application Context
   */
  public InitListener(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * 포탈 구동 이후 게이트웨이 Routing 정보들을 갱신해준다.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void initEvent(ApplicationReadyEvent e){

    /* Gateway Refresh */
    if(e.getApplicationContext().equals(applicationContext)) {

      CommonUtils.refreshGatewayRoutes();
    }
  }
}
