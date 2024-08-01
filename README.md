# 상품주문 프로그램 

상품주문 프로그램 입니다.
개발환경은 intellij 2024.1 버전에서 진행하였으며 
코틀린, 스프링부트, h2로 구성되어 있습니다. 
각 사용 언어 및 툴 버전은 다음과 같습니다.
- kotlin : 1.9.24
- springboot : 3.3.1
- java : 17

## 프로젝트 구조 
- 루트 
    ㄴ application
        ㄴ service
    ㄴ config 
        ㄴ database 
    ㄴ domain
    ㄴ enums
    ㄴ exception
    ㄴ interfaces 
        ㄴ dto 
        ㄴ runner 
    ㄴ repository 

도메인 모델을 중심으로 하여 각 계층을 명확히 나누고자 하였습니다. 
application에서는 비즈니스 로직 관련 클래스를, interfaces는 외부와의 인터페이스 관련 클래스로 분리 및 모듈화
테스트를 보다 간편하게 구성하도록 하였습니다. 
이후 새로운 비즈니스 로직 및 API등이 추가되어도 가능하도록 구성하고 싶었습니다.

## 프로젝트 요구사항 및 실행 순서
- 코드 단위의 프로젝트 흐름은 각 코드별 주석으로도 확인 가능합니다. 
- Junit으로 구성된 단위테스트는 OrderServiceTest 클래스에서 각각 테스트 할 수 있습니다. 

1. csv 데이터를 사용하여 h2 database에 적재 
   - csv 데이터는 resources/items.csv에 위치합니다
   - 프로그램 실행과 동시에 h2 database에 resources/schema.sql이 수행되어 테이블이 생성됩니다 
   - DatabaseInitializer 컴포넌트가 빈에 올라가고, items.csv의 데이터들을 테이블에 insert 합니다.
2. 콘솔로 주문데이터를 한번에 입력 받음 
   - 모든 컴포넌트가 빈처리 된 후 콘솔 system in 클래스가 실행됩니다. 
   - 특정 키를 입력 시 주문데이터는 유효성 검사 후 재고를 수정하고, 주문금액 등을 계산합니다. 
     - 결재 후에 수량이 수정되는 것이 일반적이나, 해당 프로젝트에서는 주문데이터가 유효성에 통과되면 바로 주문이 완료된다는 가정하에 수량을 수정합니다.
3. Exception 처리 
    - 예외상황이 throw되면 호출한 runner의 try catch구문에서 각 오류별 메시지가 추가됩니다
    - 주문데이터들은 space + enter가 입력되기 전의 여러건을 한 트랜잭션으로 묶어, 하나의 주문데이터에 ㄷxception이 발생할 경우 해당 주문데이터들은 처리되지 않습니다. 

