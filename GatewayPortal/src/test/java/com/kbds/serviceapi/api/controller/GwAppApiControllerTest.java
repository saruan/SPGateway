package com.kbds.serviceapi.api.controller;

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
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.app.dto.AppDetailDTO;
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
 *  File  Name     : GwAppControllerTest
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-17         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class GwAppApiControllerTest extends DefaultTestConfig {

  @Test
  void 앱_조회() throws Exception{

    AppDTO appDTO = AppDTO.builder()
        .appId(1L)
        .appNm("앱 서비스")
        .appKey("98kxl92BqOR9bI4nnfq7mePyP")
        .appDesc("기본 앱 서비스")
        .useYn("Y")
        .regUserNo("1")
        .uptUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    List<AppDTO> appDTOList = new ArrayList<>();
    appDTOList.add(appDTO);

    SearchDTO searchDTO = SearchDTO.builder().name("앱 서비스").build();

    when(gwAppService.findApps(searchDTO)).thenReturn(appDTOList);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/service/v1/app/")
        .contentType(MediaType.APPLICATION_JSON)
        .param("name", "앱 서비스")
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            requestParameters(
                parameterWithName("name").description("앱 이름").optional()
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.[].appId").description("앱 ID"),
                fieldWithPath("resultData.[].appNm").description("앱 이름"),
                fieldWithPath("resultData.[].appKey").description("앱키"),
                fieldWithPath("resultData.[].appDesc").description("앱 설명"),
                fieldWithPath("resultData.[].useYn").description("사용 유무"),
                fieldWithPath("resultData.[].regUserNo").description("등록자"),
                fieldWithPath("resultData.[].uptUserNo").description("수정자"),
                fieldWithPath("resultData.[].regDt").description("등록일"),
                fieldWithPath("resultData.[].uptDt").description("수정일"))
        ));
  }

  @Test
  void 앱_상세_조회() throws Exception{

    List<RoutingDTO> routingDTOS = new ArrayList<>();

    RoutingDTO routingDTO = RoutingDTO.builder()
        .serviceId(41L)
        .filterId(1L)
        .serviceNm("필터 서비스")
        .servicePath("/api/filter")
        .serviceTargetUrl("http://localhost:8080/api/service/v1/filter/")
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

    routingDTOS.add(routingDTO);

    AppDetailDTO appDetailDTO = AppDetailDTO.builder()
        .appId(1L)
        .appNm("앱 서비스")
        .serviceId(routingDTOS)
        .appDesc("기본 앱 서비스")
        .regUserNo("1")
        .uptUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    when(gwAppService.findAppDetail(1L)).thenReturn(appDetailDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/service/v1/app/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("앱 ID").optional()
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.appId").description("앱 ID"),
                fieldWithPath("resultData.appNm").description("앱 이름"),
                fieldWithPath("resultData.appKey").description("앱키"),
                fieldWithPath("resultData.appDesc").description("앱 설명"),
                fieldWithPath("resultData.serviceId").description("서비스 목록"),

                fieldWithPath("resultData.serviceId[].serviceId").description("서비스 ID"),
                fieldWithPath("resultData.serviceId[].filterId").description("필터 ID"),
                fieldWithPath("resultData.serviceId[].serviceNm").description("서비스 이름"),
                fieldWithPath("resultData.serviceId[].serviceLoginType").description("로그인 타입"),
                fieldWithPath("resultData.serviceId[].serviceAuthType").description("인증 타입"),
                fieldWithPath("resultData.serviceId[].useYn").description("사용 유무"),
                fieldWithPath("resultData.serviceId[].filterUseYn").description("필터 사용 유무"),
                fieldWithPath("resultData.serviceId[].regUserNo").description("등록자"),
                fieldWithPath("resultData.serviceId[].regDt").description("등록일"),
                fieldWithPath("resultData.serviceId[].uptDt").description("수정일"),
                fieldWithPath("resultData.serviceId[].serviceTargetUrl").description("API 서버 주소"),
                fieldWithPath("resultData.serviceId[].filterBean").description("Filter Bean"),
                fieldWithPath("resultData.serviceId[].serviceDesc").description("서비스 설명"),
                fieldWithPath("resultData.serviceId[].servicePath").description("G/W 경로"),

                fieldWithPath("resultData.regUserNo").description("등록자"),
                fieldWithPath("resultData.uptUserNo").description("수정자"),
                fieldWithPath("resultData.regDt").description("등록일"),
                fieldWithPath("resultData.uptDt").description("수정일"))
        ));
  }

  @Test
  void 앱_등록() throws Exception{

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(1L);

    AppDTO appDTO = AppDTO.builder()
        .appNm("신규 앱 등록")
        .appDesc("기본 앱 서비스")
        .serviceId(serviceIdList)
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(appDTO);
    doNothing().when(gwAppService).registerApp(appDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1/app/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andDo(restDocumentationResultHandler.document(
            requestFields(
                fieldWithPath("appNm").description("앱 이름"),
                fieldWithPath("appDesc").description("앱 설명"),
                fieldWithPath("serviceId[]").description("서비스 ID 목록"),
                fieldWithPath("regUserNo").description("등록자")
           ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("등록 유무")
            )
        ));
  }

  @Test
  void 앱_수정() throws Exception{

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(1L);

    AppDTO appDTO = AppDTO.builder()
        .appNm("앱 수정")
        .appDesc("수정 앱 서비스")
        .useYn("Y")
        .serviceId(serviceIdList)
        .uptUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(appDTO);
    doNothing().when(gwAppService).updateApp(appDTO, 1L);

    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/service/v1/app/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("앱 ID")
            ),
            requestFields(
                fieldWithPath("appNm").description("앱 이름"),
                fieldWithPath("appDesc").description("앱 설명"),
                fieldWithPath("serviceId[]").description("서비스 ID 목록"),
                fieldWithPath("useYn").description("사용여부"),
                fieldWithPath("uptUserNo").description("수정자")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("등록 유무")
            )
        ));
  }

  @Test
  void 앱_삭제() throws Exception{

    Long appId = 1L;

    doNothing().when(gwAppService).deleteApp(appId);

    mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/service/v1/app/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("앱 ID")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("삭제 여부")
            )
        ));
  }

  @Test
  void 앱_등록_필수파라미터_누락() throws Exception{

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(1L);

    AppDTO appDTO = AppDTO.builder()
        .appDesc("기본 앱 서비스")
        .serviceId(serviceIdList)
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(appDTO);
    doNothing().when(gwAppService).registerApp(appDTO);

    ResultActions resultActions =
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1/app/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }

  @Test
  void 앱_수정_필수파라미터_누락() throws Exception{

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(1L);

    AppDTO appDTO = AppDTO.builder()
        .appDesc("수정 앱 서비스")
        .useYn("Y")
        .serviceId(serviceIdList)
        .uptUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(appDTO);
    doNothing().when(gwAppService).updateApp(appDTO, 1L);

    ResultActions resultActions =
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/service/v1/app/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }
}
