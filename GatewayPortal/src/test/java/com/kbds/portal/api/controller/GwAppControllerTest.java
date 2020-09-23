package com.kbds.portal.api.controller;

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

import com.kbds.portal.apis.dto.AppDTO;
import com.kbds.portal.common.code.BizExceptionCode;
import com.kbds.portal.framework.dto.SearchDTO;
import com.kbds.portal.setting.DefaultTestConfig;
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
public class GwAppControllerTest extends DefaultTestConfig {

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

    when(gwAppService.findAppDetail(1L)).thenReturn(appDTO);

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
                fieldWithPath("resultData.useYn").description("사용 유무"),
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
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM002.name()));
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
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM002.name()));
  }
}
