package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.PermadeathMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1548;
import net.minecraft.class_1560;
import net.minecraft.class_1613;
import net.minecraft.class_1628;
import net.minecraft.class_1642;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1972;
import net.minecraft.class_2338;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_2902.class_2903;

public class MushroomSpawn {
   private static boolean isEnabled = false;

   public static void enable() {
      if (!isEnabled) {
         ServerTickEvents.END_WORLD_TICK.register(MushroomSpawn::onWorldTick);
         isEnabled = true;
         PermadeathMod.LOGGER.info("Sistema spawn en mushroom activado");
      }

   }

   public static void disable() {
      isEnabled = false;
      PermadeathMod.LOGGER.info("Sistema spawn en mushroom desactivado");
   }

   private static void onWorldTick(class_3218 world) {
      if (world.method_8510() % 60L == 0L) {
         long time = world.method_8532() % 24000L;
         if (time >= 13000L && time <= 23000L) {
            if (world.method_8510() % 100L == 0L) {
               for(class_3222 player : world.method_18456()) {
                  if (world.method_23753(player.method_24515()).method_40225(class_1972.field_9462)) {
                     spawnMobsNearPlayer(player, world);
                  }
               }

            }
         }
      }
   }

   private static void spawnMobsNearPlayer(class_3222 player, class_3218 world) {
      for(int i = 0; i < 2; ++i) {
         class_2338 pos = player.method_24515();
         int offsetX = world.field_9229.method_43048(40) - 20;
         int offsetZ = world.field_9229.method_43048(40) - 20;
         int y = world.method_8624(class_2903.field_13203, pos.method_10263() + offsetX, pos.method_10260() + offsetZ);
         class_2338 spawnPos = new class_2338(pos.method_10263() + offsetX, y, pos.method_10260() + offsetZ);
         if (!(Math.sqrt(player.method_5649((double)spawnPos.method_10263(), (double)spawnPos.method_10264(), (double)spawnPos.method_10260())) < (double)5.0F) && world.method_8320(spawnPos.method_10074()).method_51367() && world.method_8320(spawnPos).method_26215()) {
            class_1297 mob = createRandomHostileMob(world);
            if (mob != null) {
               mob.method_5814((double)spawnPos.method_10263() + (double)0.5F, (double)spawnPos.method_10264(), (double)spawnPos.method_10260() + (double)0.5F);
               world.method_8649(mob);
            }
         }
      }

   }

   private static class_1297 createRandomHostileMob(class_3218 world) {
      int choice = world.field_9229.method_43048(5);
      Object var10000;
      switch (choice) {
         case 0:
            var10000 = new class_1642(class_1299.field_6051, world);
            break;
         case 1:
            class_1613 skeleton = new class_1613(class_1299.field_6137, world);
            skeleton.method_5673(class_1304.field_6173, new class_1799(class_1802.field_8102));
            var10000 = skeleton;
            break;
         case 2:
            var10000 = new class_1628(class_1299.field_6079, world);
            break;
         case 3:
            var10000 = new class_1548(class_1299.field_6046, world);
            break;
         case 4:
            var10000 = new class_1560(class_1299.field_6091, world);
            break;
         default:
            var10000 = null;
      }

      return (class_1297)var10000;
   }
}
