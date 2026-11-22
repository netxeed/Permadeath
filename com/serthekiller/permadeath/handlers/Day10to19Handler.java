package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.Cambios.PvpChanges;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1628;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day10to19Handler implements PhaseManager.PhaseHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDay10-19");
   private final Set<UUID> playersSleeping = new HashSet();
   private int sleepCheckCooldown = 0;
   private static final int MINIMUM_PLAYERS_TO_SLEEP = 4;
   private int mobSpawnCooldown = 0;
   private static final int MOB_SPAWN_INTERVAL = 100;

   public void onPhaseStart(class_3218 world) {
      LOGGER.info("Iniciando Fase 2: Días 10-19");
      MinecraftServer server = world.method_8503();
      if (server != null) {
         server.method_3760().method_43514(class_2561.method_43470("§e=== Fase 2: Días 10-19 ===\n").method_10852(class_2561.method_43470("§7- Sueño: Mínimo 4 jugadores\n")), false);
      }

      PvpChanges.applyPvpGamerule(world);
      this.handleSleepSystem(world);
      Iterable var4 = world.method_27909();
      if (var4 instanceof class_1657 player) {
         if (player.method_6032() > player.method_6063()) {
            player.method_6033(player.method_6063());
         }
      }

   }

   public void onPhaseEnd(class_3218 world) {
      LOGGER.info("Finalizando Fase 2: Días 10-19");
      this.playersSleeping.clear();
      this.sleepCheckCooldown = 0;
      this.mobSpawnCooldown = 0;
   }

   public void onTick(class_3218 world) {
      if (DateManager.getCurrentRealDay() <= 20L && DateManager.getCurrentRealDay() >= 10L) {
         this.applySpiderEffects(world);
         this.handleSleepSystem(world);
         if (this.sleepCheckCooldown > 0) {
            --this.sleepCheckCooldown;
         }

         if (this.mobSpawnCooldown > 0) {
            --this.mobSpawnCooldown;
         }

      }
   }

   public String getPhaseName() {
      return "Fase 2: Días 10-19";
   }

   private void applySpiderEffects(class_3218 world) {
      for(class_1297 entity : world.method_27909()) {
         if (entity instanceof class_1628 spider) {
            if (spider.method_6026().isEmpty()) {
               ArrayList<class_1293> availableEffects = new ArrayList();
               availableEffects.add(new class_1293(class_1294.field_5904, -1, 2, false, true));
               availableEffects.add(new class_1293(class_1294.field_5910, -1, 3, false, true));
               availableEffects.add(new class_1293(class_1294.field_5913, -1, 4, false, true));
               availableEffects.add(new class_1293(class_1294.field_5912, -1, 0, false, true));
               availableEffects.add(new class_1293(class_1294.field_5924, -1, 3, false, true));
               availableEffects.add(new class_1293(class_1294.field_5905, -1, 0, false, true));
               availableEffects.add(new class_1293(class_1294.field_5906, -1, 0, false, true));
               availableEffects.add(new class_1293(class_1294.field_5907, -1, 2, false, true));
               int effectCount = world.field_9229.method_43048(3) + 1;
               Collections.shuffle(availableEffects, new Random(world.field_9229.method_43055()));

               for(int i = 0; i < effectCount && i < availableEffects.size(); ++i) {
                  spider.method_6092((class_1293)availableEffects.get(i));
               }
            }
         }
      }

   }

   private void handleSleepSystem(class_3218 world) {
      long timeOfDay = world.method_8532() % 24000L;
      boolean isNight = timeOfDay >= 12541L && timeOfDay <= 23458L;
      if (!isNight) {
         this.playersSleeping.clear();
      } else if (world.method_8546()) {
         for(class_3222 player : world.method_18456()) {
            if (player.method_6113()) {
               player.method_43502(class_2561.method_43470("⚠ No puedes dormir durante una tormenta").method_27694((style) -> style.method_10977(class_124.field_1061)), false);
               player.method_18400();
               this.playersSleeping.remove(player.method_5667());
            }
         }

      } else {
         int totalPlayers = world.method_8503().method_3760().method_14574();
         int sleepingCount = 0;

         for(class_3222 player : world.method_18456()) {
            if (player.method_6113()) {
               if (!this.playersSleeping.contains(player.method_5667())) {
                  this.playersSleeping.add(player.method_5667());
               }

               ++sleepingCount;
            } else {
               this.playersSleeping.remove(player.method_5667());
            }
         }

         if (sleepingCount > 0 && sleepingCount < 4) {
            int missingPlayers = 4 - sleepingCount;
            class_2561 missingMessage = class_2561.method_43470("Se necesitan " + missingPlayers + " jugadores más durmiendo").method_27694((style) -> style.method_10977(class_124.field_1080));

            for(class_3222 player : world.method_18456()) {
               if (player.method_6113()) {
                  player.method_7353(missingMessage, true);
               }
            }
         }

         if (sleepingCount > 0 && this.sleepCheckCooldown == 0) {
            if (totalPlayers < 4) {
               for(class_3222 player : world.method_18456()) {
                  if (player.method_6113()) {
                     player.method_43502(class_2561.method_43470("⚠ No hay suficientes jugadores para dormir").method_27694((style) -> style.method_10977(class_124.field_1061)), false);
                     player.method_18400();
                     this.playersSleeping.remove(player.method_5667());
                  }
               }

               this.sleepCheckCooldown = 40;
            } else if (sleepingCount >= 4) {
               long currentTime = world.method_8532();
               long dayTime = currentTime / 24000L * 24000L;
               world.method_29199(dayTime + 24000L);

               for(class_3222 player : world.method_18456()) {
                  if (player.method_6113()) {
                     player.method_18400();
                  }
               }

               this.playersSleeping.clear();
               this.sleepCheckCooldown = 60;
            }
         }

      }
   }
}
