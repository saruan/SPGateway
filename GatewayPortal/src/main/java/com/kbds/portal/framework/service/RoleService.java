package com.kbds.portal.framework.service;

import com.kbds.portal.common.code.BizExceptionCode;
import com.kbds.portal.framework.dto.MenuDTO;
import com.kbds.portal.framework.dto.RoleDTO;
import com.kbds.portal.framework.dto.SearchDTO;
import com.kbds.portal.framework.entity.Role;
import com.kbds.portal.framework.exception.BizException;
import com.kbds.portal.framework.repository.querydsl.MenuCustomRepository;
import com.kbds.portal.framework.repository.querydsl.RoleCustomRepository;
import com.kbds.portal.framework.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
 * <pre>
 *  File  Name     : AuthorizationService
 *  Description    : 권한 관리 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class RoleService {

  @Autowired
  MenuCustomRepository menuCustomRepository;

  @Autowired
  RoleCustomRepository roleCustomRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * Role 조회
   * @param searchDTO 객체
   * @return 권한 목록
   */
  public List<RoleDTO> findRoles(SearchDTO searchDTO){

    try{

      return roleCustomRepository.findByConditions(searchDTO);
    }catch(Exception e){

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * Role 신규 등록
   * @param roleDTO 객체
   */
  public void registerRole(RoleDTO roleDTO){

    try {

      if (isRegisteredRole(roleDTO.getRoleCd())) {

        throw new BizException(BizExceptionCode.COM003);
      }

      roleRepository.save(modelMapper.map(roleDTO, Role.class));
    }catch(BizException e){

      throw new BizException(BizExceptionCode.valueOf(e.getMsg()));
    }catch(Exception e){

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 현재 접속한 사용자의 권한과 요청 URL 권한을 비교하는 메소드
   * @param request 객체
   * @param authentication 객체
   * @return 권한 여부
   */
  public boolean hasAuthority(HttpServletRequest request, Authentication authentication) {

    // Menu, Role 전체 목록 조회 (캐싱 데이터)
    List<MenuDTO> menuDTOS = menuCustomRepository.selectListForSecurity();

    List<String> authority = new ArrayList<>();

    for(MenuDTO menuDTO: menuDTOS){

      if(isMatchURI(menuDTO.getMenuUrl(), request.getRequestURI())){

        authority.add(menuDTO.getRoleNm());
      }
    }

    // 현재 Context에 등록된 사용자 권한과 비교
    return authentication.getAuthorities().stream()
        .anyMatch(r -> authority.contains(r.getAuthority()));
  }

  /**
   * 요청받은 URI와 실제 DB에 저장되어 있는 전체 URI와 비교
   * @param menuURI     DB에 저장되어 있는 URI 값
   * @param requestURI  현재 서버로 요청온 URI 값
   * @return URI 매치 여부
   */
  private boolean isMatchURI(String menuURI, String requestURI){

    return new AntPathMatcher().match(menuURI, requestURI);
  }

  /**
   * 이미 등록되어 있는 권한 코드인지 조회
   * @param roleCd 권한 코드
   * @return 권한 등록 여부
   */
  private boolean isRegisteredRole(String roleCd){

    return roleRepository.findByRoleCd(roleCd) != null;
  }
}
