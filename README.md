# SPGateway

## 1. 프로젝트 구성

     - RoutingService - G/W Routing Service, Service Filter를 관리하는 API 서버  
     - Gateway        - Spring Cloud Gateway 기반 API Gateway 서버
     - AuthService    - JWT, OAuth를 사용하는 인증 서버
     - APIService     - 실제 EndPoint API 서버

## 2. 프로젝트 구성도

<img src ="https://user-images.githubusercontent.com/6766147/81356556-69ddcc80-910c-11ea-85d5-735fd12b8d71.png">

## 3. 업무 상세 설명 (작성 중)

    3.1 Gateway의 Routing/Filter 정보는 RoutingService에서 관리한다.
        Filter는 사용자가 별도로 사전/사후 작업으로 원하는 작업이 있을 경우 개발 가이드 양식에 맞춰 Gateway에서 개발하여야 한다.
        이후 RoutingService에서 해당 Filter를 등록 후 원하는 URL 정보에 해당 필터를 REST API로 등록/수정한다.
        별도 Filter를 사용하지 않을 경우 현재 기본 Request/Response 관련 Logging만 처리하는 CommonFilter를 사용한다.
