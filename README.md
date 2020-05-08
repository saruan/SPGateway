# SPGateway

## 1. 프로젝트 구성

     1.1 RoutingService - G/W Routing Service, Service Filter를 관리하는 API 서버  
     1.2 Gateway    - Spring Cloud Gateway 기반 API Gateway 서버      

## 2. 프로젝트 구성도

<img src ="https://user-images.githubusercontent.com/6766147/81356556-69ddcc80-910c-11ea-85d5-735fd12b8d71.png">

    2.1 AuthSerice - 인증(OAuth 2.0 , JWT 토큰을 관리, 인증 처리를 수행하는 서버
    2.2 APIService - N개의 마이크로 서비스
