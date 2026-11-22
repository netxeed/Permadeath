package com.serthekiller.permadeath.utils;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateManager {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDateManager");
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   private static LocalDate startDate = null;
   private static final int MAX_DAYS = 70;
   private static MinecraftServer serverInstance = null;
   private static long lastKnownDay = -1L;
   private static boolean speedrunEnabled = false;
   private static long speedrunIntervalTicks = 0L;
   private static long speedrunElapsedTicks = 0L;
   private static long speedrunDay = 1L;
   private static final String SPEEDRUN_FILE = "permadeath_speedrun.txt";

   public static void init(MinecraftServer server) {
      reset();
      serverInstance = server;
      loadStartDate();
      if (startDate == null) {
         setStartDate(LocalDate.now());
      }

      loadSpeedrunState();
      GamePauseManager.load(server);
      LOGGER.info("Sistema de fechas inicializado - Fecha de inicio: {}", formatDate(startDate));
   }

   private static Path getConfigPath() {
      if (serverInstance == null) {
         throw new IllegalStateException("DateManager no ha sido inicializado con el servidor");
      } else {
         Path worldPath = serverInstance.method_27050(class_5218.field_24188);
         return worldPath.resolve("permadeath_date.txt");
      }
   }

   private static void loadStartDate() {
      try {
         Path configPath = getConfigPath();
         if (Files.exists(configPath, new LinkOption[0])) {
            String dateStr = Files.readString(configPath).trim();
            if (dateStr.isEmpty()) {
               LOGGER.warn("El archivo de fecha está vacío. Creando nueva fecha...");
               setStartDate(LocalDate.now());
               return;
            }

            startDate = LocalDate.parse(dateStr, FORMATTER);
            LOGGER.info("Fecha de inicio cargada desde mundo: {}", formatDate(startDate));
         } else {
            LOGGER.info("No se encontró archivo de fecha, este es un mundo nuevo");
            setStartDate(LocalDate.now());
         }
      } catch (Exception e) {
         LOGGER.error("Error crítico al cargar fecha. Reseteando a hoy para evitar crash.", e);
         startDate = LocalDate.now();
      }

   }

   private static void saveStartDate() {
      try {
         Path configPath = getConfigPath();
         Files.createDirectories(configPath.getParent());
         Files.writeString(configPath, formatDate(startDate));
         LOGGER.info("Fecha de inicio guardada en mundo: {}", formatDate(startDate));
      } catch (Exception e) {
         LOGGER.error("Error al guardar fecha de inicio", e);
      }

   }

   public static void setStartDate(LocalDate date) {
      startDate = date;
      saveStartDate();
   }

   public static long getCurrentRealDay() {
      if (GamePauseManager.isPaused()) {
         return GamePauseManager.getPausedDay();
      } else if (speedrunEnabled) {
         if (speedrunDay > 70L) {
            return 70L;
         } else {
            return speedrunDay < 1L ? 1L : speedrunDay;
         }
      } else if (startDate == null) {
         LOGGER.warn("startDate es null, retornando día 1");
         return 1L;
      } else {
         LocalDate today = LocalDate.now();
         long daysBetween = ChronoUnit.DAYS.between(startDate, today);
         long currentDay = daysBetween + 1L;
         if (currentDay > 70L) {
            currentDay = 70L;
         } else if (currentDay < 1L) {
            currentDay = 1L;
         }

         return currentDay;
      }
   }

   public static void setCurrentDay(MinecraftServer server, long targetDay) {
      if (!GamePauseManager.isPaused()) {
         if (serverInstance == null && server != null) {
            serverInstance = server;
         }

         if (targetDay < 1L) {
            targetDay = 1L;
         }

         if (targetDay > 70L) {
            targetDay = 70L;
         }

         if (speedrunEnabled) {
            speedrunDay = targetDay;
            speedrunElapsedTicks = 0L;
            saveSpeedrunState();
            LOGGER.info("Día speedrun cambiado a {}", targetDay);
            DayChangeHandler.forceImmediateCheck();
         } else {
            LocalDate newStartDate = LocalDate.now().minusDays(targetDay - 1L);
            setStartDate(newStartDate);
            LOGGER.info("Día cambiado a {} (nueva fecha de inicio: {})", targetDay, formatDate(newStartDate));
            DayChangeHandler.forceImmediateCheck();
         }
      }
   }

   public static LocalDate getStartDate() {
      if (startDate == null) {
         LOGGER.warn("startDate es null al intentar obtenerla");
         return LocalDate.now();
      } else {
         return startDate;
      }
   }

   public static String formatDate(LocalDate date) {
      return date.format(FORMATTER);
   }

   public static String getStatusInfo() {
      long currentDay = getCurrentRealDay();
      LocalDate today = LocalDate.now();
      String pausedLine = GamePauseManager.isPaused() ? "§c⏸ JUEGO EN STOP\n" : "";
      if (!speedrunEnabled) {
         return pausedLine + String.format("§6=== Sistema de Días Reales ===\n§7Fecha de inicio: §f%s\n§7Fecha actual: §f%s\n§7Día actual: §f%d§7/§f%d\n§7Fase: %s", formatDate(startDate), formatDate(today), currentDay, 70, getPhaseInfo(currentDay));
      } else {
         long totalMinutes = speedrunIntervalTicks / 1200L;
         String intervalDisplay = totalMinutes >= 60L && totalMinutes % 60L == 0L ? totalMinutes / 60L + "h" : totalMinutes + "m";
         return pausedLine + String.format("§6=== Sistema de Días (§dSPEEDRUN§6) ===\n§7Cambio de día cada: §f%s\n§7Día actual: §f%d§7/§f%d\n§7Fase: %s", intervalDisplay, currentDay, 70, getPhaseInfo(currentDay));
      }
   }

   private static String getPhaseInfo(long day) {
      if (day <= 9L) {
         return "§aFase 1 (Días 1-9)";
      } else if (day <= 19L) {
         return "§eFase 2 (Días 10-19)";
      } else if (day <= 29L) {
         return "§6Fase 3 (Días 20-29)";
      } else if (day <= 39L) {
         return "§cFase 4 (Días 30-39)";
      } else if (day <= 49L) {
         return "§4Fase 5 (Días 40-49)";
      } else if (day <= 59L) {
         return "§5Fase 6 (Días 50-59)";
      } else {
         return day <= 69L ? "§dFase 7 (Días 60-69)" : "§dFase 7 (Días 60-69)";
      }
   }

   public static int getCurrentPhase() {
      long day = getCurrentRealDay();
      return day <= 9L ? 0 : (int)(day / 10L);
   }

   public static boolean checkAndUpdateDay() {
      long currentDay = getCurrentRealDay();
      if (currentDay != lastKnownDay) {
         lastKnownDay = currentDay;
         return true;
      } else {
         return false;
      }
   }

   public static void reset() {
      startDate = null;
      serverInstance = null;
      lastKnownDay = -1L;
      speedrunEnabled = false;
      speedrunIntervalTicks = 0L;
      speedrunElapsedTicks = 0L;
      speedrunDay = 1L;
      GamePauseManager.reset();
   }

   public static void enableSpeedrun(long intervalTicks) {
      long startDay = getCurrentRealDay();
      activateSpeedrun(intervalTicks, startDay);
      saveSpeedrunState();
   }

   private static void activateSpeedrun(long intervalTicks, long startDay) {
      speedrunEnabled = true;
      speedrunIntervalTicks = intervalTicks;
      speedrunElapsedTicks = 0L;
      speedrunDay = Math.max(1L, Math.min(startDay, 70L));
      lastKnownDay = -1L;
      LOGGER.info("Modo speedrun ACTIVADO - {} ticks por día, empezando en día {}", intervalTicks, speedrunDay);
   }

   public static void disableSpeedrun() {
      if (speedrunEnabled) {
         long day = speedrunDay;
         speedrunEnabled = false;
         speedrunIntervalTicks = 0L;
         speedrunElapsedTicks = 0L;
         speedrunDay = 1L;
         lastKnownDay = -1L;
         setStartDate(LocalDate.now().minusDays(day - 1L));
         deleteSpeedrunFile();
         LOGGER.info("Modo speedrun DESACTIVADO - continúa en día real {}", day);
      }
   }

   public static void tickSpeedrun() {
      if (speedrunEnabled && !GamePauseManager.isPaused()) {
         ++speedrunElapsedTicks;
         if (speedrunElapsedTicks >= speedrunIntervalTicks) {
            speedrunElapsedTicks = 0L;
            if (speedrunDay < 70L) {
               ++speedrunDay;
               saveSpeedrunState();
            }
         }

      }
   }

   private static Path getSpeedrunPath() {
      if (serverInstance == null) {
         throw new IllegalStateException("DateManager no ha sido inicializado con el servidor");
      } else {
         return serverInstance.method_27050(class_5218.field_24188).resolve("permadeath_speedrun.txt");
      }
   }

   private static void saveSpeedrunState() {
      if (serverInstance != null) {
         try {
            long totalMinutes = speedrunIntervalTicks / 1200L;
            String unit;
            long amount;
            if (totalMinutes >= 60L && totalMinutes % 60L == 0L) {
               unit = "h";
               amount = totalMinutes / 60L;
            } else {
               unit = "m";
               amount = Math.max(1L, totalMinutes);
            }

            String content = "day=" + speedrunDay + "\nunit=" + unit + "\namount=" + amount + "\n";
            Files.writeString(getSpeedrunPath(), content);
         } catch (Exception e) {
            LOGGER.error("Error al guardar estado del speedrun", e);
         }

      }
   }

   private static void loadSpeedrunState() {
      try {
         Path path = getSpeedrunPath();
         if (!Files.exists(path, new LinkOption[0])) {
            return;
         }

         Properties p = new Properties();
         p.load(new StringReader(Files.readString(path)));
         long day = Long.parseLong(p.getProperty("day", "1").trim());
         long amount = Long.parseLong(p.getProperty("amount", "0").trim());
         String unit = p.getProperty("unit", "m").trim().toLowerCase();
         if (amount <= 0L) {
            LOGGER.warn("Archivo de speedrun inválido (amount={}), se ignora", amount);
            return;
         }

         long ticks = amount * 20L * ("h".equals(unit) ? 3600L : 60L);
         activateSpeedrun(ticks, day);
         LOGGER.info("Speedrun restaurado: día {}, cada {}{}", new Object[]{speedrunDay, amount, unit});
      } catch (Exception e) {
         LOGGER.error("Error al cargar estado del speedrun, se ignora", e);
      }

   }

   private static void deleteSpeedrunFile() {
      try {
         if (serverInstance != null) {
            Files.deleteIfExists(getSpeedrunPath());
         }
      } catch (Exception e) {
         LOGGER.error("Error al borrar archivo de speedrun", e);
      }

   }

   public static boolean isSpeedrunEnabled() {
      return speedrunEnabled;
   }

   public static long getSpeedrunIntervalTicks() {
      return speedrunIntervalTicks;
   }
}
