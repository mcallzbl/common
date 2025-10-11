package com.mcallzbl.commondemo;

import com.mcallzbl.common.annotation.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DemonController {

    @ResponseWrapper
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}