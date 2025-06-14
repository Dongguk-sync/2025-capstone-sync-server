package com.baekji.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러 상태별 메시지 정의 클래스
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400: 잘못된 요청 (Bad Request)
    WRONG_ENTRY_POINT(40000, HttpStatus.BAD_REQUEST, "잘못된 접근입니다"), // 사용자가 잘못된 URL로 접근했을 때 발생
    MISSING_REQUEST_PARAMETER(40001, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."), // 요청에 필요한 파라미터가 누락된 경우
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."), // 파라미터 형식이 잘못된 경우
    BAD_REQUEST_JSON(40003, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."), // JSON 요청 형식 오류
    DATA_INTEGRITY_VIOLATION(40005, HttpStatus.BAD_REQUEST, "데이터 무결성 위반입니다. 필수 값이 누락되었거나 유효하지 않습니다."), // 데이터베이스 무결성 위반 (예: NOT NULL 컬럼에 NULL 삽입 시도)
    INVALID_INPUT_VALUE(40010, HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."), // 입력 값이 유효하지 않은 경우
    INVALID_REQUEST_BODY(40011, HttpStatus.BAD_REQUEST, "잘못된 요청 본문입니다."), // 요청 본문에 유효하지 않은 데이터가 포함된 경우
    MISSING_REQUIRED_FIELD(40012, HttpStatus.BAD_REQUEST, "필수 필드가 누락되었습니다."), // JSON 또는 요청 데이터에서 필수 필드가 누락된 경우
    DUPLICATE_NICKNAME(40013, HttpStatus.UNAUTHORIZED, "이미 있는 닉네임입니다."), // 이미 있는 닉네임

    // 파일 관련 오류
    UNSUPPORTED_FILE_FORMAT(40020, HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다."), // 업로드된 파일의 형식이 지원되지 않는 경우
    FILE_UPLOAD_ERROR(40021, HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."), // 파일 업로드 중 오류 발생
    FILE_CONVERSION_ERROR(40022, HttpStatus.BAD_REQUEST, "파일 변환에 실패했습니다."), // 업로드된 파일의 변환 작업 중 오류 발생
    FILE_SIZE_EXCEEDED(40023, HttpStatus.BAD_REQUEST, "파일 크기가 허용된 최대 크기를 초과했습니다."), // 파일 크기가 제한을 초과한 경우
    AI_SERVER_ERROR(40024, HttpStatus.BAD_REQUEST, "AI 서버 오류입니다."), // AI 서버 오류인 경우
    AI_RESPONSE_FAILED(40025, HttpStatus.BAD_REQUEST, "AI 응답 오류입니다."), // AI 서버 오류인 경우

    // 401: 인증 실패 (Unauthorized)
    INVALID_HEADER_VALUE(40100, HttpStatus.UNAUTHORIZED, "올바르지 않은 헤더값입니다."), // 헤더 값이 잘못되었거나 누락된 경우
    EXPIRED_TOKEN_ERROR(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."), // 인증 토큰이 만료된 경우
    INVALID_TOKEN_ERROR(40102, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."), // 토큰이 잘못되었거나 위조된 경우
    TOKEN_MALFORMED_ERROR(40103, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."), // 토큰 구조가 올바르지 않은 경우
    TOKEN_TYPE_ERROR(40104, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않거나 비어있습니다."), // 토큰의 타입이 잘못되었거나 누락된 경우
    TOKEN_UNSUPPORTED_ERROR(40105, HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."), // 서버가 지원하지 않는 토큰 유형
    TOKEN_GENERATION_ERROR(40106, HttpStatus.UNAUTHORIZED, "토큰 생성에 실패하였습니다."), // 토큰 생성 중 오류 발생
    TOKEN_UNKNOWN_ERROR(40107, HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다."), // 알 수 없는 이유로 토큰이 유효하지 않은 경우
    LOGIN_FAILURE(40108, HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다"), // 로그인 실패
    UNAUTHORIZED_ACCESS(40110, HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."), // 인증되지 않은 사용자 접근
    EXPIRED_SESSION(40111, HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."), // 사용자 세션이 만료된 경우
    EXIST_USER(40112, HttpStatus.UNAUTHORIZED, "이미 회원가입한 회원입니다."), // 이미 회원가입된 사용자
    NOT_FOUND_USER_ID(40113, HttpStatus.UNAUTHORIZED, "아이디를 잘못 입력하셨습니다."), // 잘못된 아이디 입력
    INVALID_PASSWORD(40114, HttpStatus.UNAUTHORIZED, "비밀번호를 잘못 입력하셨습니다."), // 비밀번호가 잘못된 경우

    // 403: 권한 부족 (Forbidden)
    FORBIDDEN_ROLE(40300, HttpStatus.FORBIDDEN, "요청한 리소스에 대한 권한이 없습니다."), // 사용자가 요청한 리소스에 대한 권한이 없는 경우
    ACCESS_DENIED(40310, HttpStatus.FORBIDDEN, "접근 권한이 거부되었습니다."), // 권한 부족으로 접근이 거부된 경우
    INACTIVE_USER(40320, HttpStatus.FORBIDDEN, "탈퇴한 회원입니다. 계정을 활성화 후 로그인해주세요."), // 탈퇴한 사용자가 리소스에 접근하려고 할 때

    // 404: 리소스를 찾을 수 없음 (Not Found)
    NOT_FOUND_USER(40401, HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    NOT_FOUND_CHATBOT_HISTORY(40402, HttpStatus.NOT_FOUND, "챗봇 히스토리를 찾을 수 없습니다."),
    NOT_FOUND_CHATBOT_MESSAGE(40403, HttpStatus.NOT_FOUND, "챗봇 메시지를 찾을 수 없습니다."),
    NOT_FOUND_SUBJECT(40404, HttpStatus.NOT_FOUND, "과목을 찾을 수 없습니다."),
    NOT_FOUND_ANSWER_FILE(40405, HttpStatus.NOT_FOUND, "답안 파일을 찾을 수 없습니다."),
    NOT_FOUND_EXAM_SCHEDULE(40406, HttpStatus.NOT_FOUND, "시험 일정을 찾을 수 없습니다."),
    NOT_FOUND_STUDY_SCHEDULE(40407, HttpStatus.NOT_FOUND, "학습 일정을 찾을 수 없습니다."),
    NOT_FOUND_STUDYS(40408, HttpStatus.NOT_FOUND, "학습 기록을 찾을 수 없습니다."),
    NOT_FOUND_STUDY_MESSAGE(40409, HttpStatus.NOT_FOUND, "학습 메시지를 찾을 수 없습니다."),


    // 409: 중복 데이터 (Conflict)
    DUPLICATE_ENTRY(40900,  HttpStatus.CONFLICT, "중복된 사원입니다."),

    // 429: 요청 과다 (Too Many Requests)
    TOO_MANY_REQUESTS(42900, HttpStatus.TOO_MANY_REQUESTS, "요청 횟수가 너무 많습니다. 잠시 후 다시 시도해 주세요."),

    // 500: 서버 내부 오류 (Internal Server Error)
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다"),
    PASSWORD_ENCODING_FAILED(50001, HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 암호화 실패"),
    MAX_UPLOAD_SIZE_EXCEEDED(50002, HttpStatus.INTERNAL_SERVER_ERROR, "업로드 실패: 파일의 크기가 너무 큽니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

}
