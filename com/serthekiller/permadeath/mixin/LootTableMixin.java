package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1263;
import net.minecraft.class_1937;
import net.minecraft.class_52;
import net.minecraft.class_8567;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_52.class})
public class LootTableMixin {
   @Inject(
      method = {"method_329(Lnet/minecraft/class_1263;Lnet/minecraft/class_8567;J)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void cancelLootOnDay60(class_1263 container, class_8567 params, long seed, CallbackInfo ci) {
      class_1937 level = params.method_51863();
      if (level != null && !level.field_9236 && DateManager.getCurrentRealDay() >= 60L && level.method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
         ci.cancel();
      }

   }
}
