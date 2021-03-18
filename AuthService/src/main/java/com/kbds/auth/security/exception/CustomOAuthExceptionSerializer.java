package com.kbds.auth.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.utils.StringUtils;
import java.io.IOException;
import org.apache.commons.lang3.EnumUtils;

/**
 * <pre>
 *  Class Name     : CustomOAuthExceptionSerializer.java
 *  Description    : OAuth 관련 인증 결과 양식
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-21    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public class CustomOAuthExceptionSerializer extends StdSerializer<CustomOAuthException> {

  private static final long serialVersionUID = -3652116394491586496L;

  public CustomOAuthExceptionSerializer() {
    super(CustomOAuthException.class);
  }

  @Override
  public void serialize(CustomOAuthException value, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {

    jsonGenerator.writeStartObject();
    jsonGenerator.writeObjectField("resultCode", value.getMessage());

    if (EnumUtils.isValidEnum(BizExceptionCode.class, value.getMessage())) {

      jsonGenerator.writeObjectField("resultMessage",
          BizExceptionCode.valueOf(value.getMessage()).getDesc());
    }

    if (!StringUtils.isEmptyParams(value.getArg())) {

      jsonGenerator.writeObjectField("authCode", value.getArg());
    }

    jsonGenerator.writeEndObject();
  }
}
