package com.kbds.auth.apps.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : UserDTO
 *  Description    : 사용자 정보 통신용 객체
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDTO implements Serializable {

  private static final long serialVersionUID = 3547593150368093940L;

  private Long userId;
  private Long groupId;
  private Long roleId;

  @NotEmpty
  private String userLoginId;
  @NotEmpty
  private String password;
  private String username;
  private String roleNm;
  private String groupNm;

  @QueryProjection
  public UserDTO(String userLoginId, String username, String roleNm,
      String groupNm) {

    this.userLoginId = userLoginId;
    this.username = username;
    this.roleNm = roleNm;
    this.groupNm = groupNm;
  }
}
