package com.serthekiller.permadeath.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.serthekiller.permadeath.Cambios.BeginningCurseManager;
import com.serthekiller.permadeath.Cambios.mikecrack;
import com.serthekiller.permadeath.handlers.Day40to49Handler;
import com.serthekiller.permadeath.handlers.PhaseManager;
import com.serthekiller.permadeath.storm.RandomStorm;
import com.serthekiller.permadeath.storm.StormCommands;
import com.serthekiller.permadeath.storm.StormHandler;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.GamePauseManager;
import com.serthekiller.permadeath.utils.PlayerCustomMessageManager;
import com.serthekiller.permadeath.utils.ServerModeManager;
import com.serthekiller.permadeath.utils.SurvivalAchievementManager;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2186;
import net.minecraft.class_2338;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_2583;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5250;
import net.minecraft.class_2558.class_2559;
import net.minecraft.class_2568.class_5247;
import net.minecraft.server.MinecraftServer;

public class PermadeathCommands {
   private static final Pattern SPEEDRUN_PATTERN = Pattern.compile("^(\\d+)([hm])$");
   private static final long CURSE_DURATION_MILLIS = 43200000L;
   private static final int CURSE_DURATION_TICKS = 864000;
   public static final Predicate<class_2168> OP_ONLY = (source) -> source.method_9259(2);

   public static Predicate<class_2168> restrictedRequire() {
      return (source) -> {
         MinecraftServer server = source.method_9211();
         if (server == null) {
            return true;
         } else {
            return !ServerModeManager.get(server).isRestricted() || source.method_9259(2);
         }
      };
   }

   public static void register() {
      CommandRegistrationCallback.EVENT.register((CommandRegistrationCallback)(dispatcher, registryAccess, environment) -> registerPermadeathCommand(dispatcher));
   }

   private static void registerPermadeathCommand(CommandDispatcher<class_2168> dispatcher) {
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("permadeath").then(((LiteralArgumentBuilder)class_2170.method_9247("info").requires(OP_ONLY)).executes(PermadeathCommands::executeInfo))).then(class_2170.method_9247("days").executes(PermadeathCommands::executeDays))).then(((LiteralArgumentBuilder)class_2170.method_9247("changeday").requires(OP_ONLY)).then(class_2170.method_9244("day", IntegerArgumentType.integer(0, 70)).executes(PermadeathCommands::executeChangeDay)))).then(((LiteralArgumentBuilder)class_2170.method_9247("reload").requires(OP_ONLY)).executes(PermadeathCommands::executeReload))).then(((LiteralArgumentBuilder)class_2170.method_9247("reset").requires(OP_ONLY)).executes(PermadeathCommands::executeReset))).then(((LiteralArgumentBuilder)class_2170.method_9247("BeginningLocation").requires(OP_ONLY)).executes(PermadeathCommands::executeBeginningLocation))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("mikecrack").requires(restrictedRequire())).then(class_2170.method_9247("enable").executes(PermadeathCommands::executeMikecrackEnable))).then(class_2170.method_9247("disable").executes(PermadeathCommands::executeMikecrackDisable)))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("speedrun").requires(restrictedRequire())).executes(PermadeathCommands::executeSpeedrunStatus)).then(class_2170.method_9247("off").executes(PermadeathCommands::executeSpeedrunDisable))).then(class_2170.method_9244("valor", StringArgumentType.word()).executes(PermadeathCommands::executeSpeedrunEnable)))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("mensaje").executes(PermadeathCommands::executeMensajeStatus)).then(class_2170.method_9244("texto", StringArgumentType.greedyString()).executes(PermadeathCommands::executeMensajeSet))).then(((LiteralArgumentBuilder)class_2170.method_9247("set").requires(OP_ONLY)).then(class_2170.method_9244("jugador", class_2186.method_9305()).then(class_2170.method_9244("texto", StringArgumentType.greedyString()).executes(PermadeathCommands::executeMensajeSetOther)))))).then(((LiteralArgumentBuilder)class_2170.method_9247("server").requires(OP_ONLY)).executes(PermadeathCommands::executeServerToggle))).then(((LiteralArgumentBuilder)class_2170.method_9247("maldicion").requires(OP_ONLY)).then(class_2170.method_9244("jugador", class_2186.method_9305()).executes(PermadeathCommands::executeMaldicion)))).then(class_2170.method_9247("survival").executes(PermadeathCommands::executeSurvivalStatus))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("game").requires(restrictedRequire())).then(class_2170.method_9247("stop").executes(PermadeathCommands::executeGameStop))).then(class_2170.method_9247("play").executes(PermadeathCommands::executeGamePlay)))).then(StormCommands.build()));
   }

   private static int executeInfo(CommandContext<class_2168> context) {
      long currentDay = DateManager.getCurrentRealDay();
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§6=== Permadeath Mod ===\n§7Modo: Hardcore Permadeath\n§7Sistema de días: REAL WORLD (0-60)\n§7Día actual: §f" + currentDay + "§7/60\n§7\n§7Comandos disponibles:\n§8(OP) = solo OP · (todos) = siempre público · (sin server) = público solo con el modo servidor desactivado\n§e/permadeath info §7- (OP) Información del mod\n§e/permadeath days §7- (todos) Ver día actual y fechas\n§e/permadeath changeday <día> §7- (OP) Cambiar día (0-70)\n§e/permadeath reload §7- (OP) Recargar configuración\n§e/permadeath reset §7- (OP) Resetear sistema\n§e/permadeath BeginningLocation §7- (OP) Coords del portal a the beginning\n§e/permadeath game stop|play §7- (sin server) Pausa/reanuda el paso de los días\n§e/permadeath mikecrack enable|disable §7- (sin server) Cambio de Mikecrack (días 60-69)\n§e/permadeath speedrun <10m|10h> §7- (sin server) Activa cambio de día acelerado\n§e/permadeath speedrun off §7- (sin server) Vuelve al sistema de días reales\n§e/permadeath mensaje <texto> §7- (todos) Cambia tu mensaje personal al morir\n§e/permadeath mensaje set <jugador> <texto> §7- (OP) Cambia el mensaje de otro\n§e/permadeath survival §7- (todos) Progreso del logro Superviviente de los 70 Días\n§e/permadeath server §7- (OP) Activa/desactiva el modo restringido\n§e/permadeath maldicion <jugador> §7- (OP) Maldice al último en entrar a The Beginning\n§e/permadeath storm reset §7- (OP) Quita las tormentas (la de la serie real no se puede borrar)\n§e/permadeath storm info §7- (sin server) Estado de la tormenta y las aleatorias por caer\n§e/permadeath storm mode <permadeath|aleatorio|sin_tormenta> §7- (sin server) Cambia el modo\n§e/permadeath storm subtract <jugador> <día> §7- (OP) Resta la tormenta de una muerte\n"), false);
      return 1;
   }

   private static int executeGameStop(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      if (!GamePauseManager.pause(server)) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl juego ya está en STOP. Usa §e/permadeath game play §cpara continuar."));
         return 0;
      } else {
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Juego en STOP. Los días están congelados."), true);
         return 1;
      }
   }

   private static int executeGamePlay(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      if (!GamePauseManager.resume(server)) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl juego ya está en PLAY."));
         return 0;
      } else {
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Juego en PLAY. Los días vuelven a avanzar."), true);
         return 1;
      }
   }

   private static int executeDays(CommandContext<class_2168> context) {
      String statusInfo = DateManager.getStatusInfo();
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(statusInfo), false);
      return 1;
   }

   private static int executeChangeDay(CommandContext<class_2168> context) {
      if (GamePauseManager.isPaused()) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cNo puedes cambiar el día mientras el juego está en STOP.\n§7Usa §e/permadeath game play §7primero."));
         return 0;
      } else {
         int targetDay = IntegerArgumentType.getInteger(context, "day");
         MinecraftServer serverr = ((class_2168)context.getSource()).method_9211();
         SurvivalAchievementManager.flagChangeDayUsed(serverr);
         DateManager.setCurrentDay(serverr, (long)targetDay);
         class_3218 overworld = ((class_2168)context.getSource()).method_9211().method_30002();
         int targetPhase = DateManager.getCurrentPhase();
         PhaseManager.startTransitionTo(overworld, targetPhase);
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Día cambiado a: §f" + targetDay + "\n§7Fecha de inicio ajustada: §f" + DateManager.formatDate(DateManager.getStartDate()) + "\n§7Cargando fases secuencialmente..."), true);
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         server.method_3760().method_43514(class_2561.method_43470("§6⚠ El servidor ha cambiado al día " + targetDay), false);
         return 1;
      }
   }

   private static int executeReload(CommandContext<class_2168> context) {
      try {
         DateManager.init(((class_2168)context.getSource()).method_9211());
         PhaseManager.reloadPhases();
         ((class_2168)context.getSource()).method_9226(() -> {
            long var10000 = DateManager.getCurrentRealDay();
            return class_2561.method_43470("§a✓ Configuración recargada exitosamente\n§7Día actual: §f" + var10000 + "\n§7Fecha de inicio: §f" + DateManager.formatDate(DateManager.getStartDate()));
         }, true);
         return 1;
      } catch (Exception e) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§c✗ Error al recargar configuración: " + e.getMessage()));
         return 0;
      }
   }

   private static int executeReset(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      class_3218 world = ((class_2168)context.getSource()).method_9225();
      SurvivalAchievementManager.flagStormResetUsed(server);
      server.method_3760().method_14571().forEach((player) -> player.method_5752().remove("muerte1"));
      DateManager.setStartDate(LocalDate.now());
      PhaseManager.reloadPhases();
      StormHandler.fullReset(server);
      RandomStorm.cancelPending(server);
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Permadeath reseteado correctamente\n§7Fecha de inicio: §f" + DateManager.formatDate(DateManager.getStartDate()) + "\n§7Tags de muerte y tormenta limpiados"), true);
      return 1;
   }

   public static int executeResetstorm(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      class_3218 world = ((class_2168)context.getSource()).method_9225();
      SurvivalAchievementManager.flagStormResetUsed(server);
      server.method_3760().method_14571().forEach((player) -> player.method_5752().remove("muerte1"));
      StormHandler.clearRemovableStorms(server);
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Permadeath tormenta reseteado correctamente\n§7Tags de muerte y tormentas quitados (la de la serie real no se puede borrar)"), true);
      return 1;
   }

   private static int executeBeginningLocation(CommandContext<class_2168> context) {
      try {
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         SurvivalAchievementManager.flagBeginningPortalCommandUsed(server);
         Day40to49Handler.PortalState state = Day40to49Handler.PortalState.get(server);
         if (!state.hasSpawned) {
            ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl portal aún no ha sido generado (día 40 requerido)."));
            return 0;
         } else {
            class_2338 pos = state.getPortalPos();
            if (pos == null) {
               ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cLa posición del portal no está guardada (fue generado con una versión anterior del mod). Usa el comando /permadeath reset y espera al día 40 para regenerarlo."));
               return 0;
            } else {
               class_5250 var7 = class_2561.method_43470("§e[TP]");
               class_2583 var8 = class_2583.field_24360;
               class_2558.class_2559 var10004 = class_2559.field_11750;
               int var10005 = pos.method_10263();
               class_5250 tpButton = var7.method_10862(var8.method_10958(new class_2558(var10004, "/tp " + var10005 + " " + pos.method_10264() + " " + pos.method_10260())).method_10949(new class_2568(class_5247.field_24342, class_2561.method_43470("§7Clic para teletransportarte al portal"))));
               class_5250 message = class_2561.method_43470("§6Portal hacia The Beginning:\n§7Coordenadas: §f" + pos.method_23854() + "\n§7(Click para teletransportarse)  ").method_10852(tpButton);
               ((class_2168)context.getSource()).method_9226(() -> message, false);
               return 1;
            }
         }
      } catch (Exception e) {
         class_2168 var10000 = (class_2168)context.getSource();
         String var10001 = e.getClass().getSimpleName();
         var10000.method_9213(class_2561.method_43470("§cError: " + var10001 + " - " + e.getMessage()));
         e.printStackTrace();
         return 0;
      }
   }

   private static int executeMikecrackEnable(CommandContext<class_2168> context) {
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay >= 60L && currentDay <= 69L) {
         if (mikecrack.isEnabled()) {
            ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§eEl cambio de Mikecrack ya está activo."));
            return 0;
         } else {
            MinecraftServer server = ((class_2168)context.getSource()).method_9211();
            mikecrack.enable(server);
            ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a Cambio de Mikecrack ACTIVADO\n"), true);
            server.method_3760().method_43514(class_2561.method_43470("§c ¡CAMBIO DE MIKECRACK ACTIVADO!\n"), false);
            return 1;
         }
      } else {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl cambio de Mikecrack solo puede activarse entre los días §f60§c y §f69§c.\n§7Día actual: §f" + currentDay));
         return 0;
      }
   }

   private static int executeMikecrackDisable(CommandContext<class_2168> context) {
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay >= 60L && currentDay <= 69L) {
         if (!mikecrack.isEnabled()) {
            ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§eEl cambio de Mikecrack ya está desactivado."));
            return 0;
         } else {
            MinecraftServer server = ((class_2168)context.getSource()).method_9211();
            mikecrack.disable(server);
            ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a Cambio de Mikecrack DESACTIVADO\n"), true);
            return 1;
         }
      } else {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl cambio de Mikecrack solo puede desactivarse entre los días §f60§c y §f69§c.\n§7Día actual: §f" + currentDay));
         return 0;
      }
   }

   private static int executeSpeedrunEnable(CommandContext<class_2168> context) {
      String valor = StringArgumentType.getString(context, "valor").toLowerCase(Locale.ROOT);
      Matcher matcher = SPEEDRUN_PATTERN.matcher(valor);
      if (!matcher.matches()) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cFormato inválido: §f" + valor + "\n§7Usa un número seguido de §fh§7 (horas) o §fm§7 (minutos).\n§7Ejemplos: §f/permadeath speedrun 10m §7 o  §f/permadeath speedrun 5h"));
         return 0;
      } else {
         long amount = Long.parseLong(matcher.group(1));
         String unit = matcher.group(2);
         if (amount <= 0L) {
            ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl número debe ser mayor a 0."));
            return 0;
         } else {
            long totalMinutes = unit.equals("h") ? amount * 60L : amount;
            if (totalMinutes >= 1440L) {
               ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl intervalo debe ser MENOR a 24 horas.\n§7(El modo normal ya usa 24 horas reales por día)"));
               return 0;
            } else {
               long intervalTicks = totalMinutes * 20L * 60L;
               SurvivalAchievementManager.flagSpeedrunUsed(((class_2168)context.getSource()).method_9211());
               DateManager.enableSpeedrun(intervalTicks);
               StormHandler.setMode(((class_2168)context.getSource()).method_9211(), StormHandler.StormMode.SIN_TORMENTA);
               class_3218 overworld = ((class_2168)context.getSource()).method_9211().method_30002();
               int targetPhase = DateManager.getCurrentPhase();
               long díaactual = DateManager.getCurrentRealDay();
               PhaseManager.startTransitionTo(overworld, targetPhase);
               ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Modo Speedrun ACTIVADO\n§7Cambio de día cada: §f" + amount + unit + "\n§7Día simulado: §f" + díaactual + "§7/70"), true);
               MinecraftServer server = ((class_2168)context.getSource()).method_9211();
               server.method_3760().method_43514(class_2561.method_43470("§d⚡ ¡MODO SPEEDRUN ACTIVADO! §7Un día pasará cada §f" + amount + unit), false);
               return 1;
            }
         }
      }
   }

   private static int executeSpeedrunDisable(CommandContext<class_2168> context) {
      if (!DateManager.isSpeedrunEnabled()) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§eEl modo speedrun ya está desactivado."));
         return 0;
      } else {
         DateManager.disableSpeedrun();
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Modo Speedrun DESACTIVADO\n§7Volviendo al sistema de días reales.\n§7Día actual: §f" + DateManager.getCurrentRealDay()), true);
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         server.method_3760().method_43514(class_2561.method_43470("§7Modo Speedrun desactivado — el día vuelve a avanzar en 24h reales."), false);
         return 1;
      }
   }

   private static int executeSpeedrunStatus(CommandContext<class_2168> context) {
      if (!DateManager.isSpeedrunEnabled()) {
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§7Modo Speedrun: §cDESACTIVADO\n§7Usa §f/permadeath speedrun <10m|10h> §7para activarlo."), false);
         return 1;
      } else {
         long intervalTicks = DateManager.getSpeedrunIntervalTicks();
         long totalMinutes = intervalTicks / 1200L;
         String display = totalMinutes >= 60L && totalMinutes % 60L == 0L ? totalMinutes / 60L + "h" : totalMinutes + "m";
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§7Modo Speedrun: §aACTIVADO\n§7Cambio de día cada: §f" + display + "\n§7Día actual: §f" + DateManager.getCurrentRealDay() + "§7/70"), false);
         return 1;
      }
   }

   private static int executeMensajeStatus(CommandContext<class_2168> context) {
      class_3222 player;
      try {
         player = ((class_2168)context.getSource()).method_9207();
      } catch (Exception var4) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEste comando solo puede ser usado por un jugador."));
         return 0;
      }

      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      String current = PlayerCustomMessageManager.get(server).getMessage(player.method_5667());
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§7Tu mensaje personal al morir es:\n§f\"" + current + "\"\n§7Usa §e/permadeath mensaje <texto> §7para cambiarlo."), false);
      return 1;
   }

   private static int executeMensajeSet(CommandContext<class_2168> context) {
      class_3222 player;
      try {
         player = ((class_2168)context.getSource()).method_9207();
      } catch (Exception var4) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEste comando solo puede ser usado por un jugador."));
         return 0;
      }

      String texto = StringArgumentType.getString(context, "texto");
      if (texto.length() > 100) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl mensaje es demasiado largo (máximo 100 caracteres)."));
         return 0;
      } else {
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         PlayerCustomMessageManager.get(server).setMessage(player.method_5667(), texto);
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Tu mensaje personal al morir ahora es:\n§f\"" + texto + "\""), false);
         return 1;
      }
   }

   private static int executeMensajeSetOther(CommandContext<class_2168> context) throws CommandSyntaxException {
      class_3222 target = class_2186.method_9315(context, "jugador");
      String texto = StringArgumentType.getString(context, "texto");
      if (texto.length() > 100) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEl mensaje es demasiado largo (máximo 100 caracteres)."));
         return 0;
      } else {
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         PlayerCustomMessageManager.get(server).setMessage(target.method_5667(), texto);
         ((class_2168)context.getSource()).method_9226(() -> {
            String var10000 = target.method_5477().getString();
            return class_2561.method_43470("§a✓ Mensaje personal de " + var10000 + " actualizado a:\n§f\"" + texto + "\"");
         }, true);
         target.method_64398(class_2561.method_43470("§7Un administrador cambió tu mensaje personal al morir a:\n§f\"" + texto + "\""));
         return 1;
      }
   }

   private static int executeServerToggle(CommandContext<class_2168> context) {
      MinecraftServer server = ((class_2168)context.getSource()).method_9211();
      ServerModeManager manager = ServerModeManager.get(server);
      boolean newState = !manager.isRestricted();
      manager.setRestricted(newState);
      ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(newState ? "§a✓ Modo servidor RESTRINGIDO activado.\n§7Los jugadores sin OP solo podrán usar §e/permadeath days§7, §e/permadeath mensaje §7y §e/permadeath survival§7." : "§a✓ Modo servidor restringido DESACTIVADO.\n§7Los jugadores sin OP pueden usar §edays, game, mikecrack, speedrun, mensaje, survival, storm info §7y §estorm mode§7."), true);
      return 1;
   }

   private static int executeMaldicion(CommandContext<class_2168> context) throws CommandSyntaxException {
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay < 50L) {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cLa maldición solo puede usarse a partir del día §f50§c.\n§7Día actual: §f" + currentDay));
         return 0;
      } else {
         class_3222 target = class_2186.method_9315(context, "jugador");
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         BeginningCurseManager curseManager = BeginningCurseManager.get(server);
         curseManager.curse(target.method_5667(), 43200000L);
         target.method_6092(new class_1293(class_1294.field_5909, 864000, 0, false, true, true));
         target.method_6092(new class_1293(class_1294.field_5911, 864000, 0, false, true, true));
         server.method_3760().method_43514(class_2561.method_43470("§c[PERMADEATH] §d" + target.method_7334().getName() + ", ¡Desgracia! Has recibido la maldición de The Beginning por entrar el último. ¡Sufre y muere por lento! NO puedes usar cubos de leche durante 12 horas dentro de Permadeath o morirás al instante."), false);
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470("§a✓ Maldición aplicada a " + target.method_7334().getName() + " (12h reales, sin leche)."), true);
         return 1;
      }
   }

   private static int executeSurvivalStatus(CommandContext<class_2168> context) throws CommandSyntaxException {
      class_1297 var2 = ((class_2168)context.getSource()).method_9228();
      if (var2 instanceof class_3222 player) {
         MinecraftServer server = ((class_2168)context.getSource()).method_9211();
         String info = SurvivalAchievementManager.getProgressInfo(server, player);
         ((class_2168)context.getSource()).method_9226(() -> class_2561.method_43470(info), false);
         return 1;
      } else {
         ((class_2168)context.getSource()).method_9213(class_2561.method_43470("§cEste comando solo lo puede usar un jugador."));
         return 0;
      }
   }
}
