package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.class_1299;
import net.minecraft.class_1528;
import net.minecraft.class_1937;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitherSpawn {
   private static final Logger LOGGER = LoggerFactory.getLogger("WitherManager");
   private static final long MAX_TICKS = 72000L;
   private static final Map<UUID, Long> playerTimers = new HashMap();
   private static MinecraftServer serverInstance = null;
   private static boolean stateLoaded = false;

   private static Path getFilePath() {
      if (serverInstance == null) {
         throw new IllegalStateException("WitherSpawner no ha sido inicializado");
      } else {
         Path worldPath = serverInstance.method_27050(class_5218.field_24188);
         return worldPath.resolve("permadeath_wither.txt");
      }
   }

   public static void init() {
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> saveState());
   }

   private static void loadState() {
      try {
         Path path = getFilePath();
         if (Files.exists(path, new LinkOption[0])) {
            playerTimers.clear();

            for(String line : Files.readAllLines(path)) {
               if (line.contains(":")) {
                  String[] parts = line.split(":");
                  UUID uuid = UUID.fromString(parts[0]);
                  long ticks = Long.parseLong(parts[1]);
                  playerTimers.put(uuid, ticks);
               }
            }

            LOGGER.info("[Permadeath] Se cargaron " + playerTimers.size() + " contadores de jugadores.");
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar estado del Wither Individual", e);
      }

   }

   private static void saveState() {
      if (serverInstance != null) {
         try {
            Path path = getFilePath();
            Files.createDirectories(path.getParent());
            StringBuilder data = new StringBuilder();

            for(Map.Entry<UUID, Long> entry : playerTimers.entrySet()) {
               data.append(((UUID)entry.getKey()).toString()).append(":").append(entry.getValue()).append("\n");
            }

            Files.writeString(path, data.toString());
         } catch (Exception e) {
            LOGGER.error("Error al guardar estado del Wither Individual", e);
         }

      }
   }

   public static void handleTick(class_3218 world) {
      if (world.method_27983() == class_1937.field_25179) {
         MinecraftServer server = world.method_8503();
         if (server != null) {
            if (serverInstance == null) {
               serverInstance = server;
            }

            if (!stateLoaded) {
               loadState();
               stateLoaded = true;
            }

            long currentDay = DateManager.getCurrentRealDay();
            if (currentDay >= 60L) {
               for(class_3222 player : world.method_18456()) {
                  UUID uuid = player.method_5667();
                  playerTimers.putIfAbsent(uuid, 72000L);
                  long remaining = (Long)playerTimers.get(uuid);
                  if (remaining > 0L) {
                     playerTimers.put(uuid, remaining - 1L);
                     if (remaining % 400L == 0L) {
                        saveState();
                     }
                  } else {
                     spawnWitherOnPlayer(world, player);
                     playerTimers.put(uuid, 72000L);
                     saveState();
                  }
               }

            }
         }
      }
   }

   private static void spawnWitherOnPlayer(class_3218 world, class_3222 player) {
      class_1528 wither = new class_1528(class_1299.field_6119, world);
      wither.method_5808(player.method_23317(), player.method_23318(), player.method_23321(), player.method_36454(), player.method_36455());
      world.method_8649(wither);
      LOGGER.info("Wither invocado individualmente para: " + player.method_5820());
   }
}
