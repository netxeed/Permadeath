package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_124;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_3213;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.class_1259.class_1260;
import net.minecraft.class_1259.class_1261;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifeOrbManager {
   private static final Logger LOGGER = LoggerFactory.getLogger("LifeOrbManager");
   private static final long DURATION_MS = 28800000L;
   private static long expirationTimestamp = -1L;
   private static boolean lifeOrbActive = false;
   private static MinecraftServer serverInstance = null;
   private static boolean stateLoaded = false;
   private static final class_3213 BOSS_BAR;

   private static Path getFilePath() {
      if (serverInstance == null) {
         throw new IllegalStateException("LifeOrbManager no ha sido inicializado");
      } else {
         Path worldPath = serverInstance.method_27050(class_5218.field_24188);
         return worldPath.resolve("permadeath_lifeorb.txt");
      }
   }

   public static void init() {
      ServerLifecycleEvents.SERVER_STARTED.register((ServerLifecycleEvents.ServerStarted)(server) -> {
         serverInstance = server;
         expirationTimestamp = -1L;
         lifeOrbActive = false;
         stateLoaded = false;
         loadState();
         stateLoaded = true;
         LOGGER.info("[LifeOrb] Estado cargado al iniciar servidor — activo={}, expiración={}", lifeOrbActive, expirationTimestamp);
      });
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> saveState());
      ServerTickEvents.END_WORLD_TICK.register(LifeOrbManager::handleTick);
   }

   private static void loadState() {
      try {
         Path path = getFilePath();
         if (Files.exists(path, new LinkOption[0])) {
            String[] lines = Files.readString(path).trim().split("\n");
            if (lines.length >= 2) {
               long parsedExpiration = Long.parseLong(lines[0].trim());
               boolean parsedActive = Boolean.parseBoolean(lines[1].trim());
               expirationTimestamp = parsedExpiration;
               lifeOrbActive = parsedActive;
               LOGGER.info("[LifeOrb] Archivo leído — expiración={}, activo={}", expirationTimestamp, lifeOrbActive);
            }
         } else {
            saveState();
         }
      } catch (Exception e) {
         LOGGER.error("[LifeOrb] Error al cargar estado", e);
      }

   }

   private static void saveState() {
      if (serverInstance != null) {
         try {
            Path path = getFilePath();
            Files.createDirectories(path.getParent());
            Files.writeString(path, expirationTimestamp + "\n" + lifeOrbActive);
         } catch (Exception e) {
            LOGGER.error("[LifeOrb] Error al guardar estado", e);
         }

      }
   }

   public static void handleTick(class_3218 world) {
      if (world.method_27983() == class_1937.field_25179) {
         MinecraftServer server = world.method_8503();
         if (server != null) {
            if (!stateLoaded) {
               if (serverInstance == null) {
                  serverInstance = server;
               }

               loadState();
               stateLoaded = true;
               LOGGER.warn("[LifeOrb] Estado cargado desde handleTick (fallback).");
            }

            long currentDay = DateManager.getCurrentRealDay();
            if (currentDay < 60L) {
               if (lifeOrbActive || expirationTimestamp != -1L) {
                  lifeOrbActive = false;
                  expirationTimestamp = -1L;
                  saveState();
               }

               BOSS_BAR.method_14094();
            } else if (lifeOrbActive) {
               BOSS_BAR.method_14094();
               if (expirationTimestamp != -1L) {
                  expirationTimestamp = -1L;
                  saveState();
               }

            } else if (currentDay > 60L) {
               lifeOrbActive = true;
               expirationTimestamp = -1L;
               BOSS_BAR.method_14094();
               saveState();
               LOGGER.info("[LifeOrb] Día {} detectado (se saltó el 60). Orbe de Vida activado de inmediato.", currentDay);
               server.method_3760().method_43514(class_2561.method_43470("§c[PERMADEATH] ¡El Orbe de Vida se ha activado globalmente!").method_27692(class_124.field_1067), false);
            } else {
               if (expirationTimestamp == -1L) {
                  expirationTimestamp = System.currentTimeMillis() + 28800000L;
                  saveState();
                  LOGGER.info("[LifeOrb] ¡Día 60 iniciado! Contador de 8 horas arrancado.");
               }

               for(class_3222 player : server.method_3760().method_14571()) {
                  BOSS_BAR.method_14088(player);
               }

               long msRemaining = expirationTimestamp - System.currentTimeMillis();
               if (msRemaining <= 0L) {
                  lifeOrbActive = true;
                  expirationTimestamp = -1L;
                  BOSS_BAR.method_14094();
                  saveState();
                  LOGGER.info("[LifeOrb] Contador del día 60 expirado. Orbe de Vida activo permanentemente.");
                  server.method_3760().method_43514(class_2561.method_43470("§0[§4!§0] §c¡El tiempo ha terminado! El Orbe de Vida se ha activado globalmente.").method_27692(class_124.field_1067), false);
               } else {
                  updateBossBar(msRemaining);
               }

            }
         }
      }
   }

   private static void updateBossBar(long msRemaining) {
      long totalSeconds = msRemaining / 1000L;
      long hours = totalSeconds / 3600L;
      long minutes = totalSeconds % 3600L / 60L;
      long seconds = totalSeconds % 60L;
      String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
      BOSS_BAR.method_5413(class_2561.method_43470(timeString + " para obtener Life Orb").method_27692(class_124.field_1065));
      BOSS_BAR.method_5408(1.0F);
      long colorIndex = System.currentTimeMillis() / 500L % 5L;
      switch ((int)colorIndex) {
         case 0 -> BOSS_BAR.method_5416(class_1260.field_5786);
         case 1 -> BOSS_BAR.method_5416(class_1260.field_5785);
         case 2 -> BOSS_BAR.method_5416(class_1260.field_5788);
         case 3 -> BOSS_BAR.method_5416(class_1260.field_5783);
         case 4 -> BOSS_BAR.method_5416(class_1260.field_5784);
      }

   }

   public static boolean isLifeOrbActive() {
      return lifeOrbActive;
   }

   public static boolean isStateLoaded() {
      return stateLoaded;
   }

   static {
      BOSS_BAR = new class_3213(class_2561.method_43470("Cargando..."), class_1260.field_5786, class_1261.field_5795);
   }
}
