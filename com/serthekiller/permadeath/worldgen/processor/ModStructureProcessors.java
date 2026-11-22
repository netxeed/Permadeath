package com.serthekiller.permadeath.worldgen.processor;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3828;
import net.minecraft.class_7923;

public class ModStructureProcessors {
   public static final class_3828<ProtectTerrainProcessor> PROTECT_TERRAIN;

   public static void register() {
   }

   static {
      PROTECT_TERRAIN = (class_3828)class_2378.method_10230(class_7923.field_41161, class_2960.method_60655("permadeath", "protect_terrain"), (class_3828)() -> ProtectTerrainProcessor.CODEC);
   }
}
