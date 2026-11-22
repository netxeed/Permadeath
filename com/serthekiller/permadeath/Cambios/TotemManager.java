package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.mixin.LivingEntityAccessor;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1665;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_8103;
import net.minecraft.class_9279;
import net.minecraft.class_9334;

public final class TotemManager {
   private static final Map<UUID, ResolvedHit> lastResolved = new ConcurrentHashMap();
   private static final String MEDAL_NBT_KEY = "PermadeathSurvivorMedal";
   private static final String MEDAL_GIVEN_TAG = "permadeath_survivor_medal_given";
   private static final long MEDAL_UNLOCK_DAY = 55L;

   private TotemManager() {
   }

   public static int failThresholdForDay(long day) {
      if (day < 40L) {
         return 99;
      } else {
         return day < 50L ? 97 : 95;
      }
   }

   private static String TotemsUsed(long day) {
      if (day < 40L) {
         return "un";
      } else {
         return day < 60L ? "dos" : "tres";
      }
   }

   private static int multiTotemMessageThresholdForDay(long day) {
      if (day < 40L) {
         return 999;
      } else {
         return day < 60L ? 2 : 3;
      }
   }

   private static class_2561 buildSurvivorMedalName() {
      class_2583 bracketStyle = class_2583.field_24360.method_10977(class_124.field_1079).method_10982(true).method_10978(false);
      class_2583 skullStyle = class_2583.field_24360.method_10977(class_124.field_1061).method_10982(false).method_10978(false);
      class_2583 titleStyle = class_2583.field_24360.method_10977(class_124.field_1065).method_10982(true).method_10978(false);
      class_2583 obfStyle = class_2583.field_24360.method_10977(class_124.field_1068).method_10982(false).method_10978(false).method_36141(true);
      class_2583 spaceStyle = class_2583.field_24360.method_10982(false).method_10978(false);
      return class_2561.method_43470("[").method_27696(bracketStyle).method_10852(class_2561.method_43470("☠").method_27696(skullStyle)).method_10852(class_2561.method_43470("]").method_27696(bracketStyle)).method_10852(class_2561.method_43470(" ").method_27696(spaceStyle)).method_10852(class_2561.method_43470("0").method_27696(obfStyle)).method_10852(class_2561.method_43470(" Medalla de Superviviente ").method_27696(titleStyle)).method_10852(class_2561.method_43470("0").method_27696(obfStyle)).method_10852(class_2561.method_43470(" ").method_27696(spaceStyle)).method_10852(class_2561.method_43470("[").method_27696(bracketStyle)).method_10852(class_2561.method_43470("☠").method_27696(skullStyle)).method_10852(class_2561.method_43470("]").method_27696(bracketStyle));
   }

   public static class_1799 createSurvivorMedal() {
      class_1799 medal = new class_1799(class_1802.field_8288);
      medal.method_57379(class_9334.field_49631, buildSurvivorMedalName());
      class_2487 tag = new class_2487();
      tag.method_10556("PermadeathSurvivorMedal", true);
      medal.method_57379(class_9334.field_49628, class_9279.method_57456(tag));
      return medal;
   }

   public static boolean isSurvivorMedal(class_1799 stack) {
      if (!stack.method_31574(class_1802.field_8288)) {
         return false;
      } else {
         class_9279 data = (class_9279)stack.method_57824(class_9334.field_49628);
         return data != null && data.method_57461().method_10577("PermadeathSurvivorMedal");
      }
   }

   public static void giveSurvivorMedalIfEligible(class_3222 player) {
      long currentDay = DateManager.getCurrentRealDay();
      if (currentDay >= 55L) {
         if (!player.method_5752().contains("permadeath_survivor_medal_given")) {
            class_1799 medal = createSurvivorMedal();
            if (!player.method_31548().method_7394(medal)) {
               player.method_7328(medal, false);
            }

            player.method_5780("permadeath_survivor_medal_given");
         }
      }
   }

