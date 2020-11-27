package com.kbds.auth.security.repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.auth.security.entity.OAuthRefreshToken;

/**
 *
 * <pre>
 *  Class Name     : OAuthRefreshTokenRepository.java
 *  Description    : 인증 관련 RefreshToken JPA Repository 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-19    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Repository
public interface OAuthRefreshTokenRepository extends CrudRepository<OAuthRefreshToken, Long> {

  /**
   * RefreshToken 조회
   * 
   * @param tokenId TokenId
   * @return  RefreshToken 객체
   */
  Optional<OAuthRefreshToken> findByTokenId(String tokenId);

  /**
   * 현재 시간 기준으로 만료된 토큰 삭제
   * @param now 현재 시간
   */
  void deleteByExpiredTimeBefore(Date now);

}
