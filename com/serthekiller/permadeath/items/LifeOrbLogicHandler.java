package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.Cambios.LifeOrbManager;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_1322.class_1323;

public class LifeOrbLogicHandler {
   private static final class_2960 LIFE_ORB_PENALTY_ID = class_2960.method_60655("permadeath", "life_orb_penalty");
   private static final double PENALTY_HP = (double)-16.0F;
   private static final int GRACE_PERIOD_TICKS = 60;
   private static final Map<UUID, Integer> graceTimers = new HashMap();
   private static Boolean lastKnownActive = null;

   public static void register() {
      ServerLifecycleEvents.SERVER_STARTED.register((ServerLifecycleEvents.ServerStarted)(server) -> {
         lastKnownActive = null;
         graceTimers.clear();
      });
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> {
         lastKnownActive = null;
         graceTimers.clear();
      });
      ServerPlayConnectionEvents.DISCONNECT.register((ServerPlayConnectionEvents.Disconnect)(handler, server) -> graceTimers.remove(handler.method_32311().method_5667()));
      ServerTickEvents.END_SERVER_TICK.register((ServerTickEvents.EndTick)(server) -> {
         if (LifeOrbManager.isStateLoaded()) {
            boolean isActiveNow = LifeOrbManager.isLifeOrbActive() && DateManager.getCurrentRealDay() >= 60L;
            if (lastKnownActive == null) {
               lastKnownActive = isActiveNow;
               if (isActiveNow) {
                  for(class_3222 player : server.method_3760().method_14571()) {
                     updateLifeOrbEffect(player);
                  }
               }

            } else {
               if (isActiveNow && !lastKnownActive) {
                  for(class_3222 player : server.method_3760().method_14571()) {
                     applyInitialPenalty(player);
                  }
               }

               lastKnownActive = isActiveNow;
               if (!isActiveNow) {
                  graceTimers.clear();

                  for(class_3222 player : server.method_3760().method_14571()) {
                     class_1324 healthAttr = player.method_5996(class_5134.field_23716);
                     if (healthAttr != null && healthAttr.method_6199(LIFE_ORB_PENALTY_ID) != null) {
                        healthAttr.method_6200(LIFE_ORB_PENALTY_ID);
                        player.method_6033(player.method_6063());
                     }
                  }

               } else {
                  for(class_3222 player : server.method_3760().method_14571()) {
                     updateLifeOrbEffect(player);
                  }

               }
            }
         }
      });
   }

   private static boolean hasLifeOrb(class_3222 player) {
      for(int i = 0; i < player.method_31548().method_5439(); ++i) {
         class_1799 stack = player.method_31548().method_5438(i);
         if (stack != null && !stack.method_7960() && stack.method_31574(ModItems.LIFE_ORB)) {
            return true;
         }
      }

      return false;
   }

   private static void applyInitialPenalty(class_3222 player) {
      if (DateManager.getCurrentRealDay() >= 60L) {
         if (!player.method_7337() && !player.method_7325()) {
            if (hasLifeOrb(player)) {
               graceTimers.remove(player.method_5667());
            } else {
               graceTimers.put(player.method_5667(), 60);
            }
         } else {
            graceTimers.remove(player.method_5667());
         }
      }
   }

   private static void updateLifeOrbEffect(class_3222 player) {
      class_1324 healthAttr = player.method_5996(class_5134.field_23716);
      if (healthAttr != null) {
         UUID uuid = player.method_5667();
         boolean hasPenalty = healthAttr.method_6199(LIFE_ORB_PENALTY_ID) != null;
         if (!player.method_7337() && !player.method_7325()) {
            boolean hasOrb = hasLifeOrb(player);
            if (hasOrb) {
               graceTimers.remove(uuid);
               if (hasPenalty) {
                  healthAttr.method_6200(LIFE_ORB_PENALTY_ID);
               }
            } else {
               if (hasPenalty) {
                  graceTimers.remove(uuid);
                  return;
               }

               int remainingTicks = (Integer)graceTimers.getOrDefault(uuid, 60);
               if (remainingTicks > 0) {
                  graceTimers.put(uuid, remainingTicks - 1);
               } else {
                  graceTimers.remove(uuid);
                  healthAttr.method_26835(new class_1322(LIFE_ORB_PENALTY_ID, (double)-16.0F, class_1323.field_6328));
                  float currentHealth = player.method_6032();
                  float resultingHealth = currentHealth + -16.0F;
                  if (resultingHealth <= 0.0F) {
                     player.method_64419(player.method_37908().method_48963().method_51847(), Float.MAX_VALUE);
                  } else if (currentHealth > resultingHealth) {
                     player.method_6033(resultingHealth);
                  }
               }
            }

         } else {
            graceTimers.remove(uuid);
            if (hasPenalty) {
               healthAttr.method_6200(LIFE_ORB_PENALTY_ID);
            }

         }
      }
   }
}
