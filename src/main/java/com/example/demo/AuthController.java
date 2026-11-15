package com.example.demo.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(
            UserRepository userRepository,
            AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    // ログイン処理
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        User user = userRepository
                .findByLoginId(request.getLoginId())
                .orElse(null);

        // ログインIDとパスワードを確認
        if (user == null ||
                !authService.matchesPassword(
                        request.getPassword(),
                        user.getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message",
                            "ログインIDまたはパスワードが違います。"));
        }

        // 認証情報を作成
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getLoginId(),
                        null,
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_USER"
                                )
                        )
                );

        // 認証情報を登録
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // セッションIDを変更
        httpRequest.getSession(true);
        httpRequest.changeSessionId();

        // セッションに認証情報を保存
        HttpSessionSecurityContextRepository repository =
                new HttpSessionSecurityContextRepository();

        repository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        return ResponseEntity.ok(
                Map.of("message", "ログインに成功しました。"));
    }

    // 開発用ユーザー登録処理
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody LoginRequest request) {

        if (request.getLoginId() == null ||
                request.getLoginId().isBlank() ||
                request.getPassword() == null ||
                request.getPassword().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "入力内容を確認してください。"));
        }

        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "このログインIDは登録済みです。"));
        }

        User user = new User();
        user.setLoginId(request.getLoginId());
        user.setPassword(authService.encodePassword(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "ユーザー登録に成功しました。"));
    }

    // ★追加：ログイン状態確認処理
    @GetMapping("/api/auth/me")
    public ResponseEntity<?> getCurrentUser(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("authenticated", false));
        }

        return ResponseEntity.ok(
                Map.of(
                        "authenticated", true,
                        "loginId", authentication.getName()
                )
        );
    }

    // ★追加：ログアウト処理
    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);

        return ResponseEntity.ok(
                Map.of("message", "ログアウトしました。")
        );
    }

    // リクエストデータ
    public static class LoginRequest {

        private String loginId;
        private String password;

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
