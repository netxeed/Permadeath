package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1548;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1548.class})
public abstract class QuantumCreeperMixin {
   @Shadow
   private int field_7225;
   @Shadow
   private int field_7228;

   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void quantumFuseSpeed(CallbackInfo ci) {
      class_1548 creeper = (class_1548)(Object)this;
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay >= 60L) {
         String name = creeper.method_16914() ? creeper.method_5797().getString() : "";
         if (name.contains("Ender Quantum Creeper") || name.contains("Quantum Creeper")) {
            this.field_7228 = 15;
         }
      }

   }

   @Inject(
      method = {"method_7006()V"},
      at = {@At("HEAD")}
   )
   private void quantumExplosion(CallbackInfo ci) {
      class_1548 creeper = (class_1548)(Object)this;
      if (creeper.method_16914()) {
         String name = creeper.method_5797().getString();
         if (name.contains("Quantum Creeper") || name.contains("Ender Quantum Creeper")) {
            this.field_7225 = 20;
         }
      }

   }
}
