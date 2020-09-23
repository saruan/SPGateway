package com.kbds.portal.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : MenuDTO
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class MenuDTO implements Serializable {

  private Long menuId;

  private String menuNm;

  private String menuUrl;

  private String roleNm;

  @QueryProjection
  public MenuDTO(String menuUrl, String roleNm) {
    this.menuUrl = menuUrl;
    this.roleNm = roleNm;
  }
}
