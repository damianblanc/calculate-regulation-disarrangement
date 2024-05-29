package com.bymatech.calculateregulationdisarrangement.service;

import org.springframework.stereotype.Service;

/**
 * Defines Encryption operations
 */
@Service
public interface EncryptionService {

  String encrypt(String data) throws Exception;

  String decrypt(String encryptedData) throws Exception;
}
