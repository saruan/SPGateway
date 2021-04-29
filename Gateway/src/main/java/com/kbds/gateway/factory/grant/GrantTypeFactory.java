package com.kbds.gateway.factory.grant;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : GrantTypeFactory
 *  Description    : GrantType Factory 객체
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-20          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
@AllArgsConstructor
public class GrantTypeFactory {

  private final List<Grant> grants;

  /**
   * GrantType 에 따라 생성자를 다르게 전달해준다.
   *
   * @param grantTypeName GrantType 명
   * @return GrantType 객체
   */
  public Grant makeGrantType(String grantTypeName) {

    for (Grant grant : grants) {

      if (grantTypeName.equals(grant.getGrantTypeName())) {

        return grant;
      }
    }
    return null;
  }
}
