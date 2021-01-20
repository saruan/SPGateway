package com.kbds.auth.apps.group.service;

import com.kbds.auth.apps.group.dto.SPGroupDTO;
import com.kbds.auth.apps.group.entity.SPGroups;
import com.kbds.auth.apps.group.repository.SPGroupRepository;
import com.kbds.auth.apps.user.repository.SPUserRepository;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.exception.BizException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : SPGroupService
 *  Description    : 그룹 관련 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-24          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPGroupService {

  private final ModelMapper modelMapper;
  private final SPGroupRepository spGroupRepository;
  private final SPUserRepository spUserRepository;

  /**
   * Constructor Injection
   *  @param modelMapper       객체 변환용 ModelMapper
   * @param spGroupRepository SPGroup 레파지토리
   * @param spUserRepository  SPUser 레파지토리
   */
  public SPGroupService(ModelMapper modelMapper,
      SPGroupRepository spGroupRepository,
      SPUserRepository spUserRepository) {

    this.modelMapper = modelMapper;
    this.spGroupRepository = spGroupRepository;
    this.spUserRepository = spUserRepository;
  }

  /**
   * 그룹 목록 검색
   *
   * @return 그룹 목록 리스트
   */
  public List<SPGroupDTO> selectAllGroups() {

    return null;
  }

  /**
   * 신규 그룹 등록
   *
   * @param spGroupDTO 그룹 객체
   */
  public void registerGroup(SPGroupDTO spGroupDTO) {

    try {

      if (isRegisteredGroup(spGroupDTO.getGroupNm())) {

        throw new BizException(BizExceptionCode.GRP001);
      }

      SPGroups spGroups = modelMapper.map(spGroupDTO, SPGroups.class);

      spGroupRepository.save(spGroups);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 그룹 정보 수정
   *
   * @param spGroupDTO 수정할 데이터
   */
  public void updateGroup(SPGroupDTO spGroupDTO, Long groupId) {

    try {

      if (isRegisteredGroup(spGroupDTO.getGroupNm())) {

        throw new BizException(BizExceptionCode.GRP001);
      }

      SPGroups spGroups = modelMapper.map(spGroupDTO, SPGroups.class);
      spGroups.setGroupId(groupId);

      spGroupRepository.save(spGroups);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 그룹 삭제
   * @param groupId 삭제할 그룹 ID
   */
  public void deleteGroup(Long groupId){

    try {

      if (isUsingGroup(groupId)) {

        throw new BizException(BizExceptionCode.GRP002);
      }

      SPGroups spGroups = SPGroups.builder().groupId(groupId).build();

      spGroupRepository.delete(spGroups);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 그룹 기등록 여부 체크
   *
   * @param groupNm 그룹명
   * @return 기등록 여부
   */
  public boolean isRegisteredGroup(String groupNm) {

    return spGroupRepository.countByGroupNm(groupNm) > 0;
  }

  /**
   * 사용중인 그룹 여부 체크
   * @param groupId 그룹 ID
   * @return  사용중 여부
   */
  public boolean isUsingGroup(Long groupId){

    return spUserRepository.countBySpGroupsGroupId(groupId) > 0;
  }
}
