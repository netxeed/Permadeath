package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.Cambios.mikecrack;
import net.minecraft.class_1299;
import net.minecraft.class_1317;
import net.minecraft.class_2338;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1317.class})
public class MikecrackSpawnPlacementsMixin {
   @Inject(
      method = {"method_20638(Lnet/minecraft/class_1299;Lnet/minecraft/class_5425;Lnet/minecraft/class_3730;Lnet/minecraft/class_2338;Lnet/minecraft/class_5819;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void mikecrack_bypassNaturalRules(class_1299<?> entityType, class_5425 level, class_3730 reason, class_2338 pos, class_5819 random, CallbackInfoReturnable<Boolean> cir) {
      if (entityType == class_1299.field_6046 && mikecrack.isEnabled()) {
         cir.setReturnValue(true);
      }

   }
}
