package com.kbds.auth.common.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : SearchDTO
 *  Description    : 공통 검색 조건 DTO
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {

  private String name;

  private String regUserNm;

  private String groupNm;

  private String servicePath;

  private String useYn;

  private Date startDt;

  private Date endDt;
}
