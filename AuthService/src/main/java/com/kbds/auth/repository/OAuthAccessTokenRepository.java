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

  List<OAuthAccessToken> findByClientId(String clientId);

  List<OAuthAccessToken> findByClientIdAndUserName(String clientId, String userName);

  Optional<OAuthAccessToken> findByTokenId(String tokenId);

  Optional<OAuthAccessToken> findByRefreshToken(String refreshToken);

  Optional<OAuthAccessToken> findByAuthenticationId(String authenticationId);
}
