package com.kbds.serviceapi.apis.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 
 *
 * <pre>
 *  Class Name     : AppDTO.java
 *  Description    : App 서비스 관리용 DTO 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AppDTO {

  private Long appId;

  private String appNm;

  private String appKey;

  private String appDesc;

  private String useYn;

  private List<Long> serviceId;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

}
