package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.Cambios.GiantZombieHandler;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1266;
import net.minecraft.class_1299;
import net.minecraft.class_1315;
import net.minecraft.class_1642;
import net.minecraft.class_1937;
import net.minecraft.class_1972;
import net.minecraft.class_3218;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1642.class})
public class ZombieGiantMixin {
   @Inject(
      method = {"method_5943(Lnet/minecraft/class_5425;Lnet/minecraft/class_1266;Lnet/minecraft/class_3730;Lnet/minecraft/class_1315;)Lnet/minecraft/class_1315;"},
      at = {@At("RETURN")}
   )
   private void permadeath$markGiant(class_5425 serverLevelAccessor, class_1266 difficultyInstance, class_3730 entitySpawnReason, class_1315 spawnGroupData, CallbackInfoReturnable<class_1315> cir) {
      class_1642 self = (class_1642)(Object)this;
      if (self.method_5864() == class_1299.field_6051) {
         if (entitySpawnReason == class_3730.field_16459) {
            class_3218 sl = serverLevelAccessor.method_8410();
            if (sl instanceof class_3218 && sl.method_27983() == class_1937.field_25179) {
               if (DateManager.getCurrentRealDay() >= 50L) {
                  if (sl.method_23753(self.method_24515()).method_40225(class_1972.field_9451)) {
                     GiantZombieHandler.applyGiantStats(self);
                  }
               }
            }
         }
      }
   }

   @Inject(
      method = {"method_7216()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$giantNoSunBurn(CallbackInfoReturnable<Boolean> cir) {
      if (GiantZombieHandler.isGiant((class_1642)(Object)this)) {
         cir.setReturnValue(false);
      }

   }
}
