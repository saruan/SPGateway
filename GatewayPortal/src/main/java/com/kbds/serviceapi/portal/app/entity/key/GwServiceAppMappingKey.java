package com.kbds.serviceapi.portal.app.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * <pre>
 *  Class Name     : GwServiceAppMappingKey.java
 *  Description    : GW_SERVICE_APP_MAPPING PK Entity
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GwServiceAppMappingKey implements Serializable {

  private static final long serialVersionUID = -711685434231082611L;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "APP_ID")
  private Long appId;
}
