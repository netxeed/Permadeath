package com.serthekiller.permadeath.dimension;

import com.serthekiller.permadeath.storm.StormHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5454;
import net.minecraft.server.MinecraftServer;

public class StormExpulsionHandler {
   private static boolean wasStormActive = false;

   public static void register() {
      ServerTickEvents.END_SERVER_TICK.register(StormExpulsionHandler::onServerTick);
   }

   private static void onServerTick(MinecraftServer server) {
      boolean nowActive = StormHandler.isStormActive();
      if (nowActive && !wasStormActive) {
         expelPlayersFromBeginning(server);
      }

      wasStormActive = nowActive;
   }

   private static void expelPlayersFromBeginning(MinecraftServer server) {
      for(class_3222 player : server.method_3760().method_14571()) {
         if (player.method_37908().method_27983().equals(ModDimensions.THE_BEGINNING_LEVEL_KEY)) {
            class_3218 overworld = server.method_3847(class_1937.field_25179);
            if (overworld != null) {
               class_243 spawnPos = TheBeginningEventHandler.getPlayerSpawnInOverworld(player, overworld);
               player.method_61275(new class_5454(overworld, spawnPos, class_243.field_1353, player.method_36454(), player.method_36455(), class_5454.field_52247));
               player.method_7353(class_2561.method_43470("§c§lLa tormenta te expulsó de The Beginning."), true);
            }
         }
      }

   }
}
