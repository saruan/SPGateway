package com.kbds.auth.apps.saml.dto;

import java.util.Map;
import lombok.Data;

/**
 * <pre>
 *  File  Name     : SamlDTO
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-14          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */

@Data
public class SamlDTO {

  private String ci;
  private String sessionId;
  private String clientId;
}
