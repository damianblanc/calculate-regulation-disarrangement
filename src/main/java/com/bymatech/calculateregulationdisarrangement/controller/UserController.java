package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.domain.User;
import com.bymatech.calculateregulationdisarrangement.dto.LoginUser;
import com.bymatech.calculateregulationdisarrangement.service.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("login")
  public ResponseEntity<String> login(@RequestBody LoginUser loginUser) throws Exception {
    String login = userService.login(loginUser);
    return ResponseEntity.ok(login);
  }

  @PostMapping("register/secret/{secretPass}")
  public User register(@PathVariable String secretPass,  @RequestBody LoginUser loginUser) throws Exception {
    return userService.registerUser(secretPass, loginUser);
  }
}
