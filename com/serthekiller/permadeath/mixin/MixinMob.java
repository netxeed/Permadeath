package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1266;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1315;
import net.minecraft.class_1549;
import net.minecraft.class_1550;
import net.minecraft.class_1571;
import net.minecraft.class_1606;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1308.class})
public abstract class MixinMob {
   @Shadow
   private boolean field_6200;

   @Inject(
      method = {"method_5642()Lnet/minecraft/class_1309;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ignoreShulkerControl(CallbackInfoReturnable<class_1309> cir) {
      if (this instanceof class_1549 self) {
         class_1297 firstPassenger = self.method_31483();
         if (firstPassenger instanceof class_1606) {
            cir.setReturnValue((Object)null);
         }
      }

   }

   @Inject(
      method = {"method_5642()Lnet/minecraft/class_1309;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$fixGhastPigMount(CallbackInfoReturnable<class_1309> cir) {
      class_1308 self = (class_1308)this;
      if (self instanceof class_1571 && self.method_5752().contains("ghast_pig_mount")) {
         cir.setReturnValue((Object)null);
         cir.cancel();
      }

   }

   @Inject(
      method = {"method_5943(Lnet/minecraft/class_5425;Lnet/minecraft/class_1266;Lnet/minecraft/class_3730;Lnet/minecraft/class_1315;)Lnet/minecraft/class_1315;"},
      at = {@At("RETURN")}
   )
   private void permadeath$elderNotPersistent(class_5425 serverLevelAccessor, class_1266 difficultyInstance, class_3730 entitySpawnReason, class_1315 spawnGroupData, CallbackInfoReturnable<class_1315> cir) {
      if (entitySpawnReason == class_3730.field_16459 && this instanceof class_1550) {
         this.field_6200 = false;
      }

   }
}
