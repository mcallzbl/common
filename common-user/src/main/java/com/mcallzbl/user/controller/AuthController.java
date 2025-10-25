package com.mcallzbl.user.controller;

import com.mcallzbl.user.pojo.dto.LoginDTO;
import com.mcallzbl.user.pojo.vo.LoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口，包括登录、注册、登出、刷新Token等")
public class AuthController {

    @PostMapping("/login")
    public LoginVO login(@Valid @RequestBody LoginDTO loginDTO,
                         HttpServletResponse response) {
        return null;
    }
}
