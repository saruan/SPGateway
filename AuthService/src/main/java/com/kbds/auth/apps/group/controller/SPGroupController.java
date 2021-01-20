package com.kbds.auth.apps.group.controller;

import com.kbds.auth.apps.group.dto.SPGroupDTO;
import com.kbds.auth.apps.group.service.SPGroupService;
import com.kbds.auth.apps.saml.service.SamlService;
import com.kbds.auth.common.dto.ResponseDTO;
import com.kbds.auth.common.dto.SearchDTO;
import com.kbds.auth.common.utils.CommonUtils;
import javax.validation.Valid;
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

/**
 * <pre>
 *  File  Name     : SPGroupController
 *  Description    : 그룹 관련 컨트롤러 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-24        	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/portal/v1.0/group")
public class SPGroupController {

  private final SPGroupService spGroupService;
  private final SamlService samlService;

  /**
   * Constructor Injection
   *
   * @param spGroupService SPGroupService
   * @param samlService SamlService
   */
  public SPGroupController(SPGroupService spGroupService,
      SamlService samlService) {

    this.spGroupService = spGroupService;
    this.samlService = samlService;
  }

  /**
   * 그룹 코드 조회
   *
   * @return Menu List Array
   **/
  @GetMapping
  public ResponseEntity<Object> selectAllGroups() {

    return new ResponseEntity<>(spGroupService.selectAllGroups(), HttpStatus.OK);
  }

  /**
   * 그룹 등록 여부 확인
   *
   * @return 그룹 등록 여부
   */
  @GetMapping("/name")
  public ResponseEntity<Object> selectGroupByName(@ModelAttribute SearchDTO searchDTO) {

    ResponseDTO result = CommonUtils
        .getResponseEntity(spGroupService.isRegisteredGroup(searchDTO.getGroupNm()));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 신규 그룹 등록
   *
   * @param params 등록 정보
   * @return 등록 결과 정보
   */
  @PostMapping
  public ResponseEntity<Object> registerGroup(@RequestBody @Valid SPGroupDTO params) {

    spGroupService.registerGroup(params);

    ResponseDTO result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * 그룹 수정
   *
   * @param params 등록 정보
   * @return 등록 결과 정보
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> updateGroup(@PathVariable Long id,
      @RequestBody @Valid SPGroupDTO params) {

    spGroupService.updateGroup(params, id);

    ResponseDTO result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * 그룹 삭제
   *
   * @return 등록 결과 정보
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteGroup(@PathVariable Long id) {

    spGroupService.deleteGroup(id);

    ResponseDTO result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }
}