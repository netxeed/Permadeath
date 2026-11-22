package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.PermadeathMod;
import java.util.function.Function;
import net.minecraft.class_1792;
import net.minecraft.class_1814;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_4174;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_7924;

public class ModItems {
   public static class_1792 SUPER_GOLDEN_APPLE_PLUS;
   public static class_1792 HYPER_GOLDEN_APPLE_PLUS;
   public static class_1792 END_RELIC;
   public static class_1792 BEGINNING_RELIC;
   public static class_1792 LIFE_ORB;

   private static class_1792 registerItem(String name, Function<class_1792.class_1793, class_1792> factory) {
      class_2960 id = class_2960.method_60655("permadeath", name);
      class_5321<class_1792> key = class_5321.method_29179(class_7924.field_41197, id);
      class_1792.class_1793 properties = (new class_1792.class_1793()).method_63686(key);
      return (class_1792)class_2378.method_39197(class_7923.field_41178, key, (class_1792)factory.apply(properties));
   }

   public static void register() {
      class_4174 superAppleFood = (new class_4174.class_4175()).method_19238(4).method_19237(1.2F).method_19240().method_19242();
      class_4174 hyperAppleFood = (new class_4174.class_4175()).method_19238(4).method_19237(1.2F).method_19240().method_19242();
      SUPER_GOLDEN_APPLE_PLUS = registerItem("super_golden_apple_plus", (properties) -> new SuperGoldenApplePlus(properties.method_19265(superAppleFood).method_7894(class_1814.field_8904)));
      HYPER_GOLDEN_APPLE_PLUS = registerItem("hyper_golden_apple_plus", (properties) -> new HyperGoldenApplePlus(properties.method_19265(hyperAppleFood).method_7894(class_1814.field_8903)));
      END_RELIC = registerItem("end_relic", (properties) -> new EnchantedItem(properties.method_7894(class_1814.field_8903)));
      BEGINNING_RELIC = registerItem("beginning_relic", (properties) -> new EnchantedItem(properties.method_7894(class_1814.field_8903)));
      LIFE_ORB = registerItem("life_orb", (properties) -> new EnchantedItem(properties.method_7894(class_1814.field_8906)));
      PermadeathMod.LOGGER.info("Registrando items de permadeath");
   }
}
