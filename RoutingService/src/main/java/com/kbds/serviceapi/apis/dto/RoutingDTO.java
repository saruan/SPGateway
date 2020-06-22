package com.kbds.serviceapi.apis.dto;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 
 *
 * <pre>
 *  Class Name     : RoutingServiceDTO.java
 *  Description    : Routing 서비스 관리용 DTO 클래스
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
@Builder
@JsonInclude(value = Include.NON_NULL)
public class RoutingDTO {

  private Long serviceId;

  private Long filterId;

  @NotEmpty
  private String serviceNm;

  private String appKeys;

  @NotEmpty
  private String servicePath;

  @NotEmpty
  private String serviceTargetUrl;

  private String serviceDesc;

  @NotEmpty
  private String serviceLoginType;

  @NotEmpty
  private String serviceAuthType;

  private String useYn;

  private String filterBean;

  private String filterUseYn;

  private String regUserNo;

  private String uptUserNo;

  private Date regDt;

  private Date uptDt;

  /**
   * APP KEY 제외한 생성자
   * 
   * @param serviceId
   * @param filterId
   * @param serviceNm
   * @param servicePath
   * @param serviceTargetUrl
   * @param serviceDesc
   * @param serviceLoginType
   * @param serviceAuthType
   * @param useYn
   * @param filterBean
   * @param filterUseYn
   * @param regUserNo
   * @param uptUserNo
   * @param regDt
   * @param uptDt
   */
  public RoutingDTO(Long serviceId, Long filterId, String serviceNm, String servicePath,
      String serviceTargetUrl, String serviceDesc, String serviceLoginType, String serviceAuthType,
      String useYn, String filterBean, String filterUseYn, String regUserNo, String uptUserNo,
      Date regDt, Date uptDt) {

    super();
    this.serviceId = serviceId;
    this.filterId = filterId;
    this.serviceNm = serviceNm;
    this.servicePath = servicePath;
    this.serviceTargetUrl = serviceTargetUrl;
    this.serviceDesc = serviceDesc;
    this.serviceLoginType = serviceLoginType;
    this.serviceAuthType = serviceAuthType;
    this.useYn = useYn;
    this.filterBean = filterBean;
    this.filterUseYn = filterUseYn;
    this.regUserNo = regUserNo;
    this.uptUserNo = uptUserNo;
    this.regDt = regDt;
    this.uptDt = uptDt;
  }
}
