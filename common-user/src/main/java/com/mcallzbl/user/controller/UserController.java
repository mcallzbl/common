package com.mcallzbl.user.controller;

import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.user.pojo.vo.UserVO;
import com.mcallzbl.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息控制器
 * 提供用户信息查询和管理相关接口
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/17
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@ResponseWrapper
@Tag(name = "用户信息管理", description = "用户信息查询和管理相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @GetMapping("/me")
    @Operation(
            summary = "获取当前用户信息",
            description = "获取当前登录用户的详细信息，包括基本信息、状态、登录记录等"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "用户未登录或Token已过期"),
            @ApiResponse(responseCode = "403", description = "用户权限不足"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public UserVO getCurrentUser() {
        log.debug("获取当前登录用户信息");
        return userService.getCurrentUserVO();
    }

}