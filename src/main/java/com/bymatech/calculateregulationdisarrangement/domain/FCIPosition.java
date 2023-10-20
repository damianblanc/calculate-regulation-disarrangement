package com.bymatech.calculateregulationdisarrangement.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

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

    @Column(name = "jsonPosition", length = 4096)
    private String jsonPosition;

    @Column(name = "overview")
    private String overview;

    @Column(name = "created_on")
    private Timestamp createdOn;

    public void setCreatedOn() {
        this.createdOn = new Timestamp(Instant.now().toEpochMilli());
    }

    public String getCreatedOn( SimpleDateFormat sdf) {
        return sdf.format(this.createdOn);
    }

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

    public List<SpeciePosition> getSpeciePositions(FCIPosition fciPosition) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<SpeciePosition> speciePositions = new ArrayList<>();
            Iterator<JsonNode> elements = mapper.readTree(fciPosition.jsonPosition).elements();
            elements.forEachRemaining(e -> {
                        try {
                            speciePositions.add(mapper.treeToValue(e, SpeciePosition.class));
                        } catch (JsonProcessingException ex) {
                            throw new RuntimeException(ex);
                        };
                    });
        return speciePositions;
    }

    public void setOverview(FCIPosition fciPosition) throws JsonProcessingException {
        List<SpeciePosition> speciePositions = getSpeciePositions(fciPosition);
        StringBuffer specieTypeSums = new StringBuffer();

        Map<SpecieType, DoubleSummaryStatistics> m =
                speciePositions.stream().collect(groupingBy(SpeciePosition::getSpecieType,
                        summarizingDouble(SpeciePosition::valuePosition)));

        m.forEach((key, value) -> specieTypeSums.append(key.name()).append(": ").append(value.getSum()).append(" "));
        Double totalPosition = m.values().stream().map(DoubleSummaryStatistics::getSum).reduce(Double::sum).orElseThrow();

        this.overview = String.format("Species:%d - Valued: %.2f - Totals: %s", speciePositions.size(), totalPosition, specieTypeSums);
    }
}
