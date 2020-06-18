package com.kbds.serviceapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.service.GwRoutingService;
import com.kbds.serviceapi.framework.exception.BizException;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
public class RoutingTest extends ApiServiceApplicationTests {

  @Autowired
  GwRoutingService gwRoutingService;

  @Test
  @Rollback(true)
  public void API등록_및_중복_데이터_테스트() throws Exception {

    final RoutingDTO routingDTO = RoutingDTO.builder().serviceNm("Routing Mocking")
        .servicePath("/api/mock/").serviceTargetUrl("http://localhost:8080/")
        .serviceDesc("Routing Mocking Test").serviceLoginType("1").serviceAuthType("1")
        .filterId(new Long(1)).regUserNo("1").build();


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

    // 이미 등록이 되어 있는 경우 오류 확인
    Assertions.assertThrows(BizException.class, () -> {

      gwRoutingService.registService(routingDTO);
    });
  }

  @Test
  @Rollback(true)
  public void API등록_테스트_필수_파라미터_누락() throws Exception {

    // 서비스 Path 누락
    Assertions.assertThrows(BizException.class, () -> {

      final RoutingDTO routingDTO = RoutingDTO.builder().serviceNm("Routing Mocking")
          .serviceTargetUrl("http://localhost:8080/").serviceDesc("Routing Mocking Test")
          .serviceLoginType("1").serviceAuthType("1").filterId(new Long(1)).regUserNo("1").build();

      gwRoutingService.registService(routingDTO);
    });

    // 서비스 Target URL 누락
    Assertions.assertThrows(BizException.class, () -> {

      final RoutingDTO routingDTO = RoutingDTO.builder().serviceNm("Routing Mocking")
          .servicePath("/api/mock/").serviceDesc("Routing Mocking Test").serviceLoginType("1")
          .serviceAuthType("1").filterId(new Long(1)).regUserNo("1").build();

      gwRoutingService.registService(routingDTO);
    });
  }
}
