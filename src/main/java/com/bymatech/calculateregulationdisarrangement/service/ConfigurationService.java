package com.bymatech.calculateregulationdisarrangement.service;

import com.bymatech.calculateregulationdisarrangement.dto.ConfigurationPropertyDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
/**
 * Comprehends operations to work with general configuration
 */
public interface ConfigurationService {

  ConfigurationPropertyDto createProperty(ConfigurationPropertyDto configurationPropertyDto);

  ConfigurationPropertyDto createProperty(ConfigurationPropertyDto configurationPropertyDto, Boolean encryptionMode)
      throws Exception;

  Optional<ConfigurationPropertyDto> getProperty(String key);
}
