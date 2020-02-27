package project.code_wiki.common.annotation;

import project.code_wiki.common.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
// 회원가입시 중복된 이메일을 체크할 수 있는 검증 어노테이션
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class) // 검증 로직 클래스를 정의
@Target({ElementType.METHOD, ElementType.FIELD}) // 어노테이션 사용범위 정의
@Retention(RetentionPolicy.RUNTIME) // 런타임 이후에도 어노테이션 유지
public @interface UniqueEmail {
    String message() default "Email is Duplication"; // 기본 검증 실패 메시지

    Class<?>[] groups() default {}; // targeted group 을 커스터마이징 하기 위해 사용

    Class<? extends Payload>[] payload() default {}; // 확장성을 위해 사용
}
