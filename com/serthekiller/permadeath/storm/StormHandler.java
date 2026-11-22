package com.serthekiller.permadeath.storm;

import com.serthekiller.permadeath.utils.DateManager;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1928;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StormHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("StormHandler");
   private static final String QUEUE_FILE = "permadeath_storm_queue.txt";
   private static final String MODE_FILE = "permadeath_storm_mode.txt";
   private static final String BOOST_FILE = "permadeath_boost.txt";
   private static final String LEGACY_STORM_FILE = "permadeath_storm.txt";
   private static final List<StormEntry> queue = new ArrayList();
   private static final Map<StormMode, DayListener> listeners = new EnumMap(StormMode.class);
   private static StormMode mode;
   private static long lastDispatchedDay;
   private static boolean uhcModeActive;
   private static long lastBoostCycle;
   private static int currentBoostLevel;
   private static MinecraftServer serverInstance;
   private static boolean stateLoaded;
   private static long lastRealMillis;
   private static long tickCarryMillis;

   public static void init() {
      ServerLifecycleEvents.SERVER_STARTED.register((ServerLifecycleEvents.ServerStarted)(server) -> {
         serverInstance = server;
         loadAll(server);
         stateLoaded = true;
         LOGGER.info("Sistema de tormentas inicializado para este mundo");
      });
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> {
         saveAll(server);
         serverInstance = null;
         stateLoaded = false;
         LOGGER.info("Estado de tormenta guardado al detener servidor");
      });
      ServerTickEvents.END_WORLD_TICK.register(StormHandler::handleTick);
   }

   public static void registerModeListener(StormMode forMode, DayListener listener) {
      listeners.put(forMode, listener);
   }

   public static void clearRuntimeState() {
      queue.clear();
      mode = StormHandler.StormMode.SIN_TORMENTA;
      lastDispatchedDay = -1L;
      uhcModeActive = false;
      lastBoostCycle = 0L;
      currentBoostLevel = 0;
      lastRealMillis = 0L;
      tickCarryMillis = 0L;
      serverInstance = null;
      stateLoaded = false;
      LOGGER.info("Variables estáticas de StormHandler limpiadas en memoria (sin tocar archivos).");
   }

   public static long unitsForDay(long day) {
      long dayMod = day % 25L;
      if (dayMod == 0L) {
         return 1L;
      } else {
         return day <= 25L ? dayMod : dayMod + 1L;
      }
   }

   public static long ticksPerUnit(long day) {
      return day >= 50L ? 36000L : 72000L;
   }

   public static StormEntry addStorm(MinecraftServer server, StormSource source, String playerName, long day, long ticks) {
      return addStorm(server, source, playerName, day, ticks, 0L);
   }

   public static StormEntry addStorm(MinecraftServer server, StormSource source, String playerName, long day, long ticks, long alreadyElapsedTicks) {
      long remaining = ticks - Math.max(0L, alreadyElapsedTicks);
      if (remaining <= 0L) {
         return null;
      } else {
         StormEntry entry = new StormEntry(source, playerName == null ? "-" : playerName, day, ticks, remaining);
         queue.add(entry);
         applyWeather(server);
         saveQueue(server);
         LOGGER.info("Tormenta agregada: fuente={}, jugador={}, día={}, ticks={}", new Object[]{source, entry.playerName, day, ticks});
         return entry;
      }
   }

   public static StormEntry addPlayerDeathStorm(MinecraftServer server, String playerName) {
      long day = DateManager.getCurrentRealDay();
      long ticks = unitsForDay(day) * ticksPerUnit(day);
      return addStorm(server, StormHandler.StormSource.MUERTE_JUGADOR, playerName, day, ticks);
   }

   public static String formatDuration(long ticks) {
      long totalMinutes = ticks / 1200L;
      if (totalMinutes % 60L == 0L) {
         long hours = totalMinutes / 60L;
         return hours + " hora" + (hours == 1L ? "" : "s");
      } else {
         long h = totalMinutes / 60L;
         long m = totalMinutes % 60L;
         String var10000 = h > 0L ? h + "h " : "";
         return var10000 + m + " minutos";
      }
   }

   public static boolean isStormActive() {
      return !queue.isEmpty();
   }

   public static boolean canSleepDuringStorm() {
      return queue.isEmpty();
   }

   public static long getStormTicksRemaining() {
      return totalTicks();
   }

   public static List<StormEntry> getAllEntries() {
      return new ArrayList(queue);
   }

   public static List<StormEntry> getEntries(StormSource source) {
      List<StormEntry> result = new ArrayList();

      for(StormEntry e : queue) {
         if (e.source == source) {
            result.add(e);
         }
      }

      return result;
   }

   public static long removePlayerDeathStorm(MinecraftServer server, String playerName, long day) {
      for(int i = 0; i < queue.size(); ++i) {
         StormEntry e = (StormEntry)queue.get(i);
         if (e.source == StormHandler.StormSource.MUERTE_JUGADOR && e.day == day && e.playerName.equalsIgnoreCase(playerName)) {
            queue.remove(i);
            afterQueueChanged(server);
            LOGGER.info("Tormenta restada: jugador={}, día={}, ticks quitados={}", new Object[]{playerName, day, e.remainingTicks});
            return e.remainingTicks;
         }
      }

      return -1L;
   }

   public static int clearRemovableStorms(MinecraftServer server) {
      int before = queue.size();
      queue.removeIf((e) -> e.source != StormHandler.StormSource.SERIE);
      afterQueueChanged(server);
      return before - queue.size();
   }

   public static void fullReset(MinecraftServer server) {
      queue.clear();
      lastBoostCycle = 0L;
      currentBoostLevel = 0;
      setNaturalRegeneration(server, true);
      uhcModeActive = false;
      class_3218 overworld = server.method_3847(class_1937.field_25179);
      if (overworld != null) {
         overworld.method_27910(6000, 0, false, false);
      }

      saveQueue(server);
      saveBoost(server);
      LOGGER.info("Reset completo de tormenta y boosts.");
   }

   public static StormMode getMode() {
      return mode;
   }

   public static void setMode(MinecraftServer server, StormMode newMode) {
      if (DateManager.isSpeedrunEnabled()) {
         newMode = StormHandler.StormMode.SIN_TORMENTA;
      }

      mode = newMode;
      saveMode(server);
      LOGGER.info("Modo de tormenta cambiado a {}", newMode);
   }

   private static void handleTick(class_3218 world) {
      if (world.method_27983() == class_1937.field_25179) {
         MinecraftServer server = world.method_8503();
         if (server != null) {
            if (serverInstance == null) {
               serverInstance = server;
            }

            if (!stateLoaded) {
               loadAll(server);
               stateLoaded = true;
               LOGGER.info("Sistema de tormentas inicializado desde tick (servidor integrado)");
            }

            long currentDay = DateManager.getCurrentRealDay();
            if (DateManager.isSpeedrunEnabled() && mode != StormHandler.StormMode.SIN_TORMENTA) {
               setMode(server, StormHandler.StormMode.SIN_TORMENTA);
            }

            checkAndApplyBuffsEvery25Days(server, currentDay);
            dispatchDayChange(server, world, currentDay);
            long now = System.currentTimeMillis();
            long elapsedTicks = 0L;
            if (lastRealMillis > 0L && now > lastRealMillis) {
               tickCarryMillis += now - lastRealMillis;
               elapsedTicks = tickCarryMillis / 50L;
               tickCarryMillis %= 50L;
            }

            lastRealMillis = now;
            if (!queue.isEmpty()) {
               consumeTicks(elapsedTicks);
               if (queue.isEmpty()) {
                  finishStorm(server, world);
               } else {
                  long total = totalTicks();
                  applyBlindness(world, server, currentDay);
                  if (server.method_3780() % 20 == 0) {
                     displayStormTimer(server, total);
                     applyMobBoosts(server);
                  }

                  if (currentDay >= 50L && !uhcModeActive) {
                     setNaturalRegeneration(server, false);
                     uhcModeActive = true;
                  }

                  if (!world.method_8419() || !world.method_8546()) {
                     world.method_27910(0, (int)Math.min(total, 2147483647L), true, true);
                  }

                  if (server.method_3780() % 400 == 0) {
                     saveQueue(server);
                  }

               }
            }
         }
      }
   }

   private static void consumeTicks(long ticks) {
      while(ticks > 0L && !queue.isEmpty()) {
         StormEntry head = (StormEntry)queue.get(0);
         long used = Math.min(ticks, head.remainingTicks);
         head.remainingTicks -= used;
         ticks -= used;
         if (head.remainingTicks <= 0L) {
            queue.remove(0);
         }
      }

   }

   private static void dispatchDayChange(MinecraftServer server, class_3218 overworld, long currentDay) {
      if (lastDispatchedDay < 0L) {
         lastDispatchedDay = currentDay;
         saveMode(server);
      } else if (currentDay != lastDispatchedDay) {
         boolean natural = currentDay == lastDispatchedDay + 1L;
         lastDispatchedDay = currentDay;
         saveMode(server);
         if (!natural) {
            LOGGER.info("Salto de día a {} (no natural): no se dispara el modo {}", currentDay, mode);
         } else {
            DayListener listener = (DayListener)listeners.get(mode);
            if (listener != null) {
               listener.onDayChange(server, overworld, currentDay);
            }

         }
      }
   }

   private static void finishStorm(MinecraftServer server, class_3218 world) {
      world.method_27910(6000, 0, false, false);
      setNaturalRegeneration(server, true);
      uhcModeActive = false;
      saveQueue(server);
      class_2561 msg = class_2561.method_43473().method_10852(class_2561.method_43470("La Tormenta ha finalizado...").method_27694((style) -> style.method_10977(class_124.field_1080).method_10982(true)));
      server.method_3760().method_43514(msg, false);
      LOGGER.info("Tormenta finalizada");
   }

   private static void applyBlindness(class_3218 world, MinecraftServer server, long day) {
      int bound;
      if (day >= 40L && day < 50L) {
         bound = 10000;
      } else if (day >= 50L && day < 60L) {
         bound = 7500;
      } else {
         if (day < 60L) {
            return;
         }

         bound = 5000;
      }

      for(class_3222 player : server.method_3760().method_14571()) {
         if (player.method_37908().method_27983() == class_1937.field_25179 && world.method_8520(player.method_24515()) && world.method_8311(player.method_24515()) && world.field_9229.method_43048(bound) == 0) {
            player.method_6092(new class_1293(class_1294.field_5919, 1200, 0, false, true));
         }
      }

   }

   private static void applyMobBoosts(MinecraftServer server) {
      if (currentBoostLevel > 0) {
         int amplifier = currentBoostLevel - 1;

         for(class_3218 level : server.method_3738()) {
            for(class_1297 entity : level.method_27909()) {
               if (entity instanceof class_1308) {
                  class_1308 mob = (class_1308)entity;
                  mob.method_6092(new class_1293(class_1294.field_5910, 60, amplifier, false, true));
                  mob.method_6092(new class_1293(class_1294.field_5907, 60, amplifier, false, true));
                  mob.method_6092(new class_1293(class_1294.field_5904, 60, amplifier, false, true));
               }
            }
         }

      }
   }

   private static void displayStormTimer(MinecraftServer server, long totalTicks) {
      if (totalTicks > 0L) {
         String timeString = formatTimer(totalTicks);
         class_2561 timerMessage = class_2561.method_43470("Quedan: " + timeString + " de tormenta").method_27694((style) -> style.method_10977(class_124.field_1080));

         for(class_3222 player : server.method_3760().method_14571()) {
            player.method_43502(timerMessage, true);
         }

      }
   }

   public static String formatTimer(long ticks) {
      long totalSeconds = Math.max(0L, ticks) / 20L;
      long days = totalSeconds / 86400L;
      long hours = totalSeconds % 86400L / 3600L;
      long minutes = totalSeconds % 3600L / 60L;
      long seconds = totalSeconds % 60L;
      String clock = String.format("%02d:%02d:%02d", hours, minutes, seconds);
      return days == 0L ? clock : days + (days == 1L ? " día: " : " días: ") + clock;
   }

   private static void checkAndApplyBuffsEvery25Days(MinecraftServer server, long currentDay) {
      if (currentDay > 0L) {
         long currentCycle = currentDay / 25L;
         if (currentCycle > lastBoostCycle) {
            lastBoostCycle = currentCycle;
            ++currentBoostLevel;
            saveBoost(server);
            LOGGER.info("Ciclo de 25 días alcanzado - Día: {}, Nivel de buffs: {}", currentDay, currentBoostLevel);
         }

      }
   }

   private static long totalTicks() {
      long total = 0L;

      for(StormEntry e : queue) {
         total += e.remainingTicks;
      }

      return total;
   }

   private static void applyWeather(MinecraftServer server) {
      class_3218 overworld = server.method_3847(class_1937.field_25179);
      if (overworld != null) {
         overworld.method_27910(0, (int)Math.min(totalTicks(), 2147483647L), true, true);
      }

   }

   private static void afterQueueChanged(MinecraftServer server) {
      if (queue.isEmpty()) {
         setNaturalRegeneration(server, true);
         uhcModeActive = false;
         class_3218 overworld = server.method_3847(class_1937.field_25179);
         if (overworld != null) {
            overworld.method_27910(6000, 0, false, false);
         }
      } else {
         applyWeather(server);
      }

      saveQueue(server);
   }

   private static void setNaturalRegeneration(MinecraftServer server, boolean enable) {
      ((class_1928.class_4310)server.method_3767().method_20746(class_1928.field_19395)).method_20758(enable, server);
   }

   private static Path file(MinecraftServer server, String name) {
      return server.method_27050(class_5218.field_24188).resolve(name);
   }

   private static void loadAll(MinecraftServer server) {
      loadQueue(server);
      loadMode(server);
      loadBoost(server);
   }

   private static void saveAll(MinecraftServer server) {
      saveQueue(server);
      saveMode(server);
      saveBoost(server);
   }

   private static void loadQueue(MinecraftServer server) {
      queue.clear();
      lastRealMillis = 0L;
      tickCarryMillis = 0L;

      try {
         Path path = file(server, "permadeath_storm_queue.txt");
         if (Files.exists(path, new LinkOption[0])) {
            for(String line : Files.readAllLines(path)) {
               line = line.trim();
               if (!line.isEmpty()) {
                  if (line.startsWith("#savedAt|")) {
                     try {
                        lastRealMillis = Long.parseLong(line.substring("#savedAt|".length()));
                     } catch (NumberFormatException var7) {
                        LOGGER.error("Cabecera inválida en {}: {}", "permadeath_storm_queue.txt", line);
                     }
                  } else {
                     try {
                        String[] f = line.split("\\|");
                        if (f.length >= 5) {
                           queue.add(new StormEntry(StormHandler.StormSource.valueOf(f[0]), f[1], Long.parseLong(f[2]), Long.parseLong(f[3]), Long.parseLong(f[4])));
                        }
                     } catch (Exception e) {
                        LOGGER.error("Línea inválida en {}: {}", new Object[]{"permadeath_storm_queue.txt", line, e});
                     }
                  }
               }
            }

            LOGGER.info("Cola de tormenta cargada: {} entradas, {} ticks", queue.size(), totalTicks());
            return;
         }

         Path legacy = file(server, "permadeath_storm.txt");
         if (Files.exists(legacy, new LinkOption[0])) {
            String[] lines = Files.readString(legacy).trim().split("\n");
            if (lines.length >= 2 && Boolean.parseBoolean(lines[0].trim())) {
               long ticks = Long.parseLong(lines[1].trim());
               if (ticks > 0L) {
                  queue.add(new StormEntry(StormHandler.StormSource.MUERTE_JUGADOR, "(anterior)", DateManager.getCurrentRealDay(), ticks, ticks));
                  LOGGER.info("Tormenta migrada desde el formato viejo: {} ticks", ticks);
               }
            }
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar cola de tormenta", e);
      }

   }

   private static void saveQueue(MinecraftServer server) {
      if (server != null) {
         try {
            Path path = file(server, "permadeath_storm_queue.txt");
            Files.createDirectories(path.getParent());
            StringBuilder sb = new StringBuilder();
            long savedAt = lastRealMillis > 0L ? lastRealMillis : System.currentTimeMillis();
            sb.append("#savedAt|").append(savedAt).append('\n');

            for(StormEntry e : queue) {
               sb.append(e.source.name()).append('|').append(e.playerName).append('|').append(e.day).append('|').append(e.totalTicks).append('|').append(e.remainingTicks).append('\n');
            }

            Files.writeString(path, sb.toString());
         } catch (Exception e) {
            LOGGER.error("Error al guardar cola de tormenta", e);
         }

      }
   }

   private static void loadMode(MinecraftServer server) {
      try {
         Path path = file(server, "permadeath_storm_mode.txt");
         if (Files.exists(path, new LinkOption[0])) {
            String[] lines = Files.readString(path).trim().split("\n");
            if (lines.length >= 1) {
               mode = StormHandler.StormMode.valueOf(lines[0].trim());
            }

            if (lines.length >= 2) {
               lastDispatchedDay = Long.parseLong(lines[1].trim());
            }
         } else {
            mode = StormHandler.StormMode.SIN_TORMENTA;
            lastDispatchedDay = -1L;
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar modo de tormenta", e);
      }

   }

   private static void saveMode(MinecraftServer server) {
      if (server != null) {
         try {
            Path path = file(server, "permadeath_storm_mode.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, mode.name() + "\n" + lastDispatchedDay);
         } catch (Exception e) {
            LOGGER.error("Error al guardar modo de tormenta", e);
         }

      }
   }

   private static void loadBoost(MinecraftServer server) {
      try {
         Path path = file(server, "permadeath_boost.txt");
         if (Files.exists(path, new LinkOption[0])) {
            String[] lines = Files.readString(path).trim().split("\n");
            if (lines.length >= 2) {
               lastBoostCycle = Long.parseLong(lines[0].trim());
               currentBoostLevel = Integer.parseInt(lines[1].trim());
               LOGGER.info("Boosts cargados: ciclo={}, nivel={}", lastBoostCycle, currentBoostLevel);
            }
         } else {
            lastBoostCycle = 0L;
            currentBoostLevel = 0;
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar estado de boosts", e);
      }

   }

   private static void saveBoost(MinecraftServer server) {
      if (server != null) {
         try {
            Path path = file(server, "permadeath_boost.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, lastBoostCycle + "\n" + currentBoostLevel);
         } catch (Exception e) {
            LOGGER.error("Error al guardar estado de boosts", e);
         }

      }
   }

   static {
      mode = StormHandler.StormMode.SIN_TORMENTA;
      lastDispatchedDay = -1L;
      uhcModeActive = false;
      lastBoostCycle = 0L;
      currentBoostLevel = 0;
      serverInstance = null;
      stateLoaded = false;
      lastRealMillis = 0L;
      tickCarryMillis = 0L;
   }

   public static enum StormSource {
      SERIE,
      ALEATORIA,
      MUERTE_JUGADOR;

      // $FF: synthetic method
      private static StormSource[] $values() {
         return new StormSource[]{SERIE, ALEATORIA, MUERTE_JUGADOR};
      }
   }

   public static enum StormMode {
      PERMADEATH,
      ALEATORIO,
      SIN_TORMENTA;

      // $FF: synthetic method
      private static StormMode[] $values() {
         return new StormMode[]{PERMADEATH, ALEATORIO, SIN_TORMENTA};
      }
   }

   public static final class StormEntry {
      public final StormSource source;
      public final String playerName;
      public final long day;
      public final long totalTicks;
      long remainingTicks;

      StormEntry(StormSource source, String playerName, long day, long totalTicks, long remainingTicks) {
         this.source = source;
         this.playerName = playerName;
         this.day = day;
         this.totalTicks = totalTicks;
         this.remainingTicks = remainingTicks;
      }

      public long getRemainingTicks() {
         return this.remainingTicks;
      }
   }

   public interface DayListener {
      void onDayChange(MinecraftServer var1, class_3218 var2, long var3);
   }
}
