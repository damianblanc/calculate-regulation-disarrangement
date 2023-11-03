package com.bymatech.calculateregulationdisarrangement;

import com.bymatech.calculateregulationdisarrangement.domain.FCIComposition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpeciePosition;
import com.bymatech.calculateregulationdisarrangement.domain.FCISpecieType;
import com.bymatech.calculateregulationdisarrangement.domain.SpecieTypeGroupEnum;
import com.bymatech.calculateregulationdisarrangement.dto.FCISpeciePositionDTO;
import com.bymatech.calculateregulationdisarrangement.dto.FCIRegulationDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines all FCI Regulation Data for testing purposes
 */
public class FCITestFixture {

   protected FCISpeciePositionDTO fciPosition1;
   protected FCIRegulationDTO fciRegulation1;
   protected List<FCISpeciePosition> FCISpeciePositionList1;


   protected static String fCIRegulationName1 = "Alpha Mix Rent FCI";

   protected static String fCIRegulationSymbol1 = "AMR23";
   protected static Map<FCISpecieType, Double> regulationComposition1 = createRegulationComposition();

   /** Specie Position Definition */
   /** SHARE MARKET */
   protected static String speciePositionName1 = "BANCO GALICIA";

   protected static String speciePositionSymbol1 = "GGAL";
   protected static SpecieTypeGroupEnum speciePositionType1 = SpecieTypeGroupEnum.Equity;
   protected static Double speciePositionPrice1 = 3.15;
   protected static Integer speciePositionQuantity1 = 1500;

   protected static String speciePositionName2 = "YPF ESTATAL";

   protected static String speciePositionSymbol2 = "YPF";
   protected static SpecieTypeGroupEnum speciePositionType2 = SpecieTypeGroupEnum.Equity;
   protected static Double speciePositionPrice2 = 8.5;
   protected static Integer speciePositionQuantity2 = 6000;

   /** BOND */
   protected static String speciePositionName3 = "GLOBAL BOND GD41";

   protected static String speciePositionSymbol3 = "GD41";
   protected static SpecieTypeGroupEnum speciePositionType3 = SpecieTypeGroupEnum.Bond;
   protected static Double speciePositionPrice3 = 0.6;
   protected static Integer speciePositionQuantity3 = 40000;

   protected static String speciePositionName4 = "LOCAL BOND T3X4";
   protected static String speciePositionSymbol4 = "T3X4";
   protected static SpecieTypeGroupEnum speciePositionType4 = SpecieTypeGroupEnum.Bond;
   protected static Double speciePositionPrice4 = 1.4;
   protected static Integer speciePositionQuantity4 = 30000;

   /** CASH */
   protected static String speciePositionName5 = "CASH";

   protected static String speciePositionSymbol5 = "CASH";
   protected static SpecieTypeGroupEnum speciePositionType5 = SpecieTypeGroupEnum.Cash;
   protected static Double speciePositionPrice5 = 25000.00;
   protected static Integer speciePositionQuantity5 = 1;

   /**
    * Creates a Regulation Composition to establish its Specie Type percentages
    */
   private static Map<FCISpecieType, Double> createRegulationComposition() {
      return null;
//      return ImmutableMap.<SpecieType, Double>builder()
//              .put(SpecieType.Equity, 30.00)
//              .put(SpecieType.Bond, 50.00)
//              .put(SpecieType.Cash, 20.00).build();
   }

   /**
    * Creates a FCIPosition that comprehends a FCIRegulation & FCISpeciePosition
    * An FCIPosition represents what it is needed to be posted in order to process
    */
   protected FCISpeciePositionDTO createFCIPosition(FCIRegulationDTO fciRegulationDTO, List<FCISpeciePosition> FCISpeciePositionList) {
      return new FCISpeciePositionDTO(FCISpeciePositionList);
   }

   protected FCIRegulationDTO createFCIRegulation1() {
      return createFCIRegulation(fCIRegulationName1, fCIRegulationSymbol1, regulationComposition1);
   }

   /**
    * Creates a FCIRegulation to establish its percentage composition
    */
   protected FCIRegulationDTO createFCIRegulation(String fCIRegulationName, String symbol, Map<FCISpecieType, Double> regulationComposition) {
      FCIRegulationDTO fciRegulation = FCIRegulationDTO.builder().name(fCIRegulationName).symbol(symbol).build();

      Set<FCIComposition> fciComposition = regulationComposition.entrySet().stream()
              .map(e -> FCIComposition.builder().fciSpecieTypeId(e.getKey().getFciSpecieTypeId()).percentage(e.getValue()).build())
              .collect(Collectors.toUnmodifiableSet());

      fciRegulation.setComposition(fciComposition);

      return fciRegulation;
   }


   /** @noinspection DataFlowIssue*/
   protected List<FCISpeciePosition> createSpeciePositionList1() {
      return List.of();
//      return List.of(
//            FCISpeciePosition.builder().name(speciePositionName1).symbol(speciePositionSymbol1).ficSpecieType(speciePositionType1).price(speciePositionPrice1).quantity(speciePositionQuantity1).build(),
//            FCISpeciePosition.builder().name(speciePositionName2).symbol(speciePositionSymbol2).ficSpecieType(speciePositionType2).price(speciePositionPrice2).quantity(speciePositionQuantity2).build(),
//            FCISpeciePosition.builder().name(speciePositionName3).symbol(speciePositionSymbol3).ficSpecieType(speciePositionType3).price(speciePositionPrice3).quantity(speciePositionQuantity3).build(),
//            FCISpeciePosition.builder().name(speciePositionName4).symbol(speciePositionSymbol4).ficSpecieType(speciePositionType4).price(speciePositionPrice4).quantity(speciePositionQuantity4).build(),
//            FCISpeciePosition.builder().name(speciePositionName5).symbol(speciePositionSymbol5).ficSpecieType(speciePositionType5).price(speciePositionPrice5).quantity(speciePositionQuantity5).build());
   }

   /** @noinspection DataFlowIssue*/
   protected List<FCISpeciePosition> createSpeciePositionList2() {
      return List.of();
//      return List.of(
//            FCISpeciePosition.builder().name(speciePositionName1).symbol(speciePositionSymbol1).ficSpecieType(speciePositionType1).price(speciePositionPrice1).quantity(speciePositionQuantity1).build(),
//            FCISpeciePosition.builder().name(speciePositionName2).symbol(speciePositionSymbol2).ficSpecieType(speciePositionType2).price(speciePositionPrice2).quantity(speciePositionQuantity2).build(),
//            FCISpeciePosition.builder().name(speciePositionName3).symbol(speciePositionSymbol3).ficSpecieType(speciePositionType3).price(speciePositionPrice3).quantity(speciePositionQuantity3).build(),
//            FCISpeciePosition.builder().name(speciePositionName4).symbol(speciePositionSymbol4).ficSpecieType(speciePositionType4).price(speciePositionPrice4).quantity(speciePositionQuantity4).build());
   }
}
