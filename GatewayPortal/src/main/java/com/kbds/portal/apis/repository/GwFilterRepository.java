package com.kbds.portal.apis.repository;

import com.kbds.portal.apis.entity.GwServiceFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GwFilterRepository extends CrudRepository<GwServiceFilter, Long> {

  GwServiceFilter findByFilterId(Long filterId);

}
