package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1309;
import net.minecraft.class_1541;
import net.minecraft.class_1678;
import net.minecraft.class_239;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1678.class})
public abstract class ShulkerBulletMixin {
   @Inject(
      method = {"method_7488(Lnet/minecraft/class_239;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void spawnTntOnImpact(class_239 hitResult, CallbackInfo ci) {
      class_1678 bullet = (class_1678)(Object)this;
      if (DateManager.getCurrentRealDay() < 70L) {
         if (!bullet.method_37908().field_9236) {
            class_1541 tnt = new class_1541(bullet.method_37908(), bullet.method_23317(), bullet.method_23318(), bullet.method_23321(), (class_1309)null);
            tnt.method_6967(80);
            bullet.method_37908().method_8649(tnt);
         }

      }
   }
}
