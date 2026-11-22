package com.serthekiller.permadeath.handlers;

import com.mojang.authlib.GameProfile;
import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.storm.StormHandler;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.ModSounds;
import com.serthekiller.permadeath.utils.PlayerCustomMessageManager;
import com.serthekiller.permadeath.utils.SurvivalAchievementManager;
import java.util.Date;
import java.util.Set;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.class_124;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_1934;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_2631;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2799;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3336;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_5903;
import net.minecraft.class_5904;
import net.minecraft.class_5905;
import net.minecraft.class_9296;
import net.minecraft.class_2799.class_2800;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathHandler {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathDeath");
   private static final String MUERTE_TAG = "muerte1";
   private static final Set<String> VIEWER_NAME_TITLE_PLAYERS = Set.of("elrichmc");
   private static final String DEFAULT_PERSONAL_MESSAGE = "Ahora por fin descansa en paz...";

   private static class_2561 buildDimensionLabel(class_3222 player) {
      class_5321<class_1937> dimKey = player.method_37908().method_27983();
      String name;
      class_124 color;
      if (dimKey == class_1937.field_25179) {
         name = "Overworld";
         color = class_124.field_1060;
      } else if (dimKey == class_1937.field_25180) {
         name = "Nether";
         color = class_124.field_1079;
      } else if (dimKey == class_1937.field_25181) {
         name = "End";
         color = class_124.field_1074;
      } else if (dimKey == ModDimensions.THE_BEGINNING_LEVEL_KEY) {
         name = "The Beginning";
         color = class_124.field_1064;
      } else {
         name = dimKey.method_29177().toString();
         color = class_124.field_1080;
      }

      return class_2561.method_43470(" [" + name + "]").method_27694((style) -> style.method_10977(color).method_10982(true));
   }

   public static void init() {
      ServerLivingEntityEvents.AFTER_DEATH.register((ServerLivingEntityEvents.AfterDeath)(entity, source) -> {
         if (entity instanceof class_3222 player) {
            handlePlayerDeath(player);
         }

      });
   }

   private static void handlePlayerDeath(class_3222 player) {
      MinecraftServer server = player.method_37908().method_8503();
      if (server != null) {
         if (DateManager.getCurrentRealDay() < 70L) {
            class_3218 world = (class_3218)player.method_37908();
            String playerName = player.method_5477().getString();
            SurvivalAchievementManager.recordDeath(server, player.method_5667());
            player.method_5780("muerte1");
            player.method_7336(class_1934.field_9219);
            sendDeathTitles(server, playerName);
            playDeathSound(server);
            long currentDayForSound = DateManager.getCurrentRealDay();
            if (currentDayForSound >= 30L) {
               broadcastSoundToAllPlayers(server, ModSounds.PERMADEATH, 100.0F, 1.0F);
            }

            createDeathMonument(world, player);
            sendDeathMessages(server, playerName, player);
            scheduleRespawn(player, world, 0);
            banPlayer(server, player);
            StormHandler.StormEntry stormEntry = StormHandler.addPlayerDeathStorm(server, playerName);
            scheduleDeathTrainMessage(server, StormHandler.formatDuration(stormEntry.totalTicks), 100);
            LOGGER.info("Jugador {} ha muerto - Permadeath activado", playerName);
         }
      }
   }

   public static void announceDeath(MinecraftServer server, String playerName, String customMessage, long stormTicks, String cause) {
      if (server != null) {
         sendDeathTitles(server, playerName);
         playDeathSound(server);
         if (DateManager.getCurrentRealDay() >= 30L) {
            broadcastSoundToAllPlayers(server, ModSounds.PERMADEATH, 100.0F, 1.0F);
         }

         String message = customMessage == null ? "Ahora por fin descansa en paz..." : customMessage;
         sendMainAndPersonalMessages(server, playerName, message);
         class_2561 causeMessage = buildCauseMessage(playerName, cause);
         if (causeMessage != null) {
            server.method_3760().method_43514(causeMessage, false);
         }

         scheduleLeftGameMessage(server, playerName, 60);
         scheduleDeathTrainMessage(server, StormHandler.formatDuration(stormTicks), 100);
      }
   }

   private static class_2561 buildCauseMessage(String playerName, String cause) {
      if (cause != null && !cause.isBlank()) {
         String type = cause.trim();
         String attackerText = "";
         int colon = type.indexOf(58);
         if (colon >= 0) {
            attackerText = type.substring(colon + 1).trim();
            type = type.substring(0, colon).trim();
         }

         class_2561 name = class_2561.method_43470(playerName);
         class_2561 attacker = null;
         if (attackerText.startsWith("@")) {
            attacker = class_2561.method_43471("entity.minecraft." + attackerText.substring(1).toLowerCase());
         } else if (!attackerText.isEmpty()) {
            attacker = class_2561.method_43470(attackerText).method_27692(class_124.field_1065);
         }

         class_5250 var10000;
         switch (type.toLowerCase()) {
            case "explosion" -> var10000 = attacker != null ? class_2561.method_43469("death.attack.explosion.player", new Object[]{name, attacker}) : class_2561.method_43469("death.attack.explosion", new Object[]{name});
            case "mob" -> var10000 = attacker != null ? class_2561.method_43469("death.attack.mob", new Object[]{name, attacker}) : class_2561.method_43469("death.attack.generic", new Object[]{name});
            case "fireball" -> var10000 = attacker != null ? class_2561.method_43469("death.attack.fireball", new Object[]{name, attacker}) : class_2561.method_43469("death.attack.generic", new Object[]{name});
            case "fall_assist" -> var10000 = attacker != null ? class_2561.method_43469("death.fell.assist", new Object[]{name, attacker}) : class_2561.method_43469("death.attack.fall", new Object[]{name});
            case "void" -> var10000 = class_2561.method_43469("death.attack.outOfWorld", new Object[]{name});
            case "fall" -> var10000 = class_2561.method_43469("death.attack.fall", new Object[]{name});
            case "drown" -> var10000 = class_2561.method_43469("death.attack.drown", new Object[]{name});
            default -> var10000 = class_2561.method_43470(playerName + ": Muerte por " + cause.trim());
         }

         return var10000;
      } else {
         return null;
      }
   }

   private static void scheduleLeftGameMessage(MinecraftServer server, String playerName, int delayTicks) {
      (new Thread(() -> {
         try {
            Thread.sleep((long)delayTicks * 50L);
            server.execute(() -> {
               class_2561 leftMessage = class_2561.method_43469("multiplayer.player.left", new Object[]{class_2561.method_43470(playerName)}).method_27692(class_124.field_1054);
               server.method_3760().method_43514(leftMessage, false);
            });
         } catch (InterruptedException e) {
            LOGGER.error("Error al programar el mensaje de desconexión", e);
         }

      })).start();
   }

   private static void scheduleDeathTrainMessage(MinecraftServer server, String durationText, int delayTicks) {
      (new Thread(() -> {
         try {
            Thread.sleep((long)delayTicks * 50L);
            server.execute(() -> {
               class_2561 tellrawMessage = class_2561.method_43473().method_10852(class_2561.method_43470("¡Comienza el Death Train con duración de " + durationText + "!").method_27694((style) -> style.method_10977(class_124.field_1061).method_10982(false)));
               server.method_3760().method_43514(tellrawMessage, false);
            });
         } catch (InterruptedException e) {
            LOGGER.error("Error al programar mensaje del Death Train", e);
         }

      })).start();
   }

   private static void scheduleRespawn(class_3222 player, class_3218 world, int delayTicks) {
      MinecraftServer server = player.method_37908().method_8503();
      if (server != null) {
         long currentDay = DateManager.getCurrentRealDay();
         (new Thread(() -> {
            try {
               Thread.sleep((long)delayTicks * 50L);
               server.execute(() -> {
                  try {
                     player.field_13987.method_12068(new class_2799(class_2800.field_12774));
                     if (currentDay >= 30L) {
                        world.method_8396((class_1657)null, player.method_24515(), ModSounds.PERMADEATH, class_3419.field_15250, 100.0F, 1.0F);
                     }
                  } catch (Exception e) {
                     LOGGER.error("Error al hacer respawn automático", e);
                  }

               });
            } catch (InterruptedException e) {
               LOGGER.error("Error en el delay del respawn", e);
            }

         })).start();
      }
   }

   private static void banPlayer(MinecraftServer server, class_3222 player) {
      GameProfile profile = player.method_7334();
      (new Thread(() -> {
         try {
            Thread.sleep(4000L);
            server.execute(() -> {
               class_3336 banEntry = new class_3336(profile, (Date)null, "Permadeath", (Date)null, "Has muerto en Permadeath. Baneo permanente.");
               server.method_3760().method_14563().method_14633(banEntry);
               class_2561 kickMessage = class_2561.method_43470("Has sido PERMABANEADO").method_27694((style) -> style.method_10977(class_124.field_1061));
               class_3222 current = server.method_3760().method_14602(profile.getId());
               if (current != null && current.field_13987 != null) {
                  current.field_13987.method_52396(kickMessage);
               }

            });
         } catch (InterruptedException e) {
            LOGGER.error("Error al programar el ban con delay", e);
         }

      })).start();
   }

   private static void sendDeathTitles(MinecraftServer server, String playerName) {
      boolean showViewerName = VIEWER_NAME_TITLE_PLAYERS.contains(playerName.toLowerCase());

      for(class_3222 p : server.method_3760().method_14571()) {
         class_2561 title = class_2561.method_43470("¡Permadeath!").method_27694((style) -> style.method_10977(class_124.field_1061));
         String shownName = showViewerName ? p.method_5477().getString() : playerName;
         class_2561 subtitle = class_2561.method_43470(shownName + " ha muerto");
         p.field_13987.method_14364(new class_5905(10, 80, 30));
         p.field_13987.method_14364(new class_5904(title));
         p.field_13987.method_14364(new class_5903(subtitle));
      }

   }

   private static void createDeathMonument(class_3218 world, class_3222 player) {
      MinecraftServer server = world.method_8503();
      if (server != null) {
         class_2338 pos = player.method_24515();
         GameProfile profile = player.method_7334();
         float yaw = player.method_36454();
         int rotation = (Math.round((yaw % 360.0F + 360.0F) % 360.0F / 22.5F) + 8) % 16;
         int baseY = Math.max(pos.method_10264() - 1, world.method_31607());
         class_2338 basePos = new class_2338(pos.method_10263(), baseY, pos.method_10260());
         class_2338 skullPos = basePos.method_10086(2);
         (new Thread(() -> {
            try {
               Thread.sleep(150L);
               server.execute(() -> {
                  placeMonumentBlocks(world, basePos, skullPos, rotation, profile);
                  scheduleMonumentRepairCheck(world, basePos, skullPos, rotation, profile);
               });
            } catch (InterruptedException e) {
               LOGGER.error("Error al programar el monumento de muerte", e);
            }

         })).start();
      }
   }

   private static void placeMonumentBlocks(class_3218 world, class_2338 basePos, class_2338 skullPos, int rotation, GameProfile profile) {
      world.method_8652(basePos, class_2246.field_9987.method_9564(), 3);
      world.method_8652(basePos.method_10084(), class_2246.field_10364.method_9564(), 3);
      class_2680 skullState = (class_2680)class_2246.field_10432.method_9564().method_11657(class_2741.field_12532, rotation);
      world.method_8652(skullPos, skullState, 3);
      class_2586 blockEntity = world.method_8321(skullPos);
      if (blockEntity instanceof class_2631 skull) {
         class_9296 resolvableProfile = new class_9296(profile);
         skull.method_11333(resolvableProfile);
         skull.method_5431();
         world.method_8413(skullPos, skull.method_11010(), skull.method_11010(), 3);
      }

   }

   private static void scheduleMonumentRepairCheck(class_3218 world, class_2338 basePos, class_2338 skullPos, int rotation, GameProfile profile) {
      MinecraftServer server = world.method_8503();
      if (server != null) {
         (new Thread(() -> {
            try {
               Thread.sleep(1000L);
               server.execute(() -> {
                  boolean fenceMissing = world.method_8320(basePos.method_10084()).method_26215();
                  boolean skullMissing = world.method_8320(skullPos).method_26215();
                  if (fenceMissing || skullMissing) {
                     LOGGER.info("Reparando monumento de muerte en {} (fue destruido tras colocarlo)", basePos);
                     placeMonumentBlocks(world, basePos, skullPos, rotation, profile);
                     Iterable patt0$temp = world.method_27909();
                     if (patt0$temp instanceof class_1542) {
                        class_1542 itemEntity = (class_1542)patt0$temp;
                        if (itemEntity.method_6983().method_31574(class_1802.field_8575)) {
                           itemEntity.method_31472();
                        }
                     }
                  }

               });
            } catch (InterruptedException e) {
               LOGGER.error("Error al programar la reparación del monumento", e);
            }

         })).start();
      }
   }

   private static void broadcastSoundToAllPlayers(MinecraftServer server, class_3414 sound, float volume, float pitch) {
      for(class_3222 p : server.method_3760().method_14571()) {
         class_3218 playerLevel = p.method_51469();
         playerLevel.method_8396((class_1657)null, p.method_24515(), sound, class_3419.field_15250, volume, pitch);
      }

   }

   private static void playDeathSound(MinecraftServer server) {
      if (server != null) {
         broadcastSoundToAllPlayers(server, class_3417.field_14580, 100.0F, 0.3F);
         scheduleSecondSound(server, 100);
      }
   }

   private static void scheduleSecondSound(MinecraftServer server, int delay) {
      (new Thread(() -> {
         try {
            Thread.sleep((long)delay * 50L);
            server.execute(() -> broadcastSoundToAllPlayers(server, class_3417.field_14721, 100.0F, 1.0F));
         } catch (InterruptedException e) {
            LOGGER.error("Error al programar segundo sonido", e);
         }

      })).start();
   }

   private static void sendDeathMessages(MinecraftServer server, String playerName, class_3222 player) {
      String customMessage = PlayerCustomMessageManager.get(server).getMessage(player.method_5667());
      sendMainAndPersonalMessages(server, playerName, customMessage);
      sendCoordinatesMessage(server, player);
   }

   private static void sendMainAndPersonalMessages(MinecraftServer server, String playerName, String customMessage) {
      class_2561 tellrawMessage = class_2561.method_43473().method_10852(class_2561.method_43470("Este es el comienzo del sufrimiento eterno de ").method_27694((style) -> style.method_10977(class_124.field_1061).method_10982(true))).method_10852(class_2561.method_43470(playerName).method_27694((style) -> style.method_10977(class_124.field_1079).method_10982(true))).method_10852(class_2561.method_43470(". ¡HA SIDO PERMABANEADO!").method_27694((style) -> style.method_10977(class_124.field_1061).method_10982(true)));
      server.method_3760().method_43514(tellrawMessage, false);
      if (customMessage != null && !customMessage.isBlank()) {
         class_2561 personalMessage = class_2561.method_43473().method_10852(class_2561.method_43470(playerName).method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(", ").method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(customMessage).method_27694((style) -> style.method_10977(class_124.field_1080)));
         server.method_3760().method_43514(personalMessage, false);
      }
   }

   private static void sendCoordinatesMessage(MinecraftServer server, class_3222 player) {
      class_2338 pos = player.method_24515();
      class_2561 coordinatesMessage = class_2561.method_43473().method_10852(class_2561.method_43470("Se ha generado una cabeza en ").method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(String.valueOf(pos.method_10263())).method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(" ").method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(String.valueOf(pos.method_10264())).method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(" ").method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(class_2561.method_43470(String.valueOf(pos.method_10260())).method_27694((style) -> style.method_10977(class_124.field_1080))).method_10852(buildDimensionLabel(player));
      server.method_3760().method_43514(coordinatesMessage, false);
   }
}
