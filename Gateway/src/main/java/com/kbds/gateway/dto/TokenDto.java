package com.kbds.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Map;
import lombok.Data;

/**
 * <pre>
 *  File  Name     : TokenDto
 *  Description    : Access Token 관리용 DTO
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-26             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
public class TokenDto {
  /* Access Token 값 */
  private String accessToken;
  /* 사용자 Login Id */
  private String userLoginId;
  /* 사용자 권한 */
  private String roleNm;

  @JsonSetter("user")
  public void setUserLoginId(Map<String, String> user){

    this.userLoginId = user.get("userLoginId");
    this.roleNm = user.get("roleNm");
  }
}
