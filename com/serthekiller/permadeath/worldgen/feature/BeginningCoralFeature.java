package com.serthekiller.permadeath.worldgen.feature;

import com.mojang.serialization.Codec;
import com.serthekiller.permadeath.worldgen.processor.ProtectTerrainProcessor;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_2960;
import net.minecraft.class_3031;
import net.minecraft.class_3218;
import net.minecraft.class_3485;
import net.minecraft.class_3492;
import net.minecraft.class_3499;
import net.minecraft.class_5281;
import net.minecraft.class_5819;
import net.minecraft.class_5821;

public class BeginningCoralFeature extends class_3031<BeginningCoralConfiguration> {
   public BeginningCoralFeature(Codec<BeginningCoralConfiguration> codec) {
      super(codec);
   }

   public boolean method_13151(class_5821<BeginningCoralConfiguration> ctx) {
      class_5281 level = ctx.method_33652();
      class_2338 start = ctx.method_33655();
      class_5819 rand = ctx.method_33654();
      List<class_2960> pool = ((BeginningCoralConfiguration)ctx.method_33656()).structures();
      if (pool.isEmpty()) {
         return false;
      } else {
         class_2960 chosen = (class_2960)pool.get(rand.method_43048(pool.size()));
         class_3218 serverLevel = level.method_8410();
         class_3485 templateManager = serverLevel.method_14183();
         class_3499 template = templateManager.method_15091(chosen);
         class_2470 rotation = class_2470.method_16548(rand);
         class_3492 settings = (new class_3492()).method_15123(rotation).method_15125(class_2415.field_11302).method_15133(true).method_16184(ProtectTerrainProcessor.INSTANCE);
         class_2382 size = template.method_15166(rotation);
         class_2338 surface = null;

         for(int dy = 0; dy >= -20; --dy) {
            class_2338 check = start.method_10086(dy);
            if (level.method_8320(check).method_27852(class_2246.field_10286) && level.method_22347(check.method_10084())) {
               int solidBlocksCount = 0;

               for(int dDown = 0; dDown <= 3; ++dDown) {
                  class_2338 downCheck = check.method_10087(dDown);
                  if (level.method_8320(downCheck).method_27852(class_2246.field_10286)) {
                     ++solidBlocksCount;
                  }
               }

               if (solidBlocksCount >= 3) {
                  surface = check;
                  break;
               }
            }
         }

         if (surface == null) {
            return false;
         } else {
            class_2338 placePos = surface.method_10084().method_10069(-size.method_10263() / 2, 0, -size.method_10260() / 2);
            int groundY = surface.method_10264();

            for(int dx = 0; dx < size.method_10263(); ++dx) {
               for(int dz = 0; dz < size.method_10260(); ++dz) {
                  class_2338 groundCheck = new class_2338(placePos.method_10263() + dx, groundY, placePos.method_10260() + dz);
                  if (!level.method_8320(groundCheck).method_27852(class_2246.field_10286)) {
                     return false;
                  }

                  int solidBelow = 0;

                  for(int dDown = 1; dDown <= 3; ++dDown) {
                     if (level.method_8320(groundCheck.method_10087(dDown)).method_27852(class_2246.field_10286)) {
                        ++solidBelow;
                     }
                  }

                  if (solidBelow < 2) {
                     return false;
                  }

                  for(int dy = 0; dy < size.method_10264(); ++dy) {
                     if (!level.method_22347(groundCheck.method_10086(1 + dy))) {
                        return false;
                     }
                  }
               }
            }

            template.method_15172(level, placePos, placePos, settings, rand, 2);
            return true;
         }
      }
   }
}
