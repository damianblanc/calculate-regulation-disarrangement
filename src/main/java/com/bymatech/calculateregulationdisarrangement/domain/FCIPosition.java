package com.bymatech.calculateregulationdisarrangement.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FCIPosition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FCIPosition {
    @Transient
    private JsonNode position;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name ="jsonPosition", length = 4096)
    private String jsonPosition;

    @Transient
    public JsonNode getPosition() {
        return position;
    }

    public void setPosition(JsonNode position) {
        this.jsonPosition = position.toString();
    }

    public String getJsonPosition() {
        return this.jsonPosition;
    }

//    public void setJsonPosition(String jsonString) {
//        this.jsonPosition = jsonString;
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            this.position = mapper.readTree(jsonString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
