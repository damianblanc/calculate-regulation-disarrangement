package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.SpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieType;
import com.bymatech.calculateregulationdisarrangement.dto.FCIPosition;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines all FCI Regulation Data for testing purposes
 */
public class FCITestFixture {

   protected FCIPosition fciPosition1;
   protected FCIRegulationDTO fciRegulation1;
   protected List<SpeciePosition> speciePositionList1;


   protected static String fCIRegulationName1 = "Alpha Mix Rent FCI";

   protected static String fCIRegulationSymbol1 = "AMR23";
   protected static Map<SpecieType, Double> regulationComposition1 = createRegulationComposition();

   /** Specie Position Definition */
   /** SHARE MARKET */
   protected static String speciePositionName1 = "BANCO GALICIA";

   protected static String speciePositionSymbol1 = "GGAL";
   protected static SpecieType speciePositionType1 = SpecieType.Equity;
   protected static Double speciePositionPrice1 = 3.15;
   protected static Integer speciePositionQuantity1 = 1500;

   protected static String speciePositionName2 = "YPF ESTATAL";

   protected static String speciePositionSymbol2 = "YPF";
   protected static SpecieType speciePositionType2 = SpecieType.Equity;
   protected static Double speciePositionPrice2 = 8.5;
   protected static Integer speciePositionQuantity2 = 6000;

   /** BOND */
   protected static String speciePositionName3 = "GLOBAL BOND GD41";

   protected static String speciePositionSymbol3 = "GD41";
   protected static SpecieType speciePositionType3 = SpecieType.Bond;
   protected static Double speciePositionPrice3 = 0.6;
   protected static Integer speciePositionQuantity3 = 40000;

   protected static String speciePositionName4 = "LOCAL BOND T3X4";
   protected static String speciePositionSymbol4 = "T3X4";
   protected static SpecieType speciePositionType4 = SpecieType.Bond;
   protected static Double speciePositionPrice4 = 1.4;
   protected static Integer speciePositionQuantity4 = 30000;

   /** CASH */
   protected static String speciePositionName5 = "CASH";

   protected static String speciePositionSymbol5 = "CASH";
   protected static SpecieType speciePositionType5 = SpecieType.Cash;
   protected static Double speciePositionPrice5 = 25000.00;
   protected static Integer speciePositionQuantity5 = 1;

   /**
    * Creates a Regulation Composition to establish its Specie Type percentages
    */
   private static Map<SpecieType, Double> createRegulationComposition() {
      return ImmutableMap.<SpecieType, Double>builder()
              .put(SpecieType.Equity, 30.00)
              .put(SpecieType.Bond, 50.00)
              .put(SpecieType.Cash, 20.00).build();
   }

   /**
    * Creates a FCIPosition that comprehends a FCIRegulation & FCISpeciePosition
    * An FCIPosition represents what it is needed to be posted in order to process
    */
   protected FCIPosition createFCIPosition(FCIRegulationDTO fciRegulationDTO, List<SpeciePosition> speciePositionList) {
      return new FCIPosition(fciRegulationDTO, speciePositionList);
   }

   protected FCIRegulationDTO createFCIRegulation1() {
      return createFCIRegulation(fCIRegulationName1, fCIRegulationSymbol1, regulationComposition1);
   }

   /**
    * Creates a FCIRegulation to establish its percentage composition
    */
   protected FCIRegulationDTO createFCIRegulation(String fCIRegulationName, String symbol, Map<SpecieType, Double> regulationComposition) {
      FCIRegulationDTO fciRegulation = FCIRegulationDTO.builder().name(fCIRegulationName).symbol(symbol).build();

      Set<FCIComposition> fciComposition = regulationComposition.entrySet().stream()
              .map(e -> FCIComposition.builder().specieType(e.getKey().name()).percentage(e.getValue()).build())
              .collect(Collectors.toUnmodifiableSet());

      fciRegulation.setComposition(fciComposition);

      return fciRegulation;
   }


   /** @noinspection DataFlowIssue*/
   protected List<SpeciePosition> createSpeciePositionList1() {
      return List.of(
            SpeciePosition.builder().name(speciePositionName1).symbol(speciePositionSymbol1).specieType(speciePositionType1).price(speciePositionPrice1).quantity(speciePositionQuantity1).build(),
            SpeciePosition.builder().name(speciePositionName2).symbol(speciePositionSymbol2).specieType(speciePositionType2).price(speciePositionPrice2).quantity(speciePositionQuantity2).build(),
            SpeciePosition.builder().name(speciePositionName3).symbol(speciePositionSymbol3).specieType(speciePositionType3).price(speciePositionPrice3).quantity(speciePositionQuantity3).build(),
            SpeciePosition.builder().name(speciePositionName4).symbol(speciePositionSymbol4).specieType(speciePositionType4).price(speciePositionPrice4).quantity(speciePositionQuantity4).build(),
            SpeciePosition.builder().name(speciePositionName5).symbol(speciePositionSymbol5).specieType(speciePositionType5).price(speciePositionPrice5).quantity(speciePositionQuantity5).build());

   }

   /** @noinspection DataFlowIssue*/
   protected List<SpeciePosition> createSpeciePositionList2() {
      return List.of(
            SpeciePosition.builder().name(speciePositionName1).symbol(speciePositionSymbol1).specieType(speciePositionType1).price(speciePositionPrice1).quantity(speciePositionQuantity1).build(),
            SpeciePosition.builder().name(speciePositionName2).symbol(speciePositionSymbol2).specieType(speciePositionType2).price(speciePositionPrice2).quantity(speciePositionQuantity2).build(),
            SpeciePosition.builder().name(speciePositionName3).symbol(speciePositionSymbol3).specieType(speciePositionType3).price(speciePositionPrice3).quantity(speciePositionQuantity3).build(),
            SpeciePosition.builder().name(speciePositionName4).symbol(speciePositionSymbol4).specieType(speciePositionType4).price(speciePositionPrice4).quantity(speciePositionQuantity4).build());
   }
}
