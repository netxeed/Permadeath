package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1299;
import net.minecraft.class_1317;
import net.minecraft.class_1937;
import net.minecraft.class_1972;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2404;
import net.minecraft.class_3218;
import net.minecraft.class_3486;
import net.minecraft.class_3610;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1317.class})
public class SpawnPlacementsMixins {
   @Unique
   private static final int MIN_WATER_BLOCKS = 5;

   @Inject(
      method = {"method_20638(Lnet/minecraft/class_1299;Lnet/minecraft/class_5425;Lnet/minecraft/class_3730;Lnet/minecraft/class_2338;Lnet/minecraft/class_5819;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void permadeath$overrideSpawnRules(class_1299<?> entityType, class_5425 level, class_3730 spawnReason, class_2338 pos, class_5819 random, CallbackInfoReturnable<Boolean> cir) {
      if (spawnReason == class_3730.field_16459) {
         if (level instanceof class_3218) {
            class_3218 serverLevel = (class_3218)level;
            if (serverLevel.method_27983() == class_1937.field_25179) {
               long day = DateManager.getCurrentRealDay();
               if (entityType == class_1299.field_6099 && day >= 30L) {
                  if (pos.method_10264() >= serverLevel.method_8615()) {
                     cir.setReturnValue(false);
                  }

               } else if ((entityType == class_1299.field_6118 || entityType == class_1299.field_6086) && day >= 30L) {
                  if (!hasEnoughRealWater(level, pos)) {
                     cir.setReturnValue(false);
                  }

               } else {
                  if (entityType == class_1299.field_6051 && day >= 50L) {
                     if (!serverLevel.method_23753(pos).method_40225(class_1972.field_9451)) {
                        return;
                     }

                     if (!serverLevel.method_8311(pos)) {
                        cir.setReturnValue(false);
                        return;
                     }

                     class_2338 below = pos.method_10074();
                     if (!serverLevel.method_8320(below).method_26206(serverLevel, below, class_2350.field_11036)) {
                        cir.setReturnValue(false);
                        return;
                     }

                     float chance = day < 60L ? 0.05F : 0.2F;
                     if (random.method_43057() > chance) {
                        cir.setReturnValue(false);
                        return;
                     }

                     double hw = 1.7999999999999998;
                     class_238 box = new class_238((double)pos.method_10263() + (double)0.5F - hw, (double)pos.method_10264(), (double)pos.method_10260() + (double)0.5F - hw, (double)pos.method_10263() + (double)0.5F + hw, (double)pos.method_10264() + 11.7, (double)pos.method_10260() + (double)0.5F + hw);
                     cir.setReturnValue(serverLevel.method_18026(box));
                  }

               }
            }
         }
      }
   }

   @Unique
   private static boolean isRealWaterBlock(class_5425 level, class_2338 pos) {
      class_3610 fluidState = level.method_8316(pos);
      return fluidState.method_15767(class_3486.field_15517) && fluidState.method_15771() ? level.method_8320(pos).method_26204() instanceof class_2404 : false;
   }

   @Unique
   private static boolean hasEnoughRealWater(class_5425 level, class_2338 pos) {
      if (!isRealWaterBlock(level, pos)) {
         return false;
      } else {
         class_2338[] toCheck = new class_2338[]{pos.method_10084(), pos.method_10074(), pos.method_10095(), pos.method_10072(), pos.method_10078(), pos.method_10067()};
         int waterCount = 1;

         for(class_2338 check : toCheck) {
            if (isRealWaterBlock(level, check)) {
               ++waterCount;
               if (waterCount >= 5) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
