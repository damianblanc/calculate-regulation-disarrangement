package com.bymatech.calculateregulationdisarrangement.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "FCIComposition")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
/**
 * Respresents a composition element as a list of Composition in a FCIRegulation
 */
public class FCIComposition {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name="fci_composition_seq", sequenceName="fci_composition_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="fci_composition_seq")
    @Column(name = "fci_composition_id")
    private Integer fciCompositionId;

//    @EmbeddedId
//    private EmbeddedCompositionId fciCompositionId = new EmbeddedCompositionId();

//    @Column(name = "fci_regulation_id")
//    private int fciRegulationId;

//    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    @JoinColumn(name = "fci_regulation_id", insertable = false, updatable = false)
//    private FCIRegulation fciRegulation;

//    @OneToOne(mappedBy = "fciComposition", cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    private FCISpecieType fciSpecieType;

//    @OneToOne(cascade = CascadeType.REFRESH)
//    @JoinTable(name = "fci_composition_fci_specie_type",
//            joinColumns =
//                    { @JoinColumn(name = "fci_composition_id", referencedColumnName = "fci_composition_id") },
//            inverseJoinColumns =
//                    { @JoinColumn(name = "fci_specie_type_id", referencedColumnName = "fci_specie_type_id") })
//    @PrimaryKeyJoinColumn

    @Column(name = "fci_specie_type_id")
    private Integer fciSpecieTypeId;

    @Column(name = "percentage")
    private Double percentage;

//    @JsonProperty("fciRegulationId")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @ManyToOne(targetEntity = FCIRegulation.class)
//    @PrimaryKeyJoinColumn
//    @NotFound(action = NotFoundAction.IGNORE)
//    @ManyToOne(fetch = FetchType.EAGER, optional = true)
//    @JoinColumn(name = "fci_regulation_id", referencedColumnName = "fci_regulation_id", nullable = true)

//    @Column(name = "fci_regulation_id")
//    private Integer fciRegulationId;
}
