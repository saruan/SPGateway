package com.kbds.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  File  Name     : BlockCode
 *  Description    : BlockType Enum 목록
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-19          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Getter
@AllArgsConstructor
public enum BlockCode {

  LOG("LOG"),
  ASSERTION("ASSERTION"),
  REST("REST");

  private String code;
}
