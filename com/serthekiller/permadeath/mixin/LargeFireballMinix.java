package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1571;
import net.minecraft.class_1657;
import net.minecraft.class_1674;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_3966;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1674.class})
public abstract class LargeFireballMinix {
   @Shadow
   public int field_7624;

   @Inject(
      method = {"<init>(Lnet/minecraft/class_1937;Lnet/minecraft/class_1309;Lnet/minecraft/class_243;I)V"},
      at = {@At("RETURN")}
   )
   private void onFireballCreated(class_1937 level, class_1309 shooter, class_243 direction, int explosionPower, CallbackInfo ci) {
      if (!level.field_9236) {
         long currentDay = DateManager.getCurrentRealDay();
         if (currentDay >= 25L && DateManager.getCurrentRealDay() < 70L) {
            if (shooter instanceof class_1571) {
               class_1571 ghast = (class_1571)shooter;
               if (ghast.method_16914()) {
                  String name = ghast.method_5797().getString();
                  if (name.contains("Demoníaco")) {
                     this.field_7624 = 3 + level.method_8409().method_43048(3);
                  } else if (name.contains("Ender Ghast")) {
                     this.field_7624 = 6;
                  } else if (!name.contains("Demonio Flotate") && !name.contains("ghast feliz")) {
                     if (name.contains("Ghast Definitivo")) {
                        this.field_7624 = 12;
                     }
                  } else {
                     this.field_7624 = 0;
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"method_7454(Lnet/minecraft/class_3966;)V"},
      at = {@At("HEAD")}
   )
   private void onHitPlayer(class_3966 result, CallbackInfo ci) {
      class_1297 entityHit = result.method_17782();
      if (entityHit instanceof class_1657 player) {
         class_1674 fireball = (class_1674)(Object)this;
         class_1297 owner = fireball.method_24921();
         if (owner instanceof class_1571 ghast) {
            if (ghast.method_16914() && ghast.method_5797().getString().contains("Demonio Flotante")) {
               player.method_6092(new class_1293(class_1294.field_5902, 400, 49));
               player.method_6092(new class_1293(class_1294.field_5920, 400, 4));
            }
         }
      }

   }
}
