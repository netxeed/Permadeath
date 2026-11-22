package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.mixin.GoalSelectorAccessor;
import com.serthekiller.permadeath.mixin.MobAccessor;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.MobProcessingTracker;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1314;
import net.minecraft.class_1324;
import net.minecraft.class_1338;
import net.minecraft.class_1341;
import net.minecraft.class_1347;
import net.minecraft.class_1352;
import net.minecraft.class_1353;
import net.minecraft.class_1355;
import net.minecraft.class_1361;
import net.minecraft.class_1366;
import net.minecraft.class_1374;
import net.minecraft.class_1376;
import net.minecraft.class_1379;
import net.minecraft.class_1387;
import net.minecraft.class_1391;
import net.minecraft.class_1393;
import net.minecraft.class_1399;
import net.minecraft.class_1400;
import net.minecraft.class_1408;
import net.minecraft.class_1420;
import net.minecraft.class_1421;
import net.minecraft.class_1428;
import net.minecraft.class_1429;
import net.minecraft.class_1431;
import net.minecraft.class_1480;
import net.minecraft.class_1481;
import net.minecraft.class_1493;
import net.minecraft.class_1584;
import net.minecraft.class_1590;
import net.minecraft.class_1614;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3532;
import net.minecraft.class_4051;
import net.minecraft.class_4135;
import net.minecraft.class_4466;
import net.minecraft.class_4836;
import net.minecraft.class_5134;
import net.minecraft.class_5354;
import net.minecraft.class_5762;
import net.minecraft.class_7102;
import net.minecraft.class_1352.class_4134;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostileMobConverter {
   private static final Logger LOGGER = LoggerFactory.getLogger("HostileMobConverter");
   private static final double BASE_ATTACK_DAMAGE = (double)3.0F;
   private static final double WOLF_ATTACK_DAMAGE = (double)4.0F;
   private static final double POLAR_BEAR_ATTACK_DAMAGE = (double)6.0F;
   private static final double BEE_ATTACK_DAMAGE = (double)2.0F;
   private static final double FROG_ATTACK_DAMAGE = (double)2.0F;
   private static final double BAT_ATTACK_DAMAGE = (double)1.0F;
   private static final double ZOMBIFIED_PIGLIN_ATTACK_DAMAGE = (double)2.0F;
   private static final double AXOLOTL_ATTACK_DAMAGE = (double)2.5F;
   private static final double PIGLIN_ATTACK_DAMAGE = (double)5.0F;
   private static final double SQUID_ATTACK_DAMAGE = (double)2.0F;

   public static void convertToHostile(class_1309 entity) {
      if (isConvertible(entity)) {
         if (entity.method_5864() == class_1299.field_6108 && entity instanceof class_1308) {
            class_1308 batMob = (class_1308)entity;
            if (DateManager.getCurrentRealDay() >= 20L) {
               if (MobProcessingTracker.tryClaimSession(batMob, "hostile_convert")) {
                  try {
                     convertBat(batMob);
                  } catch (Exception e) {
                     LOGGER.error("✗ Error al convertir murciélago a hostil", e);
                     MobProcessingTracker.clearSessionProcessed(batMob, "hostile_convert");
                  }

               }
            }
         } else if (entity instanceof class_1314) {
            class_1314 pathfinderMob = (class_1314)entity;
            if (DateManager.getCurrentRealDay() >= 20L) {
               if (MobProcessingTracker.tryClaimSession(pathfinderMob, "hostile_convert")) {
                  try {
                     class_1299<?> type = entity.method_5864();
                     if (type == class_1299.field_6055 || type == class_1299.field_6042 || type == class_1299.field_20346 || type == class_1299.field_37419 || type == class_1299.field_6050 || type == class_1299.field_28315 || type == class_1299.field_22281 || type == class_1299.field_6114 || type == class_1299.field_28402) {
                        convertSpecialMob(pathfinderMob);
                        return;
                     }

                     boolean hasAttackDamage = setupAttackAttributes(pathfinderMob);
                     clearPacificGoals(pathfinderMob);
                     injectHostileGoals(pathfinderMob, hasAttackDamage);
                  } catch (Exception e) {
                     LOGGER.error("✗ Error al convertir mob a hostil: {}", entity.method_5864().method_5882(), e);
                     MobProcessingTracker.clearSessionProcessed(pathfinderMob, "hostile_convert");
                  }

               }
            }
         }
      }
   }

   public static boolean converToRavager(class_1309 entity, class_3218 world) {
      try {
         long currentDay = DateManager.getCurrentRealDay();
         if (entity.method_16914()) {
            return false;
         } else if (entity.method_5752().contains("pink_pig")) {
            return false;
         } else if (currentDay >= 40L && currentDay < 50L) {
            class_1584 ravager = new class_1584(class_1299.field_6134, world);
            ravager.method_5808(entity.method_23317(), entity.method_23318(), entity.method_23321(), entity.method_36454(), entity.method_36455());
            entity.method_37908().method_8649(ravager);
            entity.method_31472();
            return true;
         } else if (currentDay >= 50L) {
            if (entity instanceof class_1428) {
               class_1614 silverfish = new class_1614(class_1299.field_6125, world);
               silverfish.method_5808(entity.method_23317(), entity.method_23318(), entity.method_23321(), entity.method_36454(), entity.method_36455());
               entity.method_37908().method_8649(silverfish);
               entity.method_31472();
               return true;
            } else {
               class_1584 ravager = new class_1584(class_1299.field_6134, world);
               ravager.method_5808(entity.method_23317(), entity.method_23318(), entity.method_23321(), entity.method_36454(), entity.method_36455());
               ((class_1324)Objects.requireNonNull(ravager.method_5996(class_5134.field_23716))).method_6192((double)500.0F);
               ravager.method_6033(500.0F);
               ravager.method_5665(class_2561.method_43470("Ultra Ravager").method_27692(class_124.field_1065));
               ravager.method_5880(false);
               ravager.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, false));
               ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, false));
               entity.method_37908().method_8649(ravager);
               entity.method_31472();
               return true;
            }
         } else {
            return true;
         }
      } catch (Exception e) {
         LOGGER.error("✗ Error al convertir mob a hostil: {}", entity.method_5864().method_5882(), e);
         return false;
      }
   }

   private static void convertSpecialMob(class_1314 mob) {
      try {
         class_1299<?> type = mob.method_5864();
         MobAccessor mobAccessor = (MobAccessor)mob;
         class_1355 goalSelector = mobAccessor.getGoalSelector();
         class_1355 targetSelector = mobAccessor.getTargetSelector();
         clearNonPlayerTargets(mob);
         double damage = (double)3.0F;
         if (type == class_1299.field_6055) {
            damage = (double)4.0F;
         } else if (type == class_1299.field_6042) {
            damage = (double)6.0F;
         } else if (type == class_1299.field_20346) {
            long currentDay = DateManager.getCurrentRealDay();
            damage = currentDay >= 50L ? (double)30.0F : (double)2.0F;
         } else if (type == class_1299.field_37419) {
            damage = (double)2.0F;
         } else if (type == class_1299.field_6108) {
            damage = (double)1.0F;
         } else if (type == class_1299.field_6050) {
            damage = (double)2.0F;
         } else if (type == class_1299.field_28315) {
            damage = (double)2.5F;
         } else if (type == class_1299.field_22281) {
            damage = (double)5.0F;
         } else if (type == class_1299.field_6114 || type == class_1299.field_28402) {
            damage = (double)2.0F;
         }

         class_1324 attr = mob.method_5996(class_5134.field_23721);
         if (attr != null) {
            attr.method_6192(damage);
         }

         targetSelector.method_6277(1, new PersistentPlayerTargetGoal(mob));
         class_1657 nearestPlayer = mob.method_37908().method_18460(mob, (double)64.0F);
         if (nearestPlayer != null && (nearestPlayer.method_7337() || nearestPlayer.method_7325())) {
            nearestPlayer = null;
         }

         Objects.requireNonNull(mob);
         byte var10 = 0;
         //$FF: var10->value
         //0->net/minecraft/class_1590
         //1->net/minecraft/class_4836
         //2->net/minecraft/class_5762
         //3->net/minecraft/class_5354
         switch (mob.typeSwitch<invokedynamic>(mob, var10)) {
            case 0:
               class_1590 zombifiedPiglin = (class_1590)mob;
               zombifiedPiglin.method_29514(Integer.MAX_VALUE);
               zombifiedPiglin.method_19540(true);
               if (nearestPlayer != null) {
                  zombifiedPiglin.method_5980(nearestPlayer);
                  zombifiedPiglin.method_6015(nearestPlayer);
               }

               goalSelector.method_6277(2, new class_1366(mob, (double)1.0F, false));
               break;
            case 1:
               class_4836 piglin = (class_4836)mob;
               clearAllGoals(mob);
               goalSelector.method_6277(1, new PiglinAggressiveAttackGoal(piglin, damage));
               goalSelector.method_6277(2, new class_1347(mob));
               goalSelector.method_6277(3, new class_1379(mob, 0.8));
               goalSelector.method_6277(4, new class_1361(mob, class_1657.class, 8.0F));
               goalSelector.method_6277(5, new class_1376(mob));
               piglin.method_19540(true);
               if (nearestPlayer != null) {
                  piglin.method_5980(nearestPlayer);
                  piglin.method_6015(nearestPlayer);
               }
               break;
            case 2:
               class_5762 axolotl = (class_5762)mob;
               clearAllGoals(mob);
               goalSelector.method_6277(1, new AxolotlAggressiveAttackGoal(axolotl, damage));
               goalSelector.method_6277(3, new class_1379(mob, 0.8));
               goalSelector.method_6277(4, new class_1361(mob, class_1657.class, 8.0F));
               goalSelector.method_6277(5, new class_1376(mob));
               if (nearestPlayer != null) {
                  axolotl.method_5980(nearestPlayer);
               }

               goalSelector.method_6277(1, new CustomMeleeAttackGoal(mob, 1.2, true, damage));
               break;
            case 3:
               class_5354 neutral = (class_5354)mob;
               neutral.method_29514(Integer.MAX_VALUE);
               if (nearestPlayer != null) {
                  mob.method_5980(nearestPlayer);
                  neutral.method_6015(nearestPlayer);
               }

               if (mob instanceof class_4466 bee) {
                  if (bee.method_5968() instanceof class_1657) {
                     Method method = class_4466.class.getDeclaredMethod("setHasStung", Boolean.TYPE);
                     method.setAccessible(true);
                     method.invoke(bee, false);
                  }
               }

               if (mob instanceof class_1493 wolf) {
                  wolf.method_6015(nearestPlayer);
               }
         }

         if (type == class_1299.field_37419) {
            clearAllGoals(mob);
            goalSelector.method_6277(1, new FrogTongueAttackGoal((class_7102)mob, damage));
            goalSelector.method_6277(2, new class_1347(mob));
         }

         if (type == class_1299.field_6114 || type == class_1299.field_28402) {
            clearAllGoals(mob);
            goalSelector.method_6277(1, new SquidAggressiveAttackGoal(mob, damage));
         }
      } catch (Exception e) {
         LOGGER.error("Error al convertir mob especial", e);
      }

   }

   private static void convertBat(class_1308 mob) {
      try {
         if (!(mob instanceof class_1420)) {
            return;
         }

         class_1420 bat = (class_1420)mob;
         MobAccessor mobAccessor = (MobAccessor)mob;
         class_1355 goalSelector = mobAccessor.getGoalSelector();
         clearAllGoals(mob);
         class_1324 attr = mob.method_5996(class_5134.field_23721);
         if (attr != null) {
            attr.method_6192((double)1.0F);
         }

         bat.method_6449(false);
         goalSelector.method_6277(1, new BatAggressiveFlightGoal(bat, (double)1.0F));
      } catch (Exception e) {
         LOGGER.error("Error al convertir murciélago especial", e);
      }

   }

   private static void clearAllGoals(class_1308 mob) {
      try {
         MobAccessor mobAccessor = (MobAccessor)mob;
         class_1355 goalSelector = mobAccessor.getGoalSelector();
         Set<class_4135> goals = ((GoalSelectorAccessor)goalSelector).getAvailableGoals();

         for(class_4135 wrappedGoal : Set.copyOf(goals)) {
            goalSelector.method_6280(wrappedGoal.method_19058());
         }
      } catch (Exception e) {
         LOGGER.warn("Error al limpiar todos los goals", e);
      }

   }

   private static void clearNonPlayerTargets(class_1314 mob) {
      try {
         MobAccessor accessor = (MobAccessor)mob;
         class_1355 targetSelector = accessor.getTargetSelector();
         Set<class_4135> goals = ((GoalSelectorAccessor)targetSelector).getAvailableGoals();

         for(class_4135 wrapped : Set.copyOf(goals)) {
            class_1352 goal = wrapped.method_19058();
            if (!(goal instanceof class_1400) && !(goal instanceof PersistentPlayerTargetGoal) && !(goal instanceof class_1399)) {
               targetSelector.method_6280(goal);
            }
         }
      } catch (Exception e) {
         LOGGER.warn("Error limpiando targets", e);
      }

   }

   private static boolean isConvertible(class_1309 entity) {
      if (entity instanceof class_1657) {
         return false;
      } else if (entity.method_5864() == class_1299.field_6131) {
         return false;
      } else if (entity.method_5864() == class_1299.field_6091) {
         return false;
      } else {
         Objects.requireNonNull(entity);
         byte var2 = 0;
         //$FF: var2->value
         //0->net/minecraft/class_1429
         //1->net/minecraft/class_1421
         //2->net/minecraft/class_1480
         switch (entity.typeSwitch<invokedynamic>(entity, var2)) {
            case 0:
               class_1429 ignored = (class_1429)entity;
               return true;
            case 1:
               class_1421 ignored = (class_1421)entity;
               return true;
            case 2:
               class_1480 ignored = (class_1480)entity;
               return true;
            default:
               class_1299<?> type = entity.method_5864();
               if (type == class_1299.field_6047) {
                  return true;
               } else if (type == class_1299.field_6077) {
                  return true;
               } else if (type == class_1299.field_17713) {
                  return true;
               } else if (type == class_1299.field_38384) {
                  return true;
               } else if (type == class_1299.field_28315) {
                  return true;
               } else if (type == class_1299.field_28402) {
                  return true;
               } else if (type == class_1299.field_6114) {
                  return true;
               } else if (type == class_1299.field_6087) {
                  return true;
               } else if (type == class_1299.field_37420) {
                  return true;
               } else if (type == class_1299.field_37419) {
                  return true;
               } else if (type == class_1299.field_6055) {
                  return true;
               } else if (type == class_1299.field_20346) {
                  return true;
               } else if (type == class_1299.field_6042) {
                  return true;
               } else if (type == class_1299.field_6108) {
                  return true;
               } else if (type == class_1299.field_23696) {
                  return true;
               } else if (type == class_1299.field_6050) {
                  return true;
               } else if (type == class_1299.field_22281) {
                  return true;
               } else if (type == class_1299.field_6081) {
                  return true;
               } else if (type == class_1299.field_6074) {
                  return true;
               } else if (type == class_1299.field_17943) {
                  return true;
               } else {
                  return type == class_1299.field_6124;
               }
         }
      }
   }

   private static boolean setupAttackAttributes(class_1314 mob) {
      class_1324 attackAttribute = mob.method_5996(class_5134.field_23721);
      if (attackAttribute != null) {
         attackAttribute.method_6192((double)3.0F);
         return true;
      } else {
         return false;
      }
   }

   private static void clearPacificGoals(class_1314 mob) {
      try {
         MobAccessor mobAccessor = (MobAccessor)mob;
         class_1355 goalSelector = mobAccessor.getGoalSelector();
         Set<class_4135> availableGoals = ((GoalSelectorAccessor)goalSelector).getAvailableGoals();

         for(class_4135 wrappedGoal : Set.copyOf(availableGoals)) {
            class_1352 goal = wrappedGoal.method_19058();
            if (goal instanceof class_1374 || goal instanceof class_1338 || goal instanceof class_1379 || goal instanceof class_1361 || goal instanceof class_1341 || goal instanceof class_1353 || goal instanceof class_1391 || goal instanceof class_1387 || goal instanceof class_1393) {
               goalSelector.method_6280(goal);
            }
         }
      } catch (Exception e) {
         LOGGER.warn("Error al limpiar goals pacíficos", e);
      }

   }

   private static void injectHostileGoals(class_1314 mob, boolean hasAttackDamage) {
      long currentDay = DateManager.getCurrentRealDay();
      MobAccessor mobAccessor = (MobAccessor)mob;
      class_1355 goalSelector = mobAccessor.getGoalSelector();
      class_1355 targetSelector = mobAccessor.getTargetSelector();
      if (hasAttackDamage) {
         goalSelector.method_6277(1, new class_1366(mob, 1.2, false));
      } else {
         goalSelector.method_6277(1, new CustomMeleeAttackGoal(mob, 1.2, false, (double)3.0F));
      }

      if (mob instanceof class_1431 && currentDay >= 50L) {
         goalSelector.method_6277(1, new CustomMeleeAttackGoal(mob, 1.2, false, (double)59.0F));
      }

      targetSelector.method_6277(1, new class_1400(mob, class_1657.class, 10, true, false, (class_4051.class_10254)null));
      targetSelector.method_6277(2, new class_1399(mob, new Class[0]));
   }

   public static boolean isAlreadyHostile(class_1309 entity) {
      if (entity instanceof class_1308 mob) {
         return MobProcessingTracker.isSessionProcessed(mob, "hostile_convert");
      } else {
         return false;
      }
   }

   private static class PersistentPlayerTargetGoal extends class_1352 {
      private final class_1314 mob;
      private int searchCooldown;

      public PersistentPlayerTargetGoal(class_1314 mob) {
         this.mob = mob;
         this.searchCooldown = 0;
      }

      public boolean method_6264() {
         return true;
      }

      public void method_6268() {
         if (this.searchCooldown > 0) {
            --this.searchCooldown;
         } else {
            this.searchCooldown = 20;
            class_1657 nearestPlayer = this.mob.method_37908().method_18460(this.mob, (double)32.0F);
            if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
               this.mob.method_5980(nearestPlayer);
               class_1314 var3 = this.mob;
               if (var3 instanceof class_5354) {
                  class_5354 neutral = (class_5354)var3;
                  neutral.method_29514(Integer.MAX_VALUE);
                  neutral.method_6015(nearestPlayer);
                  class_1314 var4 = this.mob;
                  if (var4 instanceof class_1493) {
                     class_1493 wolf = (class_1493)var4;
                     wolf.method_6015(nearestPlayer);
                  }

                  var4 = this.mob;
                  if (var4 instanceof class_1590) {
                     class_1590 zombifiedPiglin = (class_1590)var4;
                     zombifiedPiglin.method_29514(Integer.MAX_VALUE);
                     zombifiedPiglin.method_19540(true);
                     zombifiedPiglin.method_6015(nearestPlayer);
                  }

                  var4 = this.mob;
                  if (var4 instanceof class_4836) {
                     class_4836 piglin = (class_4836)var4;
                     piglin.method_19540(true);
                     piglin.method_6015(nearestPlayer);
                  }
               }
            }

         }
      }
   }

   private static class FrogTongueAttackGoal extends class_1352 {
      private final class_7102 frog;
      private final double attackDamage;
      private class_1657 targetPlayer;
      private int attackCooldown;
      private static final int ATTACK_INTERVAL = 40;
      private static final double ATTACK_RANGE = (double)6.0F;

      public FrogTongueAttackGoal(class_7102 frog, double attackDamage) {
         this.frog = frog;
         this.attackDamage = attackDamage;
         this.attackCooldown = 0;
      }

      public boolean method_6264() {
         class_1657 nearestPlayer = this.frog.method_37908().method_18459(this.frog.method_23317(), this.frog.method_23318(), this.frog.method_23321(), (double)6.0F, false);
         if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
            this.targetPlayer = nearestPlayer;
            return true;
         } else {
            return false;
         }
      }

      public boolean method_6266() {
         return this.targetPlayer != null && this.targetPlayer.method_5805();
      }

      public void method_6269() {
         this.frog.method_5980(this.targetPlayer);
      }

      public void method_6268() {
         if (this.targetPlayer != null) {
            this.frog.method_5988().method_6226(this.targetPlayer, 30.0F, 30.0F);
            double distance = (double)this.frog.method_5739(this.targetPlayer);
            if (distance <= (double)6.0F && this.attackCooldown <= 0) {
               this.performTongueAttack();
               this.attackCooldown = 40;
            } else if (distance > (double)3.0F && this.frog.method_24828()) {
               class_243 direction = (new class_243(this.targetPlayer.method_23317() - this.frog.method_23317(), (double)0.0F, this.targetPlayer.method_23321() - this.frog.method_23321())).method_1029();
               float yaw = (float)(class_3532.method_15349(direction.field_1350, direction.field_1352) * (180D / Math.PI)) - 90.0F;
               this.frog.method_36456(yaw);
               this.frog.field_6283 = yaw;
               this.frog.field_6241 = yaw;
               this.frog.method_18800(direction.field_1352 * (double)0.5F, (double)0.5F, direction.field_1350 * (double)0.5F);
            }

            if (this.attackCooldown > 0) {
               --this.attackCooldown;
            }

         }
      }

      private void performTongueAttack() {
         class_1282 damageSource = this.frog.method_48923().method_48812(this.frog);
         this.targetPlayer.method_64419(damageSource, (float)this.attackDamage);
         class_243 direction = (new class_243(this.frog.method_23317() - this.targetPlayer.method_23317(), 0.1, this.frog.method_23321() - this.targetPlayer.method_23321())).method_1029();
         this.targetPlayer.method_18800(direction.field_1352 * 0.4, 0.3, direction.field_1350 * 0.4);
      }
   }

   private static class AxolotlAggressiveAttackGoal extends class_1352 {
      private final class_5762 axolotl;
      private final double attackDamage;
      private final double speedModifier = 0.2;
      private class_1657 targetPlayer;
      private int ticksUntilNextAttack;
      private static final int ATTACK_INTERVAL = 20;
      private static final double ATTACK_RANGE_SQR = (double)4.0F;

      public AxolotlAggressiveAttackGoal(class_5762 axolotl, double attackDamage) {
         this.axolotl = axolotl;
         this.attackDamage = attackDamage;
         this.ticksUntilNextAttack = 0;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         class_1657 nearestPlayer = this.axolotl.method_37908().method_18460(this.axolotl, (double)32.0F);
         if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
            this.targetPlayer = nearestPlayer;
            this.axolotl.method_5980(nearestPlayer);
            return true;
         } else {
            return false;
         }
      }

      public boolean method_6266() {
         return this.targetPlayer != null && this.targetPlayer.method_5805() && this.axolotl.method_5858(this.targetPlayer) < (double)1024.0F;
      }

      public void method_6269() {
         this.axolotl.method_19540(true);
         this.ticksUntilNextAttack = 0;
      }

      public void method_6270() {
         this.targetPlayer = null;
         this.axolotl.method_5942().method_6340();
      }

      public void method_6268() {
         if (this.targetPlayer != null) {
            this.axolotl.method_5988().method_6226(this.targetPlayer, 30.0F, 30.0F);
            this.axolotl.method_5980(this.targetPlayer);
            if (this.axolotl.method_5799()) {
               this.axolotl.method_5875(false);
               this.axolotl.method_5942().method_6335(this.targetPlayer, 1.2);
            } else {
               class_1408 var10000 = this.axolotl.method_5942();
               class_1657 var10001 = this.targetPlayer;
               Objects.requireNonNull(this);
               var10000.method_6335(var10001, 0.2);
            }

            double distanceSquared = this.axolotl.method_5858(this.targetPlayer);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            double reach = this.axolotl.method_5799() ? (double)5.0F : (double)4.0F;
            if (distanceSquared <= reach && this.ticksUntilNextAttack <= 0) {
               this.performAttack();
               this.ticksUntilNextAttack = 20;
            }

         }
      }

      private void performAttack() {
         class_1282 damageSource = this.axolotl.method_48923().method_48812(this.axolotl);
         this.targetPlayer.method_64419(damageSource, (float)this.attackDamage);
      }
   }

   private static class BatAggressiveFlightGoal extends class_1352 {
      private final class_1420 bat;
      private final double attackDamage;
      private class_1657 targetPlayer;
      private int ticksUntilNextAttack;
      private static final int ATTACK_INTERVAL = 20;
      private static final double ATTACK_RANGE_SQR = (double)4.0F;
      private static final double FLIGHT_SPEED = 0.15;

      public BatAggressiveFlightGoal(class_1420 bat, double attackDamage) {
         this.bat = bat;
         this.attackDamage = attackDamage;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         class_1657 nearestPlayer = this.bat.method_37908().method_18460(this.bat, (double)32.0F);
         if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
            this.targetPlayer = nearestPlayer;
            return true;
         } else {
            return false;
         }
      }

      public boolean method_6266() {
         return this.targetPlayer != null && this.targetPlayer.method_5805() && this.bat.method_5858(this.targetPlayer) < (double)1024.0F;
      }

      public void method_6269() {
         this.bat.method_6449(false);
         this.ticksUntilNextAttack = 0;
      }

      public void method_6270() {
         this.targetPlayer = null;
         this.bat.method_18799(class_243.field_1353);
      }

      public void method_6268() {
         if (this.targetPlayer != null) {
            this.bat.method_5988().method_6226(this.targetPlayer, 30.0F, 30.0F);
            class_243 direction = new class_243(this.targetPlayer.method_23317() - this.bat.method_23317(), this.targetPlayer.method_23318() + (double)this.targetPlayer.method_5751() * (double)0.5F - this.bat.method_23318(), this.targetPlayer.method_23321() - this.bat.method_23321());
            double distanceSquared = direction.method_1027();
            if (distanceSquared > 0.01) {
               class_243 normalized = direction.method_1029();
               this.bat.method_18799(this.bat.method_18798().method_1019(normalized.method_1021(0.15)).method_1021(0.8));
               float yaw = (float)(class_3532.method_15349(direction.field_1352, direction.field_1350) * (180D / Math.PI));
               this.bat.method_36456(-yaw);
               this.bat.field_6283 = this.bat.method_36454();
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if (distanceSquared <= (double)4.0F && this.ticksUntilNextAttack <= 0) {
               this.performAttack();
               this.ticksUntilNextAttack = 20;
            }

         }
      }

      private void performAttack() {
         class_1282 damageSource = this.bat.method_48923().method_48812(this.bat);
         this.targetPlayer.method_64419(damageSource, (float)this.attackDamage);
      }
   }

   private static class SquidAggressiveAttackGoal extends class_1352 {
      private final class_1314 squid;
      private final double attackDamage;
      private class_1657 targetPlayer;
      private int ticksUntilNextAttack;
      private static final int ATTACK_INTERVAL = 20;
      private static final double ATTACK_RANGE_SQR = (double)6.0F;
      private static final double SWIM_SPEED = 0.1;

      public SquidAggressiveAttackGoal(class_1314 squid, double attackDamage) {
         this.squid = squid;
         this.attackDamage = attackDamage;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         class_1657 nearestPlayer = this.squid.method_37908().method_18460(this.squid, (double)32.0F);
         if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
            this.targetPlayer = nearestPlayer;
            return true;
         } else {
            return false;
         }
      }

      public boolean method_6266() {
         return this.targetPlayer != null && this.targetPlayer.method_5805() && this.squid.method_5858(this.targetPlayer) < (double)1024.0F;
      }

      public void method_6269() {
         this.squid.method_5980(this.targetPlayer);
         this.ticksUntilNextAttack = 0;
      }

      public void method_6270() {
         this.targetPlayer = null;
         this.squid.method_18799(this.squid.method_18798().method_18805(0.2, 0.2, 0.2));
      }

      public void method_6268() {
         if (this.targetPlayer != null) {
            this.squid.method_5988().method_6226(this.targetPlayer, 30.0F, 30.0F);
            this.squid.method_5980(this.targetPlayer);
            class_243 direction = new class_243(this.targetPlayer.method_23317() - this.squid.method_23317(), this.targetPlayer.method_23318() + (double)0.5F - this.squid.method_23318(), this.targetPlayer.method_23321() - this.squid.method_23321());
            double distanceSquared = direction.method_1027();
            if (distanceSquared > (double)1.0F) {
               class_243 normalized = direction.method_1029();
               this.squid.method_18799(this.squid.method_18798().method_1019(normalized.method_1021(0.1)).method_1021(0.9));
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if (distanceSquared <= (double)6.0F && this.ticksUntilNextAttack <= 0) {
               this.performAttack();
               this.ticksUntilNextAttack = 20;
            }

         }
      }

      private void performAttack() {
         class_1282 damageSource = this.squid.method_48923().method_48812(this.squid);
         this.targetPlayer.method_64419(damageSource, (float)this.attackDamage);
      }
   }

   private static class PiglinAggressiveAttackGoal extends class_1352 {
      private final class_4836 piglin;
      private final double attackDamage;
      private final double speedModifier = (double)1.0F;
      private class_1657 targetPlayer;
      private int ticksUntilNextAttack;
      private static final int ATTACK_INTERVAL = 20;
      private static final double ATTACK_RANGE_SQR = (double)4.0F;

      public PiglinAggressiveAttackGoal(class_4836 piglin, double attackDamage) {
         this.piglin = piglin;
         this.attackDamage = attackDamage;
         this.ticksUntilNextAttack = 0;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         class_1657 nearestPlayer = this.piglin.method_37908().method_18460(this.piglin, (double)32.0F);
         if (nearestPlayer != null && nearestPlayer.method_5805() && !nearestPlayer.method_7337() && !nearestPlayer.method_7325()) {
            this.targetPlayer = nearestPlayer;
            this.piglin.method_5980(nearestPlayer);
            return true;
         } else {
            return false;
         }
      }

      public boolean method_6266() {
         return this.targetPlayer != null && this.targetPlayer.method_5805() && this.piglin.method_5858(this.targetPlayer) < (double)1024.0F;
      }

      public void method_6269() {
         this.piglin.method_19540(true);
         this.ticksUntilNextAttack = 0;
      }

      public void method_6270() {
         this.targetPlayer = null;
         this.piglin.method_5942().method_6340();
      }

      public void method_6268() {
         if (this.targetPlayer != null) {
            this.piglin.method_5988().method_6226(this.targetPlayer, 30.0F, 30.0F);
            this.piglin.method_5980(this.targetPlayer);
            this.piglin.method_19540(true);
            double distanceSquared = this.piglin.method_5649(this.targetPlayer.method_23317(), this.targetPlayer.method_23318(), this.targetPlayer.method_23321());
            class_1408 var10000 = this.piglin.method_5942();
            class_1657 var10001 = this.targetPlayer;
            Objects.requireNonNull(this);
            var10000.method_6335(var10001, (double)1.0F);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if (distanceSquared <= (double)4.0F && this.ticksUntilNextAttack <= 0) {
               this.performAttack();
               this.ticksUntilNextAttack = 20;
            }

         }
      }

      private void performAttack() {
         class_1282 damageSource = this.piglin.method_48923().method_48812(this.piglin);
         this.targetPlayer.method_64419(damageSource, (float)this.attackDamage);
      }
   }

   private static class CustomMeleeAttackGoal extends class_1352 {
      private final class_1314 mob;
      private final double speedModifier;
      private final boolean followingTargetEvenIfNotSeen;
      private final double attackDamage;
      private int ticksUntilNextAttack;
      private static final int ATTACK_INTERVAL = 20;

      public CustomMeleeAttackGoal(class_1314 mob, double speedModifier, boolean followingTargetEvenIfNotSeen, double attackDamage) {
         this.mob = mob;
         this.speedModifier = speedModifier;
         this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
         this.attackDamage = attackDamage;
         this.ticksUntilNextAttack = 0;
      }

      public boolean method_6264() {
         class_1309 target = this.mob.method_5968();
         return target != null && target.method_5805();
      }

      public boolean method_6266() {
         class_1309 target = this.mob.method_5968();
         if (target != null && target.method_5805()) {
            return this.followingTargetEvenIfNotSeen || this.mob.method_5985().method_6369(target);
         } else {
            return false;
         }
      }

      public void method_6269() {
         this.mob.method_19540(true);
         this.ticksUntilNextAttack = 0;
      }

      public void method_6270() {
         this.mob.method_19540(false);
         this.mob.method_5942().method_6340();
      }

      public void method_6268() {
         class_1309 target = this.mob.method_5968();
         if (target != null) {
            this.mob.method_5988().method_6226(target, 30.0F, 30.0F);
            double distanceSquared = this.mob.method_5649(target.method_23317(), target.method_23318(), target.method_23321());
            double attackReachSquared = this.getAttackReachSqr(target);
            this.mob.method_5942().method_6335(target, this.speedModifier);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if (distanceSquared <= attackReachSquared && this.ticksUntilNextAttack <= 0) {
               this.performAttack(target);
               this.ticksUntilNextAttack = 20;
            }

         }
      }

      private void performAttack(class_1309 target) {
         long currentDay = DateManager.getCurrentRealDay();
         class_1282 damageSource = this.mob.method_48923().method_48812(this.mob);
         float damage = (float)this.attackDamage;
         if (this.mob instanceof class_1431 && currentDay >= 50L) {
            class_243 knockbackDir = (new class_243(target.method_23317() - this.mob.method_23317(), 0.1, target.method_23321() - this.mob.method_23321())).method_1029().method_1021((double)50.0F);
            target.method_18799(knockbackDir);
            target.field_6037 = true;
         }

         if (this.mob instanceof class_1481 && currentDay >= 70L) {
            class_1293 strength = this.mob.method_6112(class_1294.field_5910);
            if (strength != null) {
               damage += (float)(strength.method_5578() + 1) * 3.0F;
            }
         }

         target.method_64419(damageSource, damage);
      }

      private double getAttackReachSqr(class_1309 target) {
         return (double)(this.mob.method_17681() * 2.0F * this.mob.method_17681() * 2.0F + target.method_17681());
      }
   }
}
