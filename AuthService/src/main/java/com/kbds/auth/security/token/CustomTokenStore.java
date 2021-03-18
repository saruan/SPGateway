package com.kbds.auth.security.token;

import com.kbds.auth.common.utils.DateUtils;
import com.kbds.auth.common.utils.OAuthUtils;
import com.kbds.auth.security.entity.OAuthAccessToken;
import com.kbds.auth.security.entity.OAuthRefreshToken;
import com.kbds.auth.security.repository.OAuthAccessTokenRepository;
import com.kbds.auth.security.repository.OAuthRefreshTokenRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  Class Name     : CustomTokenStore.java
 *  Description    : TokenStore 커스터마이징 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-19    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class CustomTokenStore implements TokenStore {

  private final OAuthAccessTokenRepository oAuthAccessTokenRepository;
  private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

  /**
   * Constructor Injections
   *
   * @param oAuthAccessTokenRepository  OAuthAccessTokenRepository 객체
   * @param oAuthRefreshTokenRepository OAuthRefreshTokenRepository 객체
   */
  public CustomTokenStore(
      OAuthAccessTokenRepository oAuthAccessTokenRepository,
      OAuthRefreshTokenRepository oAuthRefreshTokenRepository) {
    this.oAuthAccessTokenRepository = oAuthAccessTokenRepository;
    this.oAuthRefreshTokenRepository = oAuthRefreshTokenRepository;
  }

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {

    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String tokenValue) {

    Optional<OAuthAccessToken> accessToken = oAuthAccessTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(tokenValue));

    return accessToken.<OAuth2Authentication>map(oAuthAccessToken -> SerializationUtils
        .deserialize(oAuthAccessToken.getAuthentication())).orElse(null);
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken,
      OAuth2Authentication oAuth2Authentication) {

    String GRANT_TYPE_CLIENTS = "Clients";
    String refreshToken = null;

    if (oAuth2AccessToken.getRefreshToken() != null) {

      refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
    }

    if (readAccessToken(oAuth2AccessToken.getValue()) != null) {

      this.removeAccessToken(oAuth2AccessToken);
    }

    // AccessToken 저장
    OAuthAccessToken accessToken = new OAuthAccessToken();

    accessToken.setTokenId(OAuthUtils.extractTokenKey(oAuth2AccessToken.getValue()));
    accessToken.setToken(SerializationUtils.serialize(oAuth2AccessToken));
    accessToken.setAuthenticationId(OAuthUtils.extractKey(oAuth2Authentication));
    accessToken.setUserName(
        oAuth2Authentication.isClientOnly() ? GRANT_TYPE_CLIENTS : oAuth2Authentication.getName());
    accessToken.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
    accessToken.setAuthentication(SerializationUtils.serialize(oAuth2Authentication));
    accessToken.setRefreshToken(OAuthUtils.extractTokenKey(refreshToken));
    accessToken.setAdditionalInfo(
        oAuth2Authentication.getOAuth2Request().getRequestParameters().toString());
    accessToken.setExpiredTime(oAuth2AccessToken.getExpiration());

    oAuthAccessTokenRepository.save(accessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {

    Optional<OAuthAccessToken> accessToken = oAuthAccessTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(tokenValue));

    return accessToken.<OAuth2AccessToken>map(
        oAuthAccessToken -> SerializationUtils.deserialize(oAuthAccessToken.getToken()))
        .orElse(null);
  }


  @Override
  public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {

    Optional<OAuthAccessToken> accessToken = oAuthAccessTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2AccessToken.getValue()));

    accessToken.ifPresent(oAuthAccessTokenRepository::delete);
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken,
      OAuth2Authentication authentication) {

    OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();

    oAuthRefreshToken.setTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));
    oAuthRefreshToken.setToken(SerializationUtils.serialize(oAuth2RefreshToken));
    oAuthRefreshToken.setAuthentication(SerializationUtils.serialize(authentication));
    oAuthRefreshToken.setExpiredTime(DateUtils.getExpiredTime(60));

    oAuthRefreshTokenRepository.save(oAuthRefreshToken);
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {

    Optional<OAuthRefreshToken> refreshToken =
        oAuthRefreshTokenRepository.findByTokenId(OAuthUtils.extractTokenKey(tokenValue));

    return refreshToken.map(oAuthRefreshToken -> (OAuth2RefreshToken) SerializationUtils
        .deserialize(oAuthRefreshToken.getToken())).orElse(null);
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(
      OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthRefreshToken> oAuthRefreshToken = oAuthRefreshTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    return oAuthRefreshToken.map(authRefreshToken -> (OAuth2Authentication) SerializationUtils
        .deserialize(authRefreshToken.getAuthentication())).orElse(null);
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthRefreshToken> oAuthRefreshToken = oAuthRefreshTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    oAuthRefreshToken
        .ifPresent(oAuthRefreshTokenRepository::delete);
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthAccessToken> oAuthAccessToken = oAuthAccessTokenRepository
        .findByRefreshToken(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    oAuthAccessToken
        .ifPresent(oAuthAccessTokenRepository::delete);
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuthAuthentication) {

    OAuth2AccessToken oAuth2AccessToken = null;

    String authenticationId = OAuthUtils.extractKey(oAuthAuthentication);

    // Access Token 발급
    Optional<OAuthAccessToken> oAuthAccessToken =
        oAuthAccessTokenRepository.findByAuthenticationId(authenticationId);

    if (oAuthAccessToken.isPresent()) {

      oAuth2AccessToken =
          SerializationUtils.deserialize(oAuthAccessToken.get().getToken());

      if (oAuth2AccessToken != null && !authenticationId.equals(
          OAuthUtils.extractKey(this.readAuthentication(oAuth2AccessToken)))) {

        this.removeAccessToken(oAuth2AccessToken);
        this.storeAccessToken(oAuth2AccessToken, oAuthAuthentication);
      }
    }
    return oAuth2AccessToken;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
      String userName) {

    Collection<OAuth2AccessToken> tokens = new ArrayList<>();

    List<OAuthAccessToken> result =
        oAuthAccessTokenRepository.findByClientIdAndUserName(clientId, userName);

    result
        .forEach(e -> tokens.add(SerializationUtils.deserialize(e.getToken())));

    return tokens;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {

    Collection<OAuth2AccessToken> tokens = new ArrayList<>();

    List<OAuthAccessToken> result = oAuthAccessTokenRepository.findByClientId(clientId);

    result
        .forEach(e -> tokens.add(SerializationUtils.deserialize(e.getToken())));

    return tokens;
  }
}
