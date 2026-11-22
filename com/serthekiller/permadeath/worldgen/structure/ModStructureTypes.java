package com.serthekiller.permadeath.worldgen.structure;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_7151;
import net.minecraft.class_7923;

public class ModStructureTypes {
   public static final class_7151<UniformIslandJigsawStructure> UNIFORM_ISLAND_JIGSAW;

   public static void register() {
   }

   static {
      UNIFORM_ISLAND_JIGSAW = (class_7151)class_2378.method_10230(class_7923.field_41147, class_2960.method_60655("permadeath", "uniform_island_jigsaw"), (class_7151)() -> UniformIslandJigsawStructure.CODEC);
   }
}
