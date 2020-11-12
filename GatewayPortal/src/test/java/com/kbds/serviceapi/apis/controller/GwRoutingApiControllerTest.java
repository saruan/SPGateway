package com.kbds.serviceapi.apis.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kbds.serviceapi.apis.code.ServiceAuthType;
import com.kbds.serviceapi.apis.code.ServiceLoginType;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.setting.DefaultTestConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

/**
 * <pre>
 *  File  Name     : GwRoutingControllerTest
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-10         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */

public class GwRoutingApiControllerTest extends DefaultTestConfig {

  @Test
  void 서비스_조회() throws Exception{

    RoutingDTO routingDTO = RoutingDTO.builder()
        .serviceId(41L)
        .filterId(1L)
        .serviceNm("필터 서비스")
        .servicePath("/api/filter")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("필터 Service")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .useYn("Y")
        .filterBean("CommonFilter")
        .filterUseYn("Y")
        .regUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    List<RoutingDTO> routingListDTO = new ArrayList<>();
    routingListDTO.add(routingDTO);

    SearchDTO searchDTO = SearchDTO.builder().name("필터").servicePath("/api/filter").build();

    when(gwRoutingService.findServices(searchDTO)).thenReturn(routingListDTO);

    mockMvc.perform(get("/api/service/v1.0/routes/")
          .contentType(MediaType.APPLICATION_JSON)
          .param("name", "필터")
          .param("servicePath", "/api/filter")
          .accept(MediaType.APPLICATION_JSON))
          .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            requestParameters(
                parameterWithName("name").description("서비스 이름").optional(),
                parameterWithName("servicePath").description("G/W 경로").optional()),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.[].serviceId").description("서비스 ID"),
                fieldWithPath("resultData.[].filterId").description("필터 ID"),
                fieldWithPath("resultData.[].serviceNm").description("서비스 이름"),
                fieldWithPath("resultData.[].serviceLoginType").description("로그인 타입"),
                fieldWithPath("resultData.[].serviceAuthType").description("인증 타입"),
                fieldWithPath("resultData.[].useYn").description("사용 유무"),
                fieldWithPath("resultData.[].filterUseYn").description("필터 사용 유무"),
                fieldWithPath("resultData.[].regUserNo").description("등록자"),
                fieldWithPath("resultData.[].regDt").description("등록일"),
                fieldWithPath("resultData.[].uptDt").description("수정일"),
                fieldWithPath("resultData.[].serviceTargetUrl").description("API 서버 주소"),
                fieldWithPath("resultData.[].filterBean").description("Filter Bean"),
                fieldWithPath("resultData.[].serviceDesc").description("서비스 설명"),
                fieldWithPath("resultData.[].servicePath").description("G/W 경로"))
        ));
  }

  @Test
  void 서비스_상세_조회() throws Exception{

    RoutingDTO routingDTO = RoutingDTO.builder()
        .serviceId(41L)
        .filterId(1L)
        .serviceNm("필터 서비스")
        .servicePath("/api/filter")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("필터 Service")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .useYn("Y")
        .filterBean("CommonFilter")
        .filterUseYn("Y")
        .regUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    Long serviceId = 41L;

    when(gwRoutingService.findServiceDetail(serviceId)).thenReturn(routingDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/service/v1.0/routes/{id}", serviceId)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("서비스 ID")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.serviceId").description("서비스 ID"),
                fieldWithPath("resultData.filterId").description("필터 ID"),
                fieldWithPath("resultData.serviceNm").description("서비스 이름"),
                fieldWithPath("resultData.serviceLoginType").description("로그인 타입"),
                fieldWithPath("resultData.serviceAuthType").description("인증 타입"),
                fieldWithPath("resultData.useYn").description("사용 유무"),
                fieldWithPath("resultData.filterUseYn").description("필터 사용 유무"),
                fieldWithPath("resultData.regUserNo").description("등록자"),
                fieldWithPath("resultData.regDt").description("등록일"),
                fieldWithPath("resultData.uptDt").description("수정일"),
                fieldWithPath("resultData.serviceTargetUrl").description("API 서버 주소"),
                fieldWithPath("resultData.filterBean").description("Filter Bean"),
                fieldWithPath("resultData.serviceDesc").description("서비스 설명"),
                fieldWithPath("resultData.servicePath").description("G/W 경로"))
        ));
  }

  @Test
  void 서비스_신규_등록() throws Exception{

    RoutingDTO routingDTO = RoutingDTO.builder()
        .filterId(1L)
        .serviceNm("신규 API 서비스")
        .servicePath("/api/test")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("신규 등록 API 서비스")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(routingDTO);
    doNothing().when(gwRoutingService).registerService(routingDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1.0/routes/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andDo(restDocumentationResultHandler.document(
            requestFields(
                fieldWithPath("serviceNm").description("서비스명"),
                fieldWithPath("servicePath").description("G/W Proxy URL"),
                fieldWithPath("serviceTargetUrl").description("API URL"),
                fieldWithPath("serviceDesc").description("서비스 설명"),
                fieldWithPath("serviceLoginType").description("로그인 타입"),
                fieldWithPath("serviceAuthType").description("인증 타입"),
                fieldWithPath("filterId").description("필터 ID"),
                fieldWithPath("regUserNo").description("등록 사용자 번호")
                ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("등록 유무")
            )
        ));
  }

  @Test
  void 서비스_수정() throws Exception{

    RoutingDTO routingDTO = RoutingDTO.builder()
        .filterId(1L)
        .serviceNm("수정 API 서비스")
        .servicePath("/api/modify")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("수정 등록 API 서비스")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .uptUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(routingDTO);
    Long serviceId = 41L;

    doNothing().when(gwRoutingService).updateService(routingDTO, serviceId);

    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/service/v1.0/routes/{id}", serviceId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("서비스 ID")
            ),
            requestFields(
                fieldWithPath("serviceNm").description("서비스명"),
                fieldWithPath("servicePath").description("G/W Proxy URL"),
                fieldWithPath("serviceTargetUrl").description("API URL"),
                fieldWithPath("serviceDesc").description("서비스 설명"),
                fieldWithPath("serviceLoginType").description("로그인 타입"),
                fieldWithPath("serviceAuthType").description("인증 타입"),
                fieldWithPath("filterId").description("필터 ID"),
                fieldWithPath("uptUserNo").description("수정 사용자 번호")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("수정 유무")
            )
        ));
  }

  @Test
  void 서비스_삭제() throws Exception{

    Long serviceId = 41L;

    when(gwRoutingService.deleteService(serviceId)).thenReturn(1L);

    mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/service/v1.0/routes/{id}", 41L)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("서비스 ID")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("삭제된 건수")
            )
        ));
  }

  @Test
  void 서비스_등록_필수파라미터_누락() throws Exception{

    // 서비스 이름 누락
    RoutingDTO routingDTO = RoutingDTO.builder()
        .filterId(1L)
        .servicePath("/api/test")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("신규 등록 API 서비스")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(routingDTO);
    doNothing().when(gwRoutingService).registerService(routingDTO);

    ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1.0/routes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }

  @Test
  void 서비스_수정_필수파라미터_누락() throws Exception{

    // G/W Proxy Pass 누락
    RoutingDTO routingDTO = RoutingDTO.builder()
        .filterId(1L)
        .serviceNm("수정 API")
        .serviceTargetUrl("http://localhost:8080/api/service/v1.0/filter/")
        .serviceDesc("신규 등록 API 서비스")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC)
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(routingDTO);
    doNothing().when(gwRoutingService).registerService(routingDTO);

    ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1.0/routes/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }
}
