package com.serthekiller.permadeath.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1268;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1295;
import net.minecraft.class_1309;
import net.minecraft.class_1510;
import net.minecraft.class_1541;
import net.minecraft.class_1542;
import net.minecraft.class_1606;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import net.minecraft.class_8111;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public abstract class LivingEntityMixin {
   @Shadow
   public abstract void method_6033(float var1);

   @Shadow
   public abstract boolean method_6012();

   @Shadow
   public abstract boolean method_6092(class_1293 var1);

   @Shadow
   public abstract class_1799 method_5998(class_1268 var1);

   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void detectNightVisionExpire(CallbackInfo ci) {
      class_1309 entity = (class_1309)(Object)this;
      if (!entity.method_37908().field_9236 && entity instanceof class_1657) {
         if (entity.method_37908().method_27983().equals(class_1937.field_25181) || !(entity instanceof class_1657)) {
            if (DateManager.getCurrentRealDay() < 70L) {
               boolean var10000;
               label38: {
                  if (entity instanceof class_1510) {
                     class_1510 dragon = (class_1510)entity;
                     if (dragon.method_5805()) {
                        var10000 = true;
                        break label38;
                     }
                  }

                  var10000 = false;
               }

               boolean dragonAlive = var10000;
               long day = DateManager.getCurrentRealDay();
               if (day >= 30L && day < 40L && dragonAlive) {
                  class_1293 nvEffect = entity.method_6112(class_1294.field_5925);
                  if (nvEffect != null && nvEffect.method_5578() == 1 && nvEffect.method_5584() == 1) {
                     class_1295 cloud = new class_1295(entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321());
                     cloud.method_5603(1.5F);
                     cloud.method_5604(600);
                     cloud.method_5595(0);
                     cloud.method_5608(class_2398.field_11209);
                     cloud.method_5610(new class_1293(class_1294.field_5921, 1, 2));
                     entity.method_37908().method_8649(cloud);
                  }

               }
            }
         }
      }
   }

   @Inject(
      method = {"method_16077*(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$cancelShulkerVanillaDrops(class_3218 level, class_1282 damageSource, boolean hitByPlayer, CallbackInfo ci) {
      if (((Object)this) instanceof class_1606 && DateManager.getCurrentRealDay() < 70L) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_6078(Lnet/minecraft/class_1282;)V"},
      at = {@At("HEAD")}
   )
   private void onShulkerDeath(class_1282 source, CallbackInfo ci) {
      long currentDay = DateManager.getCurrentRealDay();
      class_1309 entity = (class_1309)(Object)this;
      if (entity instanceof class_1606 shulker) {
         if (!shulker.method_37908().field_9236) {
            class_1541 tnt = new class_1541(shulker.method_37908(), shulker.method_23317(), shulker.method_23318(), shulker.method_23321(), (class_1309)null);
            tnt.method_6967(80);
            shulker.method_37908().method_8649(tnt);
            if (currentDay < 40L) {
               float dropChance = currentDay == 35L ? 0.4F : 0.2F;
               if (shulker.method_59922().method_43057() < dropChance) {
                  class_1542 shell = new class_1542(shulker.method_37908(), shulker.method_23317(), shulker.method_23318(), shulker.method_23321(), new class_1799(class_1802.field_8815));
                  shell.method_5684(true);
                  shell.method_6982(85);
                  shulker.method_37908().method_8649(shell);
               }
            } else if (shulker.method_59922().method_43057() < 0.02F) {
               class_1542 shell = new class_1542(shulker.method_37908(), shulker.method_23317(), shulker.method_23318(), shulker.method_23321(), new class_1799(class_1802.field_8815));
               shell.method_5684(true);
               shell.method_6982(85);
               shulker.method_37908().method_8649(shell);
            }
         }
      }

   }

   @ModifyReturnValue(
      method = {"method_6130(I)I"},
      at = {@At("RETURN")}
   )
   private int fasterDrowning(int original) {
      long currentDay = DateManager.getCurrentRealDay();
      class_1309 self = (class_1309)(Object)this;
      int current = self.method_5669();
      if (original >= current) {
         return original;
      } else {
         int reduction;
         if (currentDay >= 60L) {
            reduction = 19;
         } else {
            if (currentDay < 50L) {
               return original;
            }

            reduction = 5;
         }

         int newValue = original - reduction;
         return original > -20 && newValue < -20 ? -20 : newValue;
      }
   }

   @Inject(
      method = {"method_64397(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;F)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void blockPotionDamage(class_3218 serverLevel, class_1282 source, float f, CallbackInfoReturnable<Boolean> cir) {
      class_1309 self = (class_1309)(Object)this;
      if (self.method_5752().contains("PotionImmune") && (source.method_49708(class_8111.field_42329) || source.method_49708(class_8111.field_42349))) {
         cir.setReturnValue(false);
         cir.cancel();
      }

   }
}
