package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1755;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1755.class})
public abstract class BucketItemMixin {
   @Shadow
   @Final
   private class_3611 field_7905;

   @Inject(
      method = {"method_7836(Lnet/minecraft/class_1937;Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void stopFilling(class_1937 level, class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      long currentDay = DateManager.getCurrentRealDay();
      if (this.field_7905 == class_3612.field_15906 && currentDay >= 50L && currentDay < 70L) {
         class_3965 hitResult = doRaycast(level, player, class_242.field_1345);
         if (hitResult.method_17783() == class_240.field_1332) {
            class_3611 targetedFluid = level.method_8316(hitResult.method_17777()).method_15772();
            if (targetedFluid.method_15791(class_3486.field_15517) || targetedFluid.method_15791(class_3486.field_15518)) {
               cir.setReturnValue(class_1269.field_5814);
            }
         }
      }

   }

   private static class_3965 doRaycast(class_1937 level, class_1657 player, class_3959.class_242 fluidMode) {
      class_243 eyePos = player.method_33571();
      class_243 lookVec = eyePos.method_1019(player.method_5720().method_1021(player.method_55754()));
      return level.method_17742(new class_3959(eyePos, lookVec, class_3960.field_17559, fluidMode, player));
   }
}
