package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_3218;
import net.minecraft.server.MinecraftServer;

public class PvpChanges {
   public static boolean pvpForDay(long day) {
      return day >= 40L && day < 70L;
   }

   public static void applyPvpGamerule(class_3218 world) {
      MinecraftServer server = world.method_8503();
      if (server != null) {
         boolean shouldBePvp = pvpForDay(DateManager.getCurrentRealDay());
         server.method_3815(shouldBePvp);
      }
   }
}
