package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.Cambios.CripticShulkerHandler;
import com.serthekiller.permadeath.Cambios.Day25GiantMobsHandler;
import com.serthekiller.permadeath.Cambios.GiantZombieHandler;
import com.serthekiller.permadeath.Cambios.HostileMobConverter;
import com.serthekiller.permadeath.Cambios.ModuloDeMuerte;
import com.serthekiller.permadeath.Cambios.MushroomSpawn;
import com.serthekiller.permadeath.Cambios.PvpChanges;
import com.serthekiller.permadeath.Cambios.Ravagercreator;
import com.serthekiller.permadeath.Cambios.SupernovaCatManager;
import com.serthekiller.permadeath.Cambios.TotemManager;
import com.serthekiller.permadeath.Cambios.WitherEmperorHandler;
import com.serthekiller.permadeath.Cambios.WitherSpawn;
import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.items.ModArmorMaterials;
import com.serthekiller.permadeath.items.ModItems;
import com.serthekiller.permadeath.mixin.MobAccessor;
import com.serthekiller.permadeath.mixin.SleepCounter;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.MobProcessingTracker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.class_124;
import net.minecraft.class_1269;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1315;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1352;
import net.minecraft.class_1400;
import net.minecraft.class_1420;
import net.minecraft.class_1439;
import net.minecraft.class_1451;
import net.minecraft.class_1452;
import net.minecraft.class_1454;
import net.minecraft.class_1456;
import net.minecraft.class_1462;
import net.minecraft.class_1473;
import net.minecraft.class_1477;
import net.minecraft.class_1493;
import net.minecraft.class_1510;
import net.minecraft.class_1528;
import net.minecraft.class_1541;
import net.minecraft.class_1542;
import net.minecraft.class_1545;
import net.minecraft.class_1547;
import net.minecraft.class_1548;
import net.minecraft.class_1549;
import net.minecraft.class_1550;
import net.minecraft.class_1551;
import net.minecraft.class_1559;
import net.minecraft.class_1560;
import net.minecraft.class_1564;
import net.minecraft.class_1569;
import net.minecraft.class_1571;
import net.minecraft.class_1576;
import net.minecraft.class_1577;
import net.minecraft.class_1581;
import net.minecraft.class_1584;
import net.minecraft.class_1589;
import net.minecraft.class_1590;
import net.minecraft.class_1593;
import net.minecraft.class_1604;
import net.minecraft.class_1606;
import net.minecraft.class_1613;
import net.minecraft.class_1614;
import net.minecraft.class_1621;
import net.minecraft.class_1627;
import net.minecraft.class_1628;
import net.minecraft.class_1632;
import net.minecraft.class_1634;
import net.minecraft.class_1639;
import net.minecraft.class_1640;
import net.minecraft.class_1642;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1665;
import net.minecraft.class_1667;
import net.minecraft.class_1675;
import net.minecraft.class_1676;
import net.minecraft.class_1686;
import net.minecraft.class_1764;
import net.minecraft.class_1766;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_1844;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2378;
import net.minecraft.class_238;
import net.minecraft.class_2398;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3213;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3730;
import net.minecraft.class_4466;
import net.minecraft.class_4590;
import net.minecraft.class_4836;
import net.minecraft.class_5134;
import net.minecraft.class_5136;
import net.minecraft.class_52;
import net.minecraft.class_5575;
import net.minecraft.class_5819;
import net.minecraft.class_7260;
import net.minecraft.class_7924;
import net.minecraft.class_8103;
import net.minecraft.class_811;
import net.minecraft.class_8111;
import net.minecraft.class_8113;
import net.minecraft.class_9254;
import net.minecraft.class_9282;
import net.minecraft.class_9300;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;
import net.minecraft.class_1352.class_4134;
import net.minecraft.class_1657.class_1658;
import net.minecraft.class_1665.class_1666;
import net.minecraft.class_1937.class_7867;
import net.minecraft.class_2902.class_2903;
import net.minecraft.class_8113.class_8114;
import net.minecraft.server.MinecraftServer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Day60to69Handler implements PhaseManager.PhaseHandler {
   private volatile boolean active = false;
   private boolean listenersRegistered = false;
   private int sleepCheckCooldown = 0;
   private static final int MOB_CONVERSION_INTERVAL = 60;
   private static final int GALACTIC_CATS_INTERVAL = 10;
   private final Set<UUID> processedEnderman = new HashSet();
   private final Set<UUID> processedCreeper = new HashSet();
   private final Set<UUID> processedillager = new HashSet();
   private final Map<UUID, Integer> bedrockCooldowns = new HashMap();
   private static final int BEDROCK_COOLDOWN = 200;
   private static final List<Integer> LOCKED_SLOTS = Arrays.asList(4, 13, 22, 31, 40, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 40);
   private static final List<Integer> LOCKEDED_SLOTS = Arrays.asList(7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 40);
   private final Set<UUID> cleanedPlayers = new HashSet();
   private final Map<UUID, Integer> supernovaCatTimers = new HashMap();
   private final Set<UUID> warnedSupernovaCats = new HashSet();
   private final Map<UUID, class_3213> galacticDragonBossBars = new HashMap();
   private final Set<UUID> galacticDragonIds = new HashSet();
   private static final class_2960 DAY40_PENALTY_ID = class_2960.method_60655("permadeath", "day40_penalty");
   private static final class_2960 DAY60_PENALTY_ID = class_2960.method_60655("permadeath", "day60_penalty");
   private static final class_2960 ORB_PENALTY_ID = class_2960.method_60655("permadeath", "orb_life");
   private static final List<class_1293> SPIDER_EFFECT_POOL;

   private static boolean isSpecialNeutral(class_1299<?> type) {
      return type == class_1299.field_6055 || type == class_1299.field_20346 || type == class_1299.field_6042 || type == class_1299.field_37419 || type == class_1299.field_6050 || type == class_1299.field_28315 || type == class_1299.field_22281 || type == class_1299.field_17943 || type == class_1299.field_6047 || type == class_1299.field_6074 || type == class_1299.field_6124 || type == class_1299.field_6081;
   }

   public void onPhaseStart(class_3218 world) {
      this.active = true;
      if (!this.listenersRegistered) {
         this.registerListeners();
         this.listenersRegistered = true;
      }

      long currentDay = DateManager.getCurrentRealDay();
      MinecraftServer server = world.method_8503();

      for(class_3222 player : server.method_3760().method_14571()) {
         class_1324 healthAttribute = player.method_5996(class_5134.field_23716);
         if (healthAttribute != null) {
            healthAttribute.method_6200(DAY40_PENALTY_ID);
            healthAttribute.method_6200(DAY60_PENALTY_ID);
            class_1322 penalty = new class_1322(DAY40_PENALTY_ID, (double)-8.0F, class_1323.field_6328);
            class_1322 penaltys = new class_1322(DAY60_PENALTY_ID, (double)-8.0F, class_1323.field_6328);
            healthAttribute.method_26837(penalty);
            healthAttribute.method_26837(penaltys);
            if (player.method_6032() > player.method_6063()) {
               player.method_6033(player.method_6063());
            }
         }
      }

      this.convertAllExistingMobs(world);
      if (server != null) {
         class_2561 message = class_2561.method_43470("§e=== Fase 7: Días 60-69 ===\n");
         server.method_3760().method_43514(message, false);
      }

      PvpChanges.applyPvpGamerule(world);
   }

   private void registerListeners() {
      UseBlockCallback.EVENT.register((UseBlockCallback)(player, world1, hand, hitResult) -> {
         if (!this.active) {
            return class_1269.field_5811;
         } else {
            class_1799 stack = player.method_5998(hand);
            return (class_1269)(stack.method_31574(class_1802.field_8615) ? class_1269.field_5814 : class_1269.field_5811);
         }
      });
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
               if (entity.method_16914() && (entity.method_5797().getString().contains("Ender Creeper") || entity.method_5797().getString().contains("Ender Quantum Creeper")) && isDodgeableDamage(source)) {
                  dodgeWithTeleport(creeper);
                  return false;
               }
            }

            if (entity instanceof class_1454 && !(source.method_5526() instanceof class_1657)) {
               return false;
            } else {
               label96: {
                  if (entity instanceof class_1571) {
                     class_1571 ghast = (class_1571)entity;
                     if (ghast.method_16914() && ghast.method_5797().getString().contains("Ghast Definitivo")) {
                        if (source.method_5526() instanceof class_1676) {
                           if (ghast.method_37908().field_9229.method_43057() < 0.3F) {
                              dodgeWithTeleport(ghast);
                              return false;
                           }
                        } else if (source.method_48789(class_8103.field_42250)) {
                           dodgeWithTeleport(ghast);
                           return false;
                        }
                        break label96;
                     }
                  }

                  if (entity instanceof class_1571) {
                     class_1571 ghast = (class_1571)entity;
                     if (ghast.method_16914() && ghast.method_5797().getString().toUpperCase().contains("ENDER GHAST") && isDodgeableDamage(source)) {
                        dodgeWithTeleport(ghast);
                        return false;
                     }
                  }
               }

               if (entity instanceof class_1639) {
                  class_1639 witherSkeleton = (class_1639)entity;
                  if (witherSkeleton.method_5752().contains("wither_emperor") && !(source.method_5526() instanceof class_1657)) {
                     return false;
                  }
               }

               if (entity instanceof class_1590) {
                  class_1590 zombifiedPiglin = (class_1590)entity;
                  if (zombifiedPiglin.method_5752().contains("magma_pig") && source.method_5526() instanceof class_1665) {
                     return false;
                  }
               }

               if (entity instanceof class_1589) {
                  class_1589 magmaCube = (class_1589)entity;
                  if (magmaCube.method_5752().contains("magma_pig_mount") && source.method_5526() instanceof class_1665) {
                     return false;
                  }
               }

               if (entity instanceof class_1571) {
                  class_1571 ghast = (class_1571)entity;
                  if (ghast.method_5797() != null && ghast.method_5797().getString().equals("ghast")) {
                     teleportGhastPigman(ghast);
                  }
               }

               if (entity instanceof class_1590) {
                  class_1590 pigman = (class_1590)entity;
                  class_1297 patt0$temp = pigman.method_5854();
                  if (patt0$temp instanceof class_1571) {
                     class_1571 ghast = (class_1571)patt0$temp;
                     if (pigman.method_5752().contains("ghast_pig")) {
                        teleportGhastPigman(ghast);
                     }
                  }
               }

               class_1297 patt1$temp = source.method_5526();
               if (patt1$temp instanceof class_1667) {
                  class_1667 arrow = (class_1667)patt1$temp;
                  if (arrow.method_5752().contains("DemonicArrow")) {
                     class_1937 patt2$temp = entity.method_37908();
                     if (patt2$temp instanceof class_3218) {
                        class_3218 serverLevel = (class_3218)patt2$temp;
                        class_1541 tnt = new class_1541(class_1299.field_6063, serverLevel);
                        tnt.method_5814(arrow.method_23317(), arrow.method_23318(), arrow.method_23321());
                        tnt.method_6967(0);
                        serverLevel.method_8649(tnt);
                        arrow.method_31472();
                        return false;
                     }
                  }
               }

               return true;
            }
         }
      });
      ServerEntityEvents.ENTITY_LOAD.register((ServerEntityEvents.Load)(entity, world1) -> {
         if (this.active) {
            if (entity instanceof class_1542) {
               class_1542 itemEntity = (class_1542)entity;
               if (itemEntity.method_6983().method_31574(class_1802.field_8615)) {
                  itemEntity.method_31472();
                  return;
               }
            }

            if (world1 instanceof class_3218 && entity instanceof class_1309) {
               class_1309 livingEntity = (class_1309)entity;
               if (WitherEmperorHandler.trySpawnEmperor(livingEntity, world1)) {
                  return;
               }

               if (livingEntity instanceof class_1560) {
                  class_1560 enderman = (class_1560)livingEntity;
                  if (!enderman.method_31481() && world1.field_9229.method_43057() < 0.2F) {
                     MobAccessor accessor = (MobAccessor)enderman;
                     accessor.getTargetSelector().method_6277(1, new class_1400(enderman, class_1657.class, true));
                  }

                  if (world1.method_27983() == class_1937.field_25181) {
                     if (enderman.method_31481()) {
                        return;
                     }

                     int chance = world1.field_9229.method_43048(100);
                     if (this.processedEnderman.contains(enderman.method_5667())) {
                        return;
                     }

                     if (chance <= 10) {
                        class_1548 newCreeper = new class_1548(class_1299.field_6046, world1);
                        newCreeper.method_5665(class_2561.method_43470("§6Ender Quantum Creeper"));
                        newCreeper.method_5814(enderman.method_23317(), enderman.method_23318(), enderman.method_23321());
                        world1.method_8649(newCreeper);
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
                     } else {
                        this.processedEnderman.add(enderman.method_5667());
                     }
                  }
               }

               if (livingEntity instanceof class_1560) {
                  class_1560 enderman = (class_1560)livingEntity;
                  if (world1.method_27983() == class_1937.field_25180) {
                     class_1548 newCreeper = new class_1548(class_1299.field_6046, world1);
                     newCreeper.method_5665(class_2561.method_43470("§6Ender Quantum Creeper"));
                     newCreeper.method_5814(enderman.method_23317(), enderman.method_23318(), enderman.method_23321());
                     world1.method_8649(newCreeper);
                     enderman.method_31472();
                     return;
                  }
               }

               if (livingEntity instanceof class_1590) {
                  class_1590 zombifiedPiglin = (class_1590)livingEntity;
                  if (world1.method_27983() == class_1937.field_25180) {
                     zombifiedPiglin.method_31472();
                  }
               }

               if (livingEntity instanceof class_1548) {
                  class_1548 creeper = (class_1548)livingEntity;
                  class_2487 nbt = new class_2487();
                  creeper.method_5652(nbt);
                  nbt.method_10556("powered", true);
                  creeper.method_5749(nbt);
                  if (this.processedCreeper.contains(creeper.method_5667())) {
                     return;
                  }

                  creeper.method_5665(class_2561.method_43470("§6Ender Quantum Creeper"));
                  creeper.method_6092(new class_1293(class_1294.field_5905, -1, 0, false, false));
                  this.applyEndermanBehavior(creeper);
                  this.processedCreeper.add(creeper.method_5667());
                  if (world1.method_27983() == class_1937.field_25181 || world1.method_27983() == class_1937.field_25180 || world1.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                     creeper.method_6092(new class_1293(class_1294.field_5905, -1, 0, false, false));
                     if (world1.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                        creeper.method_5665(class_2561.method_43470("§6Ender Quantum Creeper"));
                     }

                     this.applyEndermanBehavior(creeper);
                  }
               }

               if (livingEntity instanceof class_1639) {
                  class_1639 witherSkeleton = (class_1639)livingEntity;
                  if (world1.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                     class_1799 sword = new class_1799(class_1802.field_22022);
                     class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
                     class_2378<class_1887> registry = world1.method_30349().method_30530(class_7924.field_41265);
                     registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 25));
                     sword.method_57379(class_9334.field_49633, enchantments.method_57549());
                     class_1324 maxHealthAttribute = witherSkeleton.method_5996(class_5134.field_23716);
                     if (maxHealthAttribute != null) {
                        double currentMaxHealth = maxHealthAttribute.method_6201();
                        maxHealthAttribute.method_6192(currentMaxHealth * (double)3.0F);
                        witherSkeleton.method_6033(witherSkeleton.method_6063());
                     }

                     witherSkeleton.method_5673(class_1304.field_6173, sword);
                     witherSkeleton.method_5673(class_1304.field_6174, new class_1799(ModArmorMaterials.NETHERITE_PINK_CHESTPLATE));
                     witherSkeleton.method_5673(class_1304.field_6166, new class_1799(ModArmorMaterials.NETHERITE_PINK_BOOTS));
                     witherSkeleton.method_5665(class_2561.method_43470("§6Wither Skeleton Rosáceo"));
                  }
               }

               if (livingEntity instanceof class_1571) {
                  class_1571 ghast = (class_1571)livingEntity;
                  if (!ghast.method_16914() && world1.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                     spawnEnderGhast(world1, ghast.method_23317(), ghast.method_23318() + (double)3.0F, ghast.method_23321());
                     ghast.method_31472();
                  }
               }

               if (livingEntity instanceof class_1634) {
                  class_1634 vex = (class_1634)livingEntity;
                  if (!vex.method_16914() && world1.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                     class_1799 crystal = new class_1799(class_1802.field_8301);
                     class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
                     class_2378<class_1887> registry = world1.method_30349().method_30530(class_7924.field_41265);
                     registry.method_46746(class_1893.field_9121).ifPresent((holder) -> enchantments.method_57547(holder, 20));
                     crystal.method_57379(class_9334.field_49633, enchantments.method_57549());
                     vex.method_5673(class_1304.field_6173, crystal);
                     vex.method_5665(class_2561.method_43470("§6Vex Definitivo"));
                     class_8113.class_8122 honeyBlockDisplay = new class_8113.class_8122(class_1299.field_42456, world1);
                     honeyBlockDisplay.method_48897(new class_1799(class_1802.field_21086));
                     honeyBlockDisplay.method_48896(class_811.field_4316);
                     class_4590 transform = new class_4590(new Vector3f(0.0F, -0.15F, 0.0F), new Quaternionf(), new Vector3f(0.55F, 0.55F, 0.55F), new Quaternionf());
                     honeyBlockDisplay.method_48849(transform);
                     honeyBlockDisplay.method_48847(class_8114.field_42407);
                     honeyBlockDisplay.method_5814(vex.method_23317(), vex.method_23318(), vex.method_23321());
                     honeyBlockDisplay.method_5780("permadeath_vex_honey_head");
                     world1.method_8649(honeyBlockDisplay);
                     honeyBlockDisplay.method_5873(vex, true);
                  }
               }

               if (this.handleMobReplacement(livingEntity, world1)) {
                  return;
               }

               this.convertNormalSkeletons(livingEntity, world1);
               this.InsertEffect(livingEntity, world1);
               Day25GiantMobsHandler.transformGiantMobs(livingEntity, world1);
               if (entity instanceof class_1569) {
                  class_1799 stack = livingEntity.method_6047();
                  if (!stack.method_7960()) {
                     class_2378<class_1887> registry = world1.method_30349().method_30530(class_7924.field_41265);
                     class_9304.class_9305 enchantments = new class_9304.class_9305((class_9304)stack.method_57825(class_9334.field_49633, class_9304.field_49385));
                     boolean modified = false;
                     if (!(stack.method_7909() instanceof class_1829) && !(stack.method_7909() instanceof class_1766) && !(stack.method_7909() instanceof class_1835)) {
                        if (stack.method_31574(class_1802.field_8102) || stack.method_31574(class_1802.field_8399)) {
                           registry.method_46746(class_1893.field_9126).ifPresent((holder) -> enchantments.method_57547(holder, 1));
                           modified = true;
                        }
                     } else {
                        registry.method_46746(class_1893.field_9124).ifPresent((holder) -> enchantments.method_57547(holder, 2));
                        modified = true;
                     }

                     if (modified) {
                        stack.method_57379(class_9334.field_49633, enchantments.method_57549());
                     }
                  }
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

                  if (world1.field_9229.method_43048(100) <= 25) {
                     spawnEnderGhast(world1, phantom.method_23317(), phantom.method_23318() + (double)3.0F, phantom.method_23321());
                     spawnEnderGhast(world1, phantom.method_23317(), phantom.method_23318() + (double)3.0F, phantom.method_23321());
                     spawnEnderGhast(world1, phantom.method_23317(), phantom.method_23318() + (double)3.0F, phantom.method_23321());
                     spawnEnderGhast(world1, phantom.method_23317(), phantom.method_23318() + (double)3.0F, phantom.method_23321());
                     phantom.method_31472();
                  }

                  this.addSkeletonRiders(phantom, world1);
               }

               class_1299<?> type = livingEntity.method_5864();
               if (!isSpecialNeutral(type) && HostileMobConverter.isAlreadyHostile(livingEntity)) {
                  return;
               }

               HostileMobConverter.convertToHostile(livingEntity);
            }

            for(class_3222 player : world1.method_18456()) {
               this.checkBedrockEffect(player);
               this.invisibilityplayer(player);
               this.checkRandomLevitation(player);
               this.handleInventoryRestriction(player);
            }

         }
      });
      ServerTickEvents.END_WORLD_TICK.register((ServerTickEvents.EndWorldTick)(world) -> {
         if (world.method_8510() % 20L == 0L) {
            for(class_8113.class_8122 display : world.method_18198(class_5575.method_31795(class_8113.class_8122.class), (d) -> d.method_5752().contains("permadeath_vex_honey_head"))) {
               class_1297 vehicle = display.method_5854();
               if (vehicle == null || vehicle.method_31481() || !vehicle.method_5805()) {
                  display.method_31472();
               }
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
               if (serverLevel.field_9229.method_43048(100) <= 10) {
                  ((SleepCounter.PlayerAccessor)player).setSleepCounter(0);
                  player.method_7353(class_2561.method_43470("[PERMADEATH] Contador de phantoms reiniciado").method_27692(class_124.field_1061), false);
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
               Day25GiantMobsHandler.handleGiantMobArmorDrops(entity, damageSource);
            }

            this.handleemperatrisdrop(entity, damageSource);
            class_1937 patt0$temp = entity.method_37908();
            if (patt0$temp instanceof class_3218) {
               class_3218 serverLevel = (class_3218)patt0$temp;
               GiantZombieHandler.handleDrops(entity, serverLevel);
            }

            WitherEmperorHandler.handleDropsE(entity, (class_3218)entity.method_37908());
            if (entity instanceof class_1510) {
               class_1510 dragon = (class_1510)entity;
               if (dragon.method_5752().contains("galactic_dragon")) {
                  class_3213 be = (class_3213)this.galacticDragonBossBars.remove(dragon.method_5667());
                  if (be != null) {
                     be.method_14094();
                  }
               }
            }

            if (entity instanceof class_1451) {
               class_1451 cat = (class_1451)entity;
               if (cat.method_5752().contains("galactic_cat")) {
                  class_1937 patt1$temp = entity.method_37908();
                  if (patt1$temp instanceof class_3218) {
                     class_3218 serverLevel = (class_3218)patt1$temp;
                     this.spawnRandomMobFromGalacticCat(serverLevel, cat.method_23317(), cat.method_23318(), cat.method_23321());
                  }
               }
            }

            if (entity instanceof class_1634) {
               class_1634 vex = (class_1634)entity;
               if (vex.method_5797() != null && vex.method_5797().getString().contains("Vex Definitivo")) {
                  for(class_1297 passenger : vex.method_5685()) {
                     if (passenger instanceof class_8113.class_8122) {
                        passenger.method_31472();
                     }
                  }
               }
            }

            if (entity instanceof class_1439 || entity instanceof class_5136 || entity instanceof class_1590 || entity instanceof class_1577 || entity instanceof class_1560 || entity instanceof class_1640 || entity instanceof class_1639 || entity instanceof class_1564 || entity instanceof class_1593 || entity instanceof class_1551 || entity instanceof class_1545 || entity instanceof class_4836 || entity instanceof class_1621 || entity instanceof class_1613 || entity instanceof class_1589 || entity instanceof class_1571) {
               patt0$temp = entity.method_37908();
               if (patt0$temp instanceof class_3218) {
                  class_3218 serverLevel = (class_3218)patt0$temp;
                  if (entity instanceof class_1590 && entity.method_5752().contains("CarlosPigman")) {
                     return;
                  }

                  if (entity instanceof class_1639 && entity.method_5752().contains("wither_emperor")) {
                     return;
                  }

                  if (entity instanceof class_1590 && !entity.method_5752().contains("CarlosPigman") && serverLevel.field_9229.method_43048(100) < 33) {
                     return;
                  }

                  serverLevel.method_18467(class_1542.class, entity.method_5829().method_1014((double)2.0F)).forEach((itemEntity) -> {
                     class_1799 itemStack = itemEntity.method_6983();
                     itemEntity.method_31472();
                  });
               }
            }

         }
      });
      LootTableEvents.REPLACE.register((LootTableEvents.Replace)(key, original, source, registries) -> {
         if (!this.active) {
            return null;
         } else {
            return source.isBuiltin() && key.method_29177().method_12832().startsWith("chests/") ? class_52.method_324().method_338() : null;
         }
      });
      PlayerBlockBreakEvents.AFTER.register((PlayerBlockBreakEvents.After)(world1, player, pos, state, blockEntity) -> {
         if (this.active) {
            if (player instanceof class_3222) {
               class_3222 serverPlayer = (class_3222)player;
               class_1799 tool = serverPlayer.method_6047();
               boolean isNetheriteTool = tool.method_31574(class_1802.field_22024) || tool.method_31574(class_1802.field_22025) || tool.method_31574(class_1802.field_22023) || tool.method_31574(class_1802.field_22026) || tool.method_31574(class_1802.field_22022);
               if (!isNetheriteTool) {
                  serverPlayer.field_6008 = 0;
                  serverPlayer.method_64419(serverPlayer.method_48923().method_48830(), 16.0F);
               }
            }

         }
      });
   }

   private void checkBedrockEffect(class_3222 player) {
      if ((player.method_37908().method_27983().equals(class_1937.field_25181) || player.method_37908().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) && player.method_37908().method_8320(player.method_23312()).method_27852(class_2246.field_9987)) {
         UUID playerId = player.method_5667();
         int currentTick = player.method_5682().method_3780();
         Integer lastApplication = (Integer)this.bedrockCooldowns.get(playerId);
         if (lastApplication == null || currentTick - lastApplication >= 200) {
            player.method_6092(new class_1293(class_1294.field_5902, 200, 9, false, true));
            ((class_3218)player.method_37908()).method_65096(class_2398.field_11214, player.method_23317(), player.method_23318() + 0.1, player.method_23321(), 20, (double)0.5F, 0.1, (double)0.5F, 0.1);
            this.bedrockCooldowns.put(playerId, currentTick);
         }
      }

      if ((player.method_37908().method_27983().equals(class_1937.field_25179) || player.method_37908().method_27983().equals(class_1937.field_25180) || player.method_37908().method_27983().equals(class_1937.field_25181) || player.method_37908().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) && (player.method_37908().method_8320(player.method_23312()).method_27852(class_2246.field_10114) || player.method_37908().method_8320(player.method_23312()).method_27852(class_2246.field_22090))) {
         player.method_6092(new class_1293(class_1294.field_5909, 60, 2, false, true));
      }

   }

   private void invisibilityplayer(class_3222 player) {
      if (player.method_37908().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY) && player.method_6059(class_1294.field_5905)) {
         player.method_6016(class_1294.field_5905);
      }

   }

   private void detectPolarBears(class_3218 world) {
      for(class_3222 player : world.method_18456()) {
         for(class_1456 polarBear : world.method_8390(class_1456.class, player.method_5829().method_1014((double)20.0F), (polarBearx) -> polarBearx.method_5805() && !polarBearx.method_31481())) {
            UUID Polarid = polarBear.method_5667();
            boolean isCarrying = player.method_5739(polarBear) < 20.0F;
            if (isCarrying) {
               this.supernovaCatTimers.putIfAbsent(Polarid, 40);
            }
         }
      }

   }

   private void processPolarBearTimers(class_3218 world) {
      if (!this.supernovaCatTimers.isEmpty()) {
         Iterator<Map.Entry<UUID, Integer>> iterator = this.supernovaCatTimers.entrySet().iterator();

         while(iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = (Map.Entry)iterator.next();
            UUID Polarid = (UUID)entry.getKey();
            int timeLeft = (Integer)entry.getValue();
            class_1297 entity = world.method_14190(Polarid);
            if (entity != null) {
               if (entity instanceof class_1456) {
                  class_1456 polarBear = (class_1456)entity;
                  if (polarBear.method_5805()) {
                     if (timeLeft <= 0) {
                        world.method_8437(polarBear, polarBear.method_23317(), polarBear.method_23318(), polarBear.method_23321(), 10.0F, class_7867.field_40891);
                        polarBear.method_31472();
                        iterator.remove();
                     } else {
                        entry.setValue(timeLeft - 1);
                     }
                     continue;
                  }
               }

               iterator.remove();
            }
         }

      }
   }

   private void handleGalacticCats(class_3218 world) {
      for(class_3222 player : world.method_18456()) {
         for(class_1451 cat : world.method_8390(class_1451.class, player.method_5829().method_1014((double)200.0F), (catx) -> catx.method_5805() && !catx.method_31481())) {
            if (cat.method_5752().contains("SupernovaCat")) {
               SupernovaCatManager.arm(world, cat);
            } else {
               UUID catId = cat.method_5667();
               boolean isNearby = player.method_5739(cat) < 200.0F;
               if (isNearby && !this.warnedSupernovaCats.contains(catId)) {
                  cat.method_5665(class_2561.method_43470("Gato Galáctico").method_27692(class_124.field_1065));
                  cat.method_5880(true);
                  cat.method_5780("galactic_cat");
                  this.warnedSupernovaCats.add(catId);
               }
            }
         }
      }

   }

   private void spawnRandomMobFromGalacticCat(class_3218 world, double x, double y, double z) {
      class_5819 rng = world.method_8409();
      world.method_8437((class_1297)null, x, y, z, 2.0F, class_7867.field_40888);
      int roll = rng.method_43048(42);
      Object var10000;
      switch (roll) {
         case 0:
            class_1549 caveSpider = new class_1549(class_1299.field_6084, world);
            class_1547 skeleton = new class_1613(class_1299.field_6137, world);
            skeleton.method_5814(x, y, z);
            world.method_8649(skeleton);
            caveSpider.method_5873(skeleton, true);
            var10000 = caveSpider;
            break;
         case 1:
            var10000 = new class_1581(class_1299.field_6065, world);
            break;
         case 2:
            var10000 = new class_1613(class_1299.field_6137, world);
            break;
         case 3:
            var10000 = new class_1548(class_1299.field_6046, world);
            break;
         case 4:
            var10000 = new class_1560(class_1299.field_6091, world);
            break;
         case 5:
            var10000 = new class_1545(class_1299.field_6099, world);
            break;
         case 6:
            var10000 = new class_1639(class_1299.field_6076, world);
            break;
         case 7:
            var10000 = new class_1593(class_1299.field_6078, world);
            break;
         case 8:
            var10000 = new class_1551(class_1299.field_6123, world);
            break;
         case 9:
            var10000 = new class_1576(class_1299.field_6071, world);
            break;
         case 10:
            var10000 = new class_1627(class_1299.field_6098, world);
            break;
         case 11:
            var10000 = new class_1604(class_1299.field_6105, world);
            break;
         case 12:
            var10000 = new class_1632(class_1299.field_6117, world);
            break;
         case 13:
            var10000 = new class_1564(class_1299.field_6090, world);
            break;
         case 14:
            var10000 = new class_1559(class_1299.field_6128, world);
            break;
         case 15:
            var10000 = new class_1634(class_1299.field_6059, world);
            break;
         case 16:
            var10000 = new class_1621(class_1299.field_6069, world);
            break;
         case 17:
            var10000 = new class_1589(class_1299.field_6102, world);
            break;
         case 18:
            var10000 = new class_1571(class_1299.field_6107, world);
            break;
         case 19:
            var10000 = new class_7260(class_1299.field_38095, world);
            break;
         case 20:
            var10000 = new class_1590(class_1299.field_6050, world);
            break;
         case 21:
            var10000 = new class_4836(class_1299.field_22281, world);
            break;
         case 22:
            var10000 = new class_1577(class_1299.field_6118, world);
            break;
         case 23:
            var10000 = new class_1550(class_1299.field_6086, world);
            break;
         case 24:
            var10000 = new class_1640(class_1299.field_6145, world);
            break;
         case 25:
            var10000 = new class_9254(class_1299.field_49148, world);
            break;
         case 26:
            class_1584 ravager = new class_1584(class_1299.field_6134, world);
            ravager.method_24203(x, y, z);
            ((class_1324)Objects.requireNonNull(ravager.method_5996(class_5134.field_23716))).method_6192((double)500.0F);
            ravager.method_6033(500.0F);
            ravager.method_5665(class_2561.method_43470("Ultra Ravager").method_27692(class_124.field_1065));
            ravager.method_5880(false);
            ravager.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, false));
            ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, false));
            var10000 = ravager;
            break;
         case 27:
            class_1584 ultraRav = new class_1584(class_1299.field_6134, world);
            Ravagercreator.setupUltraRavagerStack(ultraRav, world);
            var10000 = ultraRav;
            break;
         case 28:
            class_1642 gigant = new class_1642(class_1299.field_6051, world);
            GiantZombieHandler.applyGiantStats(gigant);
            var10000 = gigant;
            break;
         case 29:
            class_1451 supernovaCat = new class_1451(class_1299.field_16281, world);
            supernovaCat.method_5814(x, y, z);
            SupernovaCatManager.arm(world, supernovaCat);
            this.warnedSupernovaCats.add(supernovaCat.method_5667());
            var10000 = supernovaCat;
            break;
         case 30:
            class_1528 wither = new class_1528(class_1299.field_6119, world);
            var10000 = wither;
            break;
         case 31:
            class_1510 dragon = new class_1510(class_1299.field_6116, world);
            dragon.method_5780("galactic_dragon");
            this.galacticDragonIds.add(dragon.method_5667());
            var10000 = dragon;
            break;
         case 32:
            class_1439 golem = new class_1439(class_1299.field_6147, world);
            golem.method_6092(new class_1293(class_1294.field_5910, -1, 3, false, false));
            golem.method_6092(new class_1293(class_1294.field_5904, -1, 2, false, false));
            MobAccessor golemAccessor = (MobAccessor)golem;
            golemAccessor.getTargetSelector().method_6277(1, new class_1400(golem, class_1657.class, true));
            var10000 = golem;
            break;
         case 33:
            var10000 = new class_1549(class_1299.field_6084, world);
            break;
         case 34:
            class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
            WitherEmperorHandler.SpawnEmperor(witherSkeleton, world);
            var10000 = witherSkeleton;
            break;
         default:
            var10000 = new class_1576(class_1299.field_6071, world);
      }

      class_1297 spawned = (class_1297)var10000;
      if (spawned != null) {
         spawned.method_5814(x, y, z);
         world.method_8649(spawned);
         class_2561 spawnMsg = class_2561.method_43470("§5✦ ¡Un Gato Galáctico ha caído! Ha invocado a " + spawned.method_5477().getString() + "!");

         for(class_3222 p : world.method_8503().method_3760().method_14571()) {
            p.method_64398(spawnMsg);
         }
      }

   }

   public static boolean teleportRandomly(class_1309 entity) {
      if (!entity.method_5805()) {
         return false;
      } else {
         double x = entity.method_23317() + (entity.method_59922().method_43058() - (double)0.5F) * (double)48.0F;
         double y = entity.method_23318() + (double)entity.method_59922().method_43048(32);
         double z = entity.method_23321() + (entity.method_59922().method_43058() - (double)0.5F) * (double)48.0F;
         boolean success = entity.method_6082(x, y, z, true);
         if (success) {
            entity.method_37908().method_43128((class_1657)null, entity.method_23317(), entity.method_23318(), entity.method_23321(), class_3417.field_14879, class_3419.field_15251, 1.0F, 1.0F);
         }

         return success;
      }
   }

   private static boolean isDodgeableDamage(class_1282 source) {
      return source.method_5526() instanceof class_1676 || source.method_48789(class_8103.field_42250) || source.method_48789(class_8103.field_42246) || source.method_49708(class_8111.field_42342);
   }

   private static boolean dodgeWithTeleport(class_1309 entity) {
      for(int i = 0; i < 64; ++i) {
         if (teleportRandomly(entity)) {
            return true;
         }
      }

      return false;
   }

   public static void spawnEnderGhast(class_3218 world, double x, double y, double z) {
      final class_1571 enderGhast = new class_1571(class_1299.field_6107, world);
      enderGhast.method_5814(x, y, z);
      if (world.method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
         enderGhast.method_5665(class_2561.method_43470("§6Ghast Definitivo"));
         class_1324 maxHealth = enderGhast.method_5996(class_5134.field_23716);
         if (maxHealth != null) {
            maxHealth.method_6192((double)240.0F);
            enderGhast.method_6033(240.0F);
         }
      } else {
         enderGhast.method_5665(class_2561.method_43470("§6Ender Ghast"));
         class_1324 maxHealth = enderGhast.method_5996(class_5134.field_23716);
         if (maxHealth != null) {
            maxHealth.method_6192((double)75.0F);
            enderGhast.method_6033(75.0F);
         }
      }

      enderGhast.method_5880(false);
      MobAccessor accessor = (MobAccessor)enderGhast;
      accessor.getGoalSelector().method_6277(1, new class_1352() {
         public boolean method_6264() {
            return enderGhast.method_59922().method_43057() < 0.02F;
         }

         public void method_6269() {
            Day60to69Handler.teleportRandomly(enderGhast);
         }
      });
      world.method_8649(enderGhast);
   }

   private boolean handleMobReplacement(class_1309 entity, class_3218 world) {
      if (entity.method_31481()) {
         return true;
      } else if (DateManager.getCurrentRealDay() < 60L) {
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
      } else if (entity instanceof class_1642 && !(entity instanceof class_1590) && !(entity instanceof class_1551) && !GiantZombieHandler.isGiant(entity)) {
         class_1632 vindicator = new class_1632(class_1299.field_6117, world);
         vindicator.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         vindicator.method_36456(entity.method_36454());
         vindicator.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8475));
         vindicator.method_6092(new class_1293(class_1294.field_5910, -1, 0));
         class_1324 maxHealthAttribute = vindicator.method_5996(class_5134.field_23716);
         if (maxHealthAttribute != null) {
            double currentMaxHealth = maxHealthAttribute.method_6201();
            maxHealthAttribute.method_6192(currentMaxHealth * (double)2.0F);
            vindicator.method_6033(vindicator.method_6063());
            world.method_8649(vindicator);
         }

         entity.method_31472();
         return true;
      } else if (entity instanceof class_1493) {
         class_1451 cat = new class_1451(class_1299.field_16281, world);
         cat.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         cat.method_36456(entity.method_36454());
         world.method_8649(cat);
         entity.method_31472();
         return true;
      } else if (entity instanceof class_1462) {
         class_1454 pufferfish = new class_1454(class_1299.field_6062, world);
         pufferfish.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         pufferfish.method_36456(entity.method_36454());
         world.method_8649(pufferfish);
         entity.method_31472();
         return true;
      } else if (entity instanceof class_1646) {
         class_1632 vindicator = new class_1632(class_1299.field_6117, world);
         vindicator.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
         vindicator.method_36456(entity.method_36454());
         world.method_8649(vindicator);
         entity.method_31472();
         return true;
      } else {
         return false;
      }
   }

   private void InsertEffect(class_1309 entity, class_3218 world) {
      if (entity instanceof class_1308 mob) {
         if (MobProcessingTracker.tryClaim(mob, "insert_effect_d60_69")) {
            if (entity instanceof class_1439) {
               class_1439 ironGolem = (class_1439)entity;
               ironGolem.method_6092(new class_1293(class_1294.field_5904, -1, 3, false, true));
               ironGolem.method_6092(new class_1293(class_1294.field_5910, -1, 3, false, true));
               ironGolem.method_6092(new class_1293(class_1294.field_5907, -1, 3, false, true));
               ironGolem.method_6092(new class_1293(class_1294.field_5918, -1, 0, false, true));
            } else if (entity instanceof class_1560) {
               class_1560 enderman = (class_1560)entity;
               enderman.method_6092(new class_1293(class_1294.field_5910, -1, 9, false, true));
            } else if (entity instanceof class_1604) {
               class_1604 pillager = (class_1604)entity;
               pillager.method_6092(new class_1293(class_1294.field_5905, -1, 0, false, false));
               class_1799 crossbox = new class_1799(class_1802.field_8399);
               class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
               class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
               registry.method_46746(class_1893.field_9098).ifPresent((holder) -> enchantments.method_57547(holder, 1));
               crossbox.method_57379(class_9334.field_49633, enchantments.method_57549());
               pillager.method_5673(class_1304.field_6173, crossbox);
               MobAccessor accessor = (MobAccessor)pillager;
               accessor.getGoalSelector().method_6277(1, new MachineGunGoal(pillager));
            } else if (entity instanceof class_1584) {
               class_1584 ravager = (class_1584)entity;
               ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, true));
               ravager.method_6092(new class_1293(class_1294.field_5907, -1, 0, false, true));
            } else if (entity instanceof class_1632) {
               class_1632 vindicator = (class_1632)entity;
               class_1799 axe = new class_1799(class_1802.field_8556);
               class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
               class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
               registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 5));
               axe.method_57379(class_9334.field_49633, enchantments.method_57549());
               vindicator.method_5673(class_1304.field_6173, axe);
            } else if (entity instanceof class_1548) {
               class_1548 creeper = (class_1548)entity;
               creeper.method_6092(new class_1293(class_1294.field_5907, -1, 1, false, true));
               creeper.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, true));
            } else if (entity instanceof class_1640) {
               class_1640 witch = (class_1640)entity;
               class_1324 maxHealth = witch.method_5996(class_5134.field_23716);
               if (maxHealth != null && maxHealth.method_6201() <= (double)26.0F) {
                  maxHealth.method_6192((double)52.0F);
                  witch.method_6033(52.0F);
               }

               witch.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, true));
               MobAccessor accessor = (MobAccessor)witch;
               accessor.getGoalSelector().method_6277(1, new ImpossibleWitchGoal(witch));
            } else if (entity instanceof class_1551) {
               class_1551 p = (class_1551)entity;
               p.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8547));
               class_1324 attackDamage = p.method_5996(class_5134.field_23721);
               if (attackDamage != null) {
                  attackDamage.method_6192((double)30.0F);
               }
            } else if (entity instanceof class_4466) {
               class_4466 bee = (class_4466)entity;
               class_1324 attackDamage = bee.method_5996(class_5134.field_23721);
               if (attackDamage != null) {
                  attackDamage.method_6192((double)30.0F);
               }
            } else if (entity instanceof class_1634) {
               class_1634 vex = (class_1634)entity;
               vex.method_6092(new class_1293(class_1294.field_5907, -1, 2, false, true));
               vex.method_6092(new class_1293(class_1294.field_5910, -1, 2, false, true));
               class_1324 attackDamage = vex.method_5996(class_5134.field_23721);
               if (attackDamage != null) {
                  attackDamage.method_6192((double)14.0F);
               }
            } else if (!(entity instanceof class_1621) && !(entity instanceof class_1589)) {
               if (entity instanceof class_1606) {
                  class_1606 shulker = (class_1606)entity;
                  shulker.method_6092(new class_1293(class_1294.field_5907, Integer.MAX_VALUE, 5, false, true));
               } else if (entity instanceof class_1454) {
                  class_1454 pufferfish = (class_1454)entity;
                  pufferfish.method_6092(new class_1293(class_1294.field_5907, Integer.MAX_VALUE, 5, false, true));
               } else if (entity instanceof class_1545) {
                  class_1545 b = (class_1545)entity;
                  b.method_6092(new class_1293(class_1294.field_5907, -1, 1));
                  class_1324 maxHealth = entity.method_5996(class_5134.field_23716);
                  if (maxHealth != null) {
                     maxHealth.method_6192((double)200.0F);
                     entity.method_6033(200.0F);
                  }
               } else if (entity instanceof class_1550) {
                  class_1550 g = (class_1550)entity;
                  g.method_6092(new class_1293(class_1294.field_5904, -1, 2));
                  g.method_6092(new class_1293(class_1294.field_5907, -1, 1));
               }
            } else {
               class_1324 maxHealth = entity.method_5996(class_5134.field_23716);
               if (maxHealth != null) {
                  maxHealth.method_6192((double)200.0F);
                  entity.method_6033(200.0F);
               }
            }

            return;
         }
      }

   }

   public void onPhaseEnd(class_3218 world) {
      this.active = false;
      MinecraftServer server = world.method_8503();
      if (server != null) {
         for(class_3222 player : server.method_3760().method_14571()) {
            class_1324 healthAttribute = player.method_5996(class_5134.field_23716);
            if (healthAttribute != null) {
               healthAttribute.method_6200(DAY40_PENALTY_ID);
               healthAttribute.method_6200(DAY60_PENALTY_ID);
               healthAttribute.method_6200(ORB_PENALTY_ID);
               if (player.method_6032() > player.method_6063()) {
                  player.method_6033(player.method_6063());
               }
            }

            for(int i = 0; i < player.method_31548().method_5439(); ++i) {
               if (player.method_31548().method_5438(i).method_31574(class_1802.field_8615)) {
                  player.method_31548().method_5447(i, class_1799.field_8037);
               }
            }
         }
      }

      this.processedEnderman.clear();
      this.processedCreeper.clear();
      this.processedillager.clear();
      this.bedrockCooldowns.clear();
      this.cleanedPlayers.clear();
      this.supernovaCatTimers.clear();
      this.warnedSupernovaCats.clear();
      this.sleepCheckCooldown = 0;
      MushroomSpawn.disable();
   }

   public void onTick(class_3218 world) {
      if (DateManager.getCurrentRealDay() <= 70L && DateManager.getCurrentRealDay() >= 60L) {
         if (this.sleepCheckCooldown > 0) {
            --this.sleepCheckCooldown;
         }

         for(class_3222 player : world.method_18456()) {
            class_1324 healthAttribute = player.method_5996(class_5134.field_23716);
            if (healthAttribute != null && healthAttribute.method_6199(DAY40_PENALTY_ID) == null && healthAttribute.method_6199(DAY60_PENALTY_ID) == null) {
               class_1322 penalty = new class_1322(DAY40_PENALTY_ID, (double)-8.0F, class_1323.field_6328);
               class_1322 penaltys = new class_1322(DAY60_PENALTY_ID, (double)-8.0F, class_1323.field_6328);
               healthAttribute.method_26837(penalty);
               healthAttribute.method_26837(penaltys);
               if (player.method_6032() > player.method_6063()) {
                  player.method_6033(player.method_6063());
               }
            }

            this.checkBedrockEffect(player);
            this.invisibilityplayer(player);
            this.checkRandomLevitation(player);
            this.handleInventoryRestriction(player);
            TotemManager.giveSurvivorMedalIfEligible(player);
            this.trySpawnWildSnowGolem(player);
            CripticShulkerHandler.trySpawnCripticShulker(player);
         }

         if (world.method_8510() % 10L == 0L) {
            this.handleGalacticCats(world);
            this.detectPolarBears(world);
         }

         this.processPolarBearTimers(world);
         MushroomSpawn.enable();
         WitherSpawn.handleTick(world);
         if (world.method_8510() % 60L == 0L) {
            this.runPeriodicMobPass(world);
         }

         for(class_1297 entity : world.method_27909()) {
            if (entity instanceof class_1667) {
               class_1667 arrow = (class_1667)entity;
               if (!arrow.method_5752().contains("DemonicArrow")) {
                  class_1297 var11 = arrow.method_24921();
                  if (var11 instanceof class_1613) {
                     class_1613 skeleton = (class_1613)var11;
                     if (skeleton.method_5752().contains("DemonicSkeleton")) {
                        arrow.method_5780("DemonicArrow");
                     }
                  }
               }

               if (arrow.method_5752().contains("DemonicArrow") && arrow.field_6012 > 2 && arrow.method_18798().method_1027() < 0.01) {
                  new class_1541(class_1299.field_6063, world);
                  world.method_8437(arrow.method_24921(), arrow.method_23317(), arrow.method_23318(), arrow.method_23321(), 6.0F, class_7867.field_40891);
                  arrow.method_31472();
               }
            }
         }

      }
   }

   public String getPhaseName() {
      return "Fase 7: Días 60-69";
   }

   private void handleInventoryRestriction(class_3222 player) {
      boolean hasEndRelic = false;
      boolean hasBeginningRelic = false;

      for(int i = 0; i < player.method_31548().method_5439(); ++i) {
         class_1799 stack = player.method_31548().method_5438(i);
         if (stack.method_31574(ModItems.END_RELIC)) {
            hasEndRelic = true;
         }

         if (stack.method_31574(ModItems.BEGINNING_RELIC)) {
            hasBeginningRelic = true;
         }
      }

      List<Integer> slotsToLock;
      if (hasBeginningRelic) {
         slotsToLock = Collections.emptyList();
      } else if (hasEndRelic) {
         slotsToLock = LOCKEDED_SLOTS;
      } else {
         slotsToLock = LOCKED_SLOTS;
      }

      for(int i = 0; i < player.method_31548().method_5439(); ++i) {
         class_1799 stack = player.method_31548().method_5438(i);
         if (stack.method_31574(class_1802.field_8615) && !slotsToLock.contains(i)) {
            player.method_31548().method_5447(i, class_1799.field_8037);
         }
      }

      if (!slotsToLock.isEmpty()) {
         String msg = hasEndRelic ? "§c¡Necesitas la Reliquia del Comienzo para usar este slot!" : "§c¡Necesitas la Reliquia del Fin para usar este slot!";

         for(int slot : slotsToLock) {
            class_1799 stack = player.method_31548().method_5438(slot);
            if (!stack.method_31574(class_1802.field_8615)) {
               player.method_31548().method_5447(slot, new class_1799(class_1802.field_8615));
               player.method_7328(stack, false);
               player.method_7353(class_2561.method_43470(msg).method_27692(class_124.field_1067), true);
            }
         }

         this.cleanedPlayers.remove(player.method_5667());
      } else {
         this.cleanedPlayers.add(player.method_5667());
      }

   }

   private static void teleportGhastPigman(class_1309 ghast) {
      if (!(ghast.method_59922().method_43057() >= 0.8F)) {
         class_1937 level = ghast.method_37908();

         for(int i = 0; i < 32; ++i) {
            double x = ghast.method_23317() + (ghast.method_59922().method_43058() - (double)0.5F) * (double)16.0F;
            double y = ghast.method_23318() + (double)(ghast.method_59922().method_43048(16) - 8);
            double z = ghast.method_23321() + (ghast.method_59922().method_43058() - (double)0.5F) * (double)16.0F;
            class_2338 targetPos = class_2338.method_49637(x, y, z);
            if (level.method_8320(targetPos).method_26215()) {
               ghast.method_5859(x, y, z);
               if (ghast.method_5782()) {
                  level.method_43128((class_1657)null, x, y, z, class_3417.field_14890, class_3419.field_15251, 1.0F, 1.0F);
                  ghast.method_5783(class_3417.field_14890, 1.0F, 1.0F);
                  break;
               }
            }
         }

      }
   }

   private void runPeriodicMobPass(class_3218 world) {
      for(class_1297 entity : world.method_27909()) {
         if (entity instanceof class_1309 livingEntity) {
            if (!this.handleMobReplacement(livingEntity, world)) {
               class_1299<?> type = livingEntity.method_5864();
               if (!isSpecialNeutral(type)) {
                  if (type != class_1299.field_6093 && type != class_1299.field_6085 && type != class_1299.field_6115 && type != class_1299.field_6132 && type != class_1299.field_6143) {
                     if (!HostileMobConverter.isAlreadyHostile(livingEntity)) {
                        HostileMobConverter.convertToHostile(livingEntity);
                     }
                  } else {
                     HostileMobConverter.converToRavager(livingEntity, world);
                  }
               }

               this.applySpiderEffects(entity, world);
            }
         }
      }

   }

   private void applySpiderEffects(class_1297 entity, class_3218 world) {
      if (entity instanceof class_1628 spider) {
         if (!(spider instanceof class_1549)) {
            class_1549 caveSpider = new class_1549(class_1299.field_6084, world);
            caveSpider.method_5808(spider.method_23317(), spider.method_23318(), spider.method_23321(), spider.method_36454(), spider.method_36455());
            world.method_8649(caveSpider);
            spider.method_31472();
            if (world.field_9229.method_43048(100) <= 10) {
               ModuloDeMuerte.setupCaveSpiderStack(caveSpider, world);
            }

            return;
         }
      }

      boolean var10000;
      label94: {
         if (!(entity instanceof class_1549) && !(entity instanceof class_1614) && !(entity instanceof class_1559)) {
            label90: {
               if (entity instanceof class_1452) {
                  class_1452 pig = (class_1452)entity;
                  if (pig.method_16914()) {
                     break label90;
                  }
               }

               if (!(entity instanceof class_1593) && !(entity instanceof class_1632)) {
                  var10000 = false;
                  break label94;
               }
            }
         }

         var10000 = true;
      }

      boolean isTarget = var10000;
      if (isTarget) {
         if (entity instanceof class_1549) {
            class_1549 cs = (class_1549)entity;
            if (cs.method_6059(class_1294.field_5924)) {
               return;
            }
         }

         if (entity instanceof class_1614) {
            class_1614 sf = (class_1614)entity;
            if (sf.method_6059(class_1294.field_5912)) {
               return;
            }
         }

         if (entity instanceof class_1559) {
            class_1559 em = (class_1559)entity;
            if (em.method_6059(class_1294.field_5912)) {
               return;
            }
         }

         if (entity instanceof class_1452) {
            class_1452 pig = (class_1452)entity;
            if (pig.method_5752().contains("EffectsApplied")) {
               return;
            }
         }

         if (entity instanceof class_1593) {
            class_1593 phantom = (class_1593)entity;
            if (phantom.method_5752().contains("EffectsApplied")) {
               return;
            }
         }

         if (entity instanceof class_1632) {
            class_1632 vindicator = (class_1632)entity;
            if (vindicator.method_5752().contains("EffectsApplied")) {
               return;
            }
         }

         if (entity instanceof class_1549) {
            class_1549 caveSpider = (class_1549)entity;
            this.applyRandomEffects(caveSpider, world.field_9229);
            if (caveSpider.method_5685().isEmpty()) {
               this.addSkeletonRider(caveSpider, world);
            }
         } else if (entity instanceof class_1614) {
            class_1614 silverfish = (class_1614)entity;
            this.applyRandomEffects(silverfish, world.field_9229);
         } else if (entity instanceof class_1559) {
            class_1559 endermite = (class_1559)entity;
            this.applyRandomEffects(endermite, world.field_9229);
         } else if (entity instanceof class_1452) {
            class_1452 pig = (class_1452)entity;
            pig.method_5780("EffectsApplied");
            this.applyRandomEffects(pig, world.field_9229);
         } else if (entity instanceof class_1593) {
            class_1593 phantom = (class_1593)entity;
            phantom.method_5780("EffectsApplied");
            this.applyRandomEffects(phantom, world.field_9229);
         } else if (entity instanceof class_1632) {
            class_1632 vindicator = (class_1632)entity;
            vindicator.method_5780("EffectsApplied");
            this.applyRandomEffects(vindicator, world.field_9229);
         }

      }
   }

   private void applyRandomEffects(class_1309 entity, class_5819 random) {
      List<class_1293> effects = new ArrayList(SPIDER_EFFECT_POOL);
      Collections.shuffle(effects, new Random(random.method_43055()));
      int maxEffects = entity instanceof class_1593 ? 3 : 5;
      int i = 0;

      for(int applied = 0; i < effects.size() && applied < maxEffects; ++i) {
         class_1293 effect = (class_1293)effects.get(i);
         if (!(entity instanceof class_1549) || !effect.method_5579().equals(class_1294.field_5912)) {
            entity.method_6092(new class_1293(effect));
            ++applied;
         }
      }

   }

   private void handleemperatrisdrop(class_1309 entity, class_1282 damageSource) {
      if (entity instanceof class_1584 && entity.method_5752().contains("UltraRavager")) {
         class_1799 totem = new class_1799(class_1802.field_8288);
         class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), totem);
         entity.method_37908().method_8649(itemEntity);
      } else if (entity instanceof class_1590 && entity.method_5752().contains("CarlosPigman")) {
         class_1799 golden = new class_1799(class_1802.field_8695, 32);
         class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), golden);
         entity.method_37908().method_8649(itemEntity);
      } else if (entity instanceof class_1646 && entity.method_5752().contains("JessVillager")) {
         class_1799 apple = new class_1799(class_1802.field_8463, 2);
         class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), apple);
         entity.method_37908().method_8649(itemEntity);
      }

   }

   private void convertNormalSkeletons(class_1309 entity, class_3218 world) {
      if (entity instanceof class_1613 skeleton) {
         if (!MobProcessingTracker.tryClaim(skeleton, "skeleton_class_d60_69")) {
            return;
         }

         this.createRandomSkeletonClass(skeleton, world, skeleton.method_5854());
      }

   }

   private void addSkeletonRider(class_1628 spider, class_3218 world) {
      if (!spider.method_5782()) {
         class_1613 skeleton = new class_1613(class_1299.field_6137, world);
         skeleton.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
         this.createRandomSkeletonClass(skeleton, world, spider);
      }

   }

   private void addSkeletonRiders(class_1593 phantom, class_3218 world) {
      class_1613 skeleton = new class_1613(class_1299.field_6137, world);
      skeleton.method_5814(phantom.method_23317(), phantom.method_23318(), phantom.method_23321());
      this.createRandomSkeletonClass(skeleton, world, phantom);
   }

   private void createRandomSkeletonClass(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      int roll = world.field_9229.method_43048(99);
      class_1297 actualVehicle = vehicle != null ? vehicle : skeleton.method_5854();
      if (roll < 14) {
         this.applyDiamondSkeletonEquipment(skeleton, world, actualVehicle);
      } else if (roll < 28) {
         this.convertToWitherSkeletonWithBow(skeleton, world, actualVehicle);
      } else if (roll < 42) {
         this.applyDiamondAxeSkeletonEquipment(skeleton, world, actualVehicle);
      } else if (roll < 56) {
         this.applyCrossbowGoldSkeletonEquipment(skeleton, world, actualVehicle);
      } else if (roll < 70) {
         this.convertToWitherSkeletonWithPowerBow(skeleton, world, actualVehicle);
      } else if (roll < 84) {
         this.applyCientificSkeletonEquipment(skeleton, world, actualVehicle);
      } else if (roll < 98) {
         this.applyDemonicSkeletonEquipment(skeleton, world, actualVehicle);
      } else {
         this.convertToWitherSkeletonWithPowerBowXXXMMDCCLXV(skeleton, world, vehicle);
      }

   }

   private void applyDiamondSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      class_1799 helmet = new class_1799(class_1802.field_8805);
      class_1799 chest = new class_1799(class_1802.field_8058);
      class_1799 leggings = new class_1799(class_1802.field_8348);
      class_1799 boots = new class_1799(class_1802.field_8285);
      class_1799 bow = new class_1799(class_1802.field_8102);
      registry.method_46746(class_1893.field_9111).ifPresent((holder) -> {
         helmet.method_7978(holder, 5);
         chest.method_7978(holder, 5);
         leggings.method_7978(holder, 5);
         boots.method_7978(holder, 5);
      });
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> bow.method_7978(holder, 50));
      skeleton.method_5673(class_1304.field_6169, helmet);
      skeleton.method_5673(class_1304.field_6174, chest);
      skeleton.method_5673(class_1304.field_6172, leggings);
      skeleton.method_5673(class_1304.field_6166, boots);
      skeleton.method_5673(class_1304.field_6173, bow);
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(4393481), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)100.0F);
         skeleton.method_6033(100.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Guerrero"));
      if (vehicle != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(vehicle);
      }

   }

   private void convertToWitherSkeletonWithBow(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5814(skeleton.method_23317(), skeleton.method_23318(), skeleton.method_23321());
      witherSkeleton.method_36456(skeleton.method_36454());
      witherSkeleton.method_5847(skeleton.method_5791());
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9116).ifPresent((holder) -> enchantments.method_57547(holder, 50));
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 110));
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
         maxHealthAttribute.method_6192((double)60.0F);
         witherSkeleton.method_6033(60.0F);
      }

      witherSkeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Táctico"));
      world.method_8649(witherSkeleton);
      if (vehicle != null) {
         witherSkeleton.method_5804(vehicle);
      }

      skeleton.method_31472();
   }

   private void applyDiamondAxeSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1799 axe = new class_1799(class_1802.field_8556);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9124).ifPresent((holder) -> enchantments.method_57547(holder, 20));
      registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 100));
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
         maxHealthAttribute.method_6192((double)100.0F);
         skeleton.method_6033(100.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Infernal"));
      if (vehicle != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(vehicle);
      }

   }

   private void applyCrossbowGoldSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1799 crossbow = new class_1799(class_1802.field_8399);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9118).ifPresent((holder) -> enchantments.method_57547(holder, 100));
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
      skeleton.method_6092(new class_1293(class_1294.field_5904, -1, 3, false, true));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)60.0F);
         skeleton.method_6033(60.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Asesino"));
      if (vehicle != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(vehicle);
      }

   }

   private void applyDemonicSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1799 helmet = new class_1799(ModArmorMaterials.NETHERITE_DEMONIACA_HELMET);
      class_1799 chest = new class_1799(ModArmorMaterials.NETHERITE_DEMONIACA_CHESTPLATE);
      class_1799 leggings = new class_1799(ModArmorMaterials.NETHERITE_DEMONIACA_LEGGINGS);
      class_1799 boots = new class_1799(ModArmorMaterials.NETHERITE_DEMONIACA_BOOTS);
      class_1799 bow = new class_1799(class_1802.field_8102);
      skeleton.method_5673(class_1304.field_6169, helmet);
      skeleton.method_5673(class_1304.field_6174, chest);
      skeleton.method_5673(class_1304.field_6172, leggings);
      skeleton.method_5673(class_1304.field_6166, boots);
      skeleton.method_5673(class_1304.field_6173, bow);
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 effect = new class_1293(class_1294.field_5921, 1, 1);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(8454016), List.of(effect), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      skeleton.method_5780("DemonicSkeleton");
      skeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Demoníaco"));
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)100.0F);
         skeleton.method_6033(100.0F);
      }

      if (vehicle != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(vehicle);
      }

   }

   private void applyCientificSkeletonEquipment(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1799 helmet = new class_1799(ModArmorMaterials.NETHERITE_CIENTIFICA_HELMET);
      class_1799 chest = new class_1799(ModArmorMaterials.NETHERITE_CIENTIFICA_CHESTPLATE);
      class_1799 leggings = new class_1799(ModArmorMaterials.NETHERITE_CIENTIFICA_LEGGINGS);
      class_1799 boots = new class_1799(ModArmorMaterials.NETHERITE_CIENTIFICA_BOOTS);
      class_1799 bow = new class_1799(class_1802.field_8102);
      skeleton.method_5673(class_1304.field_6169, helmet);
      skeleton.method_5673(class_1304.field_6174, chest);
      skeleton.method_5673(class_1304.field_6172, leggings);
      skeleton.method_5673(class_1304.field_6166, boots);
      skeleton.method_5673(class_1304.field_6173, bow);
      class_1799 arrow = new class_1799(class_1802.field_8087);
      class_1293 slow = new class_1293(class_1294.field_5909, 360, 2);
      class_1293 weakness = new class_1293(class_1294.field_5911, 360, 0);
      class_1293 glowing = new class_1293(class_1294.field_5912, 360, 0);
      class_1293 poison = new class_1293(class_1294.field_5899, 360, 2);
      class_1844 potionContents = new class_1844(Optional.empty(), Optional.of(8366264), List.of(slow, weakness, glowing, poison), Optional.empty());
      arrow.method_57379(class_9334.field_49651, potionContents);
      skeleton.method_5673(class_1304.field_6171, arrow);
      class_1324 maxHealthAttribute = skeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)100.0F);
         skeleton.method_6033(100.0F);
      }

      skeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Científico"));
      if (vehicle != null) {
         world.method_8649(skeleton);
         skeleton.method_5804(vehicle);
      }

   }

   private void convertToWitherSkeletonWithPowerBowXXXMMDCCLXV(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5814(skeleton.method_23317(), skeleton.method_23318(), skeleton.method_23321());
      witherSkeleton.method_36456(skeleton.method_36454());
      witherSkeleton.method_5847(skeleton.method_5791());
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 32765));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      witherSkeleton.method_5673(class_1304.field_6173, bow);
      class_1324 maxHealthAttribute = witherSkeleton.method_5996(class_5134.field_23716);
      if (maxHealthAttribute != null) {
         maxHealthAttribute.method_6192((double)400.0F);
         witherSkeleton.method_6033(400.0F);
      }

      witherSkeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Definitivo"));
      world.method_8649(witherSkeleton);
      if (vehicle != null) {
         witherSkeleton.method_5873(vehicle, true);
      }

      skeleton.method_31472();
   }

   private void convertToWitherSkeletonWithPowerBow(class_1613 skeleton, class_3218 world, class_1297 vehicle) {
      class_1639 witherSkeleton = new class_1639(class_1299.field_6076, world);
      witherSkeleton.method_5814(skeleton.method_23317(), skeleton.method_23318(), skeleton.method_23321());
      witherSkeleton.method_36456(skeleton.method_36454());
      witherSkeleton.method_5847(skeleton.method_5791());
      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 150));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      witherSkeleton.method_5673(class_1304.field_6173, bow);
      skeleton.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, true));
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

      witherSkeleton.method_5665(class_2561.method_43470("§6Ultra Esqueleto Pesadilla"));
      world.method_8649(witherSkeleton);
      if (vehicle != null) {
         witherSkeleton.method_5873(vehicle, true);
      }

      skeleton.method_31472();
   }

   private void dyeLeatherArmor(class_1799 armor, int color) {
      armor.method_57379(class_9334.field_49644, new class_9282(color, false));
   }

   private void convertAllExistingMobs(class_3218 world) {
      for(class_1297 entity : world.method_27909()) {
         if (entity instanceof class_1309 livingEntity) {
            if (!this.handleMobReplacement(livingEntity, world)) {
               class_1299<?> type = livingEntity.method_5864();
               if (!isSpecialNeutral(type)) {
                  if (type != class_1299.field_6093 && type != class_1299.field_6085 && type != class_1299.field_6115 && type != class_1299.field_6132 && type != class_1299.field_6143) {
                     if (!HostileMobConverter.isAlreadyHostile(livingEntity)) {
                        HostileMobConverter.convertToHostile(livingEntity);
                     }
                  } else {
                     HostileMobConverter.converToRavager(livingEntity, world);
                  }
               }
            }
         }
      }

   }

   private void applyEndermanBehavior(final class_1548 creeper) {
      MobAccessor accessor = (MobAccessor)creeper;
      class_1309 target = creeper.method_5968();
      accessor.getGoalSelector().method_6277(1, new class_1352() {
         public boolean method_6264() {
            return creeper.method_5805() && creeper.method_7007() <= 0 && creeper.method_5968() == null && creeper.method_59922().method_43057() < 0.005F;
         }

         public boolean method_6266() {
            return false;
         }

         public void method_6269() {
            Day60to69Handler.teleportRandomly(creeper);
         }
      });
   }

   private void checkRandomLevitation(class_3222 player) {
      class_1937 level = player.method_37908();
      if (level.method_27983() == class_1937.field_25179) {
         boolean isNight = !level.method_8530();
         boolean isRaining = level.method_8419() && level.method_8311(player.method_24515());
         if (isNight || isRaining) {
            if (level.field_9229.method_43048(10000) == 0) {
               int durationTicks = 60 + level.field_9229.method_43048(341);
               player.method_6092(new class_1293(class_1294.field_5902, durationTicks, 0, false, true));
            }
         }
      }
   }

   private void trySpawnWildSnowGolem(class_3222 player) {
      class_3218 level = player.method_51469();
      if (level.method_27983() == class_1937.field_25179) {
         if (level.field_9229.method_43048(400) == 0) {
            class_238 checkArea = player.method_5829().method_1014((double)64.0F);
            if (level.method_18467(class_1473.class, checkArea).size() < 12) {
               double angle = level.field_9229.method_43058() * Math.PI * (double)2.0F;
               double distance = (double)16.0F + level.field_9229.method_43058() * (double)24.0F;
               int x = player.method_31477() + (int)Math.round(Math.cos(angle) * distance);
               int z = player.method_31479() + (int)Math.round(Math.sin(angle) * distance);
               int y = level.method_8624(class_2903.field_13203, x, z);
               class_2338 groundPos = new class_2338(x, y - 1, z);
               class_2338 spawnPos = new class_2338(x, y, z);
               if (level.method_8320(groundPos).method_51367()) {
                  if (level.method_8320(spawnPos).method_26215() && level.method_8320(spawnPos.method_10084()).method_26215()) {
                     class_1473 snowGolem = new class_1473(class_1299.field_6047, level);
                     snowGolem.method_5808((double)x + (double)0.5F, (double)y, (double)z + (double)0.5F, level.field_9229.method_43057() * 360.0F, 0.0F);
                     snowGolem.method_5943(level, level.method_8404(spawnPos), class_3730.field_16459, (class_1315)null);
                     level.method_8649(snowGolem);
                  }
               }
            }
         }
      }
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

   public static class ImpossibleWitchGoal extends class_1352 {
      private final class_1640 witch;
      private int attackCooldown;

      public ImpossibleWitchGoal(class_1640 witch) {
         this.witch = witch;
         this.method_6265(EnumSet.of(class_4134.field_18405, class_4134.field_18406));
      }

      public boolean method_6264() {
         class_1309 target = this.witch.method_5968();
         return target != null && target.method_5805();
      }

      public void method_6268() {
         class_1309 target = this.witch.method_5968();
         if (target != null) {
            double distSq = this.witch.method_5858(target);
            boolean canSee = this.witch.method_5985().method_6369(target);
            if (distSq > (double)100.0F) {
               this.witch.method_5942().method_6335(target, (double)1.0F);
            } else if (distSq < (double)16.0F) {
               this.witch.method_5942().method_6340();
            }

            this.witch.method_5988().method_6226(target, 30.0F, 30.0F);
            if (--this.attackCooldown <= 0 && canSee && distSq <= (double)225.0F) {
               this.throwImpossiblePotion(target);
               this.attackCooldown = 30;
            }

         }
      }

      private void throwImpossiblePotion(class_1309 target) {
         class_1686 thrownPotion = new class_1686(class_1299.field_6045, this.witch.method_37908());
         thrownPotion.method_7432(this.witch);
         thrownPotion.method_5814(this.witch.method_23317(), this.witch.method_23320(), this.witch.method_23321());
         class_1799 potionStack = new class_1799(class_1802.field_8436);
         List<class_1293> effects = new ArrayList();
         int random = this.witch.method_59922().method_43048(3);
         if (random == 0) {
            effects.add(new class_1293(class_1294.field_5921, 1, 3));
         } else if (random == 1) {
            effects.add(new class_1293(class_1294.field_5899, 6000, 2));
         } else {
            effects.add(new class_1293(class_1294.field_5909, 400, 4));
         }

         potionStack.method_57379(class_9334.field_49651, new class_1844(Optional.empty(), Optional.of(3692632), effects, Optional.empty()));
         thrownPotion.method_16940(potionStack);
         double dX = target.method_23317() - this.witch.method_23317();
         double dY = target.method_23323(0.6) - thrownPotion.method_23318();
         double dZ = target.method_23321() - this.witch.method_23321();
         double dHorizontal = Math.sqrt(dX * dX + dZ * dZ);
         thrownPotion.method_7485(dX, dY + dHorizontal * 0.2, dZ, 0.75F, 4.0F);
         this.witch.method_37908().method_43128((class_1657)null, this.witch.method_23317(), this.witch.method_23318(), this.witch.method_23321(), class_3417.field_15067, class_3419.field_15251, 1.0F, 0.8F + this.witch.method_59922().method_43057() * 0.4F);
         this.witch.method_37908().method_8649(thrownPotion);
      }
   }
}
