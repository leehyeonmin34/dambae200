# 담배200 : 담배 검수 앱

# 실행 방법

1. [백엔드](https://github.com/leehyeonmin34/dambae200) 코드를 실행시킵니다.
2. POSTMAN과 같은 API 툴을 사용해 다음의 요청을 보내, 데이터베이스에 담배 정보를 입력합니다.
    - [POST] localhost:8060/api/cigarettes/multiple
    - [cigarettes_for_db](./cigarettes_for_db) 파일에 있는 json을 JSON 형식으로 본문에 담아 요청을 보냅니다.
3. [프론트엔드](https://github.com/leehyeonmin34/dambae200_front) 코드를 5500포트에서 실행시킵니다.
4. 브라우저에서 프론트 서버(http://localhost:5500)에 접속해 서비스를 이용합니다.
