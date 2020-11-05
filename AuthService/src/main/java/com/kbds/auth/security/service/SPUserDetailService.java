package com.kbds.auth.security.service;

import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.security.dto.UserDTO;
import com.kbds.auth.security.entity.SPRoles;
import com.kbds.auth.security.entity.SPUsers;
import com.kbds.auth.security.repository.SPUserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
  private SPUserRepository SPUserRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

    SPUsers user = SPUserRepository.findByUserLoginId(userLoginId);

    if(user == null){

      throw new UsernameNotFoundException(BizExceptionCode.USR001.getDesc());
    }

    UserDTO userInfo = new UserDTO();
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
   * @param user  사용자 객체
   * @return  현재 사용자 권한 정보
   */
  public Collection<GrantedAuthority> getAuthorities(SPUsers user) {

    SPRoles role = user.getSPRoles();

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.getRoleCd()));

    return authorities;
  }
}
