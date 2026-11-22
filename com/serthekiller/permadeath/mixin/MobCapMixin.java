package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.ServerContext;
import net.minecraft.class_1311;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1311.class})
public class MobCapMixin {
   @Inject(
      method = {"method_6134()I"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void increaseMobCap(CallbackInfoReturnable<Integer> cir) {
      class_1311 self = (class_1311)this;
      long currentDay = DateManager.getCurrentRealDay();
      if (self == class_1311.field_6302 && currentDay > 10L && currentDay < 70L) {
         int base = 140;
         if (ServerContext.isDedicated()) {
            int players = ServerContext.getPlayerCount();
            int scaled = 90 + players * 10;
            cir.setReturnValue(Math.min(scaled, 140));
         } else {
            cir.setReturnValue(base);
         }
      }

   }
}
