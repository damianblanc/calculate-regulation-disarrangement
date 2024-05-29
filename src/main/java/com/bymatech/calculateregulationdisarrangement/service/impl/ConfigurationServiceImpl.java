package com.bymatech.calculateregulationdisarrangement.service.impl;

import com.bymatech.calculateregulationdisarrangement.domain.FCIConfiguration;
import com.bymatech.calculateregulationdisarrangement.dto.ConfigurationPropertyDto;
import com.bymatech.calculateregulationdisarrangement.exception.AuthorizationException;
import com.bymatech.calculateregulationdisarrangement.repository.ConfigurationRepository;
import com.bymatech.calculateregulationdisarrangement.service.ConfigurationService;
import com.bymatech.calculateregulationdisarrangement.service.EncryptionService;
import com.bymatech.calculateregulationdisarrangement.util.ExceptionMessage;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/**
 * Comprehends operations to work with general configuration
 */
public class ConfigurationServiceImpl implements ConfigurationService {

  @Autowired
  private ConfigurationRepository configurationRepository;

  @Autowired
  private EncryptionService encryptionService;

  @Override
  public ConfigurationPropertyDto createProperty(ConfigurationPropertyDto configurationPropertyDto) {
    if (configurationRepository.findByKey(configurationPropertyDto.getKey()).isPresent()) {
      throw new AuthorizationException(String.format(ExceptionMessage.PROPERTY_KEY_ALREADY_DEFINED.msg, configurationPropertyDto.getKey()));
    }
    FCIConfiguration saveProperty = configurationRepository.save(toFciConfiguration(configurationPropertyDto));
    return toConfigurationPropertyDto(saveProperty);
  }

  @Override
  public ConfigurationPropertyDto createProperty(ConfigurationPropertyDto configurationPropertyDto,
      Boolean encryptionMode) throws Exception {
    String encryptedValue = encryptionService.encrypt(configurationPropertyDto.getValue());
    configurationPropertyDto.setValue(encryptedValue);
    return createProperty(configurationPropertyDto);
  }

  @Override
  public Optional<ConfigurationPropertyDto> getProperty(String key) {
    return configurationRepository.findByKey(key).map(this::toConfigurationPropertyDto);
  }

  private static FCIConfiguration toFciConfiguration(ConfigurationPropertyDto configurationPropertyDto) {
    return FCIConfiguration.builder().key(configurationPropertyDto.getKey())
        .value(configurationPropertyDto.getValue()).build();
  }

  private ConfigurationPropertyDto toConfigurationPropertyDto(FCIConfiguration property) {
    return new ConfigurationPropertyDto(property.getKey(), property.getValue());
  }
}
