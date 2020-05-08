package com.kbds.serviceapi.apis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kbds.serviceapi.apis.entity.GwServiceFilter;

@Repository
public interface GwFilterRepository extends CrudRepository<GwServiceFilter, Long> {

}
