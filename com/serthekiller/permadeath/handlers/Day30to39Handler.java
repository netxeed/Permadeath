package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.Cambios.Day25GiantMobsHandler;
import com.serthekiller.permadeath.Cambios.HostileMobConverter;
import com.serthekiller.permadeath.Cambios.PvpChanges;
import com.serthekiller.permadeath.Cambios.TotemManager;
import com.serthekiller.permadeath.mixin.GoalSelectorAccessor;
import com.serthekiller.permadeath.mixin.MobAccessor;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.MobProcessingTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1352;
import net.minecraft.class_1355;
import net.minecraft.class_1420;
import net.minecraft.class_1439;
import net.minecraft.class_1477;
import net.minecraft.class_1510;
import net.minecraft.class_1542;
import net.minecraft.class_1545;
import net.minecraft.class_1548;
import net.minecraft.class_1549;
import net.minecraft.class_1551;
import net.minecraft.class_1559;
import net.minecraft.class_1560;
import net.minecraft.class_1564;
import net.minecraft.class_1571;
import net.minecraft.class_1577;
import net.minecraft.class_1584;
import net.minecraft.class_1589;
import net.minecraft.class_1590;
import net.minecraft.class_1593;
import net.minecraft.class_1604;
import net.minecraft.class_1613;
import net.minecraft.class_1614;
import net.minecraft.class_1621;
import net.minecraft.class_1628;
import net.minecraft.class_1639;
import net.minecraft.class_1640;
import net.minecraft.class_1657;
import net.minecraft.class_1665;
import net.minecraft.class_1675;
import net.minecraft.class_1676;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2378;
import net.minecraft.class_2398;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3468;
import net.minecraft.class_4135;
import net.minecraft.class_4836;
import net.minecraft.class_5134;
import net.minecraft.class_5136;
import net.minecraft.class_7924;
import net.minecraft.class_9282;
import net.minecraft.class_9300;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_1352.class_4134;
import net.minecraft.class_1657.class_1658;
import net.minecraft.class_1665.class_1666;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day30to39Handler implements PhaseManager.PhaseHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDay30-39");
   private volatile boolean active = false;
   private boolean listenersRegistered = false;
   private int mobSpawnCooldown = 0;
   private static final int MOB_SPAWN_INTERVAL = 100;
   private int sleepCheckCooldown = 0;
   private static final int MOB_CONVERSION_INTERVAL = 60;
   private final Map<UUID, Integer> bedrockCooldowns = new HashMap();
   private static final int BEDROCK_COOLDOWN = 200;
   private static final List<class_1293> SPIDER_EFFECT_POOL;

   private static boolean isSpecialNeutral(class_1299<?> type) {
      return type == class_1299.field_6055 || type == class_1299.field_20346 || type == class_1299.field_6042 || type == class_1299.field_37419 || type == class_1299.field_6050 || type == class_1299.field_28315 || type == class_1299.field_22281 || type == class_1299.field_17943 || type == class_1299.field_6047 || type == class_1299.field_6074 || type == class_1299.field_6124 || type == class_1299.field_6081;
   }

   public void onPhaseStart(class_3218 world) {
      LOGGER.info("Iniciando Fase 4: Días 30-39");
      this.active = true;
      if (!this.listenersRegistered) {
         this.registerListeners();
         this.listenersRegistered = true;
      }

      MinecraftServer server = world.method_8503();
      if (server != null) {
         class_2561 message = class_2561.method_43470("§e=== Fase 4: Días 30-39 ===\n");
         server.method_3760().method_43514(message, false);
      }

      PvpChanges.applyPvpGamerule(world);
      Iterable var4 = world.method_27909();
      if (var4 instanceof class_1657 player) {
         if (player.method_6032() > player.method_6063()) {
            player.method_6033(player.method_6063());
         }
      }

   }

   private void registerListeners() {
      ServerLivingEntityEvents.ALLOW_DAMAGE.register((ServerLivingEntityEvents.AllowDamage)(entity, source, amount) -> {
         if (!this.active) {
            return true;
         } else {
            if (entity instanceof class_3222) {
               class_3222 player = (class_3222)entity;
               Boolean totemResult = TotemManager.handleTotemDamage(player, source, amount);
               if (totemResult != null) {
                  return totemResult;
               }
            }

            if (entity instanceof class_1548) {
               class_1548 creeper = (class_1548)entity;
               if (entity.method_37908().method_27983() == class_1937.field_25181 && source.method_5526() instanceof class_1676) {
                  for(int i = 0; i < 64; ++i) {
                     if (teleportRandomly(creeper)) {
                        return false;
                     }
                  }

                  return false;
               }
            }

            if (entity instanceof class_1571) {
               class_1571 ghast = (class_1571)entity;
               if (ghast.method_16914() && ghast.method_5797().getString().toUpperCase().contains("ENDER GHAST") && source.method_5526() instanceof class_1665 && ghast.method_37908().field_9229.method_43057() < 0.1F && teleportRandomly(ghast)) {
                  return false;
               }
            }

            return true;
         }
      });
      ServerEntityEvents.ENTITY_LOAD.register((ServerEntityEvents.Load)(entity, world1) -> {
         if (this.active) {
            if (world1 instanceof class_3218 && entity instanceof class_1309) {
               class_1309 livingEntity = (class_1309)entity;
               if (livingEntity instanceof class_1560) {
                  class_1560 enderman = (class_1560)livingEntity;
                  if (world1.method_27983() == class_1937.field_25181) {
                     if (enderman.method_31481()) {
                        return;
                     }

                     int chance = world1.field_9229.method_43048(100);
                     if (!MobProcessingTracker.tryClaim(enderman, "enderman_replaced")) {
                        return;
                     }

                     if (chance <= 10) {
                        class_1548 newCreeper = new class_1548(class_1299.field_6046, world1);
                        newCreeper.method_5814(enderman.method_23317(), enderman.method_23318(), enderman.method_23321());
                        world1.method_8649(newCreeper);
                        newCreeper.method_5665(class_2561.method_43470("§6Ender Creeper"));
                        enderman.method_31472();
                        return;
                     }

                     if (chance < 15) {
                        boolean dragonAlive = false;

                        for(class_1297 e : world1.method_27909()) {
                           if (e instanceof class_1510) {
                              class_1510 dragon = (class_1510)e;
                              if (dragon.method_5805()) {
                                 dragonAlive = true;
                                 break;
                              }
                           }
                        }

                        if (!dragonAlive) {
                           spawnEnderGhast(world1, enderman.method_23317(), enderman.method_23318() + (double)3.0F, enderman.method_23321());
                           enderman.method_31472();
                           return;
                        }
                     }
                  }
               }

               if (livingEntity instanceof class_1548) {
                  class_1548 creeper = (class_1548)livingEntity;
                  class_2487 nbt = new class_2487();
                  creeper.method_5652(nbt);
                  nbt.method_10556("powered", true);
                  creeper.method_5749(nbt);
                  if (world1.method_27983() == class_1937.field_25181) {
                     creeper.method_6092(new class_1293(class_1294.field_5905, -1, 0, false, false));
                     this.applyEndermanBehavior(creeper);
                  }
               }

               if (this.handleMobReplacement(livingEntity, world1)) {
                  return;
               }

               if (entity instanceof class_1613) {
                  class_1613 skeleton = (class_1613)entity;
                  this.convertNormalSkeletons(world1, skeleton);
               }

               this.applySpiderEffects(world1, livingEntity);
               this.applyRavagerBuffs(livingEntity);
               this.InsertEffect(livingEntity, world1);
               Day25GiantMobsHandler.transformGiantMobs(livingEntity, world1);
               class_1299<?> type = livingEntity.method_5864();
               if (!isSpecialNeutral(type) && HostileMobConverter.isAlreadyHostile(livingEntity)) {
                  return;
               }

               if (livingEntity instanceof class_1593) {
                  class_1593 phantom = (class_1593)livingEntity;
                  phantom.method_7091(9);
                  class_1324 maxHealthAttribute = phantom.method_5996(class_5134.field_23716);
                  if (maxHealthAttribute != null) {
                     double currentMaxHealth = maxHealthAttribute.method_6201();
                     maxHealthAttribute.method_6192(currentMaxHealth * (double)2.0F);
                     phantom.method_6033(phantom.method_6063());
                  }
               }

               HostileMobConverter.convertToHostile(livingEntity);
            }

         }
      });
      EntitySleepEvents.ALLOW_SLEEPING.register((EntitySleepEvents.AllowSleeping)(player, sleepingPos) -> {
         if (!this.active) {
            return null;
         } else {
            class_1937 patt0$temp = player.method_37908();
            if (patt0$temp instanceof class_3218) {
               class_3218 serverLevel = (class_3218)patt0$temp;
               serverLevel.method_65096(class_2398.field_11221, (double)sleepingPos.method_10263() + (double)0.5F, (double)(sleepingPos.method_10264() + 1), (double)sleepingPos.method_10260() + (double)0.5F, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
               serverLevel.method_60511((class_1657)null, (double)sleepingPos.method_10263(), (double)sleepingPos.method_10264(), (double)sleepingPos.method_10260(), class_3417.field_15152, class_3419.field_15245, 1.0F, 1.0F);
               if (player instanceof class_3222) {
                  class_3222 serverPlayer = (class_3222)player;
                  serverPlayer.method_14248().method_15023(serverPlayer, class_3468.field_15419.method_14956(class_3468.field_15429), 0);
               }

               player.method_7353(class_2561.method_43470("[PERMADEATH] Contador de phantoms reiniciado").method_27692(class_124.field_1061), false);
               return class_1658.field_7531;
            } else {
               return null;
            }
         }
      });
      ServerLivingEntityEvents.AFTER_DEATH.register((ServerLivingEntityEvents.AfterDeath)(entity, damageSource) -> {
         if (this.active) {
            long day = DateManager.getCurrentRealDay();
            if (day >= 25L) {
               Day25GiantMobsHandler.handleGiantMobArmorDrops(entity, damageSource);
            }

            this.handleRavagerTotemDrop(entity);
            if (entity instanceof class_1439 || entity instanceof class_5136 || entity instanceof class_1590 || entity instanceof class_1577 || entity instanceof class_1560 || entity instanceof class_1640 || entity instanceof class_1639 || entity instanceof class_1564 || entity instanceof class_1593 || entity instanceof class_1551 || entity instanceof class_1545 || entity instanceof class_4836 || entity instanceof class_1621 || entity instanceof class_1589 || entity instanceof class_1613 || entity instanceof class_1571) {
               class_1937 patt0$temp = entity.method_37908();
               if (patt0$temp instanceof class_3218) {
                  class_3218 serverLevel = (class_3218)patt0$temp;
                  serverLevel.method_18467(class_1542.class, entity.method_5829().method_1014((double)2.0F)).forEach((itemEntity) -> {
                     class_1799 itemStack = itemEntity.method_6983();
                     itemEntity.method_31472();
                  });
               }
            }

         }
      });
   }

   private void checkBedrockEffect(class_3222 player) {
      if (player.method_37908().method_27983().equals(class_1937.field_25181) && player.method_37908().method_8320(player.method_23312()).method_27852(class_2246.field_9987)) {
         UUID playerId = player.method_5667();
         int currentTick = player.method_5682().method_3780();
         Integer lastApplication = (Integer)this.bedrockCooldowns.get(playerId);
         if (lastApplication == null || currentTick - lastApplication >= 200) {
            player.method_6092(new class_1293(class_1294.field_5902, 200, 9, false, true));
            ((class_3218)player.method_37908()).method_65096(class_2398.field_11214, player.method_23317(), player.method_23318() + 0.1, player.method_23321(), 20, (double)0.5F, 0.1, (double)0.5F, 0.1);
            this.bedrockCooldowns.put(playerId, currentTick);
         }
      }

   }

   public static boolean teleportRandomly(class_1309 entity) {
      if (!entity.method_37908().method_8608() && entity.method_5805()) {
         double x = entity.method_23317() + (entity.method_59922().method_43058() - (double)0.5F) * (double)48.0F;
         double y = entity.method_23318() + (double)entity.method_59922().method_43048(32);
         double z = entity.method_23321() + (entity.method_59922().method_43058() - (double)0.5F) * (double)48.0F;
         boolean success = entity.method_6082(x, y, z, true);
         if (success) {
            entity.method_37908().method_43128((class_1657)null, entity.method_23317(), entity.method_23318(), entity.method_23321(), class_3417.field_14879, class_3419.field_15251, 1.0F, 1.0F);
         }

         return success;
      } else {
         return false;
      }
   }

   public static void spawnEnderGhast(class_3218 world, double x, double y, double z) {
      final class_1571 enderGhast = new class_1571(class_1299.field_6107, world);
      enderGhast.method_5814(x, y, z);
      enderGhast.method_5665(class_2561.method_43470("§6Ender Ghast"));
      enderGhast.method_5880(false);
      class_1324 maxHealth = enderGhast.method_5996(class_5134.field_23716);
      if (maxHealth != null) {
         maxHealth.method_6192((double)75.0F);
         enderGhast.method_6033(75.0F);
      }

      MobAccessor accessor = (MobAccessor)enderGhast;
      accessor.getGoalSelector().method_6277(1, new class_1352() {
         public boolean method_6264() {
            return enderGhast.method_59922().method_43057() < 0.02F;
         }

         public void method_6269() {
            Day30to39Handler.teleportRandomly(enderGhast);
         }
      });
      world.method_8649(enderGhast);
   }

   private boolean handleMobReplacement(class_1309 entity, class_3218 world) {
      if (entity.method_31481()) {
         return true;
      } else if (DateManager.getCurrentRealDay() < 30L) {
         return true;
      } else if (entity instanceof class_1477) {
         class_1577 guardian = new class_1577(class_1299.field_6118, world);
         guardian.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         guardian.method_36456(entity.method_36454());
         guardian.method_6092(new class_1293(class_1294.field_5904, -1, 1));
         world.method_8649(guardian);
         entity.method_31472();
         return true;
      } else if (entity instanceof class_1420) {
         class_1545 blaze = new class_1545(class_1299.field_6099, world);
         blaze.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         blaze.method_36456(entity.method_36454());
         blaze.method_6092(new class_1293(class_1294.field_5907, -1, 1));
         world.method_8649(blaze);
         entity.method_31472();
         return true;
      } else {
         return false;
      }
   }

   private void InsertEffect(class_1309 entity, class_3218 world) {
      if (entity instanceof class_1439 ironGolem) {
         if (!MobProcessingTracker.tryClaim(ironGolem, "insert_effect_d30_39")) {
            return;
         }

         ironGolem.method_6092(new class_1293(class_1294.field_5904, -1, 3, false, true));
      } else if (entity instanceof class_1560 enderman) {
         if (!MobProcessingTracker.tryClaim(enderman, "insert_effect_d30_39")) {
            return;
         }

         enderman.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, true));
      } else if (entity instanceof class_1604 pillager) {
         if (!MobProcessingTracker.tryClaim(pillager, "insert_effect_d30_39")) {
            return;
         }

         pillager.method_6092(new class_1293(class_1294.field_5905, -1, 0, false, false));
         class_1799 crossbox = new class_1799(class_1802.field_8399);
         class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
         class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
         registry.method_46746(class_1893.field_9098).ifPresent((holder) -> enchantments.method_57547(holder, 1));
         crossbox.method_57379(class_9334.field_49633, enchantments.method_57549());
         pillager.method_5673(class_1304.field_6173, crossbox);
         MobAccessor accessor = (MobAccessor)pillager;
         accessor.getGoalSelector().method_6277(1, new MachineGunGoal(pillager));
      } else if (entity instanceof class_1590 zp) {
         if (!MobProcessingTracker.tryClaim(zp, "insert_effect_d30_39")) {
            return;
         }

         zp.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8805));
         zp.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8058));
         zp.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8348));
         zp.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8285));
         zp.method_5665(class_2561.method_43470("§6Pigman Full Diamante"));
         zp.method_5880(false);
      } else if (entity instanceof class_4836 p) {
         if (!MobProcessingTracker.tryClaim(p, "insert_effect_d30_39")) {
            return;
         }

         p.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8862));
         p.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8678));
         p.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8416));
         p.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8753));
         p.method_5665(class_2561.method_43470("§6Piglin Full Oro"));
         p.method_5880(false);
      } else if (entity instanceof class_1545 b) {
         if (!MobProcessingTracker.tryClaim(b, "insert_effect_d30_39")) {
            return;
         }

         b.method_6092(new class_1293(class_1294.field_5907, -1, 1));
      } else if (entity instanceof class_1577 g) {
         if (!MobProcessingTracker.tryClaim(g, "insert_effect_d30_39")) {
            return;
         }

         g.method_6092(new class_1293(class_1294.field_5904, -1, 1));
      }

   }

   public void onPhaseEnd(class_3218 world) {
      LOGGER.info("Finalizando Fase 4: Días 30-39");
      this.active = false;
      this.bedrockCooldowns.clear();
      this.sleepCheckCooldown = 0;
      this.mobSpawnCooldown = 0;
   }

   public void onTick(class_3218 world) {
      if (DateManager.getCurrentRealDay() <= 40L && DateManager.getCurrentRealDay() >= 30L) {
         if (this.sleepCheckCooldown > 0) {
            --this.sleepCheckCooldown;
         }

         if (this.mobSpawnCooldown > 0) {
            --this.mobSpawnCooldown;
         }

         for(class_3222 player : world.method_18456()) {
            this.checkBedrockEffect(player);
         }

      }
   }

   public String getPhaseName() {
      return "Fase 4: Días 30-39";
   }

   private void applySpiderEffects(class_3218 world, class_1309 entity) {
      if (entity instanceof class_1628 spider) {
         if (!spider.method_6059(class_1294.field_5912) && !spider.method_6059(class_1294.field_5924) && !spider.method_6059(class_1294.field_5913)) {
            ArrayList<class_1293> availableEffects = new ArrayList(SPIDER_EFFECT_POOL);
            Collections.shuffle(availableEffects, new Random(world.field_9229.method_43055()));

            for(int i = 0; i < 5; ++i) {
               spider.method_6092((class_1293)availableEffects.get(i));
            }

            if (!(entity instanceof class_1549) && spider.method_5685().isEmpty()) {
               this.addSkeletonRider(spider, world);
            }
         }
      } else if (entity instanceof class_1614 silverfish) {
         if (!silverfish.method_6059(class_1294.field_5912) && !silverfish.method_6059(class_1294.field_5924) && !silverfish.method_6059(class_1294.field_5913)) {
            ArrayList<class_1293> availableEffects = new ArrayList(SPIDER_EFFECT_POOL);
            Collections.shuffle(availableEffects, new Random(world.field_9229.method_43055()));

            for(int i = 0; i < 5; ++i) {
               silverfish.method_6092((class_1293)availableEffects.get(i));
            }

            silverfish.method_5665(class_2561.method_43470("§6Silverfish de la Muerte"));
         }
      } else if (entity instanceof class_1559 endermite) {
         if (!endermite.method_6059(class_1294.field_5912) && !endermite.method_6059(class_1294.field_5924) && !endermite.method_6059(class_1294.field_5913)) {
            ArrayList<class_1293> availableEffects = new ArrayList(SPIDER_EFFECT_POOL);
            Collections.shuffle(availableEffects, new Random(world.field_9229.method_43055()));

            for(int i = 0; i < 5; ++i) {
               endermite.method_6092((class_1293)availableEffects.get(i));
            }
         }
      }

   }

   private void handleRavagerTotemDrop(class_1309 entity) {
      if (entity instanceof class_1584) {
         if (entity.method_37908().field_9229.method_43048(100) < 20) {
            class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), new class_1799(class_1802.field_8288));
            entity.method_37908().method_8649(itemEntity);
         }

      }
   }

   private void applyRavagerBuffs(class_1309 entity) {
      if (entity instanceof class_1584 ravager) {
         if (!ravager.method_6059(class_1294.field_5910)) {
            ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, false));
         }

         if (!ravager.method_6059(class_1294.field_5907)) {
            ravager.method_6092(new class_1293(class_1294.field_5907, -1, 0, false, false));
         }
      }

   }

   private void convertNormalSkeletons(class_3218 world, class_1613 skeleton) {
      if (MobProcessingTracker.tryClaim(skeleton, "skeleton_class")) {
         if (skeleton.method_6118(class_1304.field_6171).method_7909() == class_1802.field_8162) {
            if (!(skeleton.method_5854() instanceof class_1628)) {
               this.createRandomSkeletonClass(skeleton, world, (class_1628)null);
            }
         }
      }
   }

   private void addSkeletonRider(class_1628 spider, class_3218 world) {
      class_1613 skeleton = new class_1613(class_1299.field_6137, world);
      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      this.createRandomSkeletonClass(skeleton, world, spider);
   }

   private void createRandomSkeletonClass(class_1613 skeleton, class_3218 world, class_1628 spider) {
      int type = world.field_9229.method_43048(5);
      switch (type) {
         case 0 -> this.applyDiamondSkeletonEquipment(skeleton, world, spider);
         case 1 -> this.convertToWitherSkeletonWithBow(skeleton, world, spider);
         case 2 -> this.applyDiamondAxeSkeletonEquipment(skeleton, world, spider);
         case 3 -> this.applyCrossbowGoldSkeletonEquipment(skeleton, world, spider);
         case 4 -> this.convertToWitherSkeletonWithPowerBow(skeleton, world, spider);
      }

   }

   private void applyDiamondSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1628 spider) {
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      class_1799 helmet = new class_1799(class_1802.field_8805);
      class_1799 chest = new class_1799(class_1802.field_8058);
      class_1799 leggings = new class_1799(class_1802.field_8348);
      class_1799 boots = new class_1799(class_1802.field_8285);
      registry.method_46746(class_1893.field_9111).ifPresent((holder) -> {
         helmet.method_7978(holder, 4);
         chest.method_7978(holder, 4);
         leggings.method_7978(holder, 4);
         boots.method_7978(holder, 4);
      });
      skeleton.method_5673(class_1304.field_6169, helmet);
      skeleton.method_5673(class_1304.field_6174, chest);
      skeleton.method_5673(class_1304.field_6172, leggings);
      skeleton.method_5673(class_1304.field_6166, boots);
      skeleton.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8102));
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Esqueleto Guerrero"));
      if (spider != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(spider);
      }

   }

   private void convertToWitherSkeletonWithBow(class_1613 skeleton, class_3218 world, class_1628 spider) {
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5814(skeleton.method_23317(), skeleton.method_23318(), skeleton.method_23321());
      witherSkeleton.method_36456(skeleton.method_36454());
      witherSkeleton.method_5847(skeleton.method_5791());
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9116).ifPresent((holder) -> enchantments.method_57547(holder, 30));
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 25));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      witherSkeleton.method_5673(class_1304.field_6173, bow);
      witherSkeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8283));
      witherSkeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8873));
      witherSkeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8218));
      witherSkeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8313));
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      witherSkeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = witherSkeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         witherSkeleton.method_6033(40.0F);
      }

      witherSkeleton.method_5665(class_2561.method_43470("§6Esqueleto Táctico"));
      world.method_8649(witherSkeleton);
      if (spider != null) {
         witherSkeleton.method_5804(spider);
      }

      if (spider == null) {
         skeleton.method_31472();
      }

   }

   private void applyDiamondAxeSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1628 spider) {
      class_1799 axe = new class_1799(class_1802.field_8556);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9124).ifPresent((holder) -> enchantments.method_57547(holder, 10));
      axe.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, axe);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8743));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8523));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8396));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8660));
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Esqueleto Infernal"));
      if (spider != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(spider);
      }

   }

   private void applyCrossbowGoldSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1628 spider) {
      class_1799 crossbow = new class_1799(class_1802.field_8399);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 25));
      crossbow.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, crossbow);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8862));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8678));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8416));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8753));
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      skeleton.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, true));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Esqueleto Asesino"));
      if (spider != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(spider);
      }

   }

   private void convertToWitherSkeletonWithPowerBow(class_1613 skeleton, class_3218 world, class_1628 spider) {
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5814(skeleton.method_23317(), skeleton.method_23318(), skeleton.method_23321());
      witherSkeleton.method_36456(skeleton.method_36454());
      witherSkeleton.method_5847(skeleton.method_5791());
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 50));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      witherSkeleton.method_5673(class_1304.field_6173, bow);
      class_1799 helmet = new class_1799(class_1802.field_8267);
      helmet.method_57379(class_9334.field_49630, new class_9300(false));
      class_1799 chestplate = new class_1799(class_1802.field_8577);
      class_1799 leggings = new class_1799(class_1802.field_8570);
      class_1799 boots = new class_1799(class_1802.field_8370);
      this.dyeLeatherArmor(helmet, 11546150);
      this.dyeLeatherArmor(chestplate, 11546150);
      this.dyeLeatherArmor(leggings, 11546150);
      this.dyeLeatherArmor(boots, 11546150);
      witherSkeleton.method_5673(class_1304.field_6169, helmet);
      witherSkeleton.method_5673(class_1304.field_6174, chestplate);
      witherSkeleton.method_5673(class_1304.field_6172, leggings);
      witherSkeleton.method_5673(class_1304.field_6166, boots);
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      witherSkeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = witherSkeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         witherSkeleton.method_6033(40.0F);
      }

      witherSkeleton.method_5665(class_2561.method_43470("§6Esqueleto Pesadilla"));
      world.method_8649(witherSkeleton);
      if (spider != null) {
         witherSkeleton.method_5804(spider);
      }

      if (spider == null) {
         skeleton.method_31472();
      }

   }

   private void dyeLeatherArmor(class_1799 armor, int color) {
      armor.method_57379(class_9334.field_49644, new class_9282(color, false));
   }

   private void applyEndermanBehavior(class_1548 creeper) {
      MobAccessor accessor = (MobAccessor)creeper;
      class_1355 goalSelector = accessor.getGoalSelector();
      Set<class_4135> goals = ((GoalSelectorAccessor)goalSelector).getAvailableGoals();

      for(class_4135 wrapped : Set.copyOf(goals)) {
         if (wrapped.method_19058() instanceof EndermanTeleportGoal) {
            goalSelector.method_6280(wrapped.method_19058());
         }
      }

      class_1309 target = creeper.method_5968();
      goalSelector.method_6277(1, new EndermanTeleportGoal(creeper, target));
   }

   static {
      SPIDER_EFFECT_POOL = List.of(new class_1293(class_1294.field_5904, -1, 2, false, true), new class_1293(class_1294.field_5910, -1, 3, false, true), new class_1293(class_1294.field_5913, -1, 4, false, true), new class_1293(class_1294.field_5912, -1, 0, false, true), new class_1293(class_1294.field_5924, -1, 3, false, true), new class_1293(class_1294.field_5905, -1, 0, false, true), new class_1293(class_1294.field_5906, -1, 0, false, true), new class_1293(class_1294.field_5907, -1, 2, false, true));
   }

   public static class MachineGunGoal extends class_1352 {
      private final class_1604 mob;
      private int cooldown;
      private static final int FIRE_RATE = 3;

      public MachineGunGoal(class_1604 mob) {
         this.mob = mob;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         return this.mob.method_5968() != null && this.mob.method_24520((is) -> is.method_7909() instanceof class_1764);
      }

      public void method_6269() {
         this.cooldown = 0;
      }

      public void method_6268() {
         class_1309 target = this.mob.method_5968();
         if (target != null) {
            this.mob.method_5988().method_6226(target, 30.0F, 30.0F);
            if (--this.cooldown <= 0) {
               if (this.mob.method_5985().method_6369(target)) {
                  this.performShot(target);
                  this.cooldown = 3;
               } else {
                  this.cooldown = 10;
               }
            }

         }
      }

      private void performShot(class_1309 target) {
         class_1799 ammo = new class_1799(class_1802.field_8107);
         class_1665 arrow = class_1675.method_18813(this.mob, ammo, 1.0F, ammo);
         if (this.mob.method_6047().method_7909() instanceof class_1764) {
            arrow = class_1675.method_18813(this.mob, new class_1799(class_1802.field_8107), 1.0F, (class_1799)null);
         }

         double d0 = target.method_23317() - this.mob.method_23317();
         double d1 = target.method_23323(0.3333333333333333) - arrow.method_23318();
         double d2 = target.method_23321() - this.mob.method_23321();
         double d3 = Math.sqrt(d0 * d0 + d2 * d2);
         arrow.method_7485(d0, d1 + d3 * (double)0.2F, d2, 3.0F, 5.0F);
         this.mob.method_5783(class_3417.field_15187, 1.0F, 1.0F / (this.mob.method_59922().method_43057() * 0.4F + 0.8F));
         arrow.field_7572 = class_1666.field_7592;
         this.mob.method_37908().method_8649(arrow);
      }
   }

   public static class EndermanTeleportGoal extends class_1352 {
      private final class_1548 creeper;
      private final class_1309 target;

      public EndermanTeleportGoal(class_1548 creeper, class_1309 target) {
         this.creeper = creeper;
         this.target = target;
      }

      public boolean method_6264() {
         return this.creeper.method_5805() && this.creeper.method_7007() <= 0 && this.creeper.method_5968() == null && this.creeper.method_59922().method_43057() < 0.005F;
      }

      public boolean method_6266() {
         return false;
      }

      public void method_6269() {
         Day30to39Handler.teleportRandomly(this.creeper);
      }
   }
}
