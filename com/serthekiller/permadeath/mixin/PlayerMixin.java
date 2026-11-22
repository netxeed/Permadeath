package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1294;
import net.minecraft.class_1295;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1510;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1657.class})
public abstract class PlayerMixin extends class_1309 {
   @Unique
   private boolean permadeath$hadNightVision = false;

   protected PlayerMixin(class_1299<? extends class_1309> entityType, class_1937 level) {
      super(entityType, level);
   }

   @Inject(
      method = {"method_5773()V"},
      at = {@At("TAIL")}
   )
   private void trackNightVisionExpiry(CallbackInfo ci) {
      if (!this.method_37908().field_9236) {
         if (this.method_37908().method_27983().equals(class_1937.field_25181)) {
            if (DateManager.getCurrentRealDay() < 70L) {
               class_3218 serverLevel = (class_3218)(Object)this.method_37908();
               boolean dragonAlive = false;

               for(class_1297 e : serverLevel.method_27909()) {
                  if (e instanceof class_1510) {
                     class_1510 dragon = (class_1510)e;
                     if (dragon.method_5805()) {
                        dragonAlive = true;
                        break;
                     }
                  }
               }

               long day = DateManager.getCurrentRealDay();
               if (day >= 30L && day < 40L && dragonAlive) {
                  class_1657 player = (class_1657)(Object)this;
                  boolean hasNightVision = player.method_6059(class_1294.field_5925);
                  if (this.permadeath$hadNightVision && !hasNightVision) {
                     this.spawnDarkHeartsCloud(serverLevel, player);
                  }

                  this.permadeath$hadNightVision = hasNightVision;
               }
            }
         }
      }
   }

   @Unique
   private void spawnDarkHeartsCloud(class_3218 world, class_1657 player) {
      class_1295 cloud = new class_1295(world, player.method_23317(), player.method_23318(), player.method_23321());
      cloud.method_5603(1.5F);
      cloud.method_5604(600);
      cloud.method_5595(0);
      cloud.method_5608(class_2398.field_11209);
      cloud.method_5780("DarkHearts");
      world.method_8649(cloud);
   }
}
