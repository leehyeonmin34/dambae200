# 담배200 : 공동 편집과 권한 관리를 지원하는 담배 검수 앱

![A4 - 26 (2)](https://user-images.githubusercontent.com/66104031/218244917-db45b460-fd2a-4c0c-93c9-8150a3d9eff2.jpg)

편의점에서 담배 재고를 쉽게 검수하기 위해 제작된 웹 서비스입니다. 

매장 별로 '담배 목록'을 만들 수 있고 이를 편집함으로서 담배를 검수합니다.

담배 목록은 다른 사람들과 공유가 가능한데, 각 목록 생성자가 접근 권한을 관리하며, 소켓 통신을 활용해 실시간 공동 편집을 지원합니다.

<br />
<br />

# 문제점 & 아이디어

![Frame 1154](https://github.com/leehyeonmin34/dambae200/assets/66104031/6b0bc76d-0796-4e60-856a-fc47f7392fbd)
![Frame 1152](https://github.com/leehyeonmin34/weather_reminder/assets/66104031/92060bc6-5ae5-4da2-b609-0f63e4348cda)

# 핵심 플로우
1. 목록 생성
   1. 매장별로 담배 목록을 생성할 수 있음
   2. 내 목록에 담배를 진열순서대로 추가
   3. 각 담배들의 '전산 순서'를 수정 (POS기에서 출력되는 목록 순서)
2. 담배 검수
   1. '진열 순서'대로 우리 매장의 담배 갯수를 입력
   2. ‘전산순으로 정렬하기’ 버튼을 누르면 미리 설정해 놓은 전산 순서대로 정렬됨
   3. 포스기에서 출력된 재고 목록 종이와 담배 갯수 1:1 비교

[🔗 UI & 작동 영상 + 기획 보러가기](https://www.figma.com/proto/desD77sVBmkrGoZHDKkU5y/%EB%8B%B4%EB%B0%B0%EA%B2%80%EC%88%98%EC%95%B1-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4?node-id=781-53559&starting-point-node-id=856%3A43897&scaling=contain)
<br/>
</span>
<br />
<br />

### 제공 기능 
- 담배 목록
  - 담배 목록 생성/수정/삭제
  - 담배 추가/수정/삭제
  - 담배 순서 조정
  - 담배 목록 내 검색
- 동시 편집
  - 담배 목록 실시간 동시 편집 (구글 스프레드시트와 유사)
  - 목록 접근 권한 관리 (요청/허가/비허가/관리자 권한 인계)
- 그 외 기능
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
- 서버 확장, 대용량 트래픽과 운영을 고려한 방식
- 성능테스트를 통한 튜닝

## 👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇<br />[<U>문제해결 등 중요한 내용은 📓위키 를 참조해주세요 !</U>](https://github.com/leehyeonmin34/dambae200/wiki) <br />👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆

## 사용 기술
`Spring Boot`, `Spring Data JPA`, `MySQL`, `Redis`, `Stomp`, `Docker`, `nginx`, `Jenkins`, `Naver Cloud Platform`

## 프로젝트 구조
- Naver Cloud Platform의 서버로 구성되어 있습니다.
- github hook을 받아 Jenkins에서 CI/CD를 진행합니다.
- Jenkins측에서 Dockerhub에 이미지를 push하고, 앱서버 측에서 이미지를 pull합니다.
- Blue-green 방식으로 무중단 배포됩니다.
- 사용자의 요청은 nginx에 의해 프론트엔드 리소스와 api요청을 분기 처리됩니다.
- WAS는 3개 서버로 운용됩니다.
- DB는 Primary-Slave 구조의 2개 서버로 운용됩니다.

<img width="1048" alt="스크린샷 2023-10-04 오후 10 24 20" src="https://github.com/leehyeonmin34/dambae200/assets/66104031/3faee238-cdc3-4c8c-9bde-4ca29ae8061d">

<br />


<br />

# ERD
<img width="964" alt="스크린샷 2023-02-11 오후 2 34 12" src="https://user-images.githubusercontent.com/66104031/218242222-d24978f9-f91e-4909-98b5-8f1ca3278fa3.png">

<br />

# 프론트
UI는 실사용 시 마주할 수 있는 다양한 시나리오에 대응하면서, 다양한 해상도에서 일관되게 작동되도록 구현되었습니다.

![Frame 1026](https://user-images.githubusercontent.com/66104031/218942694-ddceeaa8-b448-490a-9836-752ad894f271.jpg)


-
