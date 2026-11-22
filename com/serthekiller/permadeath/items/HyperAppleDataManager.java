package com.serthekiller.permadeath.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperAppleDataManager {
   private static final Logger LOGGER = LoggerFactory.getLogger("HyperAppleData");
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final String FILE_NAME = "hyper_apple_consumed.json";

   public static void register() {
      ServerLifecycleEvents.SERVER_STARTING.register(HyperAppleDataManager::loadData);
      ServerLifecycleEvents.SERVER_STOPPING.register(HyperAppleDataManager::saveData);
      ServerLifecycleEvents.SERVER_STOPPED.register((ServerLifecycleEvents.ServerStopped)(server) -> HyperGoldenApplePlus.getConsumedPlayersMap().clear());
      HyperGoldenApplePlus.registerRespawnHandler();
      LOGGER.info("Sistema de persistencia de Hyper Golden Apple registrado (Modo Map)");
   }

   public static void saveData(MinecraftServer server) {
      File dataFile = getDataFile(server);
      File tmpFile = new File(dataFile.getParentFile(), "hyper_apple_consumed.json.tmp");
      Map<String, Integer> stringMap = new HashMap();
      HyperGoldenApplePlus.getConsumedPlayersMap().forEach((uuid, count) -> stringMap.put(uuid.toString(), count));

      try {
         Writer writer = Files.newBufferedWriter(tmpFile.toPath(), StandardCharsets.UTF_8);

         try {
            GSON.toJson(stringMap, writer);
         } catch (Throwable var9) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var7) {
                  var9.addSuppressed(var7);
               }
            }

            throw var9;
         }

         if (writer != null) {
            writer.close();
         }
      } catch (IOException e) {
         LOGGER.error("Error al guardar datos de Hyper Golden Apple", e);
         return;
      }

      try {
         Files.move(tmpFile.toPath(), dataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
         LOGGER.info("Datos de Hyper Golden Apple guardados: {} registros", stringMap.size());
      } catch (IOException e) {
         LOGGER.error("Error al reemplazar el archivo de datos de Hyper Golden Apple", e);
      }

   }

   private static void loadData(MinecraftServer server) {
      Map<UUID, Integer> playersMap = HyperGoldenApplePlus.getConsumedPlayersMap();
      playersMap.clear();
      File dataFile = getDataFile(server);
      if (!dataFile.exists()) {
         LOGGER.info("No hay datos de Hyper Golden Apple para este mundo, empezando vacío");
      } else {
         try {
            Reader reader = Files.newBufferedReader(dataFile.toPath(), StandardCharsets.UTF_8);

            try {
               Type mapType = (new TypeToken<Map<String, Integer>>() {
               }).getType();
               Map<String, Integer> stringMap = (Map)GSON.fromJson(reader, mapType);
               if (stringMap != null) {
                  stringMap.forEach((uuidStr, count) -> {
                     try {
                        if (count != null && count > 0) {
                           playersMap.put(UUID.fromString(uuidStr), count);
                        }
                     } catch (IllegalArgumentException var4) {
                        LOGGER.warn("UUID inválido omitido: {}", uuidStr);
                     }

                  });
               }

               LOGGER.info("Datos de Hyper Golden Apple cargados: {} registros", playersMap.size());
            } catch (Throwable var7) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (reader != null) {
               reader.close();
            }
         } catch (Exception e) {
            LOGGER.error("Error al cargar datos de Hyper Golden Apple", e);
         }

      }
   }

   private static File getDataFile(MinecraftServer server) {
      File worldDir = server.method_27050(class_5218.field_24188).toFile();
      File dataDir = new File(worldDir, "data");
      if (!dataDir.exists()) {
         dataDir.mkdirs();
      }

      return new File(dataDir, "hyper_apple_consumed.json");
   }
}
