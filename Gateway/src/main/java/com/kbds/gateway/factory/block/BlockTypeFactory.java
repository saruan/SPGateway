package com.kbds.gateway.factory.block;

import com.kbds.gateway.code.BlockCode.BlockType;
import java.util.List;
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
  List<Block> blocks;

  /**
   * BlockType 에 따라 생성자를 다르게 전달해준다.
   *
   * @param blockTypeName BlockType 명
   * @return BlockType 객체
   */
  public Block makeGrantType(BlockType blockTypeName) {

    for (Block block : blocks) {

      if (blockTypeName.equals(block.getBlockTypeName())) {

        return block;
      }
    }
    
    return null;
  }
}
