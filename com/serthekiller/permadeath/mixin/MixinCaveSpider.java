package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1549;
import net.minecraft.class_1606;
import net.minecraft.class_243;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1549.class})
public abstract class MixinCaveSpider {
   @Inject(
      method = {"method_6121(Lnet/minecraft/class_3218;Lnet/minecraft/class_1297;)Z"},
      at = {@At("RETURN")}
   )
   private void permadeath$onHurtTarget(class_3218 serverLevel, class_1297 target, CallbackInfoReturnable<Boolean> cir) {
      long currentDay = DateManager.getCurrentRealDay();
      if ((Boolean)cir.getReturnValue()) {
         if (target instanceof class_1309) {
            class_1309 living = (class_1309)target;
            if (!living.method_37908().field_9236) {
               if (currentDay >= 50L && DateManager.getCurrentRealDay() < 70L) {
                  living.method_6092(new class_1293(class_1294.field_5899, 200, 2, false, true));
                  living.method_6092(new class_1293(class_1294.field_5916, 200, 0, false, true));
               }

            }
         }
      }
   }

   @Inject(
      method = {"method_55668(Lnet/minecraft/class_1297;)Lnet/minecraft/class_243;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shulkerAttachmentPoint(class_1297 passenger, CallbackInfoReturnable<class_243> cir) {
      if (passenger instanceof class_1606) {
         class_1549 self = (class_1549)(Object)this;
         cir.setReturnValue(new class_243((double)0.0F, (double)self.method_17682(), (double)0.0F));
      }

   }
}
