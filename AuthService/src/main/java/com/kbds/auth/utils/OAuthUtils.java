package com.kbds.auth.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * 
 *
 * <pre>
 *  Class Name     : OAuthUtils.java
 *  Description    : OAuth 관련 공통 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-20    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class OAuthUtils {

  /**
   * Token MD5 복호화
   * 
   * @param value
   * @return
   */
  public static String extractTokenKey(String value) {

    if (value == null) {

      return null;
    } else {

      MessageDigest digest;

      try {

        digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      } catch (NoSuchAlgorithmException var5) {

        throw new IllegalStateException(
            "MD5 algorithm not available.  Fatal (should be in the JDK).");
      }

      try {

        byte[] e = digest.digest(value.getBytes(StandardCharsets.UTF_8.name()));
        return String.format("%032x", new Object[] {new BigInteger(1, e)});
      } catch (UnsupportedEncodingException var4) {

        throw new IllegalStateException(
            "UTF-8 encoding not available.  Fatal (should be in the JDK).");
      }
    }
  }

}
