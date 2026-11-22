package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.Cambios.PvpChanges;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day70Handler implements PhaseManager.PhaseHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDay70");
   private final Set<UUID> playersSleeping = new HashSet();
   private int sleepMessageCooldown = 0;

   public void onPhaseStart(class_3218 world) {
      this.handleInstantSleep(world);
      Iterable var3 = world.method_27909();
      if (var3 instanceof class_1657 player) {
         if (player.method_6032() > player.method_6063()) {
            player.method_6033(player.method_6063());
         }
      }

   }

   public void onPhaseEnd(class_3218 world) {
      LOGGER.info("Finalizando");
      this.playersSleeping.clear();
      this.sleepMessageCooldown = 0;
      PvpChanges.applyPvpGamerule(world);
   }

   public void onTick(class_3218 world) {
      if (DateManager.getCurrentRealDay() >= 70L) {
         this.handleInstantSleep(world);
         if (this.sleepMessageCooldown > 0) {
            --this.sleepMessageCooldown;
         }

      }
   }

   public String getPhaseName() {
      return "Final...";
   }

   private void handleInstantSleep(class_3218 world) {
      long timeOfDay = world.method_8532() % 24000L;
      boolean isNight = timeOfDay >= 12541L && timeOfDay <= 23458L;

      for(class_3222 player : world.method_18456()) {
         UUID playerId = player.method_5667();
         if (player.method_6113()) {
            if (!this.playersSleeping.contains(playerId)) {
               this.playersSleeping.add(playerId);
               if (!isNight) {
                  class_2561 errorMessage = class_2561.method_43470("Solo puedes dormir de noche").method_27694((style) -> style.method_10977(class_124.field_1054));
                  player.method_43502(errorMessage, false);
                  player.method_18400();
                  this.playersSleeping.remove(playerId);
               } else if (isNight) {
                  long currentTime = world.method_8532();
                  long dayTime = currentTime / 24000L * 24000L;
                  world.method_29199(dayTime + 24000L);
                  if (this.sleepMessageCooldown == 0) {
                     class_2561 sleepMessage = class_2561.method_43473().method_10852(class_2561.method_43470(player.method_5477().getString()).method_27694((style) -> style.method_10977(class_124.field_1068))).method_10852(class_2561.method_43470(" ha dormido").method_27694((style) -> style.method_10977(class_124.field_1054)));
                     world.method_8503().method_3760().method_43514(sleepMessage, false);
                     this.sleepMessageCooldown = 60;
                  }

                  world.method_8503().execute(() -> {
                     player.method_18400();
                     this.playersSleeping.remove(playerId);
                  });
               }
            }
         } else {
            this.playersSleeping.remove(playerId);
         }
      }

   }
}
