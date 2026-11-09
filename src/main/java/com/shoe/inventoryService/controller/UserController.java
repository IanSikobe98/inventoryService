package com.shoe.inventoryService.controller;

import com.shoe.inventoryService.dto.response.ApiResponse;
import com.shoe.inventoryService.service.ApiService;
import com.shoe.inventoryService.service.AuthenticationService;
import com.shoe.inventoryService.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ApiService apiService;

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse logout(HttpServletResponse httpServletResponse){
        log.info("LOGOUT  REQUEST ");
        ApiResponse response = apiService.logout(httpServletResponse,true,null);
        log.info("RESPONSE: {}", response);
        return  response;
    }
}
