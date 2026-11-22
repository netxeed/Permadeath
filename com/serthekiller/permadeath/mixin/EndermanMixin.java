package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1295;
import net.minecraft.class_1304;
import net.minecraft.class_1560;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1560.class})
public class EndermanMixin {
   @Inject(
      method = {"method_64397(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;F)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void checkGreenCloudImmunity(class_3218 serverLevel, class_1282 damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
      class_1560 enderman = (class_1560)(Object)this;

      for(class_1295 cloud : enderman.method_37908().method_18467(class_1295.class, enderman.method_5829().method_1014((double)1.0F))) {
         if (cloud.method_5600().method_10295() == class_2398.field_11211) {
            enderman.method_6092(new class_1293(class_1294.field_5924, 20, 2));
            enderman.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8288));
            cir.setReturnValue(false);
            return;
         }
      }

   }
}
