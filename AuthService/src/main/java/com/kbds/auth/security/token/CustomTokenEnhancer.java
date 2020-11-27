package com.kbds.auth.security.token;

import com.kbds.auth.apps.user.dto.UserDTO;
import com.kbds.auth.apps.user.repository.querydsl.SPUserCustomRepository;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put("user", userDTO);

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

    return accessToken;
  }
}
