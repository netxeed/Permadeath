package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public class RaidHeroEffectMixin {
   private static final int MAX_HERO_TICKS = 6000;

   @Inject(
      method = {"method_37222(Lnet/minecraft/class_1293;Lnet/minecraft/class_1297;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void limitHeroOfVillageEffect(class_1293 instance, class_1297 source, CallbackInfoReturnable<Boolean> cir) {
      long currentDay = DateManager.getCurrentRealDay();
      if (instance.method_55654(class_1294.field_18980) && instance.method_5584() > 6000 && currentDay >= 50L && DateManager.getCurrentRealDay() < 70L) {
         class_1309 self = (class_1309)this;
         self.method_6088().put(class_1294.field_18980, new class_1293(class_1294.field_18980, 6000, instance.method_5578()));
         cir.setReturnValue(true);
      }

   }
}
