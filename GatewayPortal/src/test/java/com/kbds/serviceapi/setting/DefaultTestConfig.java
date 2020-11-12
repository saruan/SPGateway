package com.kbds.serviceapi.setting;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.serviceapi.apis.controller.GwAppApiController;
import com.kbds.serviceapi.apis.controller.GwFilterApiController;
import com.kbds.serviceapi.apis.controller.GwRoutingApiController;
import com.kbds.serviceapi.portal.app.service.GwAppService;
import com.kbds.serviceapi.portal.filter.service.GwFilterService;
import com.kbds.serviceapi.portal.api.service.GwRoutingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * <pre>
 *  File  Name     : DefaultTestConfig
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-11          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {GwRoutingApiController.class,
                           GwFilterApiController.class,
                           GwAppApiController.class})
@ActiveProfiles(profiles = "dev")
public class DefaultTestConfig {

  protected MockMvc mockMvc;
  protected RestDocumentationResultHandler restDocumentationResultHandler;

  @MockBean
  protected GwRoutingService gwRoutingService;

  @MockBean
  protected GwFilterService gwFilterService;

  @MockBean
  protected GwAppService gwAppService;

  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {

    this.restDocumentationResultHandler = document(
        "{class-name}/{method-name}",
        preprocessResponse(prettyPrint())
    );

    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation)
            .uris().withScheme("http").withHost("127.0.0.1").withPort(8080))
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .alwaysDo(restDocumentationResultHandler)
        .build();
  }
}
