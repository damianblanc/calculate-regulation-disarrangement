package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a running to retrieve disarrangement comparing between current position and fci regulations
 */
@Entity
@Table(name = "Retrievals")
public class Retrieval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDateTime timestamp;

//    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //    LocalDateTime now = LocalDateTime.now();
//   System.out.println(dtf.format(now));
}
