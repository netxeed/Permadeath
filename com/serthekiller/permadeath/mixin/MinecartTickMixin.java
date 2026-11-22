package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1688;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1688.class})
public abstract class MinecartTickMixin {
   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void handleOrphaned(CallbackInfo ci) {
      class_1688 self = (class_1688)(Object)this;
      if (self.method_5752().contains("PotionSpawnerMinecart") && !self.method_5765() && self.field_6012 > 40) {
         self.field_5960 = false;
         self.method_5875(false);
      }

   }
}
