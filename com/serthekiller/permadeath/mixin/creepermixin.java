package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1548;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1548.class})
public class creepermixin {
   @Inject(
      method = {"method_7001()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$cancelPotionCloud(CallbackInfo ci) {
      System.out.println("permadeath: cancelando nube de creeper");
      ci.cancel();
   }
}
