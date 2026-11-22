package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1606;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1606.class})
public abstract class ShulkerMixin {
   @Inject(
      method = {"method_7127()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void cancelTeleport(CallbackInfoReturnable<Boolean> cir) {
      class_1606 self = (class_1606)(Object)this;
      if (self.method_5752().contains("ShulkerRojo")) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"method_30948()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void notCollidableWhenRiding(CallbackInfoReturnable<Boolean> cir) {
      class_1606 self = (class_1606)(Object)this;
      if (self.method_5765()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"method_33350()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dontPushVehicleWhenRiding(CallbackInfo ci) {
      class_1606 self = (class_1606)(Object)this;
      if (self.method_5765()) {
         ci.cancel();
      }

   }
}
