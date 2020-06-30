# SPGateway

## 1. 프로젝트 구성

     - RoutingService - G/W Routing Service, Service Filter를 관리하는 API 서버  
     - Gateway        - Spring Cloud Gateway 기반 API Gateway 서버
     - AuthService    - JWT, OAuth를 사용하는 인증 서버
     - GatewayLog     - Log 수집용 서버
     - GatewayConfig  - Spring-Cloud-Config-Bus 연동 서버
     - Properties     - Properties 관리 

## 2. 프로젝트 구성도
<img src = "https://user-images.githubusercontent.com/6766147/86095971-9f3fde80-baed-11ea-9bb5-7e8af3e6864a.PNG"/>

## 3. Gateway
※ 개발 환경
  - 언어: JAVA 1.8
  - Framework: Spring Framework 5.0
  - 기타 : FeignClient, Kafka 
    
※ 주요 기능
  - 인증 처리 ( AuthService와 연계 )
  - SAML, AccessToken 검증 및 발급
  - APP-Key 검증
  - API 관리 및 Routing 기능 수행

※ 기타
  - DMZ에 위치할 수 있도록 기능을 분산
    (라우팅, 인증, 로깅 서비스는 Internal 영역에 위치)

## 4. Routing Service
※ 개발 환경
  - 언어: JAVA 1.8
  - Framework: Spring Boot 2.2.7
  - Database: MySQL
  - 기타: FeignClient, Spring-Data-JPA, QueryDSL, Spring Cloud Config Bus 
    
※ 주요 기능
  - API 관리
  - APP 관리
  - Filter 관리

## 4. AuthService
※ 개발 환경
  - 언어: JAVA 1.8
  - Framework: Spring Boot 2.2.7
  - Database: MySQL
  - 기타: Spring-Data-JPA, QueryDSL, Spring OAuth 2.0, JWT
    
※ 주요 기능
  - SAML, AccessToken 발급 및 검증
  - SAML 공유를 위한 GatewayCluster 관리
  - OAuth GrantType은 기본적으로 Spring OAuth에서 지원하는 아래의 속성을 지원
    (패스워드 인증, 클라이언트 기반 인증, Refresh 토큰 + @)
  
※ 기타
  - Spring @AuthorizationServer가 Webflux를 미 지원
  - 현재 시스템 구성이 Gateway만을 위한 인증 시스템으로 
     인증 서버를 사용할 사용자, 권한 관련 REST API는 불 필요하다 판단하여 미 구현 

## 5. GatewayLog
※ 개발 환경
  - 언어: Kotlin, Java 1.8
  - Framework: Spring Boot 2.3.0
  - Database: MySQL -> MongoDB (전환 중)
  - 기타 사용 라이브러리: JPA, Kafka
    
※ 주요 기능
  - Message Queue에서 로그 정보를 구독하여 DB에 저장
  - 통계 데이터 배치 처리 (TO-BE)
   

 

