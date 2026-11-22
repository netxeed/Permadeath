package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public class MixinLivingEntityFatigue {
   long currentDay = DateManager.getCurrentRealDay();
   @Unique
   private class_1293 permadeath_savedFatigue = null;
   private class_1293 permadeath_savedHunger = null;
   private class_1293 permadeath_savedPosion = null;

   @Inject(
      method = {"method_37222(Lnet/minecraft/class_1293;Lnet/minecraft/class_1297;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddEffect(class_1293 effect, class_1297 source, CallbackInfoReturnable<Boolean> cir) {
      if (effect.method_5579().equals(class_1294.field_5901) && this.currentDay >= 50L && DateManager.getCurrentRealDay() < 70L) {
         class_1309 self = (class_1309)this;
         class_1293 doubled = new class_1293(class_1294.field_5901, effect.method_5584() * 2, effect.method_5578(), true, effect.method_5581());
         if (!effect.method_5591()) {
            self.method_26082(doubled, source);
            cir.setReturnValue(true);
         }
      }

   }

   @Inject(
      method = {"method_6012()Z"},
      at = {@At("HEAD")}
   )
   private void beforeRemoveAllEffects(CallbackInfoReturnable<Boolean> cir) {
      class_1309 self = (class_1309)this;
      this.permadeath_savedFatigue = self.method_6112(class_1294.field_5901);
      this.permadeath_savedHunger = self.method_6112(class_1294.field_5903);
      this.permadeath_savedPosion = self.method_6112(class_1294.field_5899);
   }

   @Inject(
      method = {"method_6012()Z"},
      at = {@At("RETURN")}
   )
   private void afterRemoveAllEffects(CallbackInfoReturnable<Boolean> cir) {
      class_1309 self = (class_1309)this;
      if (this.permadeath_savedFatigue != null && this.currentDay >= 50L) {
         self.method_26082(new class_1293(class_1294.field_5901, this.permadeath_savedFatigue.method_5584(), this.permadeath_savedFatigue.method_5578(), this.permadeath_savedFatigue.method_5591(), this.permadeath_savedFatigue.method_5581()), (class_1297)null);
         this.permadeath_savedFatigue = null;
      }

      if (this.permadeath_savedHunger != null && this.currentDay >= 50L) {
         self.method_26082(new class_1293(class_1294.field_5903, this.permadeath_savedHunger.method_5584(), this.permadeath_savedHunger.method_5578(), this.permadeath_savedHunger.method_5591(), this.permadeath_savedHunger.method_5581()), (class_1297)null);
         this.permadeath_savedHunger = null;
      }

      if (this.permadeath_savedPosion != null && this.currentDay >= 50L) {
         self.method_26082(new class_1293(class_1294.field_5899, this.permadeath_savedPosion.method_5584(), this.permadeath_savedPosion.method_5578(), this.permadeath_savedPosion.method_5591(), this.permadeath_savedPosion.method_5581()), (class_1297)null);
         this.permadeath_savedPosion = null;
      }

   }
}
