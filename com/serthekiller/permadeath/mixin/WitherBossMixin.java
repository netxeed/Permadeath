package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.dimension.ModDimensions;
import net.minecraft.class_1528;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_1528.class})
public class WitherBossMixin {
   @Redirect(
      method = {"method_5958(Lnet/minecraft/class_3218;)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1528;method_6883(Lnet/minecraft/class_2680;)Z"
)
   )
   private boolean preventBlockDestroyInDimension(class_2680 blockState) {
      class_1528 self = (class_1528)this;
      return self.method_37908().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY) ? false : class_1528.method_6883(blockState);
   }
}
