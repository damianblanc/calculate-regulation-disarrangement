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
import java.text.DecimalFormat;
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

    @Column(name = "marketPosition", length = 8192)
    private String updatedMarketPosition;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "originalPosition", length = 4096)
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

    public static List<FCISpeciePosition> getSpeciePositions(FCIPosition fciPosition) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<FCISpeciePosition> FCISpeciePositions = new ArrayList<>();
            Iterator<JsonNode> elements = mapper.readTree(fciPosition.getJsonPosition()).elements();
            elements.forEachRemaining(e -> {
                        try {
                            FCISpeciePositions.add(mapper.treeToValue(e, FCISpeciePosition.class));
                        } catch (JsonProcessingException ex) {
                            throw new RuntimeException(ex);
                        };
                    });
        return FCISpeciePositions;
    }

    public void updateMarketPosition(List<FCISpeciePosition> fciSpeciePositionList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        setUpdatedMarketPosition(mapper.writeValueAsString(fciSpeciePositionList));
    }

    public void setOverview(List<FCISpeciePosition> fciSpeciePositions) {
        StringBuffer specieTypeSums = new StringBuffer();
        DecimalFormat df = new DecimalFormat("#.##");

        Map<String, DoubleSummaryStatistics> m =
                fciSpeciePositions.stream().collect(groupingBy(FCISpeciePosition::getFciSpecieType,
                        summarizingDouble(FCISpeciePosition::valuePosition)));

        m.forEach((key, value) -> specieTypeSums.append(key).append(": $").append(df.format(value.getSum())).append(" "));
        Double totalPosition = m.values().stream().map(DoubleSummaryStatistics::getSum).reduce(Double::sum).orElseThrow();

        this.overview = String.format("Species:%d - Valued: $%.2f - Totals: %s", fciSpeciePositions.size(), totalPosition,
                specieTypeSums.toString().replace(" $0 ", " N/A "));
    }
}
