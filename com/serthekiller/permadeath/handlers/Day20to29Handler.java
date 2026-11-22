package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.Cambios.Day25GiantMobsHandler;
import com.serthekiller.permadeath.Cambios.HostileMobConverter;
import com.serthekiller.permadeath.Cambios.PvpChanges;
import com.serthekiller.permadeath.items.ModArmorMaterials;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1439;
import net.minecraft.class_1542;
import net.minecraft.class_1545;
import net.minecraft.class_1549;
import net.minecraft.class_1551;
import net.minecraft.class_1560;
import net.minecraft.class_1564;
import net.minecraft.class_1571;
import net.minecraft.class_1577;
import net.minecraft.class_1584;
import net.minecraft.class_1590;
import net.minecraft.class_1593;
import net.minecraft.class_1613;
import net.minecraft.class_1621;
import net.minecraft.class_1628;
import net.minecraft.class_1639;
import net.minecraft.class_1640;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2378;
import net.minecraft.class_2398;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3468;
import net.minecraft.class_4836;
import net.minecraft.class_5134;
import net.minecraft.class_5136;
import net.minecraft.class_7924;
import net.minecraft.class_9282;
import net.minecraft.class_9300;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_1657.class_1658;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day20to29Handler implements PhaseManager.PhaseHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDay20-29");
   private volatile boolean active = false;
   private boolean listenersRegistered = false;
   private final Set<UUID> playersTryingToSleep = new HashSet();
   private int sleepCheckCooldown = 0;
   private int mobSpawnCooldown = 0;
   private static final List<class_1293> SPIDER_EFFECT_POOL;

   private static boolean isSpecialNeutral(class_1299<?> type) {
      return type == class_1299.field_6055 || type == class_1299.field_20346 || type == class_1299.field_6042 || type == class_1299.field_37419 || type == class_1299.field_6050 || type == class_1299.field_28315 || type == class_1299.field_22281 || type == class_1299.field_17943 || type == class_1299.field_6047 || type == class_1299.field_6074 || type == class_1299.field_6124 || type == class_1299.field_6081;
   }

   public void onPhaseStart(class_3218 world) {
      LOGGER.info("Iniciando Fase 3: Días 20-29");
      this.active = true;
      if (!this.listenersRegistered) {
         this.registerListeners();
         this.listenersRegistered = true;
      }

      MinecraftServer server = world.method_8503();
      if (server != null) {
         class_2561 message = class_2561.method_43470("§e=== Fase 3: Días 20-29 ===\n");
         server.method_3760().method_43514(message, false);
      }

      PvpChanges.applyPvpGamerule(world);
      Iterable var4 = world.method_27909();
      if (var4 instanceof class_1657 player) {
         if (player.method_6032() > player.method_6063()) {
            player.method_6033(player.method_6063());
         }
      }

      this.convertAllExistingMobs(world);
   }

   private void convertAllExistingMobs(class_3218 world) {
      for(class_1297 entity : world.method_27909()) {
         if (entity instanceof class_1309 livingEntity) {
            class_1299<?> type = livingEntity.method_5864();
            if (isSpecialNeutral(type) || !HostileMobConverter.isAlreadyHostile(livingEntity)) {
               HostileMobConverter.convertToHostile(livingEntity);
            }
         }
      }

   }

   private void registerListeners() {
      ServerEntityEvents.ENTITY_LOAD.register((ServerEntityEvents.Load)(entity, world1) -> {
         if (this.active) {
            if (world1 instanceof class_3218 && entity instanceof class_1309) {
               class_1309 livingEntity = (class_1309)entity;
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
               day25Handler.handleCaveSpiderHelmetDrop(entity, damageSource);
               Day25GiantMobsHandler.handleGiantMobArmorDrops(entity, damageSource);
            }

            this.handleRavagerTotemDrop(entity);
            if ((entity instanceof class_1621 || entity instanceof class_1571) && !entity.method_16914()) {
               class_1937 patt0$temp = entity.method_37908();
               if (patt0$temp instanceof class_3218) {
                  class_3218 serverLevel = (class_3218)patt0$temp;
                  serverLevel.method_18467(class_1542.class, entity.method_5829().method_1014((double)2.0F)).forEach((itemEntity) -> itemEntity.method_31472());
               }
            }

            if (entity instanceof class_1439 || entity instanceof class_5136 || entity instanceof class_1590 || entity instanceof class_1577 || entity instanceof class_1560 || entity instanceof class_1640 || entity instanceof class_1639 || entity instanceof class_1613 || entity instanceof class_1564 || entity instanceof class_1593 || entity instanceof class_1551 || entity instanceof class_1545 || entity instanceof class_4836 || entity.method_16914() && (entity.method_5797().getString().contains("GIGA Slime") || entity.method_5797().getString().contains("GIGA MagmaCube") || entity.method_5797().getString().contains("Demoníaco"))) {
               class_1937 patt0$temp = entity.method_37908();
               if (patt0$temp instanceof class_3218) {
                  class_3218 serverLevel = (class_3218)patt0$temp;
                  serverLevel.method_18467(class_1542.class, entity.method_5829().method_1014((double)2.0F)).forEach((itemEntity) -> {
                     class_1799 itemStack = itemEntity.method_6983();
                     class_1792 item = itemStack.method_7909();
                     boolean isNetheriteArmor = item == ModArmorMaterials.NETHERITE_HELMET || item == ModArmorMaterials.NETHERITE_CHESTPLATE || item == ModArmorMaterials.NETHERITE_LEGGINGS || item == ModArmorMaterials.NETHERITE_BOOTS;
                     if (!isNetheriteArmor) {
                        itemEntity.method_31472();
                     }

                  });
               }
            }

         }
      });
   }

   public void onPhaseEnd(class_3218 world) {
      LOGGER.info("Finalizando Fase 3: Días 20-29");
      this.active = false;
      this.playersTryingToSleep.clear();
      this.sleepCheckCooldown = 0;
      this.mobSpawnCooldown = 0;
   }

   public void onTick(class_3218 world) {
      if (DateManager.getCurrentRealDay() <= 30L && DateManager.getCurrentRealDay() >= 20L) {
         if (this.sleepCheckCooldown > 0) {
            --this.sleepCheckCooldown;
         }

         if (this.mobSpawnCooldown > 0) {
            --this.mobSpawnCooldown;
         }

         this.applySpiderEffects(world);
         if (DateManager.getCurrentRealDay() >= 25L && world.method_8510() % 100L == 0L) {
            for(class_1297 entity : world.method_27909()) {
               if (entity instanceof class_1584) {
                  class_1584 ravager = (class_1584)entity;
                  this.applyRavagerBuffs(ravager);
               }
            }
         }

      }
   }

   public String getPhaseName() {
      return "";
   }

   private void applySpiderEffects(class_3218 world) {
      for(class_1297 entity : world.method_27909()) {
         if (entity instanceof class_1628 spider) {
            if (!spider.method_6059(class_1294.field_5912) && !spider.method_6059(class_1294.field_5924) && !spider.method_6059(class_1294.field_5913)) {
               ArrayList<class_1293> availableEffects = new ArrayList(SPIDER_EFFECT_POOL);
               long day = DateManager.getCurrentRealDay();
               int effectCount;
               if (day >= 25L) {
                  effectCount = 5;
               } else {
                  effectCount = world.field_9229.method_43048(3) + 3;
               }

               Collections.shuffle(availableEffects, new Random(world.field_9229.method_43055()));

               for(int i = 0; i < effectCount && i < availableEffects.size(); ++i) {
                  spider.method_6092((class_1293)availableEffects.get(i));
               }

               if (!(entity instanceof class_1549) && spider.method_5685().isEmpty()) {
                  this.addSkeletonRider(spider, world);
               }
            }
         }
      }

   }

   private void handleRavagerTotemDrop(class_1309 entity) {
      if (entity instanceof class_1584) {
         int chance = DateManager.getCurrentRealDay() >= 25L ? 20 : 1;
         if (entity.method_37908().field_9229.method_43048(100) < chance) {
            class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), new class_1799(class_1802.field_8288));
            entity.method_37908().method_8649(itemEntity);
         }

      }
   }

   private void applyRavagerBuffs(class_1584 ravager) {
      if (!ravager.method_6059(class_1294.field_5910)) {
         ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, false));
      }

      if (!ravager.method_6059(class_1294.field_5907)) {
         ravager.method_6092(new class_1293(class_1294.field_5907, -1, 0, false, false));
      }

   }

   private void addSkeletonRider(class_1628 spider, class_3218 world) {
      int type = world.field_9229.method_43048(5);
      switch (type) {
         case 0 -> this.createDiamondSkeleton(spider, world);
         case 1 -> this.createBowWitherSkeleton(spider, world);
         case 2 -> this.createIronAxeSkeleton(spider, world);
         case 3 -> this.createCrossbowGoldSkeleton(spider, world);
         case 4 -> this.createPowerBowWitherSkeleton(spider, world);
      }

   }

   private void createDiamondSkeleton(class_1628 spider, class_3218 world) {
      class_1613 skeleton = new class_1613(class_1299.field_6137, world);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8805));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8058));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8348));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8285));
      skeleton.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8102));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)20.0F);
         skeleton.method_6033(20.0F);
      }

      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      world.method_8649(skeleton);
      skeleton.method_5804(spider);
   }

   private void createBowWitherSkeleton(class_1628 spider, class_3218 world) {
      class_1639 skeleton = new class_1639(class_1299.field_6076, world);
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9116).ifPresent((holder) -> enchantments.method_57547(holder, 20));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, bow);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8283));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8873));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8218));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8313));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      world.method_8649(skeleton);
      skeleton.method_5804(spider);
   }

   private void createIronAxeSkeleton(class_1628 spider, class_3218 world) {
      class_1613 skeleton = new class_1613(class_1299.field_6137, world);
      class_1799 axe = new class_1799(class_1802.field_8475);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9124).ifPresent((holder) -> enchantments.method_57547(holder, 2));
      axe.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, axe);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8743));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8523));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8396));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8660));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)20.0F);
         skeleton.method_6033(20.0F);
      }

      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      world.method_8649(skeleton);
      skeleton.method_5804(spider);
   }

   private void createCrossbowGoldSkeleton(class_1628 spider, class_3218 world) {
      class_1613 skeleton = new class_1613(class_1299.field_6137, world);
      class_1799 crossbow = new class_1799(class_1802.field_8399);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 20));
      crossbow.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, crossbow);
      skeleton.method_5673(class_1304.field_6169, new class_1799(class_1802.field_8862));
      skeleton.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8678));
      skeleton.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8416));
      skeleton.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8753));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      world.method_8649(skeleton);
      skeleton.method_5804(spider);
   }

   private void createPowerBowWitherSkeleton(class_1628 spider, class_3218 world) {
      class_1639 skeleton = new class_1639(class_1299.field_6076, world);
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 10));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      skeleton.method_5673(class_1304.field_6173, bow);
      class_1799 helmet = new class_1799(class_1802.field_8267);
      helmet.method_57379(class_9334.field_49630, new class_9300(false));
      class_1799 chestplate = new class_1799(class_1802.field_8577);
      class_1799 leggings = new class_1799(class_1802.field_8570);
      class_1799 boots = new class_1799(class_1802.field_8370);
      this.dyeLeatherArmor(helmet, 11546150);
      this.dyeLeatherArmor(chestplate, 11546150);
      this.dyeLeatherArmor(leggings, 11546150);
      this.dyeLeatherArmor(boots, 11546150);
      skeleton.method_5673(class_1304.field_6169, helmet);
      skeleton.method_5673(class_1304.field_6174, chestplate);
      skeleton.method_5673(class_1304.field_6172, leggings);
      skeleton.method_5673(class_1304.field_6166, boots);
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)40.0F);
         skeleton.method_6033(40.0F);
      }

      skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      world.method_8649(skeleton);
      skeleton.method_5804(spider);
   }

   private void dyeLeatherArmor(class_1799 armor, int color) {
      armor.method_57379(class_9334.field_49644, new class_9282(color, false));
   }

   static {
      SPIDER_EFFECT_POOL = List.of(new class_1293(class_1294.field_5904, -1, 2, false, true), new class_1293(class_1294.field_5910, -1, 3, false, true), new class_1293(class_1294.field_5913, -1, 4, false, true), new class_1293(class_1294.field_5912, -1, 0, false, true), new class_1293(class_1294.field_5924, -1, 3, false, true), new class_1293(class_1294.field_5905, -1, 0, false, true), new class_1293(class_1294.field_5906, -1, 0, false, true), new class_1293(class_1294.field_5907, -1, 2, false, true));
   }
}
