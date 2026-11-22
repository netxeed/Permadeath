package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1299;
import net.minecraft.class_1589;
import net.minecraft.class_1936;
import net.minecraft.class_1972;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_3218;
import net.minecraft.class_3730;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1589.class})
public class MagmaCubeSpawnLimitMixin {
   private static final int MAX_MAGMA_CUBES_BASALT_DELTA = 20;
   private static final double SEARCH_RADIUS = (double)128.0F;

   @Inject(
      method = {"method_20678(Lnet/minecraft/class_1299;Lnet/minecraft/class_1936;Lnet/minecraft/class_3730;Lnet/minecraft/class_2338;Lnet/minecraft/class_5819;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void permadeath$limitMagmaCubesInBasaltDelta(class_1299<class_1589> entityType, class_1936 level, class_3730 entitySpawnReason, class_2338 pos, class_5819 randomSource, CallbackInfoReturnable<Boolean> cir) {
      if (level.method_23753(pos).method_40225(class_1972.field_23859)) {
         if (DateManager.getCurrentRealDay() >= 25L) {
            if (level instanceof class_3218) {
               class_3218 serverLevel = (class_3218)level;
               class_238 searchBox = (new class_238(pos)).method_1014((double)128.0F);
               int count = serverLevel.method_18467(class_1589.class, searchBox).size();
               if (count >= 20) {
                  cir.setReturnValue(false);
               }

            }
         }
      }
   }
}
