package com.bymatech.calculateregulationdisarrangement.domain;

import com.bymatech.calculateregulationdisarrangement.dto.FCIPositionCompositionVO;
import com.bymatech.calculateregulationdisarrangement.util.NumberFormatHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

@Entity
@Table(name = "FCIPosition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
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

    public Date getDateCreatedOn() { return new Date(createdOn.getTime()); }

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

    public static List<FCISpeciePosition> getSpeciePositions(FCIPosition fciPosition, boolean updatedMarketPosition) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<FCISpeciePosition> FCISpeciePositions = new ArrayList<>();
            Iterator<JsonNode> elements = mapper.readTree(
                    updatedMarketPosition ? fciPosition.getUpdatedMarketPosition() : fciPosition.getJsonPosition()).elements();
            elements.forEachRemaining(e -> {
                        try {
                            FCISpeciePositions.add(mapper.treeToValue(e, FCISpeciePosition.class));
                        } catch (JsonProcessingException ex) {
                            throw new RuntimeException(ex);
                        };
                    });
        return FCISpeciePositions;
    }

    public static List<FCIPositionCompositionVO> getPositionComposition(FCIPosition fciPosition, boolean updatedMarketPosition) {
        AtomicInteger index = new AtomicInteger();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<FCISpeciePosition> positionCompositionList = new ArrayList<>();

        try {
            Iterator<JsonNode> elements = mapper.readTree(
                    updatedMarketPosition ? fciPosition.getUpdatedMarketPosition() : fciPosition.getJsonPosition()).elements();
            elements.forEachRemaining(e -> {
                try {
                    positionCompositionList.add(mapper.treeToValue(e, FCISpeciePosition.class));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            });
            return positionCompositionList.stream().map(p ->
                    new FCIPositionCompositionVO(index.getAndIncrement(), p.getFciSpecieGroup(), p.getFciSpecieType(),
                            p.getName(), p.getSymbol(), p.getCurrentMarketPrice(), p.getQuantity(), p.getCurrentMarketPrice() * p.getQuantity())).toList();
        } catch (final Exception ex) {
            log.warn(ex.getMessage());
        }
        return List.of();
    }

    public void updateMarketPosition(List<FCISpeciePosition> fciSpeciePositionList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        setUpdatedMarketPosition(mapper.writeValueAsString(fciSpeciePositionList));
    }

    public void setOverview(List<FCISpeciePosition> fciSpeciePositions) {
        StringBuffer specieTypeSums = new StringBuffer();

        Map<String, DoubleSummaryStatistics> m = fciSpeciePositions.stream().collect(groupingBy(FCISpeciePosition::getFciSpecieType,
                        summarizingDouble(FCISpeciePosition::valuePosition)));

        m.forEach((key, value) -> specieTypeSums.append(key).append(": $").append(NumberFormatHelper.format(value.getSum())).append(" "));
        Double totalPosition = m.values().stream().map(DoubleSummaryStatistics::getSum).reduce(Double::sum).orElseThrow();

            this.overview = String.format("Species:%d - Valued: $ %s - Totals: %s", fciSpeciePositions.size(), NumberFormatHelper.format(totalPosition),
                specieTypeSums.toString().replace(" $0 ", " N/A ")).replace("¤", "");
    }
}
