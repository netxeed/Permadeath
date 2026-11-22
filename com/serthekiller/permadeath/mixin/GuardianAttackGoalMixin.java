package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net/minecraft/class_1577$class_1578"}
)
public abstract class GuardianAttackGoalMixin {
   @Shadow
   private int field_7291;

   @Inject(
      method = {"method_6268()V"},
      at = {@At("HEAD")}
   )
   private void doubleAttackSpeed(CallbackInfo ci) {
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay >= 40L && DateManager.getCurrentRealDay() < 70L) {
         ++this.field_7291;
      }

   }
}
