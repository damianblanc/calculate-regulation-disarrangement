package com.bymatech.calculateregulationdisarrangement.controller;

import com.bymatech.calculateregulationdisarrangement.dto.ConfigurationPropertyDto;
import com.bymatech.calculateregulationdisarrangement.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/configuration")
public class FCIConfigurationController {

  @Autowired
  private ConfigurationService configurationService;

  @PostMapping("")
  public ConfigurationPropertyDto createProperty(@RequestBody ConfigurationPropertyDto configurationPropertyDto) {
    return configurationService.createProperty(configurationPropertyDto);
  }

  @PostMapping("/encrypt/{encryptionMode}")
  public ConfigurationPropertyDto createProperty(@RequestBody ConfigurationPropertyDto configurationPropertyDto,
                                                  @PathVariable Boolean encryptionMode) throws Exception {
    return configurationService.createProperty(configurationPropertyDto, encryptionMode);
  }
}
