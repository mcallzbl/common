package com.mcallzbl.commondemo;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.Result;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.common.annotation.NoResponseWrapper;
import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.common.exception.I18nBusinessException;
import com.mcallzbl.common.util.CommonI18nUtils;
import com.mcallzbl.user.context.UserContext;
import com.mcallzbl.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Demo控制器 - 展示公共模块的各种功能
 */
@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
@ResponseWrapper // 整个Controller启用响应包装
@Tag(name = "Demo示例", description = "展示公共模块的各种功能，包括响应包装、异常处理、参数校验、国际化等")
public class DemoController {

    private final CommonI18nUtils commonI18NUtils;
    private final I18nBusinessException i18nBusinessException;

    // ==================== Spring Security用户信息获取示例 ====================

    /**
     * 获取当前用户信息 - 兼容方式（使用原ThreadLocal）
     * GET /demo/me
     */
    @ResponseWrapper
    @GetMapping("/me")
    @Operation(summary = "获取当前用户（兼容方式）", description = "使用UserContext.ThreadLocal获取当前用户信息")
    public Result<com.mcallzbl.user.pojo.entity.User> getCurrentUserCompatible() {
        return Result.success(UserContext.getCurrentUser());
    }

    /**
     * 获取当前用户信息 - Spring Security方式1（SecurityContextHolder）
     * GET /demo/me-security
     */
    @ResponseWrapper
    @GetMapping("/me-security")
    @Operation(summary = "获取当前用户（SecurityContextHolder）", description = "使用Spring Security上下文获取当前用户信息")
    public Result<com.mcallzbl.user.pojo.entity.User> getCurrentUserBySecurityContext() {
        try {
            // 获取认证对象
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                // 获取用户详情
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                com.mcallzbl.user.pojo.entity.User user = userDetails.getUser();
                return Result.success(user);
            }

            return Result.failed("用户未登录");
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.failed("获取用户信息失败");
        }
    }

    /**
     * 获取当前用户信息 - Spring Security方式2（@AuthenticationPrincipal注解）
     * GET /demo/me-annotation
     */
    @ResponseWrapper
    @GetMapping("/me-annotation")
    @Operation(summary = "获取当前用户（@AuthenticationPrincipal）", description = "使用@AuthenticationPrincipal注解获取当前用户信息，推荐方式")
    public Result<com.mcallzbl.user.pojo.entity.User> getCurrentUserByAnnotation(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails != null) {
            return Result.success(userDetails.getUser());
        }

        return Result.failed("用户未登录");
    }

    /**
     * 获取当前用户信息 - Spring Security方式3（仅获取用户名）
     * GET /demo/me-username
     */
    @ResponseWrapper
    @GetMapping("/me-username")
    @Operation(summary = "获取当前用户名", description = "仅获取当前用户的用户名信息")
    public Result<String> getCurrentUsername() {
        try {
            // 获取认证对象的用户名
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return Result.success(authentication.getName());
            }
            return Result.failed("用户未登录");
        } catch (Exception e) {
            log.error("获取用户名失败", e);
            return Result.failed("获取用户名失败");
        }
    }

    @ResponseWrapper
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // ==================== 基础响应包装示例 ====================

    /**
     * 返回字符串 - 自动包装到data字段
     * GET /demo/string
     * 期望: {"code":200,"message":"操作成功","data":"hello","timestamp":...}
     */
    @GetMapping("/string")
    public String string() {
        return "hello";
    }

    /**
     * 返回对象 - 自动包装到data字段
     * GET /demo/user
     */
    @GetMapping("/user")
    public User getUser() {
        return User.builder()
                .id(1L)
                .username("admin")
                .email("admin@example.com")
                .build();
    }

    /**
     * 返回列表 - 自动包装到data字段
     * GET /demo/users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return Arrays.asList(
                User.builder().id(1L).username("admin").email("admin@example.com").build(),
                User.builder().id(2L).username("user1").email("user1@example.com").build()
        );
    }

    /**
     * 直接返回Result对象 - 会被转换为传输DTO
     * GET /demo/result
     */
    @GetMapping("/result")
    public Result<String> getResult() {
        return Result.success("这是直接返回的Result", "自定义成功消息");
    }

    /**
     * 返回数字 - 自动包装到data字段
     * GET /demo/number
     */
    @GetMapping("/number")
    public int getNumber() {
        return 42;
    }

    /**
     * 返回布尔值 - 自动包装到data字段
     * GET /demo/boolean
     */
    @GetMapping("/boolean")
    public boolean getBoolean() {
        return true;
    }

    // ==================== 异常处理示例 ====================

    /**
     * 抛出业务异常 - 被GlobalExceptionHandler处理
     * GET /demo/business-error
     * 期望: {"code":1001,"message":"用户不存在","data":null,"timestamp":...}
     */
    @GetMapping("/business-error")
    public User businessError() {
        throw BusinessException.userNotFound("用户不存在");
    }

    /**
     * 抛出参数校验异常 - 被GlobalExceptionHandler处理
     * GET /demo/validation-error
     */
    @GetMapping("/validation-error")
    public User validationError() {
        throw BusinessException.validationFailed("用户名不能为空");
    }

    /**
     * 抛出系统异常 - 被GlobalExceptionHandler处理
     * GET /demo/system-error
     */
    @GetMapping("/system-error")
    public User systemError() {
        throw new RuntimeException("系统内部错误");
    }

    /**
     * 手动抛出带数据的业务异常
     * GET /demo/business-error-with-data
     */
    @GetMapping("/business-error-with-data")
    public Result<Object> businessErrorWithData() {
        throw BusinessException.of(ResultCode.USER_NOT_FOUND, "用户ID不存在", 12345L);
    }

    // ==================== 参数校验示例 ====================

    /**
     * 使用Bean Validation校验参数
     * POST /demo/users
     * Body: {"username":"test","email":"test@example.com"}
     */
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("创建用户: {}", request);
        return User.builder()
                .id(3L)
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    /**
     * 路径参数校验
     * GET /demo/users/123
     */
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw BusinessException.validationFailed("用户ID必须大于0");
        }
        return User.builder()
                .id(id)
                .username("user" + id)
                .email("user" + id + "@example.com")
                .build();
    }

    // ==================== 禁用响应包装示例 ====================

    /**
     * 类级别启用响应包装，但方法级别禁用
     * 返回原始字符串，不会被包装
     * GET /demo/raw
     */
    @GetMapping("/raw")
    @NoResponseWrapper
    public String getRawResponse() {
        return "这是原始响应，不会被包装";
    }

    /**
     * 返回ResponseEntity - 不会被包装
     * GET /demo/response-entity
     */
    @GetMapping("/response-entity")
    public ResponseEntity<String> getResponseEntity() {
        return ResponseEntity.ok("这是ResponseEntity响应");
    }

    /**
     * 返回ResponseEntity with headers - 不会被包装
     * GET /demo/response-entity-with-headers
     */
    @GetMapping("/response-entity-with-headers")
    public ResponseEntity<User> getResponseEntityWithHeaders() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@example.com")
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Custom-Header", "test-value")
                .body(user);
    }

    // ==================== Result的各种静态方法示例 ====================

    /**
     * 使用Result.success()的各种变体
     * GET /demo/success-variants?type=1
     */
    @GetMapping("/success-variants")
    public Result<String> successVariants(@RequestParam(defaultValue = "1") int type) {
        return switch (type) {
            case 1 -> Result.success("数据");
            case 2 -> Result.success("数据", "自定义消息");
            case 3 -> Result.success("仅消息");
            case 4 -> Result.success();
            case 5 -> Result.success(HttpStatus.CREATED, "数据", "创建成功");
            default -> Result.failed("无效的type参数");
        };
    }

    /**
     * 使用Result.failed()的各种变体
     * GET /demo/failed-variants?type=1
     */
    @GetMapping("/failed-variants")
    public Result<String> failedVariants(@RequestParam(defaultValue = "1") int type) {
        return switch (type) {
            case 1 -> Result.failed("失败消息");
            case 2 -> Result.failed(ResultCode.USER_NOT_FOUND);
            case 3 -> Result.failed(HttpStatus.BAD_REQUEST, ResultCode.VALIDATION_FAILED);
            case 4 -> Result.validationFailed("参数错误");
            case 5 -> Result.userNotFound("用户不存在");
            default -> Result.success("默认成功");
        };
    }

    /**
     * 使用Result.of()方法
     * GET /demo/of-variants?type=1
     */
    @GetMapping("/of-variants")
    public Result<String> ofVariants(@RequestParam(defaultValue = "1") int type) {
        return switch (type) {
            case 1 -> Result.success("数据");
            case 2 -> Result.success("数据", "自定义消息");
            case 3 -> Result.failed(ResultCode.BUSINESS_ERROR);
            case 4 -> Result.failed("业务异常");
            default -> Result.success("默认数据");
        };
    }

    // ==================== BusinessException的各种静态方法示例 ====================

    /**
     * 测试BusinessException的各种静态方法
     * GET /demo/business-exception-variants?type=1
     */
    @GetMapping("/business-exception-variants")
    public User businessExceptionVariants(@RequestParam(defaultValue = "1") int type) {
        switch (type) {
            case 1 -> throw BusinessException.validationFailed("参数校验失败");
            case 2 -> throw BusinessException.unauthorized("未授权访问");
            case 3 -> throw BusinessException.forbidden("权限不足");
            case 4 -> throw BusinessException.notFound("资源不存在");
            case 5 -> throw BusinessException.userNotFound("用户不存在");
            case 6 -> throw BusinessException.userAlreadyExists("用户已存在");
            case 7 -> throw BusinessException.passwordIncorrect("密码错误");
            case 8 -> throw BusinessException.captchaInvalid("验证码错误");
            case 9 -> throw BusinessException.tokenInvalid("token无效");
            case 10 -> throw BusinessException.businessError("业务错误");
            default -> throw BusinessException.of("自定义异常消息");
        }
    }

    // ==================== 国际化功能示例 ====================

    /**
     * 获取当前Locale信息
     * GET /demo/locale-info
     */
    @GetMapping("/locale-info")
    @Operation(summary = "获取当前Locale信息", description = "返回当前请求的本地化信息")
    public Result<Map<String, Object>> getLocaleInfo() {
        Map<String, Object> localeInfo = new HashMap<>();
        Locale currentLocale = commonI18NUtils.getCurrentLocale();

        localeInfo.put("currentLocale", currentLocale.toString());
        localeInfo.put("language", currentLocale.getLanguage());
        localeInfo.put("country", currentLocale.getCountry());
        localeInfo.put("displayName", currentLocale.getDisplayName());
        localeInfo.put("isChinese", commonI18NUtils.isChineseLocale());
        localeInfo.put("isEnglish", commonI18NUtils.isEnglishLocale());
        localeInfo.put("isJapanese", commonI18NUtils.isJapaneseLocale());

        return Result.success(localeInfo);
    }

    /**
     * 获取指定语言的欢迎消息
     * GET /demo/i18n/welcome?language=zh_CN
     */
    @GetMapping("/i18n/welcome")
    @Operation(summary = "获取欢迎消息", description = "根据指定的语言参数返回对应的欢迎消息")
    public Result<String> getWelcomeMessage(
            @RequestParam(defaultValue = "zh_CN") String language) {
        String message = commonI18NUtils.getMessage("common.success", language);
        return Result.success(message);
    }

    /**
     * 批量获取多个国际化消息
     * GET /demo/i18n/messages?language=en
     */
    @GetMapping("/i18n/messages")
    @Operation(summary = "批量获取消息", description = "一次性获取多个国际化消息")
    public Result<Map<String, String>> getMessages(
            @RequestParam(defaultValue = "zh_CN") String language) {

        Map<String, String> messages = new HashMap<>();

        // 获取多个常用的国际化消息
        messages.put("success", commonI18NUtils.getMessage("common.success", language));
        messages.put("failed", commonI18NUtils.getMessage("common.failed", language));
        messages.put("userNotFound", commonI18NUtils.getMessage("user.not.found", language));
        messages.put("captchaInvalid", commonI18NUtils.getMessage("captcha.invalid", language));
        messages.put("rateLimitExceeded", commonI18NUtils.getMessage("rate.limit.exceeded", language));

        return Result.success(messages);
    }

    /**
     * 抛出国际化业务异常示例
     * GET /demo/i18n/exception?type=userNotFound&language=en
     */
    @GetMapping("/i18n/exception")
    @Operation(summary = "国际化异常示例", description = "抛出支持多语言的业务异常")
    public Result<String> throwI18nException(
            @RequestParam(defaultValue = "userNotFound") String type,
            @RequestParam(defaultValue = "zh_CN") String language) {

        // 根据不同的条件抛出不同的国际化异常
        switch (type) {
            case "userNotFound" -> throw i18nBusinessException.create(ResultCode.USER_NOT_FOUND, language);
            case "verificationCode" -> throw i18nBusinessException.verificationCodeRequired();
            case "emailVerification" -> throw i18nBusinessException.emailVerificationCodeError();
            case "rateLimit" -> throw i18nBusinessException.rateLimitExceeded();
            case "systemBusy" -> throw i18nBusinessException.systemBusy();
            default -> throw i18nBusinessException.create(ResultCode.VALIDATION_FAILED, language);
        }
    }

    /**
     * 验证码错误异常示例
     * GET /demo/i18n/verification-code-error
     */
    @GetMapping("/i18n/verification-code-error")
    @Operation(summary = "验证码错误示例", description = "演示验证码相关的国际化异常")
    public Result<String> verificationCodeError() {
        throw i18nBusinessException.verificationCodeRequired();
    }

    /**
     * 邮箱验证码错误异常示例
     * GET /demo/i18n/email-verification-error
     */
    @GetMapping("/i18n/email-verification-error")
    @Operation(summary = "邮箱验证码错误示例", description = "演示邮箱验证码相关的国际化异常")
    public Result<String> emailVerificationCodeError() {
        throw i18nBusinessException.emailVerificationCodeError();
    }

    // ==================== 内部数据类 ====================

    @Data
    @Builder
    public static class User {
        private Long id;
        private String username;
        private String email;
    }

    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;
    }
}