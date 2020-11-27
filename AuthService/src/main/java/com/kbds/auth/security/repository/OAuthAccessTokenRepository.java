package com.kbds.auth.security.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.auth.security.entity.OAuthAccessToken;

/**
 *
 * <pre>
 *  Class Name     : OAuthAccessTokenRepository.java
 *  Description    : 인증 관련 JPA Repository 클래스
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
public interface OAuthAccessTokenRepository extends CrudRepository<OAuthAccessToken, Long> {

  /**
   * ClientID로 토큰 조회
   * 
   * @param clientId clientId
   * @return Token 리스트
   */
  List<OAuthAccessToken> findByClientId(String clientId);

  /**
   * ClientID, 사용자명으로 토큰 조회
   * 
   * @param clientId clientId
   * @param userName 사용자명
   * @return  Token 리스트
   */
  List<OAuthAccessToken> findByClientIdAndUserName(String clientId, String userName);

  /**
   * 토큰ID로 조회
   * 
   * @param tokenId Token Id
   * @return Token 정보
   */
  Optional<OAuthAccessToken> findByTokenId(String tokenId);

  /**
   * Refresh 토큰 조회
   * 
   * @param refreshToken Refresh Token
   * @return Refresh Token 정보
   */
  Optional<OAuthAccessToken> findByRefreshToken(String refreshToken);

  /**
   * 인증 ID 조회
   * 
   * @param authenticationId  인증 ID
   * @return  Token 정보
   */
  Optional<OAuthAccessToken> findByAuthenticationId(String authenticationId);

  /**
   * 현재 시간 기준으로 만료된 토큰 삭제
   * @param now 현재 시간
   */
  void deleteByExpiredTimeBefore(Date now);
}
