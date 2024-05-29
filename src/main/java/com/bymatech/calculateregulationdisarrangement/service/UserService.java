package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.domain.User;
import com.bymatech.calculateregulationdisarrangement.dto.LoginUser;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public interface UserService {

  String login(LoginUser loginUser) throws NoSuchAlgorithmException, InvalidKeySpecException;

  User registerUser(String secretPassKey, LoginUser loginUser) throws Exception;
}
