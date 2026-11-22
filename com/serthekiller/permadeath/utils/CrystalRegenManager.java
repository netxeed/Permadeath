package com.serthekiller.permadeath.utils;

import com.serthekiller.permadeath.handlers.Day30to39Handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1510;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2398;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class CrystalRegenManager {
   private static final int CHECK_INTERVAL = 20;
   private static final int MIN_DELAY = 100;
   private static final int DELAY_RANGE = 100;
   private static final int MIN_INTERVAL = 1200;
   private static long nextRegenAllowedTime = 0L;
   private static final Map<String, class_2338> registeredCrystals = new HashMap();
   private static final Set<String> missingKeys = new HashSet();
   private static String regenKey = null;
   private static int regenTicks = 0;

   public static void register(class_3218 world, class_2338 pos) {
      registeredCrystals.putIfAbsent(getKey(pos, world), pos);
   }

   public static void reset() {
      registeredCrystals.clear();
      missingKeys.clear();
      regenKey = null;
      regenTicks = 0;
      nextRegenAllowedTime = 0L;
   }

   public static void tick(class_3218 world) {
      if (world.method_8510() % 20L == 0L) {
         if (isDragonAlive(world) && DateManager.getCurrentRealDay() <= 70L) {
            Set<String> current = new HashSet();

            for(class_1297 e : world.method_27909()) {
               if (e instanceof class_1511) {
                  current.add(getKey(e.method_24515(), world));
               }
            }

            for(Map.Entry<String, class_2338> entry : registeredCrystals.entrySet()) {
               String key = (String)entry.getKey();
               if (!current.contains(key)) {
                  if (missingKeys.add(key)) {
                     spawnEnderGhast(world, (class_2338)entry.getValue());
                  }
               } else {
                  missingKeys.remove(key);
               }
            }

            processRegeneration(world, current.size());
         } else {
            missingKeys.clear();
            regenKey = null;
         }
      }
   }

   private static void processRegeneration(class_3218 world, int alive) {
      long now = world.method_8510();
      if (regenKey != null) {
         regenTicks -= 20;
         if (regenTicks <= 0) {
            class_2338 pos = (class_2338)registeredCrystals.get(regenKey);
            String key = regenKey;
            regenKey = null;
            if (pos != null && !crystalExistsAt(world, pos)) {
               regenerateCrystal(world, pos);
               nextRegenAllowedTime = now + 1200L;
            }

            missingKeys.remove(key);
         }
      } else if (!missingKeys.isEmpty()) {
         if (now >= nextRegenAllowedTime) {
            double chance = (double)1.0F - (double)alive / (double)registeredCrystals.size();
            if (world.field_9229.method_43058() < chance) {
               List<String> list = new ArrayList(missingKeys);
               regenKey = (String)list.get(world.field_9229.method_43048(list.size()));
               regenTicks = 100 + world.field_9229.method_43048(100);
            }

         }
      }
   }

   private static boolean crystalExistsAt(class_3218 world, class_2338 pos) {
      for(class_1297 e : world.method_27909()) {
         if (e instanceof class_1511 c) {
            if (c.method_24515().equals(pos)) {
               return true;
            }
         }
      }

      return false;
   }

   private static String getKey(class_2338 pos, class_3218 world) {
      String var10000 = pos.method_23854();
      return var10000 + "_" + String.valueOf(world.method_27983().method_29177());
   }

   private static boolean isDragonAlive(class_3218 world) {
      for(class_1297 e : world.method_27909()) {
         if (e instanceof class_1510 d) {
            if (d.method_5805()) {
               return true;
            }
         }
      }

      return false;
   }

   private static void spawnEnderGhast(class_3218 world, class_2338 pos) {
      Day30to39Handler.spawnEnderGhast(world, (double)pos.method_10263() + (double)0.5F, (double)pos.method_10264(), (double)pos.method_10260() + (double)0.5F);
      world.method_8396((class_1657)null, pos, class_3417.field_14792, class_3419.field_15250, 100.0F, 2.0F);
   }

   private static void regenerateCrystal(class_3218 world, class_2338 position) {
      for(class_2338 check : class_2338.method_10097(position.method_10069(-1, 0, -1), position.method_10069(1, 1, 1))) {
         if (world.method_8320(check).method_27852(class_2246.field_10036)) {
            world.method_8650(check.method_10062(), false);
         }
      }

      class_1511 newCrystal = new class_1511(class_1299.field_6110, world);
      newCrystal.method_5814((double)position.method_10263() + (double)0.5F, (double)position.method_10264(), (double)position.method_10260() + (double)0.5F);
      newCrystal.method_5646();
      class_2487 tag = new class_2487();
      newCrystal.method_5647(tag);
      tag.method_10556("PermadeathHasRegenerated", true);
      tag.method_10575("Fire", (short)0);
      newCrystal.method_5651(tag);
      world.method_8649(newCrystal);
      world.method_65096(class_2398.field_11214, (double)position.method_10263() + (double)0.5F, (double)(position.method_10264() + 1), (double)position.method_10260() + (double)0.5F, 50, (double)0.5F, (double)1.0F, (double)0.5F, 0.1);
      world.method_8396((class_1657)null, position, class_3417.field_14981, class_3419.field_15245, 1.0F, 1.0F);
   }
}
