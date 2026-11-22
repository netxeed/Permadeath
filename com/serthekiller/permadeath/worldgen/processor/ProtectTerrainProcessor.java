package com.serthekiller.permadeath.worldgen.processor;

import com.mojang.serialization.MapCodec;
import net.minecraft.class_2338;
import net.minecraft.class_3491;
import net.minecraft.class_3492;
import net.minecraft.class_3499;
import net.minecraft.class_3828;
import net.minecraft.class_4538;
import org.jetbrains.annotations.Nullable;

public class ProtectTerrainProcessor extends class_3491 {
   public static final ProtectTerrainProcessor INSTANCE = new ProtectTerrainProcessor();
   public static final MapCodec<ProtectTerrainProcessor> CODEC;

   private ProtectTerrainProcessor() {
   }

   public class_3499.@Nullable class_3501 method_15110(class_4538 world, class_2338 structurePos, class_2338 pivot, class_3499.class_3501 rawBlockInfo, class_3499.class_3501 blockInfo, class_3492 settings) {
      return blockInfo.comp_1342().method_26215() && !world.method_8320(blockInfo.comp_1341()).method_26215() ? null : blockInfo;
   }

   protected class_3828<?> method_16772() {
      return ModStructureProcessors.PROTECT_TERRAIN;
   }

   static {
      CODEC = MapCodec.unit(INSTANCE);
   }
}
