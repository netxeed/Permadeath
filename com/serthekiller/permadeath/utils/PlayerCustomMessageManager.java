package com.serthekiller.permadeath.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_18;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import net.minecraft.class_4284;
import net.minecraft.class_7225;
import net.minecraft.server.MinecraftServer;

public class PlayerCustomMessageManager extends class_18 {
   private static final String DATA_NAME = "permadeath_custom_messages";
   public static final String DEFAULT_MESSAGE = "Ahora por fin descansa en paz...";
   private final Map<UUID, String> messages = new HashMap();

   public static PlayerCustomMessageManager get(MinecraftServer server) {
      class_3218 overworld = server.method_3847(class_1937.field_25179);
      return (PlayerCustomMessageManager)overworld.method_17983().method_17924(new class_18.class_8645<>(PlayerCustomMessageManager::new, PlayerCustomMessageManager::load, (class_4284)null), "permadeath_custom_messages");
   }

   public String getMessage(UUID uuid) {
      return (String)this.messages.getOrDefault(uuid, "Ahora por fin descansa en paz...");
   }

   public void setMessage(UUID uuid, String message) {
      this.messages.put(uuid, message);
      this.method_80();
   }

   public class_2487 method_75(class_2487 tag, class_7225.class_7874 registries) {
      class_2487 entries = new class_2487();
      this.messages.forEach((uuid, msg) -> entries.method_10582(uuid.toString(), msg));
      tag.method_10566("messages", entries);
      return tag;
   }

   public static PlayerCustomMessageManager load(class_2487 tag, class_7225.class_7874 registries) {
      PlayerCustomMessageManager manager = new PlayerCustomMessageManager();
      class_2487 entries = tag.method_10562("messages");

      for(String key : entries.method_10541()) {
         try {
            manager.messages.put(UUID.fromString(key), entries.method_10558(key));
         } catch (IllegalArgumentException var7) {
         }
      }

      return manager;
   }
}
