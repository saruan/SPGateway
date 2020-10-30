package com.kbds.serviceapi.framework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.RoleDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.entity.Role;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.framework.repository.RoleRepository;
import com.kbds.serviceapi.framework.repository.querydsl.RoleCustomRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * <pre>
 *  File  Name     : RoleServiceTest
 *  Description    : 권한 관련 서비스 단위 테스트 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0                	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  RoleCustomRepository roleCustomRepository;

  @Mock
  RoleRepository roleRepository;

  @InjectMocks
  ModelMapper modelMapper;

  @InjectMocks
  RoleService roleService;

  private SearchDTO searchConditions;
  private List<RoleDTO> roleDTOS;
  private RoleDTO roleDTO;
  private Role role;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    searchConditions = SearchDTO.builder().name("어드민").build();

    roleDTOS = new ArrayList<>();
    roleDTO = RoleDTO.builder().roleId(1L).roleNm("어드민").roleCd("ADMIN").build();
    roleDTOS.add(roleDTO);

    role = modelMapper.map(roleDTO, Role.class);

    ReflectionTestUtils.setField(roleService, "modelMapper", new ModelMapper());
  }

  @Test
  void ROLE_조회_테스트() {

    doReturn(roleDTOS).when(roleCustomRepository).findByConditions(searchConditions);

    List<RoleDTO> result = roleService.findRoles(searchConditions);

    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getRoleId(), 1L);
    assertEquals(result.get(0).getRoleCd(), "ADMIN");
    assertEquals(result.get(0).getRoleNm(), "어드민");
  }

  @Test
  void ROLE_등록_테스트() {

    doReturn(0).when(roleRepository).countByRoleCd(roleDTO.getRoleCd());
    when(roleRepository.save(role)).thenReturn(role);
    roleService.registerRole(roleDTO);
  }

  @Test
  void ROLE_중복_등록_테스트() {

    doReturn(1).when(roleRepository).countByRoleCd(roleDTO.getRoleCd());

    BizException ex = assertThrows(BizException.class, () -> roleService.registerRole(roleDTO));
    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }
}