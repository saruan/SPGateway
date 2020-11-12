package com.kbds.auth.apps.role.service;

import com.kbds.auth.apps.role.repository.SPRoleRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
public class SPRoleService {

  @Autowired
  SPRoleRepository SPRoleRepository;

  @Autowired
  ModelMapper modelMapper;


  /**
   * 현재 접속한 사용자의 권한과 요청 URL 권한을 비교하는 메소드
   * @param request 객체
   * @param authentication 객체
   * @return 권한 여부
   */
  @Transactional
  public boolean hasAuthority(HttpServletRequest request, Authentication authentication) {

    // Menu, Role 전체 목록 조회 (캐싱 데이터)
    /*List<MenuDTO> menuDTOS = menuCustomRepository.selectListForSecurity();

    List<String> authority = new ArrayList<>();

    for(MenuDTO menuDTO: menuDTOS){

      if(isMatchURI(menuDTO.getMenuUrl(), request.getRequestURI())){

        authority.add(menuDTO.getRoleNm());
      }
    }*/

    List<String> authority = new ArrayList<>();

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
  public boolean isRegisteredRole(String roleCd){

    return SPRoleRepository.countByRoleCd(roleCd) > 0;
  }
}
