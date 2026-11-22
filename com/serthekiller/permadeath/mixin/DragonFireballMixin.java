package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1295;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1510;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_1670;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_2398;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1670.class})
public class DragonFireballMixin {
   @Inject(
      method = {"method_7488(Lnet/minecraft/class_239;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z"
)},
      cancellable = true
   )
   private void customBallImpact(class_239 result, CallbackInfo ci) {
      if (DateManager.getCurrentRealDay() < 70L) {
         class_1670 ball = (class_1670)(Object)this;
         if (!ball.method_37908().field_9236) {
            boolean isEnraged = false;
            class_1297 var6 = ball.method_24921();
            if (var6 instanceof class_1510) {
               class_1510 dragon = (class_1510)var6;
               class_2561 name = dragon.method_5797();
               if (name != null && name.getString().contains("ENRAGED")) {
                  isEnraged = true;
               }
            }

            double rand = ball.method_37908().field_9229.method_43058() * (double)100.0F;
            if (isEnraged) {
               if (rand < (double)20.0F) {
                  this.spawnCustomCloud(ball, "WHITE");
               } else if (rand < (double)40.0F) {
                  this.spawnCustomCloud(ball, "BLACK");
               } else if (rand < (double)60.0F) {
                  this.spawnCustomCloud(ball, "NIGHT_VISION");
               } else if (rand < (double)80.0F) {
                  this.spawnCustomCloud(ball, "GRAY");
               } else {
                  this.spawnLightningCloud(ball);
               }
            } else if (rand < (double)22.0F) {
               this.spawnCustomCloud(ball, "WHITE");
            } else if (rand < (double)40.0F) {
               this.spawnCustomCloud(ball, "HEARTS");
            } else if (rand < (double)55.0F) {
               this.spawnCustomCloud(ball, "BLACK");
            } else {
               if (rand < (double)68.0F) {
                  return;
               }

               if (rand < (double)79.0F) {
                  this.spawnCustomCloud(ball, "GRAY");
               } else if (rand < (double)88.0F) {
                  this.spawnCustomCloud(ball, "GREEN");
               } else if (rand < (double)94.0F) {
                  this.spawnCustomCloud(ball, "PURPLE");
               } else if (rand < (double)97.0F) {
                  this.generateBlockArea(ball, class_2246.field_10164.method_9564());
               } else if (rand < (double)99.0F) {
                  this.spawnLightningCloud(ball);
               } else {
                  this.generateBlockArea(ball, class_2246.field_9987.method_9564());
               }
            }

            ball.method_31472();
            ci.cancel();
         }
      }
   }

   @Unique
   private void spawnCustomCloud(class_1670 ball, String type) {
      class_1295 cloud = new class_1295(ball.method_37908(), ball.method_23317(), ball.method_23318(), ball.method_23321());
      cloud.method_5607(ball.method_24921() instanceof class_1309 ? (class_1309)ball.method_24921() : null);
      cloud.method_5604(600);
      cloud.method_5595(0);
      switch (type) {
         case "WHITE":
            cloud.method_5608(class_2398.field_11204);
            cloud.method_5603(6.0F);
            break;
         case "BLACK":
            cloud.method_5608(class_2398.field_11251);
            cloud.method_5603(4.0F);
            cloud.method_5610(new class_1293(class_1294.field_5919, 600, 2));
            cloud.method_5610(new class_1293(class_1294.field_5920, 600, 2));
            cloud.method_5610(new class_1293(class_1294.field_5909, 600, 2));
            break;
         case "PURPLE":
            cloud.method_5608(class_2398.field_11214);
            cloud.method_5603(4.0F);
            cloud.method_5610(new class_1293(class_1294.field_5899, 600, 1));
            break;
         case "NIGHT_VISION":
            for(class_1657 player : ball.method_37908().method_18456()) {
               player.method_6092(new class_1293(class_1294.field_5925, 200, 1));
            }

            return;
         case "GREEN":
            cloud.method_5608(class_2398.field_11211);
            cloud.method_5603(7.0F);
            break;
         case "GRAY":
            cloud.method_5608(class_2398.field_11219);
            cloud.method_5603(2.5F);
            break;
         case "HEARTS":
            cloud.method_5603(1.5F);
            cloud.method_5604(600);
            cloud.method_5595(0);
            cloud.method_5608(class_2398.field_11233);
            cloud.method_5610(new class_1293(class_1294.field_5921, 1, 2));
      }

      ball.method_37908().method_8649(cloud);
   }

   @Unique
   private void spawnLightningCloud(class_1670 ball) {
      class_3218 world = (class_3218)ball.method_37908();
      double impactX = ball.method_23317();
      double impactY = ball.method_23318();
      double impactZ = ball.method_23321();
      class_2338 impactPos = class_2338.method_49637(impactX, impactY, impactZ);
      class_2338 groundPos = impactPos;

      for(int i = 0; i < 50; ++i) {
         class_2338 checkPos = impactPos.method_10087(i);
         if (!world.method_8320(checkPos).method_26215()) {
            groundPos = checkPos.method_10084();
            break;
         }
      }

      class_1295 cloud = new class_1295(world, (double)groundPos.method_10263() + (double)0.5F, (double)groundPos.method_10264(), (double)groundPos.method_10260() + (double)0.5F);
      cloud.method_5607(ball.method_24921() instanceof class_1309 ? (class_1309)ball.method_24921() : null);
      cloud.method_5604(600);
      cloud.method_5595(0);
      cloud.method_5603(0.5F);
      cloud.method_5608(class_2398.field_29644);
      cloud.method_5780("LightningCloud");
      world.method_8649(cloud);
      class_1538 lightning = new class_1538(class_1299.field_6112, world);
      if (lightning != null) {
         lightning.method_24203((double)groundPos.method_10263() + (double)0.5F, (double)groundPos.method_10264(), (double)groundPos.method_10260() + (double)0.5F);
         lightning.method_29498(false);
         world.method_8649(lightning);
      }

   }

   @Unique
   private void generateBlockArea(class_1670 ball, class_2680 blockState) {
      class_1937 var4 = ball.method_37908();
      if (var4 instanceof class_3218 level) {
         class_2338 centerPos = class_2338.method_49637(ball.method_23317(), ball.method_23318(), ball.method_23321());
         class_2338 groundPos = centerPos;

         for(int i = 0; i < 20; ++i) {
            class_2338 checkPos = centerPos.method_10087(i);
            if (!level.method_8320(checkPos).method_26215()) {
               groundPos = checkPos;
               break;
            }
         }

         for(int x = 0; x < 2; ++x) {
            for(int z = 0; z < 2; ++z) {
               class_2338 targetPos = groundPos.method_10069(x, 0, z);
               level.method_8652(targetPos, blockState, 3);
            }
         }

      }
   }
}
