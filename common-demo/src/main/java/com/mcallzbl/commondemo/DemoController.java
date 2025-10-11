package com.mcallzbl.commondemo;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.Result;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.common.annotation.NoResponseWrapper;
import com.mcallzbl.common.annotation.ResponseWrapper;
import lombok.Data;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

/**
 * Demo控制器 - 展示公共模块的各种功能
 */
@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
@ResponseWrapper // 整个Controller启用响应包装
public class DemoController {

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