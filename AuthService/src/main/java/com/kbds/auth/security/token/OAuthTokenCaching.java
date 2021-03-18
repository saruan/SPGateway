package com.kbds.auth.security.token;

import java.util.concurrent.TimeUnit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class OAuthTokenCaching {

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  @Pointcut("execution(* org.springframework.security.oauth2.provider.client.JdbcClientDetailsService.loadClientByClientId(..))")
  public void loadClientByClientId() {
  }

  @Pointcut("execution(* com.kbds.auth.security.token.CustomTokenStore.readAuthentication(..))")
  public void readAuthentication() {
  }

  @Pointcut("execution(* com.kbds.auth.security.token.CustomTokenStore.readAccessToken(..))")
  public void readAccessToken() {
  }

  @Pointcut("execution(* com.kbds.auth.security.token.CustomTokenStore.removeAccessToken(..))")
  public void removeAccessToken() {
  }


  @Around("loadClientByClientId() || readAuthentication() || readAccessToken()")
  public Object readHandler(ProceedingJoinPoint pjp) throws Throwable {

    long expireMinutes = 5;
    Object[] args = pjp.getArgs();
    String key = generateCacheKey(args);

    return getOrProgress(pjp, key, expireMinutes);
  }

  @Before("removeAccessToken()")
  public void removeAccessTokenHandler(JoinPoint pjp) {

    Object[] args = pjp.getArgs();
    String key = generateCacheKey(args);

    redisTemplate.delete(key);
  }

  /**
   * get or set cache for oauth2 data
   *
   * @param pjp           ProceedingJoinPoint
   * @param key           Redis Key
   * @param expireMinutes expire minute
   * @return cache or method call data
   * @throws Throwable Exception
   */
  private Object getOrProgress(ProceedingJoinPoint pjp, String key, long expireMinutes)
      throws Throwable {

    Object value;
    Object cacheValue = redisTemplate.opsForValue().get(key);

    if (cacheValue == null) {

      value = pjp.proceed();

      if (value != null) {

        redisTemplate.opsForValue().set(key, value, expireMinutes, TimeUnit.MINUTES);
      }

    } else {

      value = cacheValue;
    }

    return value;
  }

  /**
   * Redis Key Generator
   *
   * @param arguments parameters
   * @return Key Value
   */
  private String generateCacheKey(Object[] arguments) {

    StringBuilder keySb = new StringBuilder();

    if ((arguments != null) && (arguments.length != 0)) {
      for (Object arg : arguments) {
        if (arg instanceof String) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Integer) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Double) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Float) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Boolean) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Long) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Byte) {
          keySb.append("_").append(arg.toString().toUpperCase());
        } else if (arg instanceof Short) {
          keySb.append("_").append(arg.toString().toUpperCase());
        }
      }
    }

    return keySb.toString();
  }
}