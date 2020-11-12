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

import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
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
 *  File  Name     : GwFilterControllerTest
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-13          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class GwFilterApiControllerTest extends DefaultTestConfig {

  @Test
  void 필터_조회() throws Exception{

    FilterDTO filterDTO = FilterDTO.builder()
        .filterId(1L)
        .filterNm("필터 정보")
        .filterBean("CommonTemplate")
        .filterDesc("기본 필터")
        .useYn("Y")
        .regUserNo("1")
        .uptUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    List<FilterDTO> filterDTOS = new ArrayList<>();
    filterDTOS.add(filterDTO);

    SearchDTO searchDTO = SearchDTO.builder().name("필터").build();

    when(gwFilterService.findFilters(searchDTO)).thenReturn(filterDTOS);

    mockMvc.perform(get("/api/service/v1.0/filter/")
        .contentType(MediaType.APPLICATION_JSON)
        .param("name", "필터")
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            requestParameters(
                parameterWithName("name").description("필터 이름").optional()
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.[].filterId").description("필터 ID"),
                fieldWithPath("resultData.[].filterNm").description("필터 이름"),
                fieldWithPath("resultData.[].filterBean").description("필터 @Service 명칭"),
                fieldWithPath("resultData.[].filterDesc").description("필터 설명"),
                fieldWithPath("resultData.[].useYn").description("사용 유무"),
                fieldWithPath("resultData.[].regUserNo").description("등록자"),
                fieldWithPath("resultData.[].uptUserNo").description("수정자"),
                fieldWithPath("resultData.[].regDt").description("등록일"),
                fieldWithPath("resultData.[].uptDt").description("수정일"))
        ));
  }

  @Test
  void 필터_상세_조회() throws Exception{

    FilterDTO filterDTO = FilterDTO.builder()
        .filterId(1L)
        .filterNm("필터 정보")
        .filterBean("CommonTemplate")
        .filterDesc("기본 필터")
        .useYn("Y")
        .regUserNo("1")
        .uptUserNo("1")
        .regDt(new Date())
        .uptDt(new Date())
        .build();

    when(gwFilterService.findFilterDetail(1L)).thenReturn(filterDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/service/v1.0/filter/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("필터 ID")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData.filterId").description("필터 ID"),
                fieldWithPath("resultData.filterNm").description("필터 이름"),
                fieldWithPath("resultData.filterBean").description("필터 @Service 명칭"),
                fieldWithPath("resultData.filterDesc").description("필터 설명"),
                fieldWithPath("resultData.useYn").description("사용 유무"),
                fieldWithPath("resultData.regUserNo").description("등록자"),
                fieldWithPath("resultData.uptUserNo").description("수정자"),
                fieldWithPath("resultData.regDt").description("등록일"),
                fieldWithPath("resultData.uptDt").description("수정일"))
        ));
  }

  @Test
  void 필터_신규_등록() throws Exception{

    FilterDTO filterDTO = FilterDTO.builder()
        .filterNm("필터 정보")
        .filterBean("CommonTemplate")
        .filterDesc("기본 필터")
        .useYn("Y")
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(filterDTO);
    doNothing().when(gwFilterService).registerFilter(filterDTO);

    mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1.0/filter/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andDo(restDocumentationResultHandler.document(
            requestFields(
                fieldWithPath("filterNm").description("필터명"),
                fieldWithPath("filterBean").description("필터 @Service 이름"),
                fieldWithPath("filterDesc").description("필터 설명"),
                fieldWithPath("useYn").description("사용 유무"),
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
  void 필터_수정() throws Exception{

    FilterDTO filterDTO = FilterDTO.builder()
        .filterNm("필터 수정")
        .filterBean("ModifyTemplate")
        .filterDesc("수정 필터")
        .useYn("Y")
        .uptUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(filterDTO);
    doNothing().when(gwFilterService).updateFilter(filterDTO, 1L);

    mockMvc.perform(RestDocumentationRequestBuilders.put("/api/service/v1.0/filter/{id}", 1L)
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
                fieldWithPath("filterNm").description("필터명"),
                fieldWithPath("filterBean").description("필터 @Service 이름"),
                fieldWithPath("filterDesc").description("필터 설명"),
                fieldWithPath("useYn").description("사용 유무"),
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
  void 필터_삭제() throws Exception{

    Long filterId = 41L;

    doNothing().when(gwFilterService).deleteFilter(filterId);

    mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/service/v1.0/filter/{id}", 41L)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(restDocumentationResultHandler.document(
            pathParameters(
                parameterWithName("id").description("필터 ID")
            ),
            responseFields(
                fieldWithPath("resultCode").description("결과 코드"),
                fieldWithPath("resultMessage").description("결과 메시지"),
                fieldWithPath("resultData").description("삭제여부")
            )
        ));
  }

  @Test
  void 필터_신규_등록_파라미터_누락() throws Exception{

    // 필터명 누락
    FilterDTO filterDTO = FilterDTO.builder()
        .filterBean("CommonTemplate")
        .filterDesc("기본 필터")
        .useYn("Y")
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(filterDTO);
    doNothing().when(gwFilterService).registerFilter(filterDTO);

    ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/service/v1.0/filter/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }

  @Test
  void 필터_수정_파라미터_누락() throws Exception{

    // 필터명 누락
    FilterDTO filterDTO = FilterDTO.builder()
        .filterBean("CommonTemplate")
        .filterDesc("기본 필터")
        .useYn("Y")
        .regUserNo("1")
        .build();

    String requestBody = objectMapper.writeValueAsString(filterDTO);
    doNothing().when(gwFilterService).updateFilter(filterDTO, 1L);

    ResultActions resultActions = mockMvc
        .perform(RestDocumentationRequestBuilders
            .put("/api/service/v1.0/filter/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    resultActions
        .andExpect(jsonPath("$.resultCode").value(BizExceptionCode.COM005.name()));
  }

}
