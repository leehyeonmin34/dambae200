# 담배200 : 공동 편집과 권한 관리를 지원하는 담배 검수 앱

![A4 - 26 (2)](https://user-images.githubusercontent.com/66104031/218244917-db45b460-fd2a-4c0c-93c9-8150a3d9eff2.jpg)

편의점에서 담배 재고를 쉽게 검수하기 위해 제작된 웹 서비스입니다. 

매장 별로 '담배 목록'을 만들 수 있고 이를 편집함으로서 담배를 검수합니다.

담배 목록은 다른 사람들과 공유가 가능한데, 각 목록 생성자가 접근 권한을 관리하며, 소켓 통신을 활용해 실시간 공동 편집을 지원합니다.

<br />
<br />

# 문제점 & 아이디어

![Frame 1153](https://github.com/leehyeonmin34/dambae200/assets/66104031/5dd39d57-46f1-4391-8e37-f45c31c71d43)



<br/>
</span>
<br />
<br />

### 제공 기능 

[🔗 UI & 작동 영상 + 기획 보러가기](https://www.figma.com/proto/desD77sVBmkrGoZHDKkU5y/%EB%8B%B4%EB%B0%B0%EA%B2%80%EC%88%98%EC%95%B1-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4?node-id=781-53559&starting-point-node-id=856%3A43897&scaling=contain)

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
- 서버 확장, 대용량 트래픽과 운영을 고려한 방식
- 성능테스트를 통한 튜닝
- 로직이 잘 보이고 확장, 유지보수하기 좋은 코드


<br />


<br />

## 사용 기술
- Spring Boot
- Spring Data JPA
- MySQL
- Redis
- nginx
- Stomp
- Docker
- Jenkins
- Naver Cloud Platform

## 프로젝트 구조
- Naver Cloud Platform의 서버로 구성되어 있습니다.
- github hook을 받아 Jenkins에서 CI/CD를 진행합니다.
- Jenkins측에서 Dockerhub에 이미지를 push하고, 앱서버 측에서 이미지를 pull합니다.
- Blue-green 방식으로 무중단 배포됩니다.
- 사용자의 요청은 nginx에 의해 프론트엔드 리소스와 api요청을 분기 처리됩니다.
- WAS는 3개 서버로 운용됩니다.
- DB는 Primary-Slave 구조의 2개 서버로 운용됩니다.

<img width="1048" alt="스크린샷 2023-10-04 오후 10 24 20" src="https://github.com/leehyeonmin34/dambae200/assets/66104031/3faee238-cdc3-4c8c-9bde-4ca29ae8061d">


# 문제 해결 사례

<br />

## 운영
[🔗 서버 Scale-out에 대비해, 세션 정보를 DB에 중앙화해 저장함으로써 Stateless하게 유지](https://velog.io/@leehyeonmin34/session-info-in-db)

[🔗 Jenkins와 Docker, nginx를 이용한 Blue-Green 무중단 배포 🔥](https://velog.io/@leehyeonmin34/dambae200-blue-green)

[🔗 로드밸런서 적용하면서 CORS, hop-by-hop 헤더, 정적리소스 반환 문제 해결](https://velog.io/@leehyeonmin34/dambae200-nginx-introduction)

[🔗 DB 레플리케이션으로 병목 완화](https://velog.io/@leehyeonmin34/dambae200-db-replication)

[🔗 성능테스트를 통한 서버 튜닝으로 핵심 요청 TPS 239 -> 4292 (스케일업 아웃, 커넥션풀, 스레드풀, 레플리케이션) 🔥](https://velog.io/@leehyeonmin34/dambae200-tuning-through-performance-test)

<br />


## 캐시 & Redis

[🔗 Redis hash 자료구조를 이용해 탐색시간 줄이기](https://velog.io/@leehyeonmin34/dambae200-redis-data-structure)

[🔗 간편하게 캐시와 DB를 연계 활용하는 캐시 모듈 제작 🔥](https://velog.io/@leehyeonmin34/cache-module)

[🔗 원자성을 위해 Redis에 @Transactional 적용하기](https://velog.io/@leehyeonmin34/dambae200-redis-transactional)

[🔗 쓰기스큐 방지를 위해 분산락 AOP 제작](https://velog.io/@leehyeonmin34/dambae200-distributed-lock)

[🔗 Redis Pipeline으로 Bulk Insert 시 네트워크 병목 개선](https://velog.io/@leehyeonmin34/dambae200-redis-pipeline)

[🔗 캐시에 저장-로드할 때 리플렉션으로 DTO-Entity 변환](https://velog.io/@leehyeonmin34/dto-entity-converter)

[🔗 캐시와 세션별로 Redis 서버 분리 및 eviction, maxmeory 최적화](https://velog.io/@leehyeonmin34/dambae200-cache-seperation)

[🔗 코드 수준의 동적 스케줄러로 캐시에서 DB로 Write-Back](https://velog.io/@leehyeonmin34/dambae200-wrtie-back)


<br />


## DB
[🔗 DB 병목 지양을 위한 N + 1 쿼리 지양](https://github.com/leehyeonmin34/dambae200/wiki/JPA%EC%9D%98-%ED%8A%B9%EC%84%B1%EC%9D%84-%EC%9D%B4%ED%95%B4%ED%95%9C-%EC%B5%9C%EC%A0%81%ED%99%94)

[🔗 DB 락 최소화를 위한 @Transational 최소화](https://github.com/leehyeonmin34/dambae200/wiki/DB-%EB%9D%BD-%EC%B5%9C%EC%86%8C%ED%99%94%EB%A5%BC-%EC%9C%84%ED%95%9C-@Transactional-%EC%B5%9C%EC%86%8C%ED%99%94)


<br />

## 소켓 통신

[🔗 실시간 공동 편집을 이벤트 기반의 STOMP 소켓통신으로 구현](https://velog.io/@leehyeonmin34/dambae200stomp-for-realtime-simultaneous-editing)

[🔗 서비스 코드가 통신 방식에 종속되지 않도록 DTO Wrapping](https://velog.io/@leehyeonmin34/protocol-independant-service-code)

[🔗 STOMP 예외처리하기](https://velog.io/@leehyeonmin34/protocol-independant-error-handling)
- Wrapper 구조를 이용해 HTTP, 소켓 통신에서의 예외를 일관되게 처리
- 비지니스 예외 발생 시 소켓 연결 유지하며 예외 메시지 발송
- 권한검사 인터셉터 예외 처리

<br />

## 로직이 잘 보이고 유지보수하기 좋은 코드
[🔗 예외 처리 및 일관된 응답 형태](https://velog.io/@leehyeonmin34/exception-handling)
- 트리구조의 런타임 예외 상속 적극 활용
- ControllerAdvice, ExceptionHandler 활용
- 성공, 실패 모두 일정한 HTTP 응답 형태

[🔗 기타](https://github.com/leehyeonmin34/dambae200/wiki/%EB%A1%9C%EC%A7%81%EC%9D%B4-%EC%9E%98-%EB%B3%B4%EC%9D%B4%EA%B3%A0-%EC%9C%A0%EC%A7%80%EB%B3%B4%EC%88%98%ED%95%98%EA%B8%B0-%EC%A2%8B%EC%9D%80-%EC%BD%94%EB%93%9C)

<br />


<br />

# ERD
<img width="964" alt="스크린샷 2023-02-11 오후 2 34 12" src="https://user-images.githubusercontent.com/66104031/218242222-d24978f9-f91e-4909-98b5-8f1ca3278fa3.png">

<br />

# 프론트
UI는 실사용 시 마주할 수 있는 다양한 시나리오에 대응하면서, 다양한 해상도에서 일관되게 작동되도록 구현되었습니다.

![Frame 1026](https://user-images.githubusercontent.com/66104031/218942694-ddceeaa8-b448-490a-9836-752ad894f271.jpg)


-
