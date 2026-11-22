package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.dimension.ModDimensions;
import net.minecraft.class_9892;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_9892.class})
public class ExplosionMixin {
   @Inject(
      method = {"method_61742()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void protectBlocks(CallbackInfoReturnable<Boolean> cir) {
      class_9892 self = (class_9892)this;
      if (self.method_64504().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
         cir.setReturnValue(false);
      }

   }
}
