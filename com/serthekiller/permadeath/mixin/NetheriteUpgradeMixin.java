package com.serthekiller.permadeath.mixin;

import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1799.class})
public abstract class NetheriteUpgradeMixin {
   @Shadow
   public abstract class_1792 method_7909();

   private static boolean isNetheriteItem(class_1792 item) {
      return item == class_1802.field_22024 || item == class_1802.field_22025 || item == class_1802.field_22023 || item == class_1802.field_22026 || item == class_1802.field_22022;
   }

   @Inject(
      method = {"method_7964()Lnet/minecraft/class_2561;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void setNetheriteGoldName(CallbackInfoReturnable<class_2561> cir) {
      if (isNetheriteItem(this.method_7909())) {
         cir.setReturnValue(((class_2561)cir.getReturnValue()).method_27661().method_27692(class_124.field_1065));
      }

   }

   @Inject(
      method = {"method_7963()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void makeNetheriteUnbreakable(CallbackInfoReturnable<Boolean> cir) {
      if (isNetheriteItem(this.method_7909())) {
         cir.setReturnValue(false);
      }

   }
}
