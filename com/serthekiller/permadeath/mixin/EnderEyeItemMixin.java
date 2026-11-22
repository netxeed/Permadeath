package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1777;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1777.class})
public class EnderEyeItemMixin {
   @Inject(
      method = {"method_7836(Lnet/minecraft/class_1937;Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$blockEarlyThrow(class_1937 level, class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      if (!level.method_8608()) {
         long day = DateManager.getCurrentRealDay();
         if (day < 30L) {
            cir.setReturnValue(class_1269.field_5814);
         }

      }
   }
}
