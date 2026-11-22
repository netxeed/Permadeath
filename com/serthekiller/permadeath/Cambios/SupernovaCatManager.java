package com.serthekiller.permadeath.Cambios;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1451;
import net.minecraft.class_1928;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_1937.class_7867;

public final class SupernovaCatManager {
   public static final String TAG = "SupernovaCat";
   private static final int FUSE_TICKS = 400;
   private static final float EXPLOSION_POWER = 100.0F;
   private static final Map<UUID, Integer> TIMERS = new HashMap();

   private SupernovaCatManager() {
   }

   public static void arm(class_3218 world, class_1451 cat) {
      arm(world, cat, true);
   }

   public static void arm(class_3218 world, class_1451 cat, boolean tag) {
      if (tag) {
         cat.method_5780("SupernovaCat");
      }

      if (!TIMERS.containsKey(cat.method_5667())) {
         class_2338 p = cat.method_24515();
         int var10000 = p.method_10263();
         class_2561 warning = class_2561.method_43470("¡Un gato supernova va a explotar en " + var10000 + ", " + p.method_10264() + ", " + p.method_10260() + "!").method_27692(class_124.field_1061);
         world.method_8503().method_3760().method_43514(warning, false);
         TIMERS.put(cat.method_5667(), world.method_8503().method_3780() + 400);
      }
   }

   public static void tick(class_3218 world) {
      if (!TIMERS.isEmpty()) {
         int now = world.method_8503().method_3780();
         Iterator<Map.Entry<UUID, Integer>> it = TIMERS.entrySet().iterator();

         while(it.hasNext()) {
            Map.Entry<UUID, Integer> entry = (Map.Entry)it.next();
            class_1297 entity = world.method_14190((UUID)entry.getKey());
            if (entity != null) {
               if (entity instanceof class_1451) {
                  class_1451 cat = (class_1451)entity;
                  if (cat.method_5805()) {
                     if (now >= (Integer)entry.getValue()) {
                        explode(world, cat);
                        cat.method_31472();
                        it.remove();
                     }
                     continue;
                  }
               }

               it.remove();
            }
         }

      }
   }

   private static void explode(class_3218 world, class_1451 cat) {
      class_1928.class_4310 drops = (class_1928.class_4310)world.method_64395().method_20746(class_1928.field_19392);
      boolean previous = drops.method_20753();
      drops.method_20758(false, world.method_8503());

      try {
         world.method_8437(cat, cat.method_23317(), cat.method_23318(), cat.method_23321(), 100.0F, class_7867.field_40891);
      } finally {
         drops.method_20758(previous, world.method_8503());
      }

   }

   public static void clear() {
      TIMERS.clear();
   }
}
