package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.User;
import com.bymatech.calculateregulationdisarrangement.dto.LoginUser;
import com.bymatech.calculateregulationdisarrangement.exception.AuthorizationException;
import com.bymatech.calculateregulationdisarrangement.repository.UserRepository;
import com.bymatech.calculateregulationdisarrangement.service.UserService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

  private String jwtSecretKey;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  public UserServiceImpl(@Value("${jwt.secret.key}") String jwtSecretKey) {
    this.jwtSecretKey = jwtSecretKey;
  }

  @Override
  public String login(final LoginUser loginUser) throws NoSuchAlgorithmException, InvalidKeySpecException {
    User user = userRepository.findByUsername(loginUser.getUsername())
        .orElseThrow(() -> new AuthorizationException(ExceptionMessage.USER_NAME_DOES_NOT_EXIST.msg));
    if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
      return generateToken(loginUser.getUsername());
    }
    throw new AuthorizationException(ExceptionMessage.USER_INCORRECT_PASSWORD.msg);
  }

  @Override
  public void registerUser(LoginUser loginUser) {
    userRepository.findByUsername(loginUser.getUsername())
        .ifPresent(user -> {
          throw new IllegalArgumentException(ExceptionMessage.USER_NAME_ALREADY_REGISTERED.msg);
        });

    User newUser =
        User.builder()
        .username(loginUser.getUsername())
        .password(passwordEncoder.encode(loginUser.getPassword()))
        .enable(true).build();
    userRepository.save(newUser);
  }


  private String generateToken(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Set token expiration time (e.g., 1 hour)
    long expirationTimeMillis = System.currentTimeMillis() + 3600000;

    // Generate JWT token
    Object SignatureAlgorithm;
    return Jwts.builder()
        .subject(username)
        .expiration(new Date(expirationTimeMillis))
//        .signWith(generateJwtKeyDecryption(jwtSecretKey))
        .compact();
  }

  public PublicKey generateJwtKeyDecryption(String jwtPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] keyBytes = Base64.decodeBase64(jwtPrivateKey);
    X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
    return keyFactory.generatePublic(x509EncodedKeySpec);
  }
}