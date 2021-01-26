package com.kbds.auth.security.token;

import com.kbds.auth.apps.user.dto.UserDTO;
import com.kbds.auth.apps.user.repository.querydsl.SPUserCustomRepository;
import com.kbds.auth.common.code.AuthCode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * <pre>
 *  File  Name     : CustomTokenEnhancer
 *  Description    : AccessToken Custom Response
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class CustomTokenEnhancer implements TokenEnhancer {

  @Autowired
  private SPUserCustomRepository spUserCustomRepository;

  @Override
  @Transactional
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {

    String userLoginId = authentication.getName();

    UserDTO userDTO = spUserCustomRepository.findByUserDetails(userLoginId);

    if (!isSystemAccount(authentication)) {

      Map<String, Object> additionalInfo = new HashMap<>();
      additionalInfo.put("user", userDTO);

      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    }

    return accessToken;
  }

  /**
   * 시스템 계정인지 확인 (시스템 계정은 사용자 추가 정보를 리턴할 필요가 없음)
   *
   * @param authentication
   * @return
   */
  private boolean isSystemAccount(OAuth2Authentication authentication) {

    Iterator<GrantedAuthority> grantedAuthorityIterator = authentication.getAuthorities()
        .iterator();

    return grantedAuthorityIterator.hasNext() &&  AuthCode.ROLE_SYSTEM.getCode()
        .equals(grantedAuthorityIterator.next().getAuthority());
  }
}
