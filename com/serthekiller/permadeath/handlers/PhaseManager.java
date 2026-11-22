package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.utils.DateManager;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhaseManager {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathPhaseManager");
   private static final Map<Integer, PhaseHandler> phaseHandlers = new HashMap();
   private static int currentPhase = -1;
   private static boolean transitionActive = false;
   private static int transitionTargetPhase = -1;
   private static int transitionCooldown = 0;
   private static final int TICKS_BETWEEN_PHASE_STEPS = 400;
   private static final int TICKS_BEFORE_FIRST_STEP = 100;
   private static final int TICKS_WARMUP_AFTER_START = 60;
   private static int warmupTicksAccumulated = 0;

   public static void init() {
      registerPhaseHandler(0, new Day0to9Handler());
      registerPhaseHandler(1, new Day10to19Handler());
      registerPhaseHandler(2, new Day20to29Handler());
      registerPhaseHandler(3, new Day30to39Handler());
      registerPhaseHandler(4, new Day40to49Handler());
      registerPhaseHandler(5, new Day50to59Handler());
      registerPhaseHandler(6, new Day60to69Handler());
      registerPhaseHandler(7, new Day70Handler());
      ServerTickEvents.END_WORLD_TICK.register(PhaseManager::checkPhaseChange);
      LOGGER.info("PhaseManager inicializado con {} fases", phaseHandlers.size());
   }

   private static void registerPhaseHandler(int phase, PhaseHandler handler) {
      phaseHandlers.put(phase, handler);
      LOGGER.info("Fase {} registrada: {}", phase, handler.getPhaseName());
   }

   private static void checkPhaseChange(class_3218 world) {
      if (transitionActive) {
         if (currentPhase >= 0) {
            PhaseHandler currentHandler = (PhaseHandler)phaseHandlers.get(currentPhase);
            if (currentHandler != null) {
               try {
                  currentHandler.onTick(world);
               } catch (Exception e) {
                  LOGGER.error("Error en onTick (transición) de fase {}: {}", new Object[]{currentPhase, e.getMessage(), e});
               }
            }

            if (currentPhase != transitionTargetPhase) {
               ++warmupTicksAccumulated;
            }
         }

         advanceTransition(world);
      } else {
         int newPhase = DateManager.getCurrentPhase();
         if (newPhase != currentPhase) {
            changePhase(world, newPhase);
         }

         if (currentPhase >= 0) {
            PhaseHandler handler = (PhaseHandler)phaseHandlers.get(currentPhase);
            if (handler != null) {
               try {
                  handler.onTick(world);
               } catch (Exception e) {
                  LOGGER.error("Error en onTick de fase {}: {}", new Object[]{currentPhase, e.getMessage(), e});
               }
            }
         }
      }

   }

   private static void advanceTransition(class_3218 world) {
      if (warmupTicksAccumulated >= 60 || currentPhase == transitionTargetPhase) {
         if (transitionCooldown > 0) {
            --transitionCooldown;
         } else if (currentPhase == transitionTargetPhase) {
            transitionActive = false;
            transitionTargetPhase = -1;
            warmupTicksAccumulated = 0;
            MinecraftServer server = world.method_8503();
            if (server != null) {
               PhaseHandler finalHandler = (PhaseHandler)phaseHandlers.get(currentPhase);
               String finalName = finalHandler != null ? finalHandler.getPhaseName() : "Fase " + currentPhase;
               server.method_3760().method_43514(class_2561.method_43470("§a✓ Carga de fases completada — " + finalName), false);
            }

            LOGGER.info("Transición secuencial completada en fase {}", currentPhase);
         } else {
            int nextPhase = currentPhase + (transitionTargetPhase > currentPhase ? 1 : -1);
            int phasesLeft = Math.abs(transitionTargetPhase - nextPhase);
            MinecraftServer server = world.method_8503();
            if (server != null) {
               PhaseHandler upcoming = (PhaseHandler)phaseHandlers.get(nextPhase);
               String upcomingName = upcoming != null ? upcoming.getPhaseName() : "Fase " + nextPhase;
               String remaining = phasesLeft > 0 ? " §7(" + phasesLeft + " más después de esta)" : " §7(última fase)";
               server.method_3760().method_43514(class_2561.method_43470("§e⏳ Cargando → " + upcomingName + remaining), false);
            }

            warmupTicksAccumulated = 0;
            changePhase(world, nextPhase);
            transitionCooldown = 400;
         }
      }
   }

   public static void startTransitionTo(class_3218 world, int targetPhase) {
      if (!phaseHandlers.containsKey(targetPhase)) {
         LOGGER.warn("Se intentó iniciar transición a una fase inexistente: {}", targetPhase);
      } else if (currentPhase == targetPhase && !transitionActive) {
         MinecraftServer server = world.method_8503();
         if (server != null) {
            PhaseHandler handler = (PhaseHandler)phaseHandlers.get(currentPhase);
            String name = handler != null ? handler.getPhaseName() : "Fase " + currentPhase;
            server.method_3760().method_43514(class_2561.method_43470("§7Ya te encuentras en " + name), false);
         }

      } else {
         if (transitionActive) {
            LOGGER.info("Cancelando transición anterior (objetivo previo: {}) por nueva solicitud a {}", transitionTargetPhase, targetPhase);
         }

         transitionTargetPhase = targetPhase;
         transitionActive = true;
         warmupTicksAccumulated = 0;
         transitionCooldown = 100;
         int totalSteps = Math.abs(targetPhase - currentPhase);
         int estimatedSeconds = 5 + totalSteps * 20;
         MinecraftServer server = world.method_8503();
         if (server != null) {
            server.method_3760().method_43514(class_2561.method_43470("§e⏳ Iniciando carga de §f" + totalSteps + "§e fase(s)... (~§f" + estimatedSeconds + "§e segundos en total)"), false);
         }

         LOGGER.info("Transición secuencial iniciada: {} -> {} ({} pasos, ~{}s)", new Object[]{currentPhase, targetPhase, totalSteps, estimatedSeconds});
      }
   }

   public static boolean isTransitioning() {
      return transitionActive;
   }

   private static void changePhase(class_3218 world, int newPhase) {
      if (currentPhase >= 0) {
         PhaseHandler oldHandler = (PhaseHandler)phaseHandlers.get(currentPhase);
         if (oldHandler != null) {
            try {
               oldHandler.onPhaseEnd(world);
               LOGGER.info("Fase {} finalizada: {}", currentPhase, oldHandler.getPhaseName());
            } catch (Exception e) {
               LOGGER.error("Error en onPhaseEnd de fase {}: {}", new Object[]{currentPhase, e.getMessage(), e});
            }
         }
      }

      currentPhase = newPhase;
      PhaseHandler newHandler = (PhaseHandler)phaseHandlers.get(currentPhase);
      if (newHandler != null) {
         try {
            newHandler.onPhaseStart(world);
            LOGGER.info("Fase {} iniciada: {}", currentPhase, newHandler.getPhaseName());
         } catch (Exception e) {
            LOGGER.error("Error en onPhaseStart de fase {}: {}", new Object[]{currentPhase, e.getMessage(), e});
         }

         if (!transitionActive) {
            MinecraftServer server = world.method_8503();
            if (server != null) {
               server.method_3760().method_43514(class_2561.method_43470("§6⚠ Nueva fase iniciada: §f" + newHandler.getPhaseName()), false);
            }
         }
      }

   }

   public static void reloadPhases() {
      if (transitionActive) {
         transitionActive = false;
         transitionTargetPhase = -1;
         transitionCooldown = 0;
         warmupTicksAccumulated = 0;
         LOGGER.info("Transición cancelada por reloadPhases()");
      }

      int phase = DateManager.getCurrentPhase();
      if (phase != currentPhase) {
         LOGGER.info("Recargando fases - Cambio detectado de {} a {}", currentPhase, phase);
         currentPhase = -1;
      } else {
         LOGGER.info("Fases recargadas - Fase actual: {}", currentPhase);
      }

   }

   public static int getCurrentPhase() {
      return currentPhase;
   }

   public static PhaseHandler getCurrentHandler() {
      return (PhaseHandler)phaseHandlers.get(currentPhase);
   }

   public interface PhaseHandler {
      void onPhaseStart(class_3218 var1);

      void onPhaseEnd(class_3218 var1);

      void onTick(class_3218 var1);

      String getPhaseName();
   }
}
