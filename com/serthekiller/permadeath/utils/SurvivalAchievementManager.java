package com.serthekiller.permadeath.utils;

import com.serthekiller.permadeath.Cambios.TotemManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_124;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_18;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3468;
import net.minecraft.class_4284;
import net.minecraft.class_7225;
import net.minecraft.class_2558.class_2559;
import net.minecraft.class_2568.class_5247;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivalAchievementManager extends class_18 {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathSurvival70");
   private static final String DATA_NAME = "permadeath_survival_achievement";
   public static final long REQUIRED_DAY = 70L;
   public static final long REQUIRED_PLAYTIME_TICKS = 3240000L;
   public static final int MEDALS_REWARD = 70;
   private static final String DISCORD_INVITE_URL = "https://discord.gg/SH489efx6N";
   private boolean changeDayUsed = false;
   private boolean speedrunUsed = false;
   private boolean beginningPortalCommandUsed = false;
   private boolean stormResetUsed = false;
   private final Map<UUID, PlayerRecord> players = new HashMap();

   public static SurvivalAchievementManager get(MinecraftServer server) {
      class_3218 overworld = server.method_3847(class_1937.field_25179);
      return (SurvivalAchievementManager)overworld.method_17983().method_17924(new class_18.class_8645<>(SurvivalAchievementManager::new, SurvivalAchievementManager::load, (class_4284)null), "permadeath_survival_achievement");
   }

   public class_2487 method_75(class_2487 tag, class_7225.class_7874 registries) {
      tag.method_10556("changeDayUsed", this.changeDayUsed);
      tag.method_10556("speedrunUsed", this.speedrunUsed);
      tag.method_10556("beginningPortalUsed", this.beginningPortalCommandUsed);
      tag.method_10556("stormResetUsed", this.stormResetUsed);
      class_2499 list = new class_2499();

      for(Map.Entry<UUID, PlayerRecord> e : this.players.entrySet()) {
         class_2487 entry = ((PlayerRecord)e.getValue()).save();
         entry.method_25927("uuid", (UUID)e.getKey());
         list.add(entry);
      }

      tag.method_10566("players", list);
      return tag;
   }

   public static SurvivalAchievementManager load(class_2487 tag, class_7225.class_7874 registries) {
      SurvivalAchievementManager m = new SurvivalAchievementManager();
      m.changeDayUsed = tag.method_10577("changeDayUsed");
      m.speedrunUsed = tag.method_10577("speedrunUsed");
      m.beginningPortalCommandUsed = tag.method_10577("beginningPortalUsed");
      m.stormResetUsed = tag.method_10577("stormResetUsed");
      class_2499 list = tag.method_10554("players", 10);

      for(int i = 0; i < list.size(); ++i) {
         class_2487 entry = list.method_10602(i);
         UUID uuid = entry.method_25926("uuid");
         m.players.put(uuid, SurvivalAchievementManager.PlayerRecord.load(entry));
      }

      return m;
   }

   private PlayerRecord getRecord(UUID uuid) {
      return (PlayerRecord)this.players.computeIfAbsent(uuid, (u) -> new PlayerRecord());
   }

   public static void flagChangeDayUsed(MinecraftServer server) {
      SurvivalAchievementManager m = get(server);
      if (!m.changeDayUsed) {
         m.changeDayUsed = true;
         m.method_80();
         LOGGER.warn("/permadeath changeday usado — logro de 70 días descalificado para TODOS los jugadores.");
         broadcastDisqualification(server, "§e/permadeath changeday");
      }

   }

   public static void flagSpeedrunUsed(MinecraftServer server) {
      SurvivalAchievementManager m = get(server);
      if (!m.speedrunUsed) {
         m.speedrunUsed = true;
         m.method_80();
         LOGGER.warn("Modo speedrun usado — logro de 70 días descalificado para TODOS los jugadores.");
         broadcastDisqualification(server, "§e/permadeath speedrun");
      }

   }

   public static void flagBeginningPortalCommandUsed(MinecraftServer server) {
      SurvivalAchievementManager m = get(server);
      if (!m.beginningPortalCommandUsed) {
         m.beginningPortalCommandUsed = true;
         m.method_80();
         LOGGER.warn("Comando de portal manual usado — logro de 70 días descalificado para TODOS los jugadores.");
         broadcastDisqualification(server, "§eel comando de portal manual");
      }

   }

   public static void flagStormResetUsed(MinecraftServer server) {
      SurvivalAchievementManager m = get(server);
      if (!m.stormResetUsed) {
         m.stormResetUsed = true;
         m.method_80();
         LOGGER.warn("/permadeath resetstorm o reset usado — logro de 70 días descalificado para TODOS los jugadores.");
         broadcastDisqualification(server, "§e/permadeath resetstorm §7o §e/permadeath reset");
      }

   }

   private static void broadcastDisqualification(MinecraftServer server, String comando) {
      server.method_3760().method_43514(class_2561.method_43470("§c⚠ " + comando + " §7fue usado — el logro §fSuperviviente de los 70 Días §7ya no se puede conseguir en este mundo."), false);
   }

   public static boolean isWorldDisqualified(MinecraftServer server) {
      SurvivalAchievementManager m = get(server);
      return m.changeDayUsed || m.speedrunUsed || m.beginningPortalCommandUsed || m.stormResetUsed;
   }

   public static void recordDeath(MinecraftServer server, UUID playerUuid) {
      SurvivalAchievementManager m = get(server);
      PlayerRecord r = m.getRecord(playerUuid);
      ++r.totalDeaths;
      m.method_80();
   }

   public static void flagCheatDetected(MinecraftServer server, UUID playerUuid, String detalle) {
      SurvivalAchievementManager m = get(server);
      PlayerRecord r = m.getRecord(playerUuid);
      if (!r.cheatDetected) {
         r.cheatDetected = true;
         m.method_80();
         class_3222 sp = server.method_3760().method_14602(playerUuid);
         String name = sp != null ? sp.method_7334().getName() : playerUuid.toString();
         LOGGER.warn("Jugador {} descalificado del logro de 70 días ({})", name, detalle);
      }

   }

   public static int getDeaths(MinecraftServer server, UUID playerUuid) {
      return get(server).getRecord(playerUuid).totalDeaths;
   }

   public static boolean isCheatFlagged(MinecraftServer server, UUID playerUuid) {
      return get(server).getRecord(playerUuid).cheatDetected;
   }

   public static boolean hasClaimedReward(MinecraftServer server, UUID playerUuid) {
      return get(server).getRecord(playerUuid).rewardClaimed;
   }

   public static void checkAndGrant(MinecraftServer server, class_3222 player) {
      SurvivalAchievementManager m = get(server);
      PlayerRecord record = m.getRecord(player.method_5667());
      if (!record.rewardClaimed) {
         if (!isWorldDisqualified(server)) {
            if (!record.cheatDetected) {
               if (record.totalDeaths <= 0) {
                  if (!DateManager.isSpeedrunEnabled()) {
                     if (DateManager.getCurrentRealDay() >= 70L) {
                        long playTicks = (long)player.method_14248().method_15025(class_3468.field_15419.method_14956(class_3468.field_15417));
                        if (playTicks >= 3240000L) {
                           grantReward(server, player);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void grantReward(MinecraftServer server, class_3222 player) {
      SurvivalAchievementManager m = get(server);
      PlayerRecord record = m.getRecord(player.method_5667());
      record.rewardClaimed = true;
      m.method_80();
      String name = player.method_7334().getName();
      TotemManager.giveSurvivorMedalIfEligible(player);
      player.method_6012();
      player.method_6092(new class_1293(class_1294.field_5924, Integer.MAX_VALUE, 1, true, false, true));
      server.method_3760().method_43514(class_2561.method_43470("§6✦ §e" + name + " §6ha sobrevivido los §f70 días §6de Permadeath sin morir ni una sola vez. §6✦"), false);
      class_2561 congrats = class_2561.method_43470("Felicidades por haber sobrevivido los 70 días, además de un logro personal serás recompensado con el rol de Permadeath en el server de Discord y poder participar en futuros proyectos de Minecraft.").method_27694((style) -> style.method_10977(class_124.field_1060));
      class_2561 discordLink = class_2561.method_43470("§b§n[Click aquí para unirte al Discord]").method_27694((style) -> style.method_10958(new class_2558(class_2559.field_11749, "https://discord.gg/SH489efx6N")).method_10949(new class_2568(class_5247.field_24342, class_2561.method_43470("Abrir invitación de Discord"))));
      player.method_64398(congrats);
      player.method_64398(discordLink);
      player.method_64398(class_2561.method_43470("§7Recuerda: envía una captura de este mensaje al staff del Discord para que te asignen el rol."));
      LOGGER.info("Jugador {} completó el logro Superviviente de los 70 Días.", name);
   }

   public static void tick(MinecraftServer server) {
      if (DateManager.getCurrentRealDay() >= 70L) {
         for(class_3222 player : server.method_3760().method_14571()) {
            checkAndGrant(server, player);
         }

      }
   }

   public static String getProgressInfo(MinecraftServer server, class_3222 player) {
      SurvivalAchievementManager m = get(server);
      PlayerRecord r = m.getRecord(player.method_5667());
      long currentDay = DateManager.getCurrentRealDay();
      long playTicks = (long)player.method_14248().method_15025(class_3468.field_15419.method_14956(class_3468.field_15417));
      double playedHours = (double)playTicks / (double)20.0F / (double)3600.0F;
      double requiredHours = (double)45.0F;
      boolean dayOk = currentDay >= 70L;
      boolean deathsOk = r.totalDeaths == 0;
      boolean worldOk = !m.isWorldDisqualifiedInstance();
      boolean cheatOk = !r.cheatDetected;
      boolean timeOk = playTicks >= 3240000L;
      return String.format(Locale.ROOT, "§6=== Superviviente de los 70 Días ===\n§7Día actual: %s§f%d§7/§f%d\n§7Muertes registradas: %s§f%d\n§7Horas jugadas: %s§f%.1fh§7/§f%.0fh\n§7Mundo sin trampas (changeday/speedrun/resetstorm/portal manual): %s\n§7Sin comandos prohibidos detectados: %s\n§7Recompensa reclamada: %s", dayOk ? "§a" : "§c", currentDay, 70L, deathsOk ? "§a" : "§c", r.totalDeaths, timeOk ? "§a" : "§c", playedHours, requiredHours, worldOk ? "§a✓" : "§c✗", cheatOk ? "§a✓" : "§c✗", r.rewardClaimed ? "§a✓" : "§7No");
   }

   private boolean isWorldDisqualifiedInstance() {
      return this.changeDayUsed || this.speedrunUsed || this.beginningPortalCommandUsed || this.stormResetUsed;
   }

   public static class PlayerRecord {
      public int totalDeaths = 0;
      public boolean cheatDetected = false;
      public boolean rewardClaimed = false;

      class_2487 save() {
         class_2487 t = new class_2487();
         t.method_10569("deaths", this.totalDeaths);
         t.method_10556("cheat", this.cheatDetected);
         t.method_10556("claimed", this.rewardClaimed);
         return t;
      }

      static PlayerRecord load(class_2487 t) {
         PlayerRecord r = new PlayerRecord();
         r.totalDeaths = t.method_10550("deaths");
         r.cheatDetected = t.method_10577("cheat");
         r.rewardClaimed = t.method_10577("claimed");
         return r;
      }
   }
}
