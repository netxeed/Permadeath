package com.serthekiller.permadeath.Cambios;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class mikecrack {
   private static final Logger LOGGER = LoggerFactory.getLogger("MikecrackMode");
   private static boolean enabled = false;

   public static void registerSpawner() {
      MikecrackSpawner.register();
   }

   public static void init(MinecraftServer server) {
      loadState(server);
      LOGGER.info("Cambio de Mikecrack inicializado — activo: {}", enabled);
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void enable(MinecraftServer server) {
      enabled = true;
      saveState(server);
      LOGGER.info("Cambio de Mikecrack ACTIVADO — Creepers sin restricciones de spawn.");
   }

   public static void disable(MinecraftServer server) {
      enabled = false;
      saveState(server);
      LOGGER.info("Cambio de Mikecrack DESACTIVADO — Creepers vuelven a spawn normal.");
   }

   private static Path getFilePath(MinecraftServer server) {
      Path worldRoot = server.method_27050(class_5218.field_24188);
      return worldRoot.resolve("permadeath_mikecrack.txt");
   }

   private static void loadState(MinecraftServer server) {
      try {
         Path path = getFilePath(server);
         if (Files.exists(path, new LinkOption[0])) {
            String content = Files.readString(path).trim();
            enabled = Boolean.parseBoolean(content);
            LOGGER.info("Estado Mikecrack cargado desde disco: {}", enabled);
         } else {
            enabled = false;
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar estado de Mikecrack", e);
         enabled = false;
      }

   }

   private static void saveState(MinecraftServer server) {
      try {
         Path path = getFilePath(server);
         Files.createDirectories(path.getParent());
         Files.writeString(path, String.valueOf(enabled));
      } catch (Exception e) {
         LOGGER.error("Error al guardar estado de Mikecrack", e);
      }

   }
}
