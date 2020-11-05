package com.kbds.serviceapi.apis.code.converter;

import com.kbds.serviceapi.apis.code.ServiceAuthType;
import com.kbds.serviceapi.apis.code.ServiceLoginType;
import com.kbds.serviceapi.apis.code.core.AbstractCodeConverter;
import javax.persistence.Converter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  File  Name     : ServiceAuthTypeConverter
 *  Description    : API Auth Type Converter
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-25         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Converter
@NoArgsConstructor
public class ServiceAuthTypeConverter extends AbstractCodeConverter<ServiceAuthType> {

}
