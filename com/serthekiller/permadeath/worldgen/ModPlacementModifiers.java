package com.serthekiller.permadeath.worldgen;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_6798;
import net.minecraft.class_7923;

public class ModPlacementModifiers {
   public static final class_6798<IslandDensityPlacementModifier> ISLAND_DENSITY;

   public static void register() {
   }

   static {
      ISLAND_DENSITY = (class_6798)class_2378.method_10230(class_7923.field_41148, class_2960.method_60655("permadeath", "island_density"), (class_6798)() -> IslandDensityPlacementModifier.CODEC);
   }
}
