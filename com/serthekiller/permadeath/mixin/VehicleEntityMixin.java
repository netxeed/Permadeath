package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1282;
import net.minecraft.class_3218;
import net.minecraft.class_8836;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_8836.class})
public abstract class VehicleEntityMixin {
   @Inject(
      method = {"method_64397(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;F)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void immuneToDamage(class_3218 serverLevel, class_1282 damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
      class_8836 self = (class_8836)(Object)this;
      if (self.method_5752().contains("PotionSpawnerMinecart")) {
         cir.setReturnValue(false);
      }

   }
}
