package com.mcallzbl.user.controller;

import com.mcallzbl.common.Result;
import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.user.config.SessionConfig;
import com.mcallzbl.user.constants.AuthConstants;
import com.mcallzbl.user.pojo.dto.TokenInfo;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.*;
import com.mcallzbl.user.pojo.response.LoginResponse;
import com.mcallzbl.user.pojo.response.RefreshTokenResponse;
import com.mcallzbl.user.pojo.response.VerificationEmailResponse;
import com.mcallzbl.user.service.AuthService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.service.UserService;
import com.mcallzbl.user.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;


    // ==================== 登录接口 ====================

    /**
     * 邮箱登录
     *
     * @param emailLoginRequest 邮箱登录请求参数
     * @param response          HTTP响应对象（用于设置Cookie）
     * @return 登录成功返回用户信息和双Token
     */
    @Operation(
            summary = "邮箱登录接口",
            description = "支持邮箱+密码登录，以及邮箱+验证码登录。当使用验证码登录且用户不存在时，会自动注册新用户。"
    )
    @ResponseWrapper
    @PostMapping("/email-login")
    public Result<LoginResponse> emailLogin(@Valid @RequestBody EmailLoginRequest emailLoginRequest,
                                            HttpServletResponse response) {
        log.debug("[com.mcallzbl.user.controller.AuthController.emailLogin()]" +
                " params: emailLoginRequest={}", emailLoginRequest);
        val user = authService.loginByEmail(emailLoginRequest);
        return generateLoginResponse(user, response);
    }

    /**
     * 用户名登录
     *
     * @param usernameLoginRequest 用户名登录请求参数
     * @param response             HTTP响应对象（用于设置Cookie）
     * @return 登录成功返回用户信息和双Token
     */
    @Operation(
            summary = "用户名登录接口",
            description = "支持用户名+密码登录。用户必须已注册存在。"
    )
    @ResponseWrapper
    @PostMapping("/username-login")
    public Result<LoginResponse> usernameLogin(@Valid @RequestBody UsernameLoginRequest usernameLoginRequest,
                                               HttpServletResponse response) {
        log.debug("[com.mcallzbl.user.controller.AuthController.usernameLogin()]" +
                " params: usernameLoginRequest={}", usernameLoginRequest);
        val user = authService.loginByUsername(usernameLoginRequest);
        return generateLoginResponse(user, response);
    }

    /**
     * 用户名密码注册
     *
     * @param usernameRegistrationRequest 用户名注册请求参数
     * @param response                    HTTP响应对象（用于设置Cookie）
     * @return 注册成功返回用户信息和双Token
     */
    @Operation(
            summary = "用户名注册接口",
            description = "支持用户名+邮箱+密码注册。需要管理员开启此功能，默认关闭。注册成功后自动登录。"
    )
    @ResponseWrapper
    @PostMapping("/username-registration")
    public Result<LoginResponse> usernameRegistration(@Valid @RequestBody UsernameRegistrationRequest usernameRegistrationRequest,
                                                      HttpServletResponse response) {
        log.debug("[com.mcallzbl.user.controller.AuthController.usernameRegistration()]" +
                " params: usernameRegistrationRequest={}", usernameRegistrationRequest);

        val user = authService.registerByUsername(usernameRegistrationRequest);
        return generateLoginResponse(user, response);
    }

    // ==================== 私有方法 ====================

    /**
     * 生成登录响应
     * 统一处理用户登录/注册成功后的响应生成
     *
     * @param user     用户对象
     * @param response HTTP响应对象（用于设置Cookie）
     * @return 登录响应对象
     */
    private Result<LoginResponse> generateLoginResponse(User user, HttpServletResponse response) {
        val accessTokenInfo = jwtUtil.generateAccessToken(String.valueOf(user.getId()), null);
        val refreshTokenInfo = generateRefreshTokenAndSetCookie(user, response);
        return Result.success(LoginResponse.builder()
                .accessToken(accessTokenInfo.getToken())
                .refreshToken(refreshTokenInfo.getToken())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .accessTokenExpiresIn(accessTokenInfo.getExpiration())
                .refreshTokenExpiresIn(refreshTokenInfo.getExpiration())
                .build()
        );
    }

    // ==================== 其他接口 ====================

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
    public Result<VerificationEmailResponse> sendEmailVerificationCode(@Valid @RequestBody VerificationEmailRequest verificationEmailDTO) {
        log.info("[AuthController.sendEmailVerificationCode] " +
                "params: email={}", verificationEmailDTO.getEmail());
        return Result.success(emailVerificationService.sendVerificationCode(verificationEmailDTO));
    }

    /**
     * 用户登出
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @return 登出结果
     */
    @Operation(
            summary = "用户登出",
            description = "用户登出接口。Web端自动从Cookie和Header获取Token，移动端需要在请求体中传入双Token。"
    )
    @ResponseWrapper
    @PostMapping("/logout")
    public String logout(
            @RequestBody(required = false) TokenRequest tokenRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 从请求体获取Token（移动端）
        String accessTokenFromBody = tokenRequest != null ? tokenRequest.getAccessToken() : null;
        String refreshTokenFromBody = tokenRequest != null ? tokenRequest.getRefreshToken() : null;

        // 从Header和Cookie获取Token（Web端）
        String accessTokenFromHeader = getAccessTokenFromRequest(request);
        String refreshTokenFromCookie = getRefreshTokenFromCookie(request);

        // 优先使用请求体中的Token
        String accessToken = accessTokenFromBody != null ? accessTokenFromBody : accessTokenFromHeader;
        String refreshToken = refreshTokenFromBody != null ? refreshTokenFromBody : refreshTokenFromCookie;

        log.info("[AuthController.logout] " +
                        "params: hasAccessToken={}, hasRefreshToken={}",
                accessToken != null,
                refreshToken != null);
        Cookie refreshCookie = new Cookie(REFRESH_TOKEN, "");
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setDomain(sessionConfig.getDomain());
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);

        log.info("用户登出完成");
        return "";
    }

    /**
     * 刷新访问令牌
     *
     * @param request  HTTP请求对象（用于从Cookie获取refreshToken）
     * @param response HTTP响应对象（用于更新Cookie）
     * @return 新的访问令牌和刷新令牌
     */
    @Operation(
            summary = "刷新访问令牌",
            description = "使用刷新Token获取新的访问Token。Web端从HttpOnly Cookie获取Refresh Token，移动端需要在请求体中传入。"
    )
    @ResponseWrapper
    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(
            @RequestBody(required = false) TokenRequest tokenRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String refreshTokenFromBody = tokenRequest != null ? tokenRequest.getRefreshToken() : null;
        String refreshTokenFromCookie = getRefreshTokenFromCookie(request);
        String refreshToken = refreshTokenFromBody != null ? refreshTokenFromBody : refreshTokenFromCookie;
        Long userId = Long.valueOf(jwtUtil.extractSubject(refreshToken));
        User user = userService.getUserById(userId);

        log.info("[AuthController.refreshToken] " +
                        "params: hasRefreshTokenInCookie={}",
                getRefreshTokenFromCookie(request) != null);

        val accessTokenInfo = jwtUtil.generateAccessToken(String.valueOf(user.getId()), null);
        val refreshTokenInfo = generateRefreshTokenAndSetCookie(user, response);

        return RefreshTokenResponse.builder()
                .accessToken(accessTokenInfo.getToken())
                .refreshToken(refreshTokenInfo.getToken())
                .accessTokenExpiresIn(accessTokenInfo.getExpiration())
                .refreshTokenExpiresIn(refreshTokenInfo.getExpiration())
                .build();
    }

    /**
     * 为用户生成refreshToken并设置Cookie
     *
     * @param user     用户对象
     * @param response HTTP响应对象（用于设置Cookie）
     * @return refreshToken信息
     */
    private TokenInfo generateRefreshTokenAndSetCookie(User user, HttpServletResponse response) {
        log.debug("[com.mcallzbl.user.controller.AuthController.generateRefreshTokenAndSetCookie]" +
                " params: userId={}", user.getId());

        val refreshTokenInfo = jwtUtil.generateRefreshToken(String.valueOf(user.getId()), null);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN, refreshTokenInfo.getToken());
        refreshCookie.setMaxAge((int) jwtUtil.getRefreshTokenExpirationSeconds());
        refreshCookie.setPath("/");
        refreshCookie.setDomain(sessionConfig.getDomain());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(sessionConfig.isSecure());
        response.addCookie(refreshCookie);

        return refreshTokenInfo;
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(AuthConstants.BEARER)) {
            return authHeader.substring(AuthConstants.BEARER.length());
        }
        return null;
    }
}
