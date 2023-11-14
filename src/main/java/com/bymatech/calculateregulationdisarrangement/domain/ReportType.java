package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ReportTypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private String link;

}
