package com.serthekiller.permadeath.Cambios;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.class_1269;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;

public class MilkCurseHandler {
   public static void register() {
      UseItemCallback.EVENT.register((UseItemCallback)(player, world, hand) -> {
         class_1799 stack = player.method_5998(hand);
         if (world.field_9236) {
            return class_1269.field_5811;
         } else if (player instanceof class_3222) {
            class_3222 serverPlayer = (class_3222)player;
            if (stack.method_7909() != class_1802.field_8103) {
               return class_1269.field_5811;
            } else {
               BeginningCurseManager curseManager = BeginningCurseManager.get(serverPlayer.method_5682());
               if (curseManager.isCursed(serverPlayer.method_5667())) {
                  killForDrinkingMilk(serverPlayer);
                  return class_1269.field_5814;
               } else {
                  return class_1269.field_5811;
               }
            }
         } else {
            return class_1269.field_5811;
         }
      });
   }

   public static void registerCleanupTick() {
      ServerTickEvents.END_SERVER_TICK.register((ServerTickEvents.EndTick)(server) -> {
         if (server.method_3780() % 100 == 0) {
            BeginningCurseManager.get(server).clearExpired();
         }
      });
   }

   private static void killForDrinkingMilk(class_3222 player) {
      player.method_64398(class_2561.method_43470("§c[PERMADEATH] §dLa maldición de The Beginning te fulminó por beber leche."));
      class_1937 var2 = player.method_37908();
      if (var2 instanceof class_3218 serverLevel) {
         player.method_5768(serverLevel);
      }

   }
}
