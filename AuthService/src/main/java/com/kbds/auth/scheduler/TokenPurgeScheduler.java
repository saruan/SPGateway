package com.kbds.auth.scheduler;

import com.kbds.auth.security.repository.OAuthAccessTokenRepository;
import com.kbds.auth.security.repository.OAuthRefreshTokenRepository;
import java.util.Date;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  File  Name     : TokenPurgeScheduler
 *  Description    : 만료 토큰 삭제 스케쥴러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-16          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
public class TokenPurgeScheduler {

  private final Logger logger = LoggerFactory.getLogger(TokenPurgeScheduler.class);

  @Autowired
  OAuthAccessTokenRepository oAuthAccessTokenRepository;

  @Autowired
  OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

  @Transactional
  @Scheduled(fixedDelay = 3600000)
  public void purgeTokens(){

    Date now = new Date();

    try {

      oAuthAccessTokenRepository.deleteByExpiredTimeBefore(now);
      oAuthRefreshTokenRepository.deleteByExpiredTimeBefore(now);
    }catch(Exception e){

      logger.error(e.toString());
    }
  }

}
