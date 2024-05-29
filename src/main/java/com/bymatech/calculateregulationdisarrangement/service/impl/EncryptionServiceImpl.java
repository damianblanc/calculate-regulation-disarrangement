package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIConfiguration;
import com.bymatech.calculateregulationdisarrangement.dto.ConfigurationPropertyDto;
import com.bymatech.calculateregulationdisarrangement.exception.AuthorizationException;
import com.bymatech.calculateregulationdisarrangement.repository.ConfigurationRepository;
import com.bymatech.calculateregulationdisarrangement.service.ConfigurationService;
import com.bymatech.calculateregulationdisarrangement.service.EncryptionService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

  private SecretKey secretKey;
  private static final String ALGORITHM_KEY = "algorithm_key";

  @Autowired
  private ConfigurationRepository configurationRepository;

  public EncryptionServiceImpl() {}

  public String encrypt(String data) throws Exception {
    retrieveSecretKey();
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedByteArray = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encryptedByteArray);
  }

  public String decrypt(String encryptedData) throws Exception {
    retrieveSecretKey();
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedByteArray = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    return new String(decryptedByteArray, StandardCharsets.UTF_8);
  }

  private void retrieveSecretKey() {
    Optional<FCIConfiguration> algorithmProperty = configurationRepository.findByKey(ALGORITHM_KEY);
    if (algorithmProperty.isEmpty()) {
      throw new AuthorizationException(ExceptionMessage.ALGORITHM_PROPERTY_NOT_DEFINED.msg);
    }
    byte[] keyBytes = Base64.getDecoder().decode(algorithmProperty.get().getValue());
    secretKey = new SecretKeySpec(keyBytes, "AES");
  }
}
