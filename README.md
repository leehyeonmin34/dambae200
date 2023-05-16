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

## 문제해결 등 중요한 내용은 [📓위키](https://github.com/leehyeonmin34/dambae200/wiki) 를 참조해주세요 !

<br />

# ERD
<img width="964" alt="스크린샷 2023-02-11 오후 2 34 12" src="https://user-images.githubusercontent.com/66104031/218242222-d24978f9-f91e-4909-98b5-8f1ca3278fa3.png">

<br />

# 프론트
UI는 실사용 시 마주할 수 있는 다양한 시나리오에 대응하면서, 다양한 해상도에서 일관되게 작동되도록 구현되었습니다.

![Frame 1026](https://user-images.githubusercontent.com/66104031/218942694-ddceeaa8-b448-490a-9836-752ad894f271.jpg)


