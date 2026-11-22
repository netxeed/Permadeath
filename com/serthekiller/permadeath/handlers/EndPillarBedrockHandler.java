package com.serthekiller.permadeath.handlers;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.class_1510;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_3218;

public class EndPillarBedrockHandler {
   private static boolean done = false;
   private static final int REPILLAR_DELAY_TICKS = 40;
   private static final List<PendingRepillar> pending = new ArrayList();

   public static void register() {
      ServerWorldEvents.LOAD.register((ServerWorldEvents.Load)(server, world) -> {
         if (world.method_27983() == class_1937.field_25181) {
            if (!done) {
               done = true;
               replaceEndPillars(world);
            }
         }
      });
      ServerEntityEvents.ENTITY_LOAD.register((ServerEntityEvents.Load)(entity, world) -> {
         if (entity instanceof class_1510) {
            if (world.method_27983() == class_1937.field_25181) {
               pending.add(new PendingRepillar(world, 40));
            }
         }
      });
      ServerTickEvents.END_WORLD_TICK.register((ServerTickEvents.EndWorldTick)(world) -> {
         if (!pending.isEmpty()) {
            if (world.method_27983() == class_1937.field_25181) {
               pending.removeIf((p) -> {
                  if (p.world() != world) {
                     return false;
                  } else if (p.ticksLeft() <= 0) {
                     replaceEndPillars(p.world());
                     return true;
                  } else {
                     pending.set(pending.indexOf(p), new PendingRepillar(p.world(), p.ticksLeft() - 1));
                     return false;
                  }
               });
            }
         }
      });
   }

   private static void replaceEndPillars(class_3218 world) {
      class_2338.class_2339 pos = new class_2338.class_2339();
      int minRadius = 0;
      int maxRadius = 200;

      for(int x = -maxRadius; x <= maxRadius; ++x) {
         for(int z = -maxRadius; z <= maxRadius; ++z) {
            double dist = Math.sqrt((double)(x * x + z * z));
            if (!(dist < (double)minRadius)) {
               int startY = -1;
               int height = 0;

               for(int y = 0; y < 256; ++y) {
                  pos.method_10103(x, y, z);
                  if (world.method_8320(pos).method_27852(class_2246.field_10540)) {
                     if (startY == -1) {
                        startY = y;
                     }

                     ++height;
                  } else if (startY != -1) {
                     if (height >= 10) {
                        convertColumn(world, x, z, startY, height);
                     }

                     startY = -1;
                     height = 0;
                  }
               }

               if (startY != -1 && height >= 40) {
                  convertColumn(world, x, z, startY, height);
               }
            }
         }
      }

   }

   private static void convertColumn(class_3218 world, int x, int z, int startY, int height) {
      class_2338.class_2339 pos = new class_2338.class_2339();

      for(int y = startY; y < startY + height; ++y) {
         pos.method_10103(x, y, z);
         if (world.method_8320(pos).method_27852(class_2246.field_10540)) {
            world.method_8652(pos, class_2246.field_9987.method_9564(), 3);
         }
      }

   }

   private static record PendingRepillar(class_3218 world, int ticksLeft) {
   }
}
