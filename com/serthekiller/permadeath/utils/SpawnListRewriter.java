package com.serthekiller.permadeath.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1311;
import net.minecraft.class_1937;
import net.minecraft.class_1959;
import net.minecraft.class_1972;
import net.minecraft.class_2338;
import net.minecraft.class_3218;
import net.minecraft.class_5483;
import net.minecraft.class_6012;
import net.minecraft.class_6880;

public final class SpawnListRewriter {
   private static final double GIANT_SHARE = 0.2;
   private static final int BLAZE_GROUP_MAX = 1;
   private static final int BIOME_PLAINS = 1;
   private static final Map<Key, class_6012<class_5483.class_1964>> CACHE = new ConcurrentHashMap();

   private SpawnListRewriter() {
   }

   public static class_6012<class_5483.class_1964> rewrite(class_3218 level, class_1311 category, class_2338 pos, class_6012<class_5483.class_1964> in) {
      if (level.method_27983() != class_1937.field_25179) {
         return in;
      } else {
         long day = DateManager.getCurrentRealDay();
         int stage = (day >= 30L ? 1 : 0) | (day >= 40L ? 2 : 0) | (day >= 50L ? 4 : 0) | (day >= 60L ? 8 : 0);
         if (stage == 0) {
            return in;
         } else {
            int biomeFlags = 0;
            if (category == class_1311.field_6302 && (stage & 4) != 0) {
               class_6880<class_1959> biome = level.method_23753(pos);
               if (biome.method_40225(class_1972.field_9451)) {
                  biomeFlags |= 1;
               }
            }

            return (class_6012)CACHE.computeIfAbsent(new Key(in, category, stage, biomeFlags), (k) -> build(in, category, stage, biomeFlags));
         }
      }
   }

   private static class_6012<class_5483.class_1964> build(class_6012<class_5483.class_1964> in, class_1311 category, int stage, int biomeFlags) {
      boolean d30 = (stage & 1) != 0;
      boolean d40 = (stage & 2) != 0;
      boolean d50 = (stage & 4) != 0;
      boolean d60 = (stage & 8) != 0;
      List<class_5483.class_1964> out = new ArrayList();

      for(class_5483.class_1964 d : in.method_34994()) {
         class_1299<?> t = d.field_9389;
         int w = d.method_34979().method_34976();
         if (!d30 || t != class_1299.field_28402) {
            if (d30 && t == class_1299.field_6108) {
               out.add(new class_5483.class_1964(class_1299.field_6099, w, 1, 1));
            } else if (d30 && t == class_1299.field_6114) {
               out.add(new class_5483.class_1964(d60 ? class_1299.field_6086 : class_1299.field_6118, w, 3, d.field_9387));
            } else if (d40 && t == class_1299.field_6051) {
               out.add(new class_5483.class_1964(class_1299.field_6117, w, d.field_9388, d.field_9387));
            } else if (d40 && t == class_1299.field_6055) {
               out.add(new class_5483.class_1964(class_1299.field_16281, w, d.field_9388, d.field_9387));
            } else if (d50 && t == class_1299.field_6073) {
               out.add(new class_5483.class_1964(class_1299.field_6062, w, d.field_9388, d.field_9387));
            } else {
               out.add(d);
            }
         }
      }

      if (category == class_1311.field_6302 && d50 && (biomeFlags & 1) != 0) {
         int total = 0;

         for(class_5483.class_1964 e : out) {
            total += e.method_34979().method_34976();
         }

         double p = 0.2;
         int giantWeight = Math.max(1, (int)Math.round((double)total * p / ((double)1.0F - p)));
         out.add(new class_5483.class_1964(class_1299.field_6051, giantWeight, 1, 1));
      }

      return class_6012.method_34988(out);
   }

   public static class_1311 capCategory(class_1297 entity, class_1311 original) {
      class_1299<?> t = entity.method_5864();
      if (t != class_1299.field_6099 && t != class_1299.field_6118 && t != class_1299.field_6086) {
         return original;
      } else if (entity.method_37908().method_27983() != class_1937.field_25179) {
         return original;
      } else if (DateManager.getCurrentRealDay() < 30L) {
         return original;
      } else {
         return t == class_1299.field_6099 ? class_1311.field_6303 : class_1311.field_6300;
      }
   }

   private static record Key(class_6012<class_5483.class_1964> source, class_1311 category, int stage, int biomeFlags) {
   }
}
