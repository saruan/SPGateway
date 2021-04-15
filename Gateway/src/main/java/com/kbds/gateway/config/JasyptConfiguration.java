package com.kbds.gateway.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  Class Name     : JasyptConfiguration.java
 *  Description    : Properties 암호화 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-08             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
public class JasyptConfiguration {

  final static String KEY = "S0JEU19HQVRFV0FZ";
  final static String ALGORITHM = "PBEWithMD5AndDES";

  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {

    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(KEY);
    config.setAlgorithm(ALGORITHM);
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }
}