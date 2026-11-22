package com.serthekiller.permadeath.storm;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.serthekiller.permadeath.commands.PermadeathCommands;
import com.serthekiller.permadeath.utils.DateManager;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.server.MinecraftServer;

public class StormCommands {
   private static final int MAX_ENTRIES_SHOWN = 12;
   private static final DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
   private static final Predicate<class_2168> OP;
   private static final SuggestionProvider<class_2168> PLAYER_SUGGESTIONS;
   private static final SuggestionProvider<class_2168> DAY_SUGGESTIONS;

   public static LiteralArgumentBuilder<class_2168> build() {
      return (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("storm").then(((LiteralArgumentBuilder)class_2170.method_9247("reset").requires(OP)).executes(PermadeathCommands::executeResetstorm))).then(((LiteralArgumentBuilder)class_2170.method_9247("info").requires(PermadeathCommands.restrictedRequire())).executes(StormCommands::executeInfo))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("mode").requires(PermadeathCommands.restrictedRequire())).executes(StormCommands::executeModeStatus)).then(class_2170.method_9247("permadeath").executes((ctx) -> executeSetMode(ctx, StormHandler.StormMode.PERMADEATH)))).then(class_2170.method_9247("aleatorio").executes((ctx) -> executeSetMode(ctx, StormHandler.StormMode.ALEATORIO)))).then(class_2170.method_9247("sin_tormenta").executes((ctx) -> executeSetMode(ctx, StormHandler.StormMode.SIN_TORMENTA))))).then(((LiteralArgumentBuilder)class_2170.method_9247("subtract").requires(OP)).then(class_2170.method_9244("jugador", StringArgumentType.string()).suggests(PLAYER_SUGGESTIONS).then(class_2170.method_9244("dia", IntegerArgumentType.integer(0)).suggests(DAY_SUGGESTIONS).executes(StormCommands::executeSubtract))));
   }

   private static int executeInfo(CommandContext<class_2168> context) {
      StormHandler.StormMode mode = StormHandler.getMode();
      StringBuilder sb = new StringBuilder("§6=== Tormenta ===\n");
      sb.append("§7Modo: §f").append(modeLabel(mode)).append("\n");
      List<StormHandler.StormEntry> entries = StormHandler.getAllEntries();
      if (entries.isEmpty()) {
         sb.append("§7Tormenta: §fsin tormenta activa\n");
      } else {
         sb.append("§7Tormenta: §factiva§7, quedan §e").append(formatClock(StormHandler.getStormTicksRemaining())).append("\n");
         int shown = 0;

         for(StormHandler.StormEntry e : entries) {
            if (shown >= 12) {
               sb.append("§8... y ").append(entries.size() - shown).append(" más\n");
               break;
            }

            sb.append("§8- §f").append(sourceLabel(e.source));
            if (!"-".equals(e.playerName)) {
               sb.append("§7 · ").append(e.playerName);
            }

            sb.append("§7 · día ").append(e.day).append(" · quedan ").append(formatClock(e.getRemainingTicks())).append("\n");
            ++shown;
         }
      }

      if (mode == StormHandler.StormMode.ALEATORIO) {
         List<RandomStorm.ScheduledStorm> scheduled = RandomStorm.getScheduled();
         if (scheduled.isEmpty()) {
            sb.append("§7Aleatorias por caer: §fninguna§7 (último día tirado: ").append(RandomStorm.getLastRolledDay()).append(")\n");
         } else {
            sb.append("§7Aleatorias por caer:\n");
            long now = System.currentTimeMillis();

            for(RandomStorm.ScheduledStorm s : scheduled) {
               long untilMillis = s.startMillis() - now;
               String when = untilMillis <= 0L ? "ahora" : "en " + formatMillis(untilMillis);
               String clock = LocalTime.ofInstant(Instant.ofEpochMilli(s.startMillis()), ZoneId.systemDefault()).format(HOUR_FORMAT);
               sb.append("§8- §f").append(when).append("§7 (").append(clock).append(" hora del servidor) · dura ").append(StormHandler.formatDuration(s.ticks())).append("\n");
            }
         }
      }

      String text = sb.toString().stripTrailing();
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(text), false);
      return 1;
   }

   private static int executeModeStatus(CommandContext<class_2168> context) {
      String text = "§7Modo de tormenta actual: §f" + modeLabel(StormHandler.getMode()) + "\n";
      if (DateManager.isSpeedrunEnabled()) {
         text = text + "§8(bloqueado mientras el modo speedrun esté activo)";
      } else {
         text = text + "§7Cambiar: §e/permadeath storm mode <permadeath|aleatorio|sin_tormenta>";
      }

      String message = text;
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(message), false);
      return 1;
   }

   private static int executeSetMode(CommandContext<class_2168> context, StormHandler.StormMode mode) {
      if (DateManager.isSpeedrunEnabled()) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cNo se puede cambiar el modo de tormenta mientras el modo speedrun está activo.\n§7Se queda en §fsin tormenta§7. Desactiva el speedrun con §e/permadeath speedrun off§7."));
         return 0;
      } else {
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         StormHandler.setMode(server, mode);
         String var10000;
         switch (mode) {
            case PERMADEATH -> var10000 = "Las muertes de la serie real se reproducen cuando avance cada día.";
            case ALEATORIO -> var10000 = "La tirada del día se hace en el próximo tick.";
            case SIN_TORMENTA -> var10000 = "Solo caerán tormentas por muertes de jugadores.";
            default -> throw new MatchException((String)null, (Throwable)null);
         }

         String detail = var10000;
         var10000 = modeLabel(mode);
         String text = "§a✓ Modo de tormenta: §f" + var10000 + "\n§7" + detail + "\n§7Lo que ya está en la cola no se toca.";
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(text), true);
         return 1;
      }
   }

   private static int executeSubtract(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      String name = StringArgumentType.getString(context, "jugador");
      int day = IntegerArgumentType.getInteger(context, "dia");
      long removedTicks = StormHandler.removePlayerDeathStorm(server, name, (long)day);
      if (removedTicks < 0L) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cNo hay una tormenta registrada de §f" + name + "§c en el día §f" + day + "§c.\n§7Puede haber terminado ya (las entradas se borran solas al acabar su tormenta)."));
         return 0;
      } else {
         String text = "§a✓ Tormenta de §f" + name + "§a (día " + day + ") restada.\n§7Se quitaron §f" + formatClock(removedTicks) + "§7 de tormenta.";
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(text), true);
         return 1;
      }
   }

   private static String modeLabel(StormHandler.StormMode mode) {
      String var10000;
      switch (mode) {
         case PERMADEATH -> var10000 = "permadeath (serie real)";
         case ALEATORIO -> var10000 = "aleatorio";
         case SIN_TORMENTA -> var10000 = "sin tormenta";
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private static String sourceLabel(StormHandler.StormSource source) {
      String var10000;
      switch (source) {
         case SERIE -> var10000 = "Serie real";
         case ALEATORIA -> var10000 = "Aleatoria";
         case MUERTE_JUGADOR -> var10000 = "Muerte de jugador";
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private static String formatClock(long ticks) {
      return StormHandler.formatTimer(ticks);
   }

   private static String formatMillis(long millis) {
      long totalMinutes = millis / 60000L;
      if (totalMinutes <= 0L) {
         return "<1m";
      } else {
         long h = totalMinutes / 60L;
         long m = totalMinutes % 60L;
         return h > 0L ? h + "h " + m + "m" : m + "m";
      }
   }

   private static String quoteIfNeeded(String name) {
      return name.matches("[A-Za-z0-9_.+-]+") ? name : "\"" + name + "\"";
   }

   static {
      OP = PermadeathCommands.OP_ONLY;
      PLAYER_SUGGESTIONS = (context, builder) -> {
         Set<String> names = new LinkedHashSet();

         for(StormHandler.StormEntry e : StormHandler.getEntries(StormHandler.StormSource.MUERTE_JUGADOR)) {
            names.add(quoteIfNeeded(e.playerName));
         }

         return class_2172.method_9265(names, builder);
      };
      DAY_SUGGESTIONS = (context, builder) -> {
         String name = StringArgumentType.getString(context, "jugador");
         List<String> days = new ArrayList();

         for(StormHandler.StormEntry e : StormHandler.getEntries(StormHandler.StormSource.MUERTE_JUGADOR)) {
            if (e.playerName.equalsIgnoreCase(name)) {
               days.add(String.valueOf(e.day));
            }
         }

         return class_2172.method_9265(days, builder);
      };
   }
}
