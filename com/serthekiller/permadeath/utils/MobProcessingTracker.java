package com.serthekiller.permadeath.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.class_1308;

public final class MobProcessingTracker {
   private static final String TAG_PREFIX = "pmd_proc_";
   private static final String GENERIC_TAG = "pmd_proc_full";
   private static final Map<class_1308, Set<String>> sessionTags = new WeakHashMap();

   private MobProcessingTracker() {
   }

   public static boolean isSessionProcessed(class_1308 mob, String featureKey) {
      Set<String> tags = (Set)sessionTags.get(mob);
      return tags != null && tags.contains(featureKey);
   }

   public static boolean tryClaimSession(class_1308 mob, String featureKey) {
      Set<String> tags = (Set)sessionTags.computeIfAbsent(mob, (k) -> new HashSet());
      if (tags.contains(featureKey)) {
         return false;
      } else {
         tags.add(featureKey);
         return true;
      }
   }

   public static void clearSessionProcessed(class_1308 mob, String featureKey) {
      Set<String> tags = (Set)sessionTags.get(mob);
      if (tags != null) {
         tags.remove(featureKey);
      }

   }

   public static boolean isProcessed(class_1308 mob, String featureKey) {
      return mob.method_5752().contains("pmd_proc_" + featureKey);
   }

   public static void markProcessed(class_1308 mob, String featureKey) {
      mob.method_5780("pmd_proc_" + featureKey);
   }

   public static boolean tryClaim(class_1308 mob, String featureKey) {
      if (isProcessed(mob, featureKey)) {
         return false;
      } else {
         markProcessed(mob, featureKey);
         return true;
      }
   }

   public static void clearProcessed(class_1308 mob, String featureKey) {
      mob.method_5738("pmd_proc_" + featureKey);
   }

   public static boolean isFullyProcessed(class_1308 mob) {
      return mob.method_5752().contains("pmd_proc_full");
   }

   public static void markFullyProcessed(class_1308 mob) {
      mob.method_5780("pmd_proc_full");
   }

   public static boolean tryClaimFull(class_1308 mob) {
      if (isFullyProcessed(mob)) {
         return false;
      } else {
         markFullyProcessed(mob);
         return true;
      }
   }
}
