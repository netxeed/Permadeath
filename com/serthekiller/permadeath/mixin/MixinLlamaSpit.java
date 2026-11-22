package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1673;
import net.minecraft.class_243;
import net.minecraft.class_3966;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1673.class})
public abstract class MixinLlamaSpit {
   long currentDay = DateManager.getCurrentRealDay();

   @Inject(
      method = {"method_7454(Lnet/minecraft/class_3966;)V"},
      at = {@At("RETURN")}
   )
   private void permadeath$onHitEntity(class_3966 hitResult, CallbackInfo ci) {
      class_1297 var4 = hitResult.method_17782();
      if (var4 instanceof class_1309 living) {
         if (!living.method_37908().field_9236) {
            if (this.currentDay >= 50L && this.currentDay < 70L) {
               class_1673 self = (class_1673)this;
               living.method_6092(new class_1293(class_1294.field_5899, 600, 2, false, true));
               living.method_6092(new class_1293(class_1294.field_5916, 200, 0, false, true));
               class_243 dir = living.method_19538().method_1020(self.method_19538()).method_1029();
               living.method_18799(living.method_18798().method_1031(dir.field_1352 * 1.2, 0.4, dir.field_1350 * 1.2));
               living.field_6037 = true;
            }
         }
      }
   }
}
