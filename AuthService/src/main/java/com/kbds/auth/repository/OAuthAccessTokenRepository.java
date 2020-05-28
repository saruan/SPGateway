package com.kbds.auth.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.auth.entity.OAuthAccessToken;

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
   * @param clientId
   * @return
   */
  public List<OAuthAccessToken> findByClientId(String clientId);

  /**
   * ClientID, 사용자명으로 토큰 조회
   * 
   * @param clientId
   * @param userName
   * @return
   */
  public List<OAuthAccessToken> findByClientIdAndUserName(String clientId, String userName);

  /**
   * 토큰ID로 조회
   * 
   * @param tokenId
   * @return
   */
  public Optional<OAuthAccessToken> findByTokenId(String tokenId);

  /**
   * Refresh 토큰 조회
   * 
   * @param refreshToken
   * @return
   */
  public Optional<OAuthAccessToken> findByRefreshToken(String refreshToken);

  /**
   * 인증 ID 조회
   * 
   * @param authenticationId
   * @return
   */
  public Optional<OAuthAccessToken> findByAuthenticationId(String authenticationId);
}
