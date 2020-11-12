package com.kbds.serviceapi.portal.filter.repository;

import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import com.kbds.serviceapi.portal.filter.repository.querydsl.GwFilterCustomRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GwFilterRepository extends CrudRepository<GwServiceFilter, Long> ,
    GwFilterCustomRepository {

  /**
   * Filter Id로 검색
   * @param filterId  필터 ID
   * @return  검색 결과
   */
  GwServiceFilter findByFilterId(Long filterId);

}
