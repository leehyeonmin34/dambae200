# 담배200 : 공동 편집과 권한 관리를 지원하는 담배 검수 앱
<div style="color:#808080"> 진행 기간 : 2022.7 ~ 2022.12 
<br/>
사용 기술 : Spring Boot, Spring Data JPA, MySQL, Redis, Stomp, Docker, nginx, Naver Cloud Platform 
<br/>
담당 부분 : 서비스 전체 (기획, 디자인, 개발) </div>
<br/>

편의점에서 담배 재고를 쉽게 검수하기 위해 제작된 웹 서비스입니다.


![A4 - 26 (2)](https://user-images.githubusercontent.com/66104031/218244917-db45b460-fd2a-4c0c-93c9-8150a3d9eff2.jpg)

# 핵심 기능
진열된 순서대로 우리 매장의 담배 갯수를 입력하고, ‘전산순으로 정렬하기’ 버튼을 누르면 미리 설정해 놓은 순서대로 정렬됩니다. 이를 포스기에서 출력된 재고 목록과 1:1 비교함으로써 재고 검수를 빠르게 해주는 앱입니다. 
<br />
<br />
담배 목록은 Google Sheet와 유사하게 매장 직원들끼리 공유가 가능하며, 다른 근무자와 실시간으로 공동 편집이 가능합니다 (소켓 통신 활용)
<br/><br/>
[🔗 UI & 작동 영상 + 기획 보러가기](http://shorturl.at/svKX8)
<br/>
[🔗 서비스 써보기](http://118.67.135.98/) <span style="color:#808080"> (회원가입이 필요합니다) </span>
<br />
<br />


### 제공 기능
- 담배 목록 생성/수정/삭제
- 목록 접근 권한 관리
- 담배 추가/수정/삭제
- 담배 순서 조정
- 담배 목록 내 검색
- 로그인, 비밀번호 찾기
- 알림 (Pageable 조회)
- 설정 (개인정보 수정)
- 업무 꿀팁 컨텐츠

<br />

#  프로젝트 구조
- github hook을 받아 Jenkins에서 CI/CD를 진행합니다.
- 젠킨스와 메인서버 2개로 작동중이며, 메인서버가 docker를 통해 MySQL, Redis, 앱서버를 구동중입니다.
- Naver Cloud Platform의 서버를 사용합니다.
- Blue-green 방식으로 무중단 배포됩니다.

<br />

# 프로젝트의 주요 관심사

### 성능 최적화
- 서버 부하를 줄이기 위해 캐싱 서버 적극 활용
- DB 서버와의 통신 최소화
  - DB 병목 지양을 위한 N + 1 쿼리 지양
  - DB 락 최소화를 위한 @Transational 최소화
  - 코드 수준의 동적 스케줄러로 캐시에서 DB로 Write-Back
  
### 서버 확장을 고려한 구조
- 세션 정보를 Http 세션이 아닌 DB와 캐시에 저장

### 소켓 통신
- 통신 방식에 종속되지 않는 서비스 코드
- MessageHandler
- 소켓 연결 해제 없이 비지니스 예외 처리
- Http에서 발생한 예외와 소켓통신 중 발생한 예외를 일관되게 처리

### 로직이 잘 보이고 유지보수하기 좋은 코드
- 인터셉터로 권한 검사
- 간편하게 캐시와 DB를 활용하는 모듈 제작 
- 트리구조의 런타임 예외 상속 적극 활용
- ControllerAdvice, ExceptionHandler 활용

### 실제 서비스 가능한 완성도
- 다양한 시나리오를 고려한 API와 UI
- Jenkins와 Docker를 이용한 자동 무중단 배포
  

<br />

# ERD
<img width="964" alt="스크린샷 2023-02-11 오후 2 34 12" src="https://user-images.githubusercontent.com/66104031/218242222-d24978f9-f91e-4909-98b5-8f1ca3278fa3.png">

<br />

# 프론트
프론트는 다양한 해상도에서 일관되게 작동되도록 구현되었습니다.

![Frame 1026](https://user-images.githubusercontent.com/66104031/218243078-ff6ce815-1281-4749-8a28-e1ed5c19278d.png)