   public static Boolean handleTotemDamage(class_3222 player, class_1282 source, float amount) {
      if (!isDamageFatal(player, source, amount)) {
         return null;
      } else {
         long tick = player.method_37908().method_8510();
         UUID id = player.method_5667();
         ResolvedHit resolved = (ResolvedHit)lastResolved.get(id);
         if (resolved != null && resolved.tick == tick) {
            return resolved.cancelDamage;
         } else if (source.method_48789(class_8103.field_42242)) {
            lastResolved.put(id, new ResolvedHit(tick, false));
            return null;
         } else {
            class_1799 mainStack = player.method_6047();
            class_1799 offStack = player.method_6079();
            boolean hasTotemMain = mainStack.method_31574(class_1802.field_8288);
            boolean hasTotemOff = offStack.method_31574(class_1802.field_8288);
            if (!hasTotemMain && !hasTotemOff) {
               return null;
            } else {
               boolean usingMedal = hasTotemMain ? isSurvivorMedal(mainStack) : isSurvivorMedal(offStack);
               long currentDay = DateManager.getCurrentRealDay();
               if (usingMedal) {
                  consumeExtraTotem(player, hasTotemMain, hasTotemOff, currentDay, false);
                  lastResolved.put(id, new ResolvedHit(tick, true));
                  return true;
               } else {
                  boolean failed = resolveTotemAttempt(player, hasTotemMain, hasTotemOff, currentDay);
                  if (failed) {
                     player.method_64419(source, Float.MAX_VALUE);
                     lastResolved.put(id, new ResolvedHit(tick, true));
                     return true;
                  } else {
                     consumeExtraTotem(player, hasTotemMain, hasTotemOff, currentDay, true);
                     lastResolved.put(id, new ResolvedHit(tick, true));
                     return true;
                  }
               }
            }
         }
      }
   }

   private static boolean isBlockingDamage(class_3222 player, class_1282 source) {
      if (source.method_48789(class_8103.field_43116)) {
         return false;
      } else if (!player.method_6039()) {
         return false;
      } else {
         class_1297 directEntity = source.method_5526();
         if (directEntity instanceof class_1665) {
            class_1665 arrow = (class_1665)directEntity;
            if (arrow.method_7447() > 0) {
               return false;
            }
         }

         class_243 sourcePos = source.method_5510();
         if (sourcePos == null) {
            return false;
         } else {
            class_243 viewVec = player.method_5631(0.0F, player.method_5791()).method_1029();
            class_243 toAttacker = sourcePos.method_1035(player.method_19538()).method_1029();
            toAttacker = new class_243(toAttacker.field_1352, (double)0.0F, toAttacker.field_1350);
            return toAttacker.method_1026(viewVec) < (double)0.0F;
         }
      }
   }

   public static void clearGuard(class_3222 player) {
      lastResolved.remove(player.method_5667());
   }

   private static boolean isDamageFatal(class_3222 player, class_1282 source, float amount) {
      if (source.method_48789(class_8103.field_42242)) {
         return amount >= player.method_6032();
      } else {
         if ((float)player.field_6008 > 10.0F) {
            float lastDamage = ((LivingEntityAccessor)player).getLastHurt();
            if (amount <= lastDamage) {
               return false;
            }

            amount -= lastDamage;
         }

         float finalDamage = amount;
         if (isBlockingDamage(player, source)) {
            return false;
         } else {
            if (!source.method_48789(class_8103.field_42241)) {
               finalDamage = class_1280.method_5496(player, amount, source, (float)player.method_6096(), (float)player.method_45325(class_5134.field_23725));
            }

            if (!source.method_48789(class_8103.field_42245)) {
               class_1937 var5 = player.method_37908();
               if (var5 instanceof class_3218) {
                  class_3218 serverLevel = (class_3218)var5;
                  float protection = class_1890.method_8219(serverLevel, player, source);
                  if (protection > 0.0F) {
                     finalDamage = class_1280.method_5497(finalDamage, protection);
                  }
               }
            }

            if (!source.method_48789(class_8103.field_42244) && player.method_6059(class_1294.field_5907)) {
               int amp = player.method_6112(class_1294.field_5907).method_5578() + 1;
               float factor = Math.max(25.0F - (float)amp * 5.0F, 0.0F) / 25.0F;
               finalDamage *= factor;
            }

            float totalLife = player.method_6032() + player.method_6067();
            return finalDamage >= totalLife - 0.1F;
         }
      }
   }

