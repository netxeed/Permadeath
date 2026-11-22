package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.SurvivalAchievementManager;
import net.minecraft.class_1297;
import net.minecraft.class_1934;
import net.minecraft.class_1937;
import net.minecraft.class_3222;
import net.minecraft.class_5454;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_3222.class})
public abstract class EndAccessMixin {
   @Inject(
      method = {"method_61275(Lnet/minecraft/class_5454;)Lnet/minecraft/class_3222;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$blockEndBeforeDay30(class_5454 transition, CallbackInfoReturnable<class_1297> cir) {
      if (transition.comp_2820().method_27983() == class_1937.field_25181 && DateManager.getCurrentRealDay() < 30L) {
         class_3222 self = (class_3222)(Object)this;
         self.method_18799(self.method_18798().method_1031((double)0.0F, 0.3, (double)0.0F));
         cir.setReturnValue(self);
      }

   }

   @Inject(
      method = {"method_7336(Lnet/minecraft/class_1934;)Z"},
      at = {@At("RETURN")}
   )
   private void permadeath$onGameMode(class_1934 gameType, CallbackInfoReturnable<Boolean> cir) {
      if (cir.getReturnValueZ() && gameType == class_1934.field_9220) {
         class_3222 p = (class_3222)(Object)this;
         if (p.method_5682() != null) {
            SurvivalAchievementManager.flagCheatDetected(p.method_5682(), p.method_5667(), "gamemode creative");
         }
      }
   }
}
