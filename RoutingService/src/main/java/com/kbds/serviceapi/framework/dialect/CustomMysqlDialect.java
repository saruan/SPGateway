package com.kbds.serviceapi.framework.dialect;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * <pre>
 *  Class Name     : CustomMysqlDialect.java
 *  Description    : Group_Concat을 사용하기 위한 Dialect
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-26    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class CustomMysqlDialect extends MySQL5Dialect {

  public CustomMysqlDialect() {

    super();
    // register custom/inner function here
    this.registerFunction("group_concat",
        new SQLFunctionTemplate(StandardBasicTypes.STRING, "group_concat(?1)"));
  }
}
