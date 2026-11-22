package com.serthekiller.permadeath.storm;

import com.serthekiller.permadeath.utils.DateManager;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomStorm {
   private static final Logger LOGGER = LoggerFactory.getLogger("RandomStorm");
   private static final String FILE = "permadeath_random_storm.txt";
   private static final int[] WEIGHTS = new int[]{50, 30, 15, 5};
   private static final long DAY_MILLIS = 86400000L;
   private static final List<ScheduledStorm> scheduled = new ArrayList();
   private static long lastRolledDay = -1L;
   private static MinecraftServer loadedFor = null;

   public static void init() {
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> {
         if (loadedFor == server) {
            save(server);
         }

         scheduled.clear();
         lastRolledDay = -1L;
         loadedFor = null;
      });
      ServerTickEvents.END_SERVER_TICK.register(RandomStorm::tick);
   }

   private static void tick(MinecraftServer server) {
      if (loadedFor != server) {
         load(server);
      }

      if (StormHandler.getMode() != StormHandler.StormMode.ALEATORIO) {
         if (!scheduled.isEmpty()) {
            scheduled.clear();
            save(server);
            LOGGER.info("Modo distinto de ALEATORIO: tormentas programadas canceladas");
         }

      } else {
         fireDue(server);
         long day = DateManager.getCurrentRealDay();
         if (day != lastRolledDay) {
            rollDay(server, day);
         }

      }
   }

   private static void fireDue(MinecraftServer server) {
      if (!scheduled.isEmpty()) {
         long now = System.currentTimeMillis();
         long day = DateManager.getCurrentRealDay();
         boolean changed = false;
         Iterator<ScheduledStorm> it = scheduled.iterator();

         while(it.hasNext()) {
            ScheduledStorm s = (ScheduledStorm)it.next();
            if (s.startMillis() <= now) {
               it.remove();
               changed = true;
               long elapsedTicks = (now - s.startMillis()) / 50L;
               StormHandler.StormEntry entry = StormHandler.addStorm(server, StormHandler.StormSource.ALEATORIA, "-", day, s.ticks(), elapsedTicks);
               if (entry != null && elapsedTicks < 40L) {
                  class_2561 msg = class_2561.method_43470("Ha comenzado una tormenta aleatoria con duración de " + StormHandler.formatDuration(s.ticks()) + ".").method_27694((style) -> style.method_10977(class_124.field_1080));
                  server.method_3760().method_43514(msg, false);
               }
            }
         }

         if (changed) {
            save(server);
         }

      }
   }

   public static int rollDay(MinecraftServer server, long day) {
      scheduled.clear();
      lastRolledDay = day;
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int count = rollCount(random);
      long now = System.currentTimeMillis();
      List<Long> starts = new ArrayList();

      for(int i = 0; i < count; ++i) {
         starts.add(now + random.nextLong(86400000L));
      }

      Collections.sort(starts);
      long ticks = StormHandler.unitsForDay(day) * StormHandler.ticksPerUnit(day);

      for(long start : starts) {
         scheduled.add(new ScheduledStorm(start, ticks));
      }

      save(server);
      LOGGER.info("Tirada del día {}: {} tormenta(s) aleatoria(s)", day, count);
      return count;
   }

   public static void cancelPending(MinecraftServer server) {
      scheduled.clear();
      save(server);
   }

   public static int getPendingCount() {
      return scheduled.size();
   }

   public static List<ScheduledStorm> getScheduled() {
      return new ArrayList(scheduled);
   }

   public static long getLastRolledDay() {
      return lastRolledDay;
   }

   private static int rollCount(ThreadLocalRandom random) {
      int total = 0;

      for(int w : WEIGHTS) {
         total += w;
      }

      int roll = random.nextInt(total);
      int cumulative = 0;

      for(int count = 0; count < WEIGHTS.length; ++count) {
         cumulative += WEIGHTS[count];
         if (roll < cumulative) {
            return count;
         }
      }

      return 0;
   }

   private static void load(MinecraftServer server) {
      scheduled.clear();
      lastRolledDay = -1L;
      loadedFor = server;

      try {
         Path path = server.method_27050(class_5218.field_24188).resolve("permadeath_random_storm.txt");
         if (!Files.exists(path, new LinkOption[0])) {
            return;
         }

         List<String> lines = Files.readAllLines(path);
         if (lines.isEmpty()) {
            return;
         }

         lastRolledDay = Long.parseLong(((String)lines.get(0)).trim());

         for(int i = 1; i < lines.size(); ++i) {
            String line = ((String)lines.get(i)).trim();
            if (!line.isEmpty()) {
               try {
                  String[] f = line.split("\\|");
                  scheduled.add(new ScheduledStorm(Long.parseLong(f[0]), Long.parseLong(f[1])));
               } catch (Exception e) {
                  LOGGER.error("Línea inválida en {}: {}", new Object[]{"permadeath_random_storm.txt", line, e});
               }
            }
         }

         LOGGER.info("Tormentas aleatorias cargadas: último día tirado={}, programadas={}", lastRolledDay, scheduled.size());
      } catch (Exception e) {
         LOGGER.error("Error al cargar {}", "permadeath_random_storm.txt", e);
      }

   }

   private static void save(MinecraftServer server) {
      if (server != null) {
         try {
            Path path = server.method_27050(class_5218.field_24188).resolve("permadeath_random_storm.txt");
            Files.createDirectories(path.getParent());
            StringBuilder sb = new StringBuilder();
            sb.append(lastRolledDay).append('\n');

            for(ScheduledStorm s : scheduled) {
               sb.append(s.startMillis()).append('|').append(s.ticks()).append('\n');
            }

            Files.writeString(path, sb.toString());
         } catch (Exception e) {
            LOGGER.error("Error al guardar {}", "permadeath_random_storm.txt", e);
         }

      }
   }

   public static record ScheduledStorm(long startMillis, long ticks) {
   }
}
