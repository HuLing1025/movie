package com.example.cinema.controller.user;

import com.example.cinema.blImpl.user.AccountServiceImpl;
import com.example.cinema.config.InterceptorConfiguration;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Api(tags = "user")
@RestController()
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR="用户名或密码错误";
    @Autowired
    private AccountServiceImpl accountService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.login(userForm);
        if(user==null){
           return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        //注册session
        int level = user.getPrivilegeLevel();
        userForm.setPrivilegeLevel(level);
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserForm userForm){
        return accountService.registerAccount(userForm);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute(InterceptorConfiguration.SESSION_KEY);
        return "index";
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/account/create")
    public ResponseVO createAccount(@RequestBody UserForm userForm) {
        return accountService.createAccount(userForm);
    }

    @ApiOperation(value = "用户详情")
    @GetMapping("/account/detail")
    public ResponseVO getUserDetail(@RequestParam Integer userId) {
        return accountService.getUserDetailByUserId(userId);
    }

    @ApiOperation(value = "删除用户")
    @GetMapping("/account/delete")
    public ResponseVO deleteUser(@RequestParam Integer userId) {
        return accountService.deleteUser(userId);
    }

    @ApiOperation(value = "校验密码")
    @PostMapping("/account/check/password")
    public ResponseVO checkPassword(@RequestBody UserForm userForm) {
        return accountService.checkPassword(userForm);
    }

    @ApiOperation(value = "用户列表")
    @GetMapping("/account/list")
    public ResponseVO getUserList() {
        return accountService.getUserList();
    }
}
