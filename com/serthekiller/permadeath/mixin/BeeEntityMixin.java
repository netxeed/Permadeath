package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_4466;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4466.class})
public class BeeEntityMixin {
   @Inject(
      method = {"method_21806(Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void preventStingerLoss(boolean hasStung, CallbackInfo ci) {
      long currentDay = DateManager.getCurrentRealDay();
      if (hasStung && currentDay >= 20L && currentDay < 70L) {
         ci.cancel();
      }

   }
}
