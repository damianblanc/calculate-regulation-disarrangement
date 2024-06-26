package com.bymatech.calculateregulationdisarrangement.domain;


import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "FCIRegulation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
/**
 * Represents FCI Regulation Composition by defining each {@link SpecieTypeGroupEnum} and its percentage
 */
public class FCIRegulation implements Comparable<FCIRegulation> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fci_regulation_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "REGULATION_ID", referencedColumnName = "fci_regulation_id")
    private List<FCIComposition> composition;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "REGULATION_ID", referencedColumnName = "fci_regulation_id")
    private List<FCIPosition> positions;

    @Column(name = "created_on")
    private Timestamp createdOn;

    public void setCreatedOn() {
        this.createdOn = new Timestamp(Instant.now().toEpochMilli());
    }

    public String getCreatedOn( SimpleDateFormat sdf) {
        return sdf.format(this.createdOn);
    }

    public Date getDateCreatedOn() {
        return new Date(createdOn.getTime());
    }

    public List<FCIPosition> getPositions() {
        return Objects.nonNull(positions) ? positions : List.of();
    }

    @Override
    public int compareTo(@NotNull FCIRegulation fciRegulation) {
        return (fciRegulation.getCreatedOn().equals(this.getCreatedOn())
        && !fciRegulation.getPositions().isEmpty()) ? 0 : 1;
    }

    public void setComposition(List<FCIComposition> composition) {
        if(this.composition == null) {
            this.composition = composition;
        } else {
            this.composition.clear();
            this.composition.addAll(composition);
        }
    }
}
