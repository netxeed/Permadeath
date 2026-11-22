package com.serthekiller.permadeath.utils;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import net.minecraft.class_1934;
import net.minecraft.class_2561;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.class_8915;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePauseManager {
   private static final Logger LOGGER = LoggerFactory.getLogger("GamePauseManager");
   private static final String FILE = "permadeath_game.txt";
   private static final int ENFORCE_INTERVAL_TICKS = 20;
   private static final int REMINDER_INTERVAL_TICKS = 1200;
   private static final String STOP_MESSAGE = "§c[PERMADEATH] §e⏸ El juego está en §cSTOP§e. Los días no avanzan, el mundo está congelado (§btick freeze§e) y no puedes jugar.\n§7Usa §a/permadeath game play §7para seguir jugando.";
   private static boolean paused = false;
   private static long pausedDay = 1L;
   private static final Map<UUID, class_1934> lockedPlayers = new HashMap();
   private static boolean tickFrozenByPause = false;
   private static int enforceCounter = 0;
   private static int reminderCounter = 0;

   public static boolean isPaused() {
      return paused;
   }

   public static long getPausedDay() {
      return pausedDay;
   }

   public static void reset() {
      paused = false;
      pausedDay = 1L;
      lockedPlayers.clear();
      enforceCounter = 0;
      reminderCounter = 0;
      tickFrozenByPause = false;
   }

   public static boolean pause(MinecraftServer server) {
      if (paused) {
         return false;
      } else {
         pausedDay = DateManager.getCurrentRealDay();
         paused = true;
         freezeTicks(server);
         reminderCounter = 0;
         enforce(server, false);
         save(server);
         server.method_3760().method_43514(class_2561.method_43470("§c[PERMADEATH] §e⏸ El juego está en §cSTOP§e. Los días no avanzan, el mundo está congelado (§btick freeze§e) y no puedes jugar.\n§7Usa §a/permadeath game play §7para seguir jugando."), false);
         LOGGER.info("Juego en STOP - día congelado: {}", pausedDay);
         return true;
      }
   }

   public static boolean resume(MinecraftServer server) {
      if (!paused) {
         return false;
      } else {
         long day = pausedDay;
         paused = false;
         unfreezeTicks(server);
         if (!DateManager.isSpeedrunEnabled()) {
            DateManager.setStartDate(LocalDate.now().minusDays(day - 1L));
         }

         restoreOnlinePlayers(server);
         save(server);
         server.method_3760().method_43514(class_2561.method_43470("§a[PERMADEATH] ▶ El juego está en §lPLAY§r§a. ¡Los días vuelven a avanzar! Día §f" + day), false);
         LOGGER.info("Juego en PLAY - continúa en el día {}", day);
         return true;
      }
   }

   private static boolean freezeTicks(MinecraftServer server) {
      class_8915 rate = server.method_54833();
      if (rate.method_54754()) {
         return false;
      } else {
         rate.method_54675(true);
         tickFrozenByPause = true;
         return true;
      }
   }

   private static void unfreezeTicks(MinecraftServer server) {
      if (tickFrozenByPause) {
         server.method_54833().method_54675(false);
         tickFrozenByPause = false;
      }
   }

   public static void tick(MinecraftServer server) {
      if (paused || !lockedPlayers.isEmpty()) {
         if (++enforceCounter >= 20) {
            enforceCounter = 0;
            if (paused) {
               if (freezeTicks(server)) {
                  save(server);
               }

               enforce(server, true);
               reminderCounter += 20;
               if (reminderCounter >= 1200) {
                  reminderCounter = 0;
                  server.method_3760().method_43514(class_2561.method_43470("§c[PERMADEATH] §e⏸ El juego está en §cSTOP§e. Los días no avanzan, el mundo está congelado (§btick freeze§e) y no puedes jugar.\n§7Usa §a/permadeath game play §7para seguir jugando."), false);
               }
            } else {
               restoreOnlinePlayers(server);
            }

         }
      }
   }

   private static void enforce(MinecraftServer server, boolean announceToNewlyLocked) {
      boolean dirty = false;

      for(class_3222 player : server.method_3760().method_14571()) {
         class_1934 mode = player.field_13974.method_14257();
         if (mode != class_1934.field_9219) {
            boolean newlyLocked = false;
            if (!lockedPlayers.containsKey(player.method_5667())) {
               lockedPlayers.put(player.method_5667(), mode);
               newlyLocked = true;
               dirty = true;
            }

            if (mode != class_1934.field_9216) {
               player.method_7336(class_1934.field_9216);
            }

            if (!player.method_5655()) {
               player.method_5684(true);
            }

            if (newlyLocked && announceToNewlyLocked) {
               player.method_64398(class_2561.method_43470("§c[PERMADEATH] §e⏸ El juego está en §cSTOP§e. Los días no avanzan, el mundo está congelado (§btick freeze§e) y no puedes jugar.\n§7Usa §a/permadeath game play §7para seguir jugando."));
            }
         }
      }

      if (dirty) {
         save(server);
      }

   }

   private static void restoreOnlinePlayers(MinecraftServer server) {
      boolean dirty = false;

      for(class_3222 player : server.method_3760().method_14571()) {
         class_1934 previous = (class_1934)lockedPlayers.remove(player.method_5667());
         if (previous != null) {
            player.method_5684(false);
            if (player.field_13974.method_14257() != class_1934.field_9219) {
               player.method_7336(previous);
            }

            dirty = true;
         }
      }

      if (dirty) {
         save(server);
      }

   }

   private static Path getPath(MinecraftServer server) {
      return server.method_27050(class_5218.field_24188).resolve("permadeath_game.txt");
   }

   private static void save(MinecraftServer server) {
      try {
         Path path = getPath(server);
         if (!paused && lockedPlayers.isEmpty()) {
            Files.deleteIfExists(path);
            return;
         }

         StringBuilder sb = new StringBuilder();
         sb.append("paused=").append(paused).append("\n");
         sb.append("day=").append(pausedDay).append("\n");
         sb.append("tickfreeze=").append(tickFrozenByPause).append("\n");

         for(Map.Entry<UUID, class_1934> e : lockedPlayers.entrySet()) {
            sb.append("player.").append(e.getKey()).append("=").append(((class_1934)e.getValue()).method_8381()).append("\n");
         }

         Files.writeString(path, sb.toString());
      } catch (Exception e) {
         LOGGER.error("Error al guardar estado de pausa", e);
      }

   }

   public static void load(MinecraftServer server) {
      try {
         Path path = getPath(server);
         if (!Files.exists(path, new LinkOption[0])) {
            return;
         }

         Properties p = new Properties();
         p.load(new StringReader(Files.readString(path)));
         paused = Boolean.parseBoolean(p.getProperty("paused", "false").trim());
         pausedDay = Long.parseLong(p.getProperty("day", "1").trim());
         tickFrozenByPause = Boolean.parseBoolean(p.getProperty("tickfreeze", "false").trim());

         for(String key : p.stringPropertyNames()) {
            if (key.startsWith("player.")) {
               try {
                  UUID id = UUID.fromString(key.substring("player.".length()));
                  lockedPlayers.put(id, class_1934.method_8378(p.getProperty(key).trim(), class_1934.field_9215));
               } catch (IllegalArgumentException var6) {
               }
            }
         }

         if (paused) {
            LOGGER.info("Juego restaurado en STOP - día congelado: {}", pausedDay);
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar estado de pausa, se ignora", e);
      }

   }
}
