# SPGateway

## 1. 프로젝트 구성

     - RoutingService - G/W Routing Service, Service Filter를 관리하는 API 서버  
     - Gateway        - Spring Cloud Gateway 기반 API Gateway 서버
     - AuthService    - JWT, OAuth를 사용하는 인증 서버
     - APIService     - 실제 EndPoint API 서버
     - GatewayConfig  - Spring-Cloud-Config-Bus 연동 서버
     - Properties     - Properties 관리 

## 2. 프로젝트 구성도
