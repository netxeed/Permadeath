package com.serthekiller.permadeath.dimension;

import com.serthekiller.permadeath.Cambios.BeginningCurseManager;
import com.serthekiller.permadeath.storm.StormHandler;
import java.util.Optional;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2561;
import net.minecraft.class_2806;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3485;
import net.minecraft.class_3492;
import net.minecraft.class_3499;
import net.minecraft.class_5321;
import net.minecraft.class_5454;

public class TheBeginningEventHandler {
   private static final int PORTAL_BASE_Y = 235;
   private static final int PORTAL_PLAYER_OFFSET_Y = 3;
   private static final int BLESSING_DURATION_TICKS = 864000;

   public static void register() {
      ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(TheBeginningEventHandler::onPlayerChangeWorld);
   }

   private static void onPlayerChangeWorld(class_3222 player, class_3218 origin, class_3218 destination) {
      if (destination.method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
         if (StormHandler.isStormActive()) {
            class_243 targetPos = ensurePortalAndGetSpawn(destination);
            player.method_61275(new class_5454(destination, targetPos, player.method_18798(), player.method_36454(), player.method_36455(), class_5454.field_52247));
            grantFirstEntryBlessingIfNeeded(player);
         }
      } else {
         if (destination.method_27983().equals(class_1937.field_25179) && origin.method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
            class_243 spawnPos = getPlayerSpawnInOverworld(player, destination);
            player.method_61275(new class_5454(destination, spawnPos, class_243.field_1353, player.method_36454(), player.method_36455(), class_5454.field_52247));
         }

      }
   }

   private static void grantFirstEntryBlessingIfNeeded(class_3222 player) {
      BeginningCurseManager curseManager = BeginningCurseManager.get(player.method_5682());
      if (!curseManager.isFirstEntryClaimed()) {
         curseManager.markFirstEntryClaimed();
         player.method_6092(new class_1293(class_1294.field_5907, 864000, 1, false, true, true));
         player.method_5682().method_3760().method_43514(class_2561.method_43470("§c[PERMADEATH] §d" + player.method_7334().getName() + ". Enhorabuena, has recibido la bendición del comienzo por entrar primero a The Beginning. Suerte."), false);
      }
   }

   public static class_243 ensurePortalAndGetSpawn(class_3218 level) {
      level.method_8402(0, 0, class_2806.field_12803, true);
      class_2338 portalCorner = new class_2338(0, 235, 0);
      if (!isPortalPresent(level, portalCorner)) {
         placePortalStructure(level, portalCorner);
      }

      return new class_243((double)1.5F, (double)238.0F, (double)1.5F);
   }

   private static boolean isPortalPresent(class_3218 level, class_2338 center) {
      for(int dx = -2; dx <= 2; ++dx) {
         for(int dz = -2; dz <= 2; ++dz) {
            if (!level.method_8320(center.method_10069(dx, 1, dz)).method_26215()) {
               return true;
            }
         }
      }

      return false;
   }

   private static void placePortalStructure(class_3218 level, class_2338 anchor) {
      class_3485 mgr = level.method_14183();
      class_2960 nbtId = class_2960.method_60655("permadeath", "begginningportal");
      Optional<class_3499> opt = mgr.method_15094(nbtId);
      if (opt.isPresent()) {
         class_3499 template = (class_3499)opt.get();
         class_2382 size = template.method_15160();
         class_2338 placementPos = anchor.method_10069(-(size.method_10263() / 2), 0, -(size.method_10260() / 2));
         class_3492 settings = (new class_3492()).method_15125(class_2415.field_11302).method_15123(class_2470.field_11467).method_15133(false);
         template.method_15172(level, placementPos, placementPos, settings, level.method_8409(), 3);
      } else {
         placeFallbackPlatform(level, anchor);
      }

   }

   private static void placeFallbackPlatform(class_3218 level, class_2338 center) {
      for(int dx = -3; dx <= 3; ++dx) {
         for(int dz = -3; dz <= 3; ++dz) {
            level.method_8652(center.method_10069(dx, 0, dz), class_2246.field_10286.method_9564(), 3);
         }
      }

      for(int dx = -3; dx <= 3; ++dx) {
         level.method_8652(center.method_10069(dx, 1, -3), class_2246.field_10505.method_9564(), 3);
         level.method_8652(center.method_10069(dx, 1, 3), class_2246.field_10505.method_9564(), 3);
      }

      for(int dz = -2; dz <= 2; ++dz) {
         level.method_8652(center.method_10069(-3, 1, dz), class_2246.field_10505.method_9564(), 3);
         level.method_8652(center.method_10069(3, 1, dz), class_2246.field_10505.method_9564(), 3);
      }

   }

   public static class_243 getPlayerSpawnInOverworld(class_3222 player, class_3218 overworld) {
      class_5321<class_1937> respawnDim = player.method_26281();
      class_2338 respawnPos = player.method_26280();
      if (respawnPos != null && respawnDim.equals(class_1937.field_25179)) {
         class_5454 respawn = player.method_60590(false, class_5454.field_52245);
         if (!respawn.comp_2821().equals(class_243.field_1353)) {
            return respawn.comp_2821();
         }
      }

      class_2338 worldSpawn = overworld.method_43126();
      return new class_243((double)worldSpawn.method_10263() + (double)0.5F, (double)worldSpawn.method_10264(), (double)worldSpawn.method_10260() + (double)0.5F);
   }
}
