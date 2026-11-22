package com.serthekiller.permadeath.worldgen;

import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.class_2338;
import net.minecraft.class_5444;
import net.minecraft.class_5819;
import net.minecraft.class_6797;
import net.minecraft.class_6798;

public class IslandDensityPlacementModifier extends class_6797 {
   public static final IslandDensityPlacementModifier INSTANCE = new IslandDensityPlacementModifier();
   public static final MapCodec<IslandDensityPlacementModifier> CODEC;

   private IslandDensityPlacementModifier() {
   }

   public Stream<class_2338> method_14452(class_5444 ctx, class_5819 rand, class_2338 pos) {
      int solid = 0;
      int total = 0;
      int radius = 8;

      for(int dx = -radius; dx <= radius; dx += 2) {
         for(int dz = -radius; dz <= radius; dz += 2) {
            for(int dy = -10; dy <= 10; dy += 2) {
               class_2338 check = pos.method_10069(dx, dy, dz);
               ++total;
               if (!ctx.method_30461(check).method_26215()) {
                  ++solid;
               }
            }
         }
      }

      double ratio = (double)solid / (double)total;
      if (ratio >= 0.55) {
         return Stream.of(pos);
      } else {
         return Stream.empty();
      }
   }

   public class_6798<?> method_39615() {
      return ModPlacementModifiers.ISLAND_DENSITY;
   }

   static {
      CODEC = MapCodec.unit(INSTANCE);
   }
}
