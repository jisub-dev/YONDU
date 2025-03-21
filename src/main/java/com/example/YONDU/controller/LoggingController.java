package com.example.YONDU.controller;

import com.example.YONDU.dto.LogginDto;
import com.example.YONDU.entity.Bank;
import com.example.YONDU.entity.Role;
import com.example.YONDU.entity.UserEntity;
import com.example.YONDU.repository.UserRepository;
import com.example.YONDU.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoggingController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoggingController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/session")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody Map<String, String> loginRequest) {
        try {
            String identifier = loginRequest.get("identifier");
            String password = loginRequest.get("password");

            // 이메일 또는 비밀번호 누락 시 400 Bad Request 반환
            if (identifier == null || password == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Email and password are required"
                ));
            }

            Optional<UserEntity> userOptional = userRepository.findById(identifier);

            if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Invalid identifier or password"
                ));
            }
            UserEntity user = userOptional.get();

            if (user.isBanned()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "success", false,
                        "message", "User is banned"
                ));
            }
            // JWT 액세스 토큰 및 리프레시 토큰 생성
            String token = jwtService.generateToken(user.getIdentifier());
            String refreshToken = jwtService.generateRefreshToken(user.getIdentifier());

            // 리프레시 토큰을 저장 후 업데이트
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "RT", refreshToken,
                    "user", new LogginDto(userOptional.get())
            ));
        } catch (Exception e) {
            //서버 내부 오류 (500 INTERNAL_SERVER_ERROR)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }
    @DeleteMapping("/session")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> logoutRequest) {
        try {
            String identifier = logoutRequest.get("identifier");

            // 이메일 또는 비밀번호 누락 시 400 Bad Request 반환
            if (identifier == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Email are required"
                ));
            }

            Optional<UserEntity> userOptional = userRepository.findById(identifier);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Invalid identifier"
                ));
            }
            UserEntity user = userOptional.get();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "logout",
                    "user", new LogginDto(userOptional.get())
            ));
        } catch (Exception e) {
            //서버 내부 오류 (500 INTERNAL_SERVER_ERROR)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody Map<String, String> signUpRequest) {
        try {
            String identifier = signUpRequest.get("identifier");
            String password = signUpRequest.get("password");
            String name = signUpRequest.get("name");
            String gender = signUpRequest.get("gender");
            String phone = signUpRequest.get("phone");
            String branch = signUpRequest.get("branch");
            int age = Integer.parseInt(signUpRequest.get("age"));
            int career = Integer.parseInt(signUpRequest.get("career"));
            double ntrp = Double.parseDouble(signUpRequest.get("ntrp"));
            String refundAccount = signUpRequest.get("refund_account");
            String refundBank = signUpRequest.get("refund_bank");
            String receiptInfo = signUpRequest.get("receipt_info");
            String trainerId = signUpRequest.get("trainer_id");

            // 필수 필드 확인 (400 Bad Request)
            if (identifier == null || password == null || name == null || phone == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Identifier, password, name, and phone are required"
                ));
            }

            // 이메일 중복 확인 (409 Conflict)
            if (userRepository.existsByIdentifier(identifier)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "success", false,
                        "message", "Identifier is already in use"
                ));
            }

            if (userRepository.existsByPhone(phone)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "success", false,
                        "message", "phonenumber is already in use"
                ));
            }

            // UserEntity 생성 및 저장
            UserEntity newUser = new UserEntity();
            newUser.setIdentifier(identifier);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setName(name);
            newUser.setGender(gender);
            newUser.setPhone(phone);
            newUser.setBranch(branch);
            newUser.setAge(age);
            newUser.setCareer(career);
            newUser.setNtrp(ntrp);
            newUser.setRefundAccount(refundAccount);
            Bank selectedBank = Bank.fromString(refundBank); // 한글 은행명을 Enum으로 변환
            newUser.setRefundBank(selectedBank);
            newUser.setReceiptInfo(receiptInfo);
            newUser.setTrainerId(trainerId);
            newUser.setBanned(false);
            newUser.setRole(Role.MEMBER);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setRefreshToken(null);


            UserEntity savedUser = userRepository.save(newUser);

            // 성공 응답 (201 Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "User registered successfully",
                    "user", new LogginDto(savedUser)
            ));
        } catch (Exception e) {
            //서버 내부 오류 (500 INTERNAL_SERVER_ERROR)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<Map<String, Object>> refreshTokens(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("RT");

            if (refreshToken == null || refreshToken.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Refresh token is required"
                ));
            }

            Optional<UserEntity> userOptional = userRepository.findByRefreshToken(refreshToken);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Invalid refresh token"
                ));
            }

            if (!jwtService.validateRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Refresh token has expired"
                ));
            }

            UserEntity user = userOptional.get();
            String newAccessToken = jwtService.generateToken(user.getIdentifier());
            String newRefreshToken = jwtService.generateRefreshToken(user.getIdentifier());

            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", newAccessToken,
                    "RT", newRefreshToken
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

}