package com.kbds.gateway.factory.block;

import com.kbds.gateway.factory.granttype.GrantType;
import java.util.List;
import jdk.nashorn.internal.ir.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : BlockTypeFactory
 *  Description    : BlockType Factory 객체
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-19          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class BlockTypeFactory {

  @Autowired
  List<BlockType> blockTypes;

  /**
   * BlockType 에 따라 생성자를 다르게 전달해준다.
   *
   * @param blockTypeName BlockType 명
   * @return BlockType 객체
   */
  public BlockType makeGrantType(String blockTypeName) {

    for (BlockType grantType : blockTypes) {

      if (blockTypeName.equals(grantType.getBlockTypeName())) {

        return grantType;
      }
    }
    
    return null;
  }
}