   private static int countTotalTotems(class_3222 player) {
      int count = 0;

      for(int i = 0; i < player.method_31548().method_5439(); ++i) {
         class_1799 stack = player.method_31548().method_5438(i);
         if (stack.method_31574(class_1802.field_8288) && !isSurvivorMedal(stack)) {
            ++count;
         }
      }

      return count;
   }

   private static boolean resolveTotemAttempt(class_3222 player, boolean hasTotemMain, boolean hasTotemOff, long currentDay) {
      int probability = player.method_59922().method_43048(100);
      int failThreshold = failThresholdForDay(currentDay);
      String totmesused = TotemsUsed(currentDay);
      boolean fails = probability >= failThreshold;
      int totalTotems = countTotalTotems(player);
      boolean showMultiMessage = totalTotems >= multiTotemMessageThresholdForDay(currentDay);
      if (currentDay < 40L) {
         if (fails) {
            String var10000 = player.method_5477().getString();
            class_2561 message = class_2561.method_43470(var10000 + " ha consumido un tótem, (Probabilidad " + failThreshold + " = " + failThreshold + ")").method_27692(class_124.field_1080);
            player.field_13995.method_3760().method_43514(message, false);
         } else {
            class_2561 message = class_2561.method_43470(player.method_5477().getString() + " ha consumido un tótem, (Probabilidad " + probability + " != " + failThreshold + ")").method_27692(class_124.field_1080);
            player.field_13995.method_3760().method_43514(message, false);
         }
      }

      if (fails) {
         if (showMultiMessage && currentDay >= 40L) {
            class_2561 message = class_2561.method_43470(player.method_5477().getString() + " ha consumido " + totmesused + " tótems, (Probabilidad " + failThreshold + " = " + failThreshold + ")").method_27692(class_124.field_1080);
            player.field_13995.method_3760().method_43514(message, false);
         }

         if (hasTotemMain) {
            player.method_6122(class_1268.field_5808, class_1799.field_8037);
         }

         if (hasTotemOff) {
            player.method_6122(class_1268.field_5810, class_1799.field_8037);
         }

         return true;
      } else {
         if (showMultiMessage && currentDay >= 40L) {
            class_2561 message = class_2561.method_43470(player.method_5477().getString() + " ha consumido " + totmesused + " tótems, (Probabilidad " + probability + " != " + failThreshold + ")").method_27692(class_124.field_1080);
            player.field_13995.method_3760().method_43514(message, false);
         }

         return false;
      }
   }

   private static void consumeExtraTotem(class_3222 player, boolean main, boolean off, long day, boolean consume) {
      if (day >= 40L) {
         int toRemove = day < 60L ? 1 : 2;
         if (main && off && !isSurvivorMedal(player.method_6079())) {
            if (consume) {
               player.method_6122(class_1268.field_5810, class_1799.field_8037);
            }

            --toRemove;
         }

         for(int i = 0; i < player.method_31548().method_5439() && toRemove > 0; ++i) {
            class_1799 stack = player.method_31548().method_5438(i);
            if (stack.method_31574(class_1802.field_8288) && !isSurvivorMedal(stack)) {
               boolean isMainHandSlot = i == player.method_31548().field_7545;
               boolean isOffHandSlot = i == 40;
               if ((!main || !isMainHandSlot) && (!off || !isOffHandSlot)) {
                  if (consume) {
                     player.method_31548().method_5447(i, class_1799.field_8037);
                  }

                  --toRemove;
               }
            }
         }

         if (toRemove > 0) {
            if (main) {
               player.method_6122(class_1268.field_5808, class_1799.field_8037);
            } else if (off) {
               player.method_6122(class_1268.field_5810, class_1799.field_8037);
            }
         }

      }
   }

   private static final class ResolvedHit {
      final long tick;
      final boolean cancelDamage;

      ResolvedHit(long tick, boolean cancelDamage) {
         this.tick = tick;
         this.cancelDamage = cancelDamage;
      }
   }
}
