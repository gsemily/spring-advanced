# SPRING ADVANCED

## 💻프로젝트 소개
코드 개선 및 테스트 코드 작성

### ⏰개발 기간
25.06.05 ~ 25.06.12

### ⚙️개발 환경
* Java 17
* JDK 1.8.0
* IDE : IntelliJ IDEA 2024.3.5
* Spring Boot


## 📌주요 변경 사항
### 코드 개선
1. Early Return

조건에 맞지 않는 경우 즉시 리턴
```
if (userRepository.existsByEmail(signupRequest.getEmail())) {
throw new InvalidRequestException("이미 존재하는 이메일입니다.");
}

String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
UserRole userRole = UserRole.of(signupRequest.getUserRole());`
```
2. 불필요한 if-else 피하기
```
WeatherDto[] weatherArray = responseEntity.getBody();
if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
}
if (weatherArray == null || weatherArray.length == 0) {
throw new ServerException("날씨 데이터가 없습니다.");
}
```
3. Validation
```
@NotBlank
@Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
@Pattern(regexp = ".*[0-9].*", message = "새 비밀번호는 숫자를 포함해야 합니다.")
@Pattern(regexp = ".*[A-Z].*", message = "새 비밀번호는 대문자를 포함해야 합니다.")
private String newPassword;
```
---
### N+1 문제 
**fetch join → @EntityGraph 리팩토링**
* 위치: org.example.expert.domain.todo.repository.TodoRepository 
* 기존 JPQL LEFT JOIN FETCH t.user 사용 → JPA 표준 @EntityGraph로 변경 
* 성능 최적화 + 가독성 향상
---
### 테스트코드 연습 
**테스트 패키지 수정**
* package org.example.expert.config; → matches_메서드가_정상적으로_동작한다()
* package org.example.expert.domain.manager.service; → manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다()
* org.example.expert.domain.comment.service; → comment_등록_중_할일을_찾지_못해_에러가_발생한다()
* org.example.expert.domain.manager.service → todo의_user가_null인_경우_예외가_발생한다()
---
### API 로깅
**AOP를 활용한 API 로깅**
* admin 사용자만 호출 가능한 API(deleteComment, changeUserRole) 대상 
* AOP를 활용해 해당 메서드 실행 전후 요청 및 응답 정보 로깅

**요청 정보 로깅**
* 사용자 ID는 HttpServletRequest의 헤더에서 추출 
* API 요청 시간과 URL를 포함한 상세 정보 로그 출력
* 메서드 인자(RequestBody)를 ObjectMapper를 통해 JSON 문자열 형식으로 변환

**응답 정보 로깅**
* 실제 컨트롤러 로직 수행 후 반환된 응답 객체를 JSON 형식으로 출력
