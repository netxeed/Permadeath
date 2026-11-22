package com.serthekiller.permadeath.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1928;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSetup {
   private static final Logger LOGGER = LoggerFactory.getLogger("PermadeathSetup");
   private static boolean rulesConfigured = false;

   public static void init() {
      ServerTickEvents.START_SERVER_TICK.register(ServerSetup::setup);
   }

   private static void setup(MinecraftServer server) {
      setupServerRules(server);
   }

   private static void setupServerRules(MinecraftServer server) {
      if (!rulesConfigured) {
         class_1928 rules = server.method_30002().method_64395();
         class_1928.class_4310 rule = (class_1928.class_4310)rules.method_20746(class_1928.field_20638);
         ((class_1928.class_4310)rules.method_20746(class_1928.field_19394)).method_20758(false, server);
         rulesConfigured = true;
      }

   }
}
