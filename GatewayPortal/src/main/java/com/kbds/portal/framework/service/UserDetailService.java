package com.kbds.portal.framework.service;

import com.kbds.portal.common.code.BizExceptionCode;
import com.kbds.portal.framework.entity.Role;
import com.kbds.portal.framework.entity.User;
import com.kbds.portal.framework.repository.UserRepository;
import com.kbds.portal.framework.dto.UserDTO;
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
public class UserDetailService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

    User user = userRepository.findByUserLoginId(userLoginId);

    if(user == null){

      throw new UsernameNotFoundException(BizExceptionCode.USR001.getMsg());
    }

    UserDTO userInfo = new UserDTO();
    userInfo.setUsername(user.getUserNm());
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
  public Collection<GrantedAuthority> getAuthorities(User user) {

    Role role = user.getRole();

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.getRoleNm()));

    return authorities;
  }
}
