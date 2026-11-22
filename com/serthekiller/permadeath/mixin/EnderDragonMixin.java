package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1295;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1315;
import net.minecraft.class_1510;
import net.minecraft.class_1527;
import net.minecraft.class_1538;
import net.minecraft.class_1541;
import net.minecraft.class_1559;
import net.minecraft.class_1639;
import net.minecraft.class_1657;
import net.minecraft.class_1670;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3730;
import net.minecraft.class_5134;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1510.class})
public abstract class EnderDragonMixin extends class_1308 {
   @Unique
   private boolean isEnraged = false;
   @Unique
   private int ticksAtPortal = 0;
   @Unique
   private int flyingAttackTimer = 0;
   @Unique
   private boolean isSpinning = false;
   @Unique
   private int spinTicks = 0;
   @Unique
   private static final float SPIN_SPEED = 3.6F;
   @Unique
   private int cloudSpawnTimer = 0;
   @Unique
   private boolean isLightningAttackActive = false;
   @Unique
   private int lightningAttackTicks = 0;
   @Unique
   private int lightningStrikeTimer = 0;
   @Unique
   private class_1657 lightningTargetPlayer = null;
   @Unique
   private static final int LIGHTNING_ATTACK_DURATION = 200;
   @Unique
   private static final int LIGHTNING_STRIKE_INTERVAL = 20;
   @Unique
   private int customFireballTimer = 0;
   @Unique
   private static final int FIREBALL_INTERVAL_NORMAL = 40;
   @Unique
   private static final int FIREBALL_INTERVAL_ENRAGED = 20;

