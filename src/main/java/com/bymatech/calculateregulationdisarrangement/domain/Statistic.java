package com.bymatech.calculateregulationdisarrangement.domain;

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
@Table(name = "Statistic")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistic {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private Integer reportQuantity;
  private Integer adviceQuantity;
}
