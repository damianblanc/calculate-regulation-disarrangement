package com.bymatech.calculateregulationdisarrangement.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class SpeciePosition {

    private String name;
    private SpecieType specieType;
    private Double price;
    private Integer quantity;


    public static SpeciePositionBuilder builder() { return new SpeciePositionBuilder(); }

    public Double valuePosition() {
        return price * quantity;
    }

    public static class SpeciePositionBuilder {
        private String name;
        private SpecieType specieType;
        private Double price;
        private Integer quantity;


        public SpeciePositionBuilder withName(@NonNull String name) {
            this.name = name;
            return this;
        }

        public SpeciePositionBuilder withSpecieType(SpecieType specieType) {
            this.specieType = specieType;
            return this;
        }

        public SpeciePositionBuilder withPrice(@NonNull Double price) {
            this.price = price;
            return this;
        }

        public SpeciePositionBuilder withQuantity(@NonNull Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public SpeciePosition build() {
            SpeciePosition speciePosition = new SpeciePosition();
            speciePosition.setName(name);
            speciePosition.setSpecieType(specieType);
            speciePosition.setPrice(price);
            speciePosition.setQuantity(quantity);
            return speciePosition;
        }
    }

}
