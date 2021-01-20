package com.kbds.auth.security.provider;

import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.dto.SessionDTO;
import com.kbds.auth.apps.user.service.SPUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  File  Name     : CAuthenticationProvider
 *  Description    : 사용자 정의 Provider
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private SPUserDetailService SPUserDetailService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String userNm = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    /* 사용자 정보 조회 후 사용자 검증 */
    SessionDTO sessionDTO = (SessionDTO) SPUserDetailService.loadUserByUsername(userNm);

    if(!isValidPassword(password, sessionDTO.getPassword())) {

      throw new BadCredentialsException(BizExceptionCode.USR001.getCode());
    }

    if(!sessionDTO.isEnabled()) {

      throw new BadCredentialsException(userNm);
    }

    return new UsernamePasswordAuthenticationToken(userNm, password, sessionDTO.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {

    return true;
  }

  /**
   * 비밀번호 체크
   * @param loginPassword 입력받은 비밀번호
   * @param password      DB 비밀번호
   * @return  비밀번호 유효성 여부
   */
  private boolean isValidPassword(String loginPassword, String password) {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    return passwordEncoder.matches(loginPassword, password);
  }
}
