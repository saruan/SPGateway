package com.kbds.portal.framework.component;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * <pre>
 *  Class Name     : ConnectionFactory.java
 *  Description    : Log4j2 LogToDatabase Connection Factory 설정을 위한 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-24     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Component
public class ConnectionComponent {

  private static DataSource dataSource;

  @Autowired
  public void setDataSource(DataSource dataSource) {

    ConnectionComponent.dataSource = dataSource;
  }

  public static Connection getDatabaseConnection() throws Exception {
    return dataSource.getConnection();
  }
}
