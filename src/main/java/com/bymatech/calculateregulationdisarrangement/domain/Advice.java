package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Advices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advice {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @Column(name = "created_on")
  private Timestamp createdOn;

  public void setCreatedOn() {
    this.createdOn = new Timestamp(Instant.now().toEpochMilli());
  }
}