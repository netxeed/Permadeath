package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1297;
import net.minecraft.class_1473;
import net.minecraft.class_1657;
import net.minecraft.class_1680;
import net.minecraft.class_3966;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1680.class})
public abstract class SnowGolemSnowballMixin {
   @Inject(
      method = {"method_7454(Lnet/minecraft/class_3966;)V"},
      at = {@At("HEAD")}
   )
   private void permadeath$onSnowGolemHit(class_3966 result, CallbackInfo ci) {
      class_1680 self = (class_1680)(Object)this;
      class_1297 owner = self.method_24921();
      if (owner instanceof class_1473) {
         class_1297 target = result.method_17782();
         if (target instanceof class_1657) {
            class_1657 player = (class_1657)target;
            if (DateManager.getCurrentRealDay() > 60L && DateManager.getCurrentRealDay() < 70L) {
               player.method_64419(player.method_48923().method_48811(self, owner), 50.0F);
            }

         }
      }
   }
}
