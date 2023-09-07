package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.domain.FCIRegulation;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines all FCI Regulation Data for testing purposes
 */
public class FCITestFixture {

   protected static String fCIRegulationName1 = "Alpha Mix Rent FCI";
   protected static Map<SpecieType, Double> regulationComposition1 = createRegulationComposition1();

   /** Specie Position Definition */
   /** SHARE MARKET */
   protected static String speciePositionName1 = "GGAL";
   protected static SpecieType speciePositionType1 = SpecieType.MARKET_SHARE;
   protected static Double speciePositionPrice1 = 3.15;
   protected static Integer speciePositionQuantity1 = 200;

   protected static String speciePositionName2 = "YPF";
   protected static SpecieType speciePositionType2 = SpecieType.MARKET_SHARE;
   protected static Double speciePositionPrice2 = 8.5;
   protected static Integer speciePositionQuantity2 = 600;

   /** BOND */
   protected static String speciePositionName3 = "GD30";
   protected static SpecieType speciePositionType3 = SpecieType.BOND;
   protected static Double speciePositionPrice3 = 0.6;
   protected static Integer speciePositionQuantity3 = 4000;

   protected static String speciePositionName4 = "AL30";
   protected static SpecieType speciePositionType4 = SpecieType.BOND;
   protected static Double speciePositionPrice4 = 1.4;
   protected static Integer speciePositionQuantity4 = 3000;

   /** CASH */
   protected static String speciePositionName5 = "CASH";
   protected static SpecieType speciePositionType5 = SpecieType.CASH;
   protected static Double speciePositionPrice5 = 250000.00;
   protected static Integer speciePositionQuantity5 = 1;

   /**
    * Creates a Regulation Composition to establish its Specie Type percentages
    */
   private static Map<SpecieType, Double> createRegulationComposition1() {
      return ImmutableMap.<SpecieType, Double>builder()
              .put(SpecieType.MARKET_SHARE, 30.00)
              .put(SpecieType.BOND, 50.00)
              .put(SpecieType.CASH, 20.00).build();
   }

   /**
    * Creates a FCIPosition that comprehends a FCIRegulation & FCISpeciePosition
    * An FCIPosition represents what it is needed to be posted in order to process
    */
   protected FCIPosition createFCIPosition(FCIRegulation fciRegulation, List<SpeciePosition> speciePositionList) {
      return new FCIPosition(fciRegulation, speciePositionList);
   }

   protected FCIRegulation createFCIRegulation1() {
      return createFCIRegulation(fCIRegulationName1, regulationComposition1);
   }

   /**
    * Creates a FCIRegulation to establish its percentage composition
    */
   protected FCIRegulation createFCIRegulation(String fCIRegulationName, Map<SpecieType, Double> regulationComposition) {
      return FCIRegulation.builder().withName(fCIRegulationName)
              .withComposition(regulationComposition)
              .build();
   }


   /** @noinspection DataFlowIssue*/
   protected List<SpeciePosition> createSpeciePositionList1() {
      return List.of(
         SpeciePosition.builder().withName(speciePositionName1).withSpecieType(speciePositionType1).withPrice(speciePositionPrice1).withQuantity(speciePositionQuantity1).build(),
         SpeciePosition.builder().withName(speciePositionName2).withSpecieType(speciePositionType2).withPrice(speciePositionPrice2).withQuantity(speciePositionQuantity2).build(),
         SpeciePosition.builder().withName(speciePositionName3).withSpecieType(speciePositionType3).withPrice(speciePositionPrice3).withQuantity(speciePositionQuantity3).build(),
         SpeciePosition.builder().withName(speciePositionName4).withSpecieType(speciePositionType4).withPrice(speciePositionPrice4).withQuantity(speciePositionQuantity4).build(),
         SpeciePosition.builder().withName(speciePositionName5).withSpecieType(speciePositionType5).withPrice(speciePositionPrice5).withQuantity(speciePositionQuantity5).build());
   }

   /** @noinspection DataFlowIssue*/
   protected List<SpeciePosition> createSpeciePositionList2() {
      return List.of(
              SpeciePosition.builder().withName(speciePositionName1).withSpecieType(speciePositionType1).withPrice(speciePositionPrice1).withQuantity(speciePositionQuantity1).build(),
              SpeciePosition.builder().withName(speciePositionName2).withSpecieType(speciePositionType2).withPrice(speciePositionPrice2).withQuantity(speciePositionQuantity2).build(),
              SpeciePosition.builder().withName(speciePositionName3).withSpecieType(speciePositionType3).withPrice(speciePositionPrice3).withQuantity(speciePositionQuantity3).build(),
              SpeciePosition.builder().withName(speciePositionName4).withSpecieType(speciePositionType4).withPrice(speciePositionPrice4).withQuantity(speciePositionQuantity4).build());
   }
}
