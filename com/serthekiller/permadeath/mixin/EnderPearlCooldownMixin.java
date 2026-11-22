package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1776;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1776.class})
public class EnderPearlCooldownMixin {
   @Inject(
      method = {"method_7836(Lnet/minecraft/class_1937;Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("TAIL")}
   )
   private void addCustomCooldown(class_1937 level, class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      if (!level.field_9236 && DateManager.getCurrentRealDay() >= 60L && DateManager.getCurrentRealDay() < 70L) {
         player.method_7357().method_62835(player.method_6047(), 40);
      }

   }
}
