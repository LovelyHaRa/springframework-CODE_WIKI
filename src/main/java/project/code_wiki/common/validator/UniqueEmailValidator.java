package project.code_wiki.common.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.code_wiki.common.annotation.UniqueEmail;
import project.code_wiki.domain.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
// 중복된 이메일 검증 로직
@Component
@RequiredArgsConstructor // 생성자가 필요한 필드에 자동으로 주입
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;
    // 검증 조건을 작성하는 메서드
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 이메일이 데이터베이스에 존재하는지 확인
        boolean isExistEmail = userRepository.existsById(value);
        // 이메일이 존재하면
        if(isExistEmail) {
            context.disableDefaultConstraintViolation(); // 메시지 템플릿을 사용하는 디폴트 객체 생성을 비활성화
            context.buildConstraintViolationWithTemplate(
                    "이미 존재하는 이메일 입니다."
            ).addConstraintViolation(); // 새로운 검증 실패 메시지 템플릿을 추가
        }
        // 이메일이 존재하면 false, 존재하지않으면 true (isExistEmail 값을 토클)
        return !isExistEmail;
    }
    // 로직 내에서 사용할 필드를 초기화(해당없음)
    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }
}
