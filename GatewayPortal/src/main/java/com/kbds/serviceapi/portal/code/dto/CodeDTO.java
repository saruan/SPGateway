package com.kbds.serviceapi.portal.code.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.kbds.serviceapi.apis.code.core.AbstractCode;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *  Class Name     : CodeDTO.java
 *  Description    : Enum Code List 조회용 DTO 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-14             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */

@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CodeDTO implements Serializable {

  private static final long serialVersionUID = -8822227480171810902L;

  private String type;
  private String desc;

}