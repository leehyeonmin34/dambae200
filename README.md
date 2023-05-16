# 담배200 : 공동 편집과 권한 관리를 지원하는 담배 검수 앱

![A4 - 26 (2)](https://user-images.githubusercontent.com/66104031/218244917-db45b460-fd2a-4c0c-93c9-8150a3d9eff2.jpg)

편의점에서 담배 재고를 쉽게 검수하기 위해 제작된 웹 서비스입니다. 

매장 별로 '담배 목록'을 만들 수 있고 이를 편집함으로서 담배를 검수합니다.

담배 목록은 다른 사람들과 공유가 가능한데, 각 목록 생성자가 접근 권한을 관리하며, 소켓 통신을 활용해 실시간 공동 편집을 지원합니다.

<br />
<br />

# 핵심 컨셉 (담배 검수)
1. 담배 목록의 각 담배마다 진열순과 정렬순 정보를 지정
2. 진열된 순서대로 우리 매장의 담배 갯수를 입력
3. ‘전산순으로 정렬하기’ 버튼을 눌러서, 미리 설정해 놓은 순서대로 정렬
4. 이를 포스기에서 출력된 재고 목록 종이와 1:1 비교


[🔗 UI & 작동 영상 + 기획 보러가기](https://www.figma.com/proto/desD77sVBmkrGoZHDKkU5y/%EB%8B%B4%EB%B0%B0%EA%B2%80%EC%88%98%EC%95%B1-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4?node-id=781-55129&starting-point-node-id=856%3A43897&scaling=scale-down-width)
<br/>
🔗 ~~서비스 써보기~~ <span style="color:#808080"> ~~(회원가입이 필요합니다)~~ </span> (비용문제로 현재는 서버 중단됨)
<br />
<br />


### 제공 기능
- 담배 목록
  - 담배 목록 생성/수정/삭제
  - 담배 추가/수정/삭제
  - 담배 순서 조정
  - 담배 목록 내 검색
- 목록 접근 권한 관리
  - 요청/허가/비허가/관리자 권한 인계
- 로그인, 비밀번호 찾기
- 알림 (Pageable 조회)
- 설정 (개인정보 수정)
- 업무 꿀팁 컨텐츠

<br />

# 프로젝트의 주요 관심사
- 실제 서비스 가능한 완성도로 서버-클라이언트 구현(API, UI, 통신 규약, 기능성)
- 소켓 통신을 통한 동시 편집 구현
- 캐시, DB 통신 최소화를 통한 성능 최적화
- 로직이 잘 보이고 확장, 유지보수하기 좋은 코드

## 사용 기술
Spring Boot, Spring Data JPA, MySQL, Redis, Stomp, Docker, nginx, Naver Cloud Platform

## 프로젝트 구조
- github hook을 받아 Jenkins에서 CI/CD를 진행합니다.
- 젠킨스와 메인서버 2개로 작동중이며, 메인서버가 docker를 통해 MySQL, Redis, 앱서버를 구동중입니다.
- Naver Cloud Platform의 서버를 사용합니다.
- Blue-green 방식으로 무중단 배포됩니다.


<br />

## 문제 해결 사례
### 성능 최적화
- 캐시
    - [서버 부하를 줄이기 위해 캐싱 서버 적극 활용](https://github.com/leehyeonmin34/dambae200/wiki/%EC%9E%90%EC%A3%BC-%EC%93%B0%EC%9D%B4%EB%8A%94-%EC%A0%95%EB%B3%B4%EB%8A%94-%EC%BA%90%EC%8B%9C%EB%A1%9C-%EC%B5%9C%EC%A0%81%ED%99%94)
    - [간편하게 캐시와 DB를 활용하는 모듈 제작](https://github.com/leehyeonmin34/dambae200/wiki/%EC%BA%90%EC%8B%9C-%EB%AA%A8%EB%93%88%EB%A1%9C-%EA%B0%84%ED%8E%B8%ED%95%98%EA%B2%8C-%EC%BA%90%EC%8B%9C%EC%99%80-DB-%EC%97%B0%EA%B3%84-%ED%99%9C%EC%9A%A9)
- DB 서버와의 통신 최소화
  - [DB 병목 지양을 위한 N + 1 쿼리 지양](https://github.com/leehyeonmin34/dambae200/wiki/JPA%EC%9D%98-%ED%8A%B9%EC%84%B1%EC%9D%84-%EC%9D%B4%ED%95%B4%ED%95%9C-%EC%B5%9C%EC%A0%81%ED%99%94)
  - [DB 락 최소화를 위한 @Transational 최소화](https://github.com/leehyeonmin34/dambae200/wiki/JPA%EC%9D%98-%ED%8A%B9%EC%84%B1%EC%9D%84-%EC%9D%B4%ED%95%B4%ED%95%9C-%EC%B5%9C%EC%A0%81%ED%99%94)
  - [코드 수준의 동적 스케줄러로 캐시에서 DB로 Write-Back](https://github.com/leehyeonmin34/dambae200/wiki/%EB%8F%99%EC%A0%81-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%BA%90%EC%8B%9C-Write-back%EC%9C%BC%EB%A1%9C-DB-%ED%86%B5%EC%8B%A0-%ED%9A%9F%EC%88%98-%EC%A4%84%EC%9D%B4%EA%B8%B0)


### 서버 확장과 운영을 고려한 방식
- [세션 정보를 Http 세션이 아닌 DB와 캐시에 저장](https://github.com/leehyeonmin34/dambae200/wiki/%EC%84%9C%EB%B2%84-Scale-out%EC%97%90-%EB%8C%80%EB%B9%84%ED%95%9C-%EC%84%B8%EC%85%98-%EA%B4%80%EB%A6%AC-(DB-%EC%B0%B8%EC%A1%B0-%EB%B0%A9%EC%8B%9D))
- [Jenkins와 Docker를 이용한 자동 무중단 배포](https://github.com/leehyeonmin34/dambae200/wiki/Jenkins%EC%99%80-Docker%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9E%90%EB%8F%99-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC)

### 소켓 통신 활용
- [공동 편집 기술을 소켓통신 으로 결정](https://github.com/leehyeonmin34/dambae200/wiki/%EC%9E%A6%EC%9D%80-%EC%88%98%EC%A0%95%EC%9D%98-%EA%B3%B5%EB%8F%99-%ED%8E%B8%EC%A7%91%EC%9D%84-%EC%86%8C%EC%BC%93%ED%86%B5%EC%8B%A0%EC%9C%BC%EB%A1%9C-%EA%B5%AC%ED%98%84)
- [통신 방식에 종속되지 않는 서비스 코드](https://github.com/leehyeonmin34/dambae200/wiki/%ED%86%B5%EC%8B%A0-%EB%B0%A9%EC%8B%9D%EC%97%90-%EC%A2%85%EC%86%8D%EB%90%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%BD%94%EB%93%9C)
- [Http에서 발생한 예외와 소켓통신 중 발생한 예외를 일관되게 처리](https://github.com/leehyeonmin34/dambae200/wiki/%EB%9E%98%ED%8D%BC-%EA%B5%AC%EC%A1%B0%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-%ED%95%98%EB%82%98%EC%9D%98-%EC%BD%94%EB%93%9C%EB%A1%9C-HTTP,-%EC%86%8C%EC%BC%93-%EC%98%88%EC%99%B8-%EB%AA%A8%EB%91%90-%EC%B2%98%EB%A6%AC)
- [비지니스 예외 발생 시 소켓 연결 유지하며 예외 메시지 발송](https://github.com/leehyeonmin34/dambae200/wiki/%EB%9E%98%ED%8D%BC-%EA%B5%AC%EC%A1%B0%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-%ED%95%98%EB%82%98%EC%9D%98-%EC%BD%94%EB%93%9C%EB%A1%9C-HTTP,-%EC%86%8C%EC%BC%93-%EC%98%88%EC%99%B8-%EB%AA%A8%EB%91%90-%EC%B2%98%EB%A6%AC)
- [Channel Interceptor를 활용한 권한 체크 및 예외 처리](https://github.com/leehyeonmin34/dambae200/wiki/%EC%9D%B8%ED%84%B0%EC%85%89%ED%84%B0%EB%A5%BC-%ED%86%B5%ED%95%9C-%EA%B6%8C%ED%95%9C-%EA%B2%80%EC%82%AC%EC%99%80-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC)
  - ChannelInterceptor로 소켓 통신 권한 검사
  - StompSubProtocolErrorHandler로 소켓 통신 용 ControllerAdvice 밖 예외 처리

### 로직이 잘 보이고 유지보수하기 좋은 코드
- [예외 처리](https://github.com/leehyeonmin34/dambae200/wiki/%ED%8A%B8%EB%A6%AC%EA%B5%AC%EC%A1%B0%EC%9D%98-%EB%9F%B0%ED%83%80%EC%9E%84-%EC%98%88%EC%99%B8%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC)
  - 트리구조의 런타임 예외 상속 적극 활용
  - ControllerAdvice, ExceptionHandler 활용
  - 성공, 실패 모두 일정한 HTTP 응답 형태
- [기타](https://github.com/leehyeonmin34/dambae200/wiki/%EB%A1%9C%EC%A7%81%EC%9D%B4-%EC%9E%98-%EB%B3%B4%EC%9D%B4%EA%B3%A0-%EC%9C%A0%EC%A7%80%EB%B3%B4%EC%88%98%ED%95%98%EA%B8%B0-%EC%A2%8B%EC%9D%80-%EC%BD%94%EB%93%9C)
  

<br />

# ERD
<img width="964" alt="스크린샷 2023-02-11 오후 2 34 12" src="https://user-images.githubusercontent.com/66104031/218242222-d24978f9-f91e-4909-98b5-8f1ca3278fa3.png">

<br />

# 프론트
UI는 실사용 시 마주할 수 있는 다양한 시나리오에 대응하면서, 다양한 해상도에서 일관되게 작동되도록 구현되었습니다.

![Frame 1026](https://user-images.githubusercontent.com/66104031/218942694-ddceeaa8-b448-490a-9836-752ad894f271.jpg)


