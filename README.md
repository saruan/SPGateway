# SPGateway

## 1. 프로젝트 구성

※ 서버 구성

    - GatewayPortal  : Gateway 데이타 관리 서버
    - gateway-portal-frontend : 포탈 화면 프로젝트  
    - Gateway        : API Gateway 서버
    - AuthService    : 인증, 사용자, 권한 관리 서버
    - GatewayLog     : 로그, 통계 관리 서버 (구성중)
    - GatewayConfig  : Spring-Cloud-Config-Bus Server
    - ETC            : Redis, RabbitMQ, MySQL 
     
※ 개발 환경

    - 백엔드 서버 : Java 1.8, Spring Boot, MySQL, RabbitMQ, Redis
    - 프론트 서버 : React, Typescript     

## 2. 프로젝트 구성도

<img src = "https://user-images.githubusercontent.com/6766147/99205879-2b347780-27fd-11eb-9ef1-0dd33c672b0a.png"/>

## 3. 설치 가이드 

  ##### 아래 설치 가이드는 git 저장소를 변경하는 신규 환경에서의 설치 가이드
  
    MySQL, RabbitMQ, Redis, npm 설치 필요

  ### 설치 순서

  ##### GatewayConfig 서버 설치 
  
    - application.yml 내부의 git 서버 uri, 사용자 정보, rabbitmq 정보를 환경에 맞게 변경한다.
    - Properties 프로젝트 경로를 search-paths 에 저장한다.
    
  ##### DB 환경 구성
  
    - GatewayConfig/schema 폴더에 있는 create.sql 을 실행하여 Database, Table 를 생성한다.
    - DB 서버도 분리를 한다면 해당 파일의 항목별 스크립트를 추출하여 별도로 생성한다.  
    
  ##### GatewayConfig/{spring.profiles.active} 경로의 각 yml 파일들의 속성들을 변경해준다.
  
    - datasource 의 정보를 3번에서 생성한 url, 계정 정보로 변경한다.
    - rabbitmq, redis 정보를 설치한 정보로 변경한다. 
   
  ##### AuthService, GatewayPortal, Gateway, gateway-portal-frontend 프로젝트를 설치한다.
  
    - 최초 구동 순서는 GatewayConfig -> AuthService -> GatewayPortal -> Gateway -> gateway-portal-frontend 
   
  ##### gateway-portal-frontend 의 루트 URL 로 접속할 경우 포탈 접속이 가능하다.
  
    - 기본 비밀번호는 admin / password 로 필요 시 수정

## 4. 사용 가이드

  ##### API/APP 관리
  
    - http://GatewayPortal_IP:PORT/docs/restdoc.html 페이지의 RestAPI 규격서를 확인 후 API를 호출한다.
    - http://gateway-portal-frontend_IP:3000/ 을 접속한 후 메뉴의 API/APP을 관리한다.
    
  ##### 필터 관리
   
    - Gateway 프로젝트에 생성하되 별도 제약은 없지만 com.kbds.gateway.filter.custom에 생성하는 것을 권고하며,
      Spring Application Context로 필터를 등록하게 되어 있어 @Service Annotation을 붙여야 한다.
    - 필터 생성 후에는 반드시 GatewayPortal에 필터 정보를 등록 한 후에 API에서 해당 필터를 적용할 수 있다.
    
  ##### 토큰 관리
  
    - 인증 서버의 oauth_client_details에 초기 클라이언트가 등록 되어 있고 id, secret 기반으로 access_token 발급이 가능하다. 
      Oauth2.0에서 제공하는 grant_type은 지원하나 현재 사용하는 필터에서는 password 타입을 사용한다.
    - 토큰 발급 시 현재 인증에 대한 허가를 위한 검증을 써드파티 어플리케이션에서 처리하는 2단계 인증, Gateway 사용자 정보로,
      acceess_token르 발급하는 1단계 인증이 있다.
      
      2단계 인증의 경우 아래의 프로세스로 구성되어 있다.
      
      1) http://GATEWAY_IP:PORT/gateway/auth/jwt를 호출하여 JWT 토큰을 전달 받는다. 
        - 현재 써드파티 API의 인증 프로세스의 기준은 써드파티 어플리케이션/서버에서 사용자 확인/인증을 선행으로,
          진행했다는 것을 기준으로 구성되어 있다. 따라서 TokenFilter에는 어플리케이션 서버와 통신하여 사용자가 인증이 된 것을
          확인하여야 하며 기본은 별다른 제약 없이 바로 JWT 토큰을 리턴한다.
      2) http://GATEWAY_IP:PORT/gateway/oauth/token 을 호출하여 access_token을 전달 받는다. 
        (1단계에서 발급 받은 JWT토큰을 검증한 후 access_token 발급)
          
      해당 내용이 프로젝트 환경에 따라 수정이 필요할 경우 TokenFilter, CommonFilter 에서 발급 프로세스를 환경에 맞춰 
      수정/생성해야 한다. (com.kbds.gateway.filter.system.TokenFilter)
