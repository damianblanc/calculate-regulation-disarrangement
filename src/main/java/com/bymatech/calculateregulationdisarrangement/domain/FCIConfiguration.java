package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FCIConfiguration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FCIConfiguration {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "fciKey")
  private String key;

  @Column(name = "value")
  private String value;
}
