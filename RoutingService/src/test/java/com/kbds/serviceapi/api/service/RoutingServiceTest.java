package com.kbds.serviceapi.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.service.GwRoutingService;
import com.kbds.serviceapi.framework.exception.BizException;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class RoutingServiceTest {

  @Autowired
  private GwRoutingService gwRoutingService;

  static RoutingDTO routingDTO;

  static Long testRegistedServiceId;

  @BeforeAll
  public static void init() {

    routingDTO = RoutingDTO.builder().serviceNm("Routing Mocking").servicePath("/api/mock/")
        .serviceTargetUrl("http://localhost:8080/").serviceDesc("Routing Mocking Test")
        .serviceLoginType("1").serviceAuthType("1").filterId(new Long(1)).regUserNo("1").build();
  }

  @Test
  @Order(1)
  @Rollback(false)
  public void API등록_테스트() throws Exception {

    GwService result = gwRoutingService.registService(routingDTO);

    assertNotNull(result, "등록 실패");
    assertNotNull(result.getServiceId(), "서비스 아이디 미존재");

    assertEquals(routingDTO.getServiceNm(), result.getServiceNm());
    assertEquals(routingDTO.getServicePath(), result.getServicePath());
    assertEquals(routingDTO.getServiceTargetUrl(), result.getServiceTargetUrl());
    assertEquals(routingDTO.getServiceDesc(), result.getServiceDesc());
    assertEquals(routingDTO.getServiceLoginType(), result.getServiceLoginType());
    assertEquals(routingDTO.getServiceAuthType(), result.getServiceAuthType());
    assertEquals(routingDTO.getFilterId(), result.getFilter().getFilterId());
    assertEquals(routingDTO.getRegUserNo(), result.getRegUserNo());

    testRegistedServiceId = result.getServiceId();
  }

  @Test
  @Order(2)
  public void API등록_중복_테스트() throws Exception {

    assertThrows(BizException.class, () -> {

      gwRoutingService.registService(routingDTO);
    });
  }

  @Test
  @Order(3)
  public void API조회_리스트_테스트() throws Exception {

    // 전체 리스트 조회
    List<RoutingDTO> result = gwRoutingService.findServices(new RoutingDTO());

    assertFalse(result.isEmpty());

    // 등록한 정보 조회
    result = gwRoutingService.findServices(routingDTO);

    assertEquals(testRegistedServiceId, result.get(0).getServiceId());
    assertEquals(routingDTO.getServicePath(), result.get(0).getServicePath());
    assertEquals(routingDTO.getServiceTargetUrl(), result.get(0).getServiceTargetUrl());
    assertEquals(routingDTO.getServiceDesc(), result.get(0).getServiceDesc());
    assertEquals(routingDTO.getServiceLoginType(), result.get(0).getServiceLoginType());
    assertEquals(routingDTO.getServiceAuthType(), result.get(0).getServiceAuthType());
    assertEquals(routingDTO.getFilterId(), result.get(0).getFilterId());
  }

  @Test
  @Order(4)
  public void API조회_상세_테스트() throws Exception {

    RoutingDTO result = (RoutingDTO) gwRoutingService.findServiceDetail(testRegistedServiceId);

    assertEquals(testRegistedServiceId, result.getServiceId());
    assertEquals(routingDTO.getServicePath(), result.getServicePath());
    assertEquals(routingDTO.getServiceTargetUrl(), result.getServiceTargetUrl());
    assertEquals(routingDTO.getServiceDesc(), result.getServiceDesc());
    assertEquals(routingDTO.getServiceLoginType(), result.getServiceLoginType());
    assertEquals(routingDTO.getServiceAuthType(), result.getServiceAuthType());
    assertEquals(routingDTO.getFilterId(), result.getFilterId());
  }

  @Test
  @Order(5)
  public void API수정_테스트() throws Exception {

    routingDTO.setServiceDesc("서비스 설명 수정");
    routingDTO.setServiceNm("서비스 명칭 수정");
    routingDTO.setUptUserNo("2");
    routingDTO.setUseYn("N");

    gwRoutingService.updateService(routingDTO, testRegistedServiceId);
  }

  @Test
  @Order(6)
  public void API수정_기등록_테스트() throws Exception {

    RoutingDTO result = gwRoutingService.findServices(new RoutingDTO()).stream()
        .filter(data -> data.getServiceId() != testRegistedServiceId).collect(Collectors.toList())
        .get(0);

    routingDTO.setServicePath(result.getServicePath());

    assertThrows(BizException.class, () -> {

      gwRoutingService.updateService(routingDTO, testRegistedServiceId);
    });
  }

  @Test
  @Order(7)
  @Rollback(false)
  public void API삭제_테스트() throws Exception {

    Long[] data = {testRegistedServiceId};

    assertEquals(1, gwRoutingService.deleteService(data));
  }
}
