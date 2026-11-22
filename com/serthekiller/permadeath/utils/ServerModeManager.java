package com.serthekiller.permadeath.utils;

import net.minecraft.class_18;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import net.minecraft.class_4284;
import net.minecraft.class_7225;
import net.minecraft.server.MinecraftServer;

public class ServerModeManager extends class_18 {
   private static final String DATA_NAME = "permadeath_server_mode";
   private boolean restricted = false;

   public static ServerModeManager get(MinecraftServer server) {
      class_3218 overworld = server.method_3847(class_1937.field_25179);
      return (ServerModeManager)overworld.method_17983().method_17924(new class_18.class_8645<>(ServerModeManager::new, ServerModeManager::load, (class_4284)null), "permadeath_server_mode");
   }

   public boolean isRestricted() {
      return this.restricted;
   }

   public void setRestricted(boolean restricted) {
      this.restricted = restricted;
      this.method_80();
   }

   public class_2487 method_75(class_2487 tag, class_7225.class_7874 registries) {
      tag.method_10556("restricted", this.restricted);
      return tag;
   }

   public static ServerModeManager load(class_2487 tag, class_7225.class_7874 registries) {
      ServerModeManager manager = new ServerModeManager();
      manager.restricted = tag.method_10577("restricted");
      return manager;
   }
}
