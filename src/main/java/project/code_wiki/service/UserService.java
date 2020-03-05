package project.code_wiki.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import project.code_wiki.common.ResultMessage;
import project.code_wiki.domain.entity.UserEntity;
import project.code_wiki.domain.repository.UserRepository;
import project.code_wiki.dto.MyPageUserDto;
import project.code_wiki.dto.UpdateUserDto;
import project.code_wiki.dto.UserDto;
import project.code_wiki.security.CustomUserDetails;
import project.code_wiki.security.role.UserRole;

import javax.transaction.Transactional;
import java.util.*;
// 유저 컨트롤러의 요청들을 처리하는 객체
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    // 회원가입
    @Transactional
    public void joinUser(UserDto userDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // 회원 정보 저장
        userRepository.save(userDto.toEntity());
    }
    // 로그인 인증 처리
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        // email 검색
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(userEmail);
        UserEntity userEntity = userEntityWrapper.orElse(null);
        try {
            if(userEntity==null) {
                throw new NullPointerException();
            }
            // 로그인한 사용자 정보를 저장
            return initCustomUserDetail(userEntity);
        } catch (NullPointerException e) {
            // 유저 정보가 없으면 로그인 실패 exception 발생
            throw new UsernameNotFoundException(ResultMessage.NOT_EXIST_EMAIL.getValue());
        }
    }

    // 유저 정보 저장
    private CustomUserDetails initCustomUserDetail(UserEntity userEntity) {

        CustomUserDetails customUserDetails = new CustomUserDetails();
        // 유저 기본 정보 저장
        customUserDetails.setUsername(userEntity.getEmail());
        customUserDetails.setPassword(userEntity.getPassword());
        customUserDetails.setName(userEntity.getName());
        // 인증 정보 저장
        customUserDetails.setAuthorities(this.getAuthorities(userEntity.getEmail()));
        // 상태 정보 저장
        customUserDetails.setEnabled(true);
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setCredentialsNonExpired(true);
        // 리턴
        return customUserDetails;
    }
    // 인증 정보 저장
    private Collection<GrantedAuthority> getAuthorities(String email) {
        // ROLE 부여
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(("master@codewiki.com").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        return authorities;
    }

    // 검증 실패한 필드에 대해서 메시지 저장
    public Map<String, String> validateHandling(Errors errors) {
        // key: 검증 실패한 필드명(valid_필드명), value: 검증 실패 메시지
        Map<String, String> validatorResult = new HashMap<>();
        // 검증 실패 메시지 저장
        for(FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        // 리턴
        return validatorResult;
    }

    // 비밀번호 일치여부 확인
    public boolean isPasswordEqual(MyPageUserDto updateUserDto) {
        // 1. email 검색
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(updateUserDto.getEmail());
        UserEntity userEntity = userEntityWrapper.orElse(null);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 2. 패스워드 비교
        try {
            assert userEntity != null;
            // 암호화된 비밀번호 비교
            return passwordEncoder.matches(updateUserDto.getPassword(), userEntity.getPassword());
        } catch (NullPointerException e) {
            return false;
        }
    }

    // 회원정보 수정
    public void updateUser(UpdateUserDto updateUserDto) {
        // 1. 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 2. User DTO 변환
        UserDto userDto = UserDto.builder()
                .email(updateUserDto.getEmail())
                .password(passwordEncoder.encode(updateUserDto.getNewPassword()))
                .name(updateUserDto.getName())
                .build();
        // 3. 저장
        userRepository.save(userDto.toEntity());
    }

    // 비밀번호 찾기 STEP 1: 유저 이메일 찾기
    public boolean isExistUser(String email) {
        // 1. 이메일 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(email);
        // 2. Optional 언박싱
        UserEntity userEntity = userEntityWrapper.orElse(null);
        // 3. 결과 리턴
        return userEntity != null;
    }
    // 비밀번호 찾기 STEP 2: 사용자 이름도 일치하는지 확인
    public boolean isExistUser(String email, String name) {
        // 1. 이메일, 이름 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmailAndName(email, name);
        // 2. Optional 언박싱
        UserEntity userEntity = userEntityWrapper.orElse(null);
        // 3. 결과 리턴
        return userEntity != null;
    }

    // 유저 테이블 전부 불러오기
    @Transactional
    public List<UserDto> getUserData() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(UserEntity userEntity : userEntities) {
            userDtoList.add(this.convertEntityToDto(userEntity));
        }

        return userDtoList;
    }

    // Entity -> DTO 변환 메서드
    private UserDto convertEntityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .password("encrypted")
                .registerDateTime(userEntity.getRegisterDateTime())
                .build();
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
