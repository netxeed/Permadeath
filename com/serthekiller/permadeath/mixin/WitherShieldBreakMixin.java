package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1528;
import net.minecraft.class_1657;
import net.minecraft.class_1687;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public abstract class WitherShieldBreakMixin {
   private static final int SHIELD_DISABLE_TICKS = 100;

   @Inject(
      method = {"method_6061(Lnet/minecraft/class_1282;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$witherIgnoresShield(class_1282 damageSource, CallbackInfoReturnable<Boolean> cir) {
      if (isFromWither(damageSource)) {
         if (DateManager.getCurrentRealDay() >= 60L && DateManager.getCurrentRealDay() < 70L) {
            class_1309 self = (class_1309)(Object)this;
            if (self.method_6039() && !self.method_37908().method_8608()) {
               class_1799 blockingStack = self.method_62821();
               self.method_37908().method_8421(self, (byte)30);
               if (self instanceof class_1657) {
                  class_1657 player = (class_1657)self;
                  if (blockingStack != null) {
                     player.method_7357().method_62835(blockingStack, 100);
                  }
               }
            }

            cir.setReturnValue(false);
         }
      }
   }

   private static boolean isFromWither(class_1282 damageSource) {
      class_1297 direct = damageSource.method_5526();
      class_1297 causing = damageSource.method_5529();
      if (!(direct instanceof class_1528) && !(causing instanceof class_1528)) {
         boolean var10000;
         if (direct instanceof class_1687) {
            class_1687 witherSkull = (class_1687)direct;
            if (witherSkull.method_24921() instanceof class_1528) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      } else {
         return true;
      }
   }
}