   protected EnderDragonMixin(class_1299<? extends class_1308> entityType, class_1937 level) {
      super(entityType, level);
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/class_1299;Lnet/minecraft/class_1937;)V"},
      at = {@At("TAIL")}
   )
   private void initPermadeathDemon(class_1299<? extends class_1510> entityType, class_1937 level, CallbackInfo ci) {
      if (this.method_5996(class_5134.field_23716) != null) {
         this.method_5996(class_5134.field_23716).method_6192((double)2000.0F);
      }

      this.method_6033(2000.0F);
      this.method_5665(class_2561.method_43470("PERMADEATH DEMON").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067}));
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At("HEAD")}
   )
   private void managePhasesAndAttacks(CallbackInfo ci) {
      if (DateManager.getCurrentRealDay() < 70L) {
         if (!this.method_37908().field_9236) {
            class_3218 world = (class_3218)(Object)this.method_37908();
            this.checkEnragedPhase();
            this.handleSpinningAttack(world);
            if (!this.isSpinning) {
               this.handleFlyingAttacks(world);
               this.handleExtraFireballs(world);
            }

         }
      }
   }

   @Unique
   private void handleExtraFireballs(class_3218 world) {
      class_1510 dragon = (class_1510)(Object)this;
      class_1527 currentPhase = dragon.method_6831().method_6864().method_6849();
      if (currentPhase != class_1527.field_7073) {
         this.customFireballTimer = 0;
      } else if (dragon.method_5968() != null) {
         ++this.customFireballTimer;
         int interval = this.isEnraged ? 20 : 40;
         if (this.customFireballTimer >= interval) {
            this.shootCustomFireball(world, dragon.method_5968());
            this.customFireballTimer = 0;
         }

      }
   }

   @Unique
   private void shootCustomFireball(class_3218 world, class_1309 target) {
      class_243 dragonPos = this.method_19538().method_1031((double)0.0F, (double)this.method_17682() / (double)2.0F, (double)0.0F);
      class_243 targetPos = target.method_19538().method_1031((double)0.0F, (double)target.method_17682() / (double)2.0F, (double)0.0F);
      class_243 direction = targetPos.method_1020(dragonPos);
      class_1670 fireball = new class_1670(world, (class_1309)(Object)this, direction);
      fireball.method_5814(dragonPos.field_1352, dragonPos.field_1351, dragonPos.field_1350);
      world.method_8649(fireball);
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At("TAIL")}
   )
   private void applyContinuousSpin(CallbackInfo ci) {
      if (!this.method_37908().field_9236) {
         if (this.isSpinning) {
            this.performContinuousSpin((class_3218)(Object)this.method_37908());
         }

         if (this.isLightningAttackActive) {
            this.continueLightningAttack((class_3218)(Object)this.method_37908());
         }

      }
   }

   @Unique
   private void checkEnragedPhase() {
      float threshold = this.method_6063() / 6.0F;
      if (this.method_6032() <= threshold && !this.isEnraged) {
         this.isEnraged = true;
         this.method_5665(class_2561.method_43470("ENRAGED PERMADEATH DEMON").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067}));
      }

   }

   @Unique
   private void handleSpinningAttack(class_3218 world) {
      class_1510 dragon = (class_1510)(Object)this;
      class_1527 currentPhase = dragon.method_6831().method_6864().method_6849();
      boolean isPerched = currentPhase == class_1527.field_7081 || currentPhase == class_1527.field_7073 || currentPhase == class_1527.field_7072;
      if (isPerched) {
         if (this.isSpinning) {
            return;
         }

         ++this.ticksAtPortal;
         int triggerTime = 200;
         if (this.ticksAtPortal >= triggerTime) {
            this.isSpinning = true;
            this.spinTicks = 0;
            this.cloudSpawnTimer = 0;
            world.method_8396((class_1657)null, this.method_24515(), class_3417.field_14671, class_3419.field_15250, 5.0F, 0.5F);
         }
      } else {
         this.isSpinning = false;
         this.ticksAtPortal = 0;
      }

   }

   @Unique
   private void performContinuousSpin(class_3218 world) {
      ++this.spinTicks;
      float currentYRot = this.method_36454();
      float newYRot = currentYRot + 3.6F;
      if (newYRot >= 360.0F) {
         newYRot -= 360.0F;
      }

      this.method_36456(newYRot);
      this.field_5982 = currentYRot;
      this.method_5847(newYRot);
      this.field_6283 = newYRot;
      ++this.cloudSpawnTimer;
      if (this.cloudSpawnTimer >= 10) {
         this.spawnCircularWhiteClouds(world, this.method_19538());
         this.cloudSpawnTimer = 0;
      }

      if (this.spinTicks % 5 == 0) {
         world.method_65096(class_2398.field_11204, this.method_23317(), this.method_23318() + (double)1.0F, this.method_23321(), 10, (double)2.0F, (double)1.0F, (double)2.0F, 0.02);
      }

      if (this.spinTicks % 100 == 0) {
         world.method_8396((class_1657)null, this.method_24515(), class_3417.field_14550, class_3419.field_15250, 3.0F, 1.5F);
      }

   }

   @Unique
   private void spawnCircularWhiteClouds(class_3218 world, class_243 center) {
      float radius = 5.0F;

      for(int i = 0; i < 8; ++i) {
         double angle = (double)i * (double)45.0F;
         double radians = Math.toRadians(angle);
         double x = center.field_1352 + (double)radius * Math.cos(radians);
         double z = center.field_1350 + (double)radius * Math.sin(radians);
         double spawnY = center.field_1351;
         class_2338.class_2339 searchPos = new class_2338.class_2339(x, center.field_1351, z);

         for(int yOff = 0; yOff < 10; ++yOff) {
            if (!world.method_8320(searchPos).method_26215()) {
               spawnY = (double)searchPos.method_10264() + (double)1.5F;
               break;
            }

            searchPos.method_10100(0, -1, 0);
         }

         class_1295 cloud = new class_1295(world, x, spawnY, z);
         cloud.method_5603(2.5F);
         cloud.method_5604(60);
         cloud.method_5595(0);
         cloud.method_5608(class_2398.field_11204);
         world.method_8649(cloud);
      }

   }

   @Unique
   private void handleFlyingAttacks(class_3218 world) {
      class_1510 dragon = (class_1510)(Object)this;
      class_1527 currentPhase = dragon.method_6831().method_6864().method_6849();
      if (currentPhase != class_1527.field_7081 && currentPhase != class_1527.field_7073) {
         ++this.flyingAttackTimer;
         int attackInterval = this.isEnraged ? 900 : 1200;
         if (this.flyingAttackTimer >= attackInterval) {
            class_5819 rand = world.method_8409();
            float roll = rand.method_43057();
            if (roll < 0.48F) {
               this.spawnNightVisionAttack(world, this.method_19538());
            } else if (roll < 0.82F) {
               this.spawnTNTCircle(world, this.method_19538());
            } else if (roll < 0.98F) {
               this.spawnLightningAttack(world);
            } else if (roll < 0.99F) {
               this.spawnWitherSkeleton(world);
            } else {
               this.spawnEndermite(world);
            }

            this.flyingAttackTimer = 0;
         }
      } else {
         this.flyingAttackTimer = 0;
      }

   }

   @Unique
   private void spawnTNTCircle(class_3218 world, class_243 center) {
      double radius = (double)5.0F;

      for(double angle = (double)0.0F; angle < (double)360.0F; angle += (double)30.0F) {
         double radians = Math.toRadians(angle);
         double x = center.field_1352 + radius * Math.cos(radians);
         double z = center.field_1350 + radius * Math.sin(radians);
         class_1541 tnt = new class_1541(world, x, center.field_1351, z, (class_1309)(Object)this);
         tnt.method_6967(80);
         tnt.method_5780("PermadeathTNT");
         world.method_8649(tnt);
      }

      world.method_8396((class_1657)null, class_2338.method_49638(center), class_3417.field_14792, class_3419.field_15250, 100.0F, 2.0F);
   }

   @Unique
   private void spawnNightVisionAttack(class_3218 world, class_243 pos) {
      for(class_1657 player : world.method_18456()) {
         player.method_6092(new class_1293(class_1294.field_5925, 200, 1));
      }

   }

   @Unique
   private double findGroundY(class_3218 world, double x, double startY, double z) {
      class_2338.class_2339 searchPos = new class_2338.class_2339(x, startY, z);

      for(int yOff = 0; yOff < 10; ++yOff) {
         if (!world.method_8320(searchPos).method_26215()) {
            return (double)searchPos.method_10264() + (double)1.0F;
         }

         searchPos.method_10100(0, -1, 0);
      }

      return startY;
   }

   @Unique
   private void spawnLightningAttack(class_3218 world) {
      if (!world.method_18456().isEmpty()) {
         class_5819 rand = world.method_8409();
         List<? extends class_1657> players = world.method_18456();
         this.lightningTargetPlayer = (class_1657)players.get(rand.method_43048(players.size()));
         this.isLightningAttackActive = true;
         this.lightningAttackTicks = 0;
         this.lightningStrikeTimer = 0;
         world.method_8396((class_1657)null, this.method_24515(), class_3417.field_14865, class_3419.field_15250, 10.0F, 1.0F);
         this.strikeLightningNearTarget(world);
      }
   }

   @Unique
   private void continueLightningAttack(class_3218 world) {
      if (this.lightningTargetPlayer != null && !this.lightningTargetPlayer.method_31481()) {
         ++this.lightningAttackTicks;
         ++this.lightningStrikeTimer;
         if (this.lightningStrikeTimer >= 20) {
            this.strikeLightningNearTarget(world);
            this.lightningStrikeTimer = 0;
         }

         if (this.lightningAttackTicks >= 200) {
            this.isLightningAttackActive = false;
            this.lightningTargetPlayer = null;
         }

      } else {
         this.isLightningAttackActive = false;
      }
   }

   @Unique
   private void strikeLightningNearTarget(class_3218 world) {
      if (this.lightningTargetPlayer != null) {
         class_5819 rand = world.method_8409();
         double offsetX = (rand.method_43058() - (double)0.5F) * (double)4.0F;
         double offsetZ = (rand.method_43058() - (double)0.5F) * (double)4.0F;
         double x = this.lightningTargetPlayer.method_23317() + offsetX;
         double z = this.lightningTargetPlayer.method_23321() + offsetZ;
         class_1538 bolt = (class_1538)class_1299.field_6112.method_5883(world, class_3730.field_16461);
         if (bolt != null) {
            bolt.method_24203(x, this.lightningTargetPlayer.method_23318(), z);
            world.method_8649(bolt);
         }
      }
   }

   @Unique
   private void spawnWitherSkeleton(class_3218 world) {
      class_243 center = this.method_19538();
      double groundY = this.findGroundY(world, center.field_1352, center.field_1351, center.field_1350);
      class_2338 spawnPos = class_2338.method_49637(center.field_1352, groundY, center.field_1350);
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5808((double)spawnPos.method_10263() + (double)0.5F, (double)spawnPos.method_10264(), (double)spawnPos.method_10260() + (double)0.5F, world.method_8409().method_43057() * 360.0F, 0.0F);
      witherSkeleton.method_5943(world, world.method_8404(spawnPos), class_3730.field_16461, (class_1315)null);
      witherSkeleton.method_5780("PermadeathDragonSpawn");
      world.method_8649(witherSkeleton);
      world.method_8396((class_1657)null, spawnPos, class_3417.field_15163, class_3419.field_15250, 3.0F, 1.0F);
   }

   @Unique
   private void spawnEndermite(class_3218 world) {
      class_243 center = this.method_19538();
      double groundY = this.findGroundY(world, center.field_1352, center.field_1351, center.field_1350);
      class_2338 spawnPos = class_2338.method_49637(center.field_1352, groundY, center.field_1350);
      class_1559 endermite = new class_1559(class_1299.field_6128, world);
      endermite.method_5808((double)spawnPos.method_10263() + (double)0.5F, (double)spawnPos.method_10264(), (double)spawnPos.method_10260() + (double)0.5F, world.method_8409().method_43057() * 360.0F, 0.0F);
      endermite.method_5943(world, world.method_8404(spawnPos), class_3730.field_16461, (class_1315)null);
      endermite.method_5780("PermadeathDragonSpawn");
      world.method_8649(endermite);
      world.method_8396((class_1657)null, spawnPos, class_3417.field_15137, class_3419.field_15250, 3.0F, 1.0F);
   }
}
