package com.serthekiller.permadeath.utils;

import com.serthekiller.permadeath.changes.DaySyncPayload;
import java.util.Set;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DayChangeHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("DayChangeHandler");
   private static final Set<Long> RELOAD_THRESHOLDS = Set.of(40L, 50L, 60L);
   private static final int CHECK_INTERVAL_TICKS = 6000;
   private static final int CHECK_INTERVAL_TICKS_SPEEDRUN = 20;
   private static int tickCounter = 0;
   private static long highestReloadedThreshold = 0L;
   private static volatile boolean forceImmediateCheck = false;

   public static void register() {
      ServerTickEvents.END_SERVER_TICK.register(DayChangeHandler::onServerTick);
      LOGGER.info("DayChangeHandler registrado");
   }

   public static void forceImmediateCheck() {
      forceImmediateCheck = true;
   }

   private static void onServerTick(MinecraftServer server) {
      DateManager.tickSpeedrun();
      GamePauseManager.tick(server);
      SurvivalAchievementManager.tick(server);
      int interval = DateManager.isSpeedrunEnabled() ? 20 : 6000;
      ++tickCounter;
      if (tickCounter >= interval || forceImmediateCheck) {
         tickCounter = 0;
         forceImmediateCheck = false;
         boolean dayChanged = DateManager.checkAndUpdateDay();
         if (dayChanged) {
            long currentDay = DateManager.getCurrentRealDay();
            LOGGER.info("Día cambiado, día actual: {}", currentDay);
            long pendingThreshold = (Long)RELOAD_THRESHOLDS.stream().filter((t) -> t > highestReloadedThreshold && currentDay >= t).max(Long::compareTo).orElse(-1L);
            if (pendingThreshold > 0L) {
               highestReloadedThreshold = pendingThreshold;
               LOGGER.info("Umbral(es) hasta día {} alcanzado(s) — recargando recetas...", pendingThreshold);
               server.method_29439(server.method_3836().method_29210()).thenRun(() -> LOGGER.info("Recetas recargadas correctamente para el día {}", pendingThreshold)).exceptionally((e) -> {
                  LOGGER.error("Error al recargar recetas", e);
                  return null;
               });
            }

            for(class_3222 player : server.method_3760().method_14571()) {
               DaySyncPayload.send(player, currentDay);
            }

         }
      }
   }
}
