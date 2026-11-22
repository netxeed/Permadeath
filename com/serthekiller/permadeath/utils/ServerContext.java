package com.serthekiller.permadeath.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerContext {
   private static MinecraftServer server;

   public static void register() {
      ServerLifecycleEvents.SERVER_STARTED.register((ServerLifecycleEvents.ServerStarted)(s) -> server = s);
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping)(s) -> server = null);
   }

   public static MinecraftServer get() {
      return server;
   }

   public static boolean isDedicated() {
      return server != null && server.method_3816();
   }

   public static int getPlayerCount() {
      return server != null ? server.method_3760().method_14574() : 0;
   }
}
