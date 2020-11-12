package com.kbds.serviceapi.framework.service;

import com.kbds.serviceapi.common.feign.AuthClient;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SessionDTO;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : UserDetailService
 *  Description    : 사용자 정의 UserDetailService
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPUserService {

  @Autowired
  AuthClient authClient;

  /**
   * Login 처리
   *
   * @param id       사용자 ID
   * @param password 사용자 비밀번호
   * @return Access Token
   */
  @Transactional
  public SessionDTO getAccessToken(String id, String password) {

    final String CONST_GRANT_TYPE = "password";
    final String CONST_SCOPE = "read_profile";
    final String CONST_ACCESS_TOKEN = "access_token";
    final String CONST_REFRESH_TOKEN = "refresh_token";
    final String CONST_USER_NM = "userNm";
    final String CONST_GROUP_NM = "groupNm";
    final String CONST_ROLE_NM = "roleNm";

    Map<String, Object> tokens = authClient
        .login(CommonUtils.setFeignCommonHeaders(), id, password, CONST_SCOPE, CONST_GRANT_TYPE);

    Map<String, Object> userInfo = (Map<String, Object>) tokens.get("user");

    String accessToken = String.valueOf(tokens.get(CONST_ACCESS_TOKEN));
    String refreshToken = String.valueOf(tokens.get(CONST_REFRESH_TOKEN));

    String userNm = String.valueOf(userInfo.get(CONST_USER_NM));
    String groupNm = String.valueOf(userInfo.get(CONST_GROUP_NM));
    String roleNm = String.valueOf(userInfo.get(CONST_ROLE_NM));

    return new SessionDTO(userNm, groupNm, roleNm, accessToken, refreshToken);
  }
}
