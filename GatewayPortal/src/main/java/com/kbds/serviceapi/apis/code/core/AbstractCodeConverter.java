package com.kbds.serviceapi.apis.code.core;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.exception.BizException;
import java.lang.reflect.ParameterizedType;
import java.util.EnumSet;
import javax.persistence.AttributeConverter;

/**
 * <pre>
 *  File  Name     : AbstractCodeConverter
 *  Description    : 공통 Enum 코드 컨버터
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-05         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class AbstractCodeConverter<T extends Enum<T> & AbstractCode> implements
    AttributeConverter<T, String> {

  private final Class<T> enumClass;

  /**
   * 생정자에서 enumClass 초기화 시킨다.
   */
  public AbstractCodeConverter() {

    ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();

    this.enumClass = (Class<T>) type.getActualTypeArguments()[0];
  }

  @Override
  public String convertToDatabaseColumn(T attribute) {

    return attribute.getCode();
  }

  @Override
  public T convertToEntityAttribute(String dbData) {

    return EnumSet.allOf(enumClass).stream()
        .filter(f -> f.getCode().equals(dbData))
        .findAny()
        .orElseThrow(() -> new BizException(BizExceptionCode.COM008));
  }
}
