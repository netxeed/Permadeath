package com.serthekiller.permadeath.storm;

import com.serthekiller.permadeath.Cambios.TotemManager;
import com.serthekiller.permadeath.handlers.DeathHandler;
import com.serthekiller.permadeath.utils.DateManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2564;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermadeathStorms {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathStorms");
   private static final String RESOURCE_PATH = "/data/permadeath/realpermadeath.txt";
   private static final String TOTEMS_RESOURCE_PATH = "/data/permadeath/realpermadeath_totems.txt";
   private static final String STATE_FILE = "permadeath_series_state.txt";
   private static final int ANNOUNCE_SPACING_TICKS = 200;
   private static final long LATE_ANNOUNCE_LIMIT_MS = 300000L;
   private static final long TOTEM_MESSAGE_FROM_DAY = 30L;
   private static final long TOTEM_COUNT_FROM_DAY = 40L;
   private static final ZoneId ZONE = ZoneId.systemDefault();
   private static final Pattern DURATION = Pattern.compile("^(?:(\\d+)h)?(?:(\\d+)m)?$", 2);
   private static final List<RealDeath> DEATHS = new ArrayList();
   private static final List<TotemEvent> TOTEMS = new ArrayList();
   private static final List<PendingAction> pending = new ArrayList();
   private static long lastCheckMillis = -1L;
   private static MinecraftServer loadedFor = null;

   public static void init() {
      loadDeaths();
      loadTotems();
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(server) -> {
         if (loadedFor == server) {
            saveState(server);
         }

         pending.clear();
         lastCheckMillis = -1L;
         loadedFor = null;
      });
      ServerTickEvents.END_SERVER_TICK.register(PermadeathStorms::tick);
   }

   private static void tick(MinecraftServer server) {
      if (loadedFor != server) {
         loadState(server);
      }

      tickPending(server);
      if (server.method_3780() % 20 == 0) {
         long now = System.currentTimeMillis();
         boolean permadeath = StormHandler.getMode() == StormHandler.StormMode.PERMADEATH;
         int fired = 0;
         if (permadeath && lastCheckMillis >= 0L && now > lastCheckMillis) {
            fired = fireBetween(server, lastCheckMillis, now);
         }

         lastCheckMillis = now;
         if (fired > 0 || server.method_3780() % 1200 == 0) {
            saveState(server);
         }

      }
   }

   private static int fireBetween(MinecraftServer server, long from, long to) {
      long currentDay = DateManager.getCurrentRealDay();
      int fired = 0;

      for(TotemEvent ev : TOTEMS) {
         if (ev.death().day() <= currentDay) {
            long epoch = epochOf(ev.death(), currentDay) - (long)ev.secondsBefore() * 1000L;
            if (epoch > from && epoch <= to && to - epoch <= 300000L) {
               sendTotemMessages(server, ev);
               ++fired;
            }
         }
      }

      List<Due> due = new ArrayList();

      for(RealDeath death : DEATHS) {
         if (death.day() <= currentDay) {
            long epoch = epochOf(death, currentDay);
            if (epoch > from && epoch <= to) {
               due.add(new Due(death, epoch));
            }
         }
      }

      due.sort((a, b) -> Long.compare(a.epochMillis(), b.epochMillis()));
      int index = 0;

      for(Due d : due) {
         fireDeath(server, d.death(), to - d.epochMillis(), index++);
      }

      return fired + due.size();
   }

   private static long epochOf(RealDeath death, long currentDay) {
      LocalDate date = LocalDate.now(ZONE).minusDays(currentDay - death.day());
      return date.atTime(death.time()).atZone(ZONE).toInstant().toEpochMilli();
   }

   private static void fireDeath(MinecraftServer server, RealDeath death, long lateMillis, int index) {
      long elapsedTicks = Math.max(0L, lateMillis) / 50L;
      StormHandler.StormEntry entry = StormHandler.addStorm(server, StormHandler.StormSource.SERIE, death.name(), death.day(), death.ticks(), elapsedTicks);
      if (entry == null) {
         LOGGER.info("Muerte de la serie de {} (día {}): su tormenta ya había terminado", death.name(), death.day());
      } else {
         if (lateMillis <= 300000L) {
            pending.add(new PendingAction(index * 200, announceAction(death)));
         }

         LOGGER.info("Muerte de la serie de {} (día {}) reproducida", death.name(), death.day());
      }
   }

   private static Consumer<MinecraftServer> announceAction(RealDeath death) {
      return (server) -> DeathHandler.announceDeath(server, death.name(), death.message(), death.ticks(), death.cause());
   }

   private static void sendTotemMessages(MinecraftServer server, TotemEvent ev) {
      RealDeath death = ev.death();
      String name = death.name();
      long day = death.day();
      if (day >= 30L && ev.probability() != null) {
         int threshold = TotemManager.failThresholdForDay(day);
         boolean failed = ev.probability() >= threshold;
         int shownProbability = failed ? threshold : ev.probability();
         String totemsText = day >= 40L ? totemsText(ev.count()) : "un tótem";
         class_2561 message = class_2561.method_43470(name + " ha consumido " + totemsText + ", (Probabilidad " + shownProbability + (failed ? " = " : " != ") + threshold + ")").method_27692(class_124.field_1080);
         server.method_3760().method_43514(message, false);
      }

      if (ev.achievement()) {
         class_2561 title = class_2564.method_10885(class_2561.method_43471("advancements.adventure.totem_of_undying.title")).method_27692(class_124.field_1060);
         class_2561 achievement = class_2561.method_43469("chat.type.advancement.goal", new Object[]{class_2561.method_43470(name), title});
         server.method_3760().method_43514(achievement, false);
      }

   }

   private static String totemsText(int count) {
      String var10000;
      switch (count) {
         case 2 -> var10000 = "dos tótems";
         case 3 -> var10000 = "tres tótems";
         default -> var10000 = "un tótem";
      }

      return var10000;
   }

   public static int triggerDay(MinecraftServer server, long day) {
      int index = 0;

      for(RealDeath death : DEATHS) {
         if (death.day() == day) {
            StormHandler.addStorm(server, StormHandler.StormSource.SERIE, death.name(), death.day(), death.ticks());
            List<TotemEvent> totems = new ArrayList();
            int maxSeconds = 0;

            for(TotemEvent ev : TOTEMS) {
               if (ev.death() == death) {
                  totems.add(ev);
                  maxSeconds = Math.max(maxSeconds, ev.secondsBefore());
               }
            }

            int deathDelay = index * 200 + maxSeconds * 20;

            for(TotemEvent ev : totems) {
               pending.add(new PendingAction(deathDelay - ev.secondsBefore() * 20, (s) -> sendTotemMessages(s, ev)));
            }

            pending.add(new PendingAction(deathDelay, announceAction(death)));
            ++index;
         }
      }

      return index;
   }

   private static void tickPending(MinecraftServer server) {
      if (!pending.isEmpty()) {
         Iterator<PendingAction> it = pending.iterator();
         List<PendingAction> ready = new ArrayList();

         while(it.hasNext()) {
            PendingAction p = (PendingAction)it.next();
            --p.delayTicks;
            if (p.delayTicks <= 0) {
               it.remove();
               ready.add(p);
            }
         }

         for(PendingAction p : ready) {
            p.action.accept(server);
         }

      }
   }

   private static void loadState(MinecraftServer server) {
      loadedFor = server;
      lastCheckMillis = -1L;

      try {
         Path path = server.method_27050(class_5218.field_24188).resolve("permadeath_series_state.txt");
         if (Files.exists(path, new LinkOption[0])) {
            lastCheckMillis = Long.parseLong(Files.readString(path).trim());
         }
      } catch (Exception e) {
         LOGGER.error("Error al cargar {}", "permadeath_series_state.txt", e);
      }

   }

   private static void saveState(MinecraftServer server) {
      if (server != null && lastCheckMillis >= 0L) {
         try {
            Path path = server.method_27050(class_5218.field_24188).resolve("permadeath_series_state.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, String.valueOf(lastCheckMillis));
         } catch (Exception e) {
            LOGGER.error("Error al guardar {}", "permadeath_series_state.txt", e);
         }

      }
   }

   private static List<String> readResourceLines(String resourcePath) {
      try {
         InputStream in = PermadeathStorms.class.getResourceAsStream(resourcePath);

         BufferedReader reader;
         label55: {
            Object var5;
            try {
               if (in == null) {
                  reader = null;
                  break label55;
               }

               reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
               List<String> lines = new ArrayList();

               String line;
               while((line = reader.readLine()) != null) {
                  lines.add(line);
               }

               var5 = lines;
            } catch (Throwable var7) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (in != null) {
               in.close();
            }

            return (List<String>)var5;
         }

         if (in != null) {
            in.close();
         }

         return reader;
      } catch (Exception e) {
         LOGGER.error("Error al leer {}", resourcePath, e);
         return null;
      }
   }

   private static void loadDeaths() {
      DEATHS.clear();
      List<String> lines = readResourceLines("/data/permadeath/realpermadeath.txt");
      if (lines == null) {
         LOGGER.warn("No se encontró {} dentro del jar: el modo permadeath no tendrá muertes", "/data/permadeath/realpermadeath.txt");
      } else {
         for(int i = 0; i < lines.size(); ++i) {
            String line = ((String)lines.get(i)).trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
               RealDeath death = parseLine(line, i + 1);
               if (death != null) {
                  DEATHS.add(death);
               }
            }
         }

         LOGGER.info("realpermadeath.txt cargado: {} muertes", DEATHS.size());
      }
   }

   private static void loadTotems() {
      TOTEMS.clear();
      List<String> lines = readResourceLines("/data/permadeath/realpermadeath_totems.txt");
      if (lines == null) {
         LOGGER.info("No hay {} dentro del jar: las muertes no tendrán tótems antes", "/data/permadeath/realpermadeath_totems.txt");
      } else {
         for(int i = 0; i < lines.size(); ++i) {
            String line = ((String)lines.get(i)).trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
               TotemEvent ev = parseTotemLine(line, i + 1);
               if (ev != null) {
                  TOTEMS.add(ev);
               }
            }
         }

         LOGGER.info("realpermadeath_totems.txt cargado: {} consumos de tótem", TOTEMS.size());
      }
   }

   private static TotemEvent parseTotemLine(String line, int lineNumber) {
      String[] f = line.split("\\|", -1);
      if (f.length < 3) {
         LOGGER.error("realpermadeath_totems.txt línea {}: faltan campos (jugador|dia|segundos|probabilidad|cantidad|logro): {}", lineNumber, line);
         return null;
      } else {
         String player = f[0].trim();

         long day;
         int seconds;
         try {
            day = Long.parseLong(f[1].trim());
            seconds = Integer.parseInt(f[2].trim());
            if (seconds < 0) {
               throw new NumberFormatException();
            }
         } catch (NumberFormatException var16) {
            LOGGER.error("realpermadeath_totems.txt línea {}: día o segundos inválidos: {}", lineNumber, line);
            return null;
         }

         Integer probability = null;
         String probabilityText = f.length >= 4 ? f[3].trim() : "";
         if (!probabilityText.isEmpty()) {
            try {
               probability = Integer.parseInt(probabilityText);
               if (probability < 0) {
                  throw new NumberFormatException();
               }
            } catch (NumberFormatException var14) {
               LOGGER.error("realpermadeath_totems.txt línea {}: probabilidad inválida '{}'", lineNumber, probabilityText);
               return null;
            }
         }

         int count = 1;
         String countText = f.length >= 5 ? f[4].trim() : "";
         if (!countText.isEmpty()) {
            try {
               count = Integer.parseInt(countText);
               if (count < 1 || count > 3) {
                  throw new NumberFormatException();
               }
            } catch (NumberFormatException var15) {
               LOGGER.error("realpermadeath_totems.txt línea {}: cantidad inválida '{}' (1, 2 o 3)", lineNumber, countText);
               return null;
            }
         }

         String achievementText = f.length >= 6 ? f[5].trim().toLowerCase() : "";
         boolean achievement = achievementText.equals("si") || achievementText.equals("sí") || achievementText.equals("1") || achievementText.equals("true");
         RealDeath death = findDeath(player, day);
         if (death == null) {
            LOGGER.error("realpermadeath_totems.txt línea {}: no hay una muerte de {} el día {} en realpermadeath.txt", new Object[]{lineNumber, player, day});
            return null;
         } else {
            return new TotemEvent(death, seconds, probability, count, achievement);
         }
      }
   }

   private static RealDeath findDeath(String player, long day) {
      for(RealDeath death : DEATHS) {
         if (death.day() == day && death.name().equalsIgnoreCase(player)) {
            return death;
         }
      }

      return null;
   }

   private static RealDeath parseLine(String line, int lineNumber) {
      String[] f = line.split("\\|", -1);
      if (f.length < 3) {
         LOGGER.error("realpermadeath.txt línea {}: faltan campos (dia|nombre|mensaje|duracion|hora|causa): {}", lineNumber, line);
         return null;
      } else {
         long day;
         try {
            day = Long.parseLong(f[0].trim());
            if (day < 0L) {
               throw new NumberFormatException();
            }
         } catch (NumberFormatException var15) {
            LOGGER.error("realpermadeath.txt línea {}: día inválido '{}'", lineNumber, f[0].trim());
            return null;
         }

         String name = f[1].trim();
         if (name.isEmpty()) {
            LOGGER.error("realpermadeath.txt línea {}: nombre vacío", lineNumber);
            return null;
         } else {
            String rawMessage = f[2].trim();
            String message = rawMessage.isEmpty() ? null : (rawMessage.equals("-") ? "" : rawMessage);
            String durationText = f.length >= 4 ? f[3].trim() : "";
            long ticks;
            if (durationText.isEmpty()) {
               ticks = StormHandler.unitsForDay(day) * StormHandler.ticksPerUnit(day);
            } else {
               ticks = parseDurationTicks(durationText);
               if (ticks <= 0L) {
                  LOGGER.error("realpermadeath.txt línea {}: duración inválida '{}' (usa 2h, 45m o 1h30m)", lineNumber, durationText);
                  return null;
               }
            }

            String timeText = f.length >= 5 ? f[4].trim() : "";
            LocalTime time = LocalTime.MIDNIGHT;
            if (!timeText.isEmpty()) {
               try {
                  time = LocalTime.parse(timeText);
               } catch (DateTimeParseException var14) {
                  LOGGER.error("realpermadeath.txt línea {}: hora inválida '{}' (usa HH:mm o HH:mm:ss)", lineNumber, timeText);
                  return null;
               }
            }

            String cause = f.length >= 6 ? f[5].trim() : "";
            return new RealDeath(day, name, message, ticks, time, cause);
         }
      }
   }

   private static long parseDurationTicks(String text) {
      Matcher m = DURATION.matcher(text);
      if (m.matches() && (m.group(1) != null || m.group(2) != null)) {
         long hours = m.group(1) != null ? Long.parseLong(m.group(1)) : 0L;
         long minutes = m.group(2) != null ? Long.parseLong(m.group(2)) : 0L;
         return hours * 72000L + minutes * 1200L;
      } else {
         return -1L;
      }
   }

   public static record RealDeath(long day, String name, String message, long ticks, LocalTime time, String cause) {
   }

   public static record TotemEvent(RealDeath death, int secondsBefore, Integer probability, int count, boolean achievement) {
   }

   private static final class PendingAction {
      int delayTicks;
      final Consumer<MinecraftServer> action;

      PendingAction(int delayTicks, Consumer<MinecraftServer> action) {
         this.delayTicks = delayTicks;
         this.action = action;
      }
   }

   private static record Due(RealDeath death, long epochMillis) {
   }
}
