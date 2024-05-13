package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FCISpecieToSpecieTypePosition")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents an {@link FCIPosition} that reference a {@link FCISpeciePosition} through {@link FCISpecieToSpecieType}
 * Since a FCIPosition references a Specie (FCISpeciePosition) a restriction is imposed to update or even delete a binding between
 * {@link FCISpeciePosition} and {@link FCISpecieType}
 */
public class FCISpecieToSpecieTypePosition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "fci_position_id")
  private Integer fciPositionId;

  @Column(name = "position_created_on")
  private Timestamp fciPositionCreatedOn;
}
