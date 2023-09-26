package com.bymatech.calculateregulationdisarrangement.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "advices")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCIPositionAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner")
    private String owner;

    @Column(name = "registration_time")
    private Timestamp timestamp;

    /**
     * JSON formatted Advice processing outcome
     */
    @Column(name = "advice")
    private String advice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="fci_regulation_id",referencedColumnName = "fci_regulation_id")
    @JsonBackReference
    private FCIRegulation fciRegulation;

}
