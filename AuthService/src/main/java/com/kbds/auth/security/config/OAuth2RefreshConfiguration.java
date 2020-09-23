package com.kbds.auth.security.config;

import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.security.exception.CustomOAuthException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <pre>
 *  Class Name     : OAuth2RefreshConfig.java
 *  Description    : Spring OAuth Refresh Config (Inmemory에서 사용자 정보가 공유가 안됨 추후 DB로 변경)
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-20    	       구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
public class OAuth2RefreshConfiguration {

  @Bean
  public CustomUserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  public static class CustomUserDetailsService implements UserDetailsService {

    private List<UserDetailsService> userDetailsServices = new LinkedList<>();

    public CustomUserDetailsService() {
    }

    /**
     * UserDetailsService 인증서버 사용자에 추가하기 위한 인자 수집
     *
     * @param userDetailsService
     */
    public void addService(UserDetailsService userDetailsService) {

      userDetailsServices.add(userDetailsService);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

      // Spring Security에서 등록한 사용자 목록인 userDetailsServices에서 사용자 조회
      if (userDetailsServices != null) {

        for (UserDetailsService userDetailsService : userDetailsServices) {

          try {

            final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if (userDetails != null) {

              return userDetails;
            }
          } catch (UsernameNotFoundException ex) {

            continue;
          } catch (Exception ex) {

            throw new CustomOAuthException(BizExceptionCode.COM001, ex.toString());
          }
        }
      }

      throw new CustomOAuthException("Unknown user");
    }
  }
}