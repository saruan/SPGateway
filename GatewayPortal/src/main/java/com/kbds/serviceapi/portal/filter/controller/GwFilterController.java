package com.kbds.serviceapi.portal.filter.controller;

import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
import com.kbds.serviceapi.portal.filter.service.GwFilterService;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/service/v1.0/filter")
public class GwFilterController {

  @Autowired
  GwFilterService gwFilterService;

  /**
   * 전체 Filter 서비스 목록 조회 API
   *
   * @return
   */
  @GetMapping
  public ResponseEntity<Object> findFilters(@ModelAttribute SearchDTO searchDTO) {

    Object result = CommonUtils.getResponseEntity(gwFilterService.findFilters(searchDTO));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 필터 등록 컨트롤러
   *
   * @param id
   * @return
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> findFilterDetail(@PathVariable Long id) {

    Object result = CommonUtils.getResponseEntity(gwFilterService.findFilterDetail(id));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 필터 등록 컨트롤러
   *
   * @param params
   * @return
   */
  @PostMapping
  public ResponseEntity<Object> registerFilter(@RequestBody @Valid FilterDTO params) {

    gwFilterService.registerFilter(params);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * 필터 수정 컨트롤러
   *
   * @param params
   * @return
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> updateFilter(@RequestBody @Valid FilterDTO params,
      @PathVariable Long id) {

    gwFilterService.updateFilter(params, id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 필터 삭제
   *
   * @param id
   * @return
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteService(@PathVariable Long id) {

    gwFilterService.deleteFilter(id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
