package com.kbds.auth.apps.group.repository;

import com.kbds.auth.apps.group.entity.SPGroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : SPGroupRepository
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-04          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public interface SPGroupRepository extends CrudRepository<SPGroups, Long> {

  Long countByGroupNm(String groupNm);
}
