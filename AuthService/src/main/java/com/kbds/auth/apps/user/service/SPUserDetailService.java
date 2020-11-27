package com.kbds.auth.apps.user.service;

import com.kbds.auth.apps.group.entity.SPGroups;
import com.kbds.auth.apps.role.service.SPRoleService;
import com.kbds.auth.apps.user.dto.UserDTO;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.dto.SessionDTO;
import com.kbds.auth.apps.role.entity.SPRoles;
import com.kbds.auth.apps.user.entity.SPUsers;
import com.kbds.auth.apps.user.repository.SPUserRepository;
import com.kbds.auth.common.exception.BizException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : UserDetailService
 *  Description    : 사용자 정의 UserDetailService
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPUserDetailService implements UserDetailsService {

  @Autowired
  private SPUserRepository spUserRepository;

  @Autowired
  private SPRoleService spRoleService;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

    SPUsers user = spUserRepository.findByUserLoginId(userLoginId);

    if (user == null) {

      throw new UsernameNotFoundException(BizExceptionCode.USR001.getDesc());
    }

    SessionDTO userInfo = new SessionDTO();
    userInfo.setAuthorities(getAuthorities(user));
    userInfo.setAccountNonExpired(true);
    userInfo.setAccountNonLocked(true);
    userInfo.setEnabled(true);
    userInfo.setPassword(user.getPassword());
    userInfo.setCredentialsNonExpired(true);

    return userInfo;
  }

  /**
   * 권한 정보 등록
   *
   * @param user 사용자 객체
   * @return 현재 사용자 권한 정보
   */
  public Collection<GrantedAuthority> getAuthorities(SPUsers user) {

    SPRoles role = user.getSpRoles();

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.getRoleCd()));

    return authorities;
  }

  /**
   * 신규 사용자 등록
   *
   * @param userDTO 사용자 정보 객체
   */
  public void registerUser(UserDTO userDTO) {

    try {

      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

      String userLoginId = userDTO.getUserLoginId();
      String password = userDTO.getPassword();
      String userNm = userDTO.getUsername();
      Long roleId = userDTO.getRoleId();
      Long groupId = userDTO.getGroupId();

      if (isRegisteredUser(userLoginId)) {

        throw new BizException(BizExceptionCode.USR002);
      }

      if (!isValidRoleOrGroups(roleId, groupId)) {

        throw new BizException(BizExceptionCode.COM002);
      }

      SPGroups spGroups = SPGroups.builder().groupId(groupId).build();
      SPRoles spRoles = SPRoles.builder().roleId(roleId).build();
      SPUsers spUsers = SPUsers.builder().spGroups(spGroups).spRoles(spRoles)
          .userLoginId(userLoginId).password(passwordEncoder.encode(password)).userNm(userNm)
          .build();

      spUserRepository.save(spUsers);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 이미 등록 되어 있는 사용자 여부
   *
   * @param userLoginId 사용자 로그인 ID
   * @return 등록 여부
   */
  private boolean isRegisteredUser(String userLoginId) {

    return spUserRepository.countByUserLoginId(userLoginId) > 0;
  }

  /**
   * 그룹, 권한 유효성 체크
   *
   * @param groupId groupId
   * @param roleId  roleId
   * @return 유효 여부
   */
  private boolean isValidRoleOrGroups(Long groupId, Long roleId) {

    return spRoleService.isRegisteredRoleById(roleId);
  }
}
