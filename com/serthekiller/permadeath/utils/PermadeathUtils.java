package com.serthekiller.permadeath.utils;

import com.serthekiller.permadeath.storm.StormHandler;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;

public class PermadeathUtils {
   public static void init() {
      StormHandler.init();
   }

   public static void clearRuntimeState() {
      StormHandler.clearRuntimeState();
   }

   /** @deprecated */
   @Deprecated
   public static void triggerStormByDay(class_3218 world) {
      StormHandler.addPlayerDeathStorm(world.method_8503(), "(desconocido)");
   }

   /** @deprecated */
   @Deprecated
   public static void resetStorm(MinecraftServer server) {
      StormHandler.clearRemovableStorms(server);
   }

   public static boolean canSleepDuringStorm() {
      return StormHandler.canSleepDuringStorm();
   }

   public static boolean isStormActive() {
      return StormHandler.isStormActive();
   }

   public static long getStormTicksRemaining() {
      return StormHandler.getStormTicksRemaining();
   }

   public static long getCurrentRealDay() {
      return DateManager.getCurrentRealDay();
   }

   public static boolean hasDeathTag(class_3222 player) {
      return player.method_5752().contains("muerte1");
   }
}
