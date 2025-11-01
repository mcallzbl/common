package com.mcallzbl.user.controller;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.user.config.SessionConfig;
import com.mcallzbl.user.constants.JwtClaimsConstant;
import com.mcallzbl.user.pojo.request.LoginRequest;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.pojo.response.LoginResponse;
import com.mcallzbl.user.pojo.response.VerificationEmailResponse;
import com.mcallzbl.user.service.AuthService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.mcallzbl.user.constants.AuthConstants.REFRESH_TOKEN;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口，包括登录、注册、登出、刷新Token等")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final SessionConfig sessionConfig;
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @ResponseWrapper
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest,
                               HttpServletResponse response) {
        log.debug("[com.mcallzbl.user.controller.AuthController.login()]" +
                " params: loginRequest={}", loginRequest);
        val user = authService.login(loginRequest);
        if(user == null) {
            throw BusinessException.of("登录失败");
        }
        val accessTokenInfo = jwtUtil.generateAccessToken(String.valueOf(user.getId()), null);
        val refreshTokenInfo = jwtUtil.generateRefreshToken(String.valueOf(user.getId()), null);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN, refreshTokenInfo.getToken());
        refreshCookie.setMaxAge((int) jwtUtil.getRefreshTokenExpirationSeconds());
        refreshCookie.setPath("/");
        refreshCookie.setDomain(sessionConfig.getDomain());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(sessionConfig.isSecure());
        response.addCookie(refreshCookie);

        return LoginResponse.builder()
                .accessToken(accessTokenInfo.getToken())
                .refreshToken(refreshTokenInfo.getToken())
                .nickname(user.getNickname())
                .accessTokenExpiresIn(accessTokenInfo.getExpiration())
                .refreshTokenExpiresIn(refreshTokenInfo.getExpiration())
                .build();
    }

    /**
     * 发送邮箱验证码
     *
     * @param verificationEmailDTO 邮箱验证码请求参数
     * @return 发送结果
     */
    @Operation(
            summary = "发送邮箱验证码",
            description = "向指定邮箱发送验证码，用于邮箱登录。"
    )
    @ResponseWrapper
    @PostMapping("/verification/emails")
    public VerificationEmailResponse sendEmailVerificationCode(@Valid @RequestBody VerificationEmailRequest verificationEmailDTO) {
        log.info("[AuthController.sendEmailVerificationCode] " +
                "params: email={}", verificationEmailDTO.getEmail());
        return emailVerificationService.sendVerificationCode(verificationEmailDTO);
    }
}
