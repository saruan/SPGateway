package com.kbds.auth.apps.menu.dto;

import com.kbds.auth.apps.menu.entity.SPMenus;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : MenuDTO
 *  Description    : 메뉴 입출력 표준 dto
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO implements Serializable {

  private static final long serialVersionUID = -2049883898770648L;

  private Long menuId;

  private String menuNm;

  private String menuUrl;

  private String roleNm;

  private String useYn;

  private Set<SPMenus> subSPMenus;

  public MenuDTO(Long menuId, String menuUrl, String menuNm, String useYn,
      Set<SPMenus> subSPMenus) {

    this.menuId = menuId;
    this.menuUrl = menuUrl;
    this.menuNm = menuNm;
    this.useYn = useYn;
    this.subSPMenus = subSPMenus;
  }

  @QueryProjection
  public MenuDTO(String menuUrl, String roleNm) {

    this.menuUrl = menuUrl;
    this.roleNm = roleNm;
  }
}