package com.kbds.serviceapi.framework.dto;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <pre>
 *  File  Name     : UserDetails
 *  Description    : 로그인 사용자 정보
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
public class UserDTO{

  private Long userId;
  private Long groupId;
  private Long roleId;

  @NotEmpty
  private String userLoginId;
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
}
