package com.kbds.serviceapi.portal.filter.repository;

import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GwFilterRepository extends CrudRepository<GwServiceFilter, Long> {

  GwServiceFilter findByFilterId(Long filterId);

}
