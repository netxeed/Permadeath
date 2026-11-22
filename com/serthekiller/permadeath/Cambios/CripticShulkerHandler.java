package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1299;
import net.minecraft.class_1315;
import net.minecraft.class_1606;
import net.minecraft.class_1767;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3730;

public class CripticShulkerHandler {
   public static final String TAG = "permadeath:criptic_shulker";
   public static final long SPAWN_START_DAY = 60L;
   private static final int SPAWN_CHANCE_DENOMINATOR = 600;
   private static final int MAX_NEARBY = 4;
   private static final double MIN_DISTANCE = (double)20.0F;
   private static final double MAX_DISTANCE = (double)40.0F;

   public static void trySpawnCripticShulker(class_3222 player) {
      class_3218 level = player.method_51469();
      if (level.method_27983() == class_1937.field_25180) {
         if (DateManager.getCurrentRealDay() >= 60L) {
            if (level.field_9229.method_43048(600) == 0) {
               class_238 checkArea = player.method_5829().method_1014((double)48.0F);
               long nearby = (long)level.method_8390(class_1606.class, checkArea, (s) -> s.method_5752().contains("permadeath:criptic_shulker")).size();
               if (nearby < 4L) {
                  double angle = level.field_9229.method_43058() * Math.PI * (double)2.0F;
                  double distance = (double)20.0F + level.field_9229.method_43058() * (double)20.0F;
                  int x = player.method_31477() + (int)Math.round(Math.cos(angle) * distance);
                  int z = player.method_31479() + (int)Math.round(Math.sin(angle) * distance);
                  class_2338 spawnPos = findAirPocket(level, x, player.method_31478(), z);
                  if (spawnPos != null) {
                     spawnCripticShulker(level, spawnPos);
                  }
               }
            }
         }
      }
   }

   private static class_2338 findAirPocket(class_3218 level, int x, int startY, int z) {
      for(int dy = 0; dy <= 8; ++dy) {
         for(int sign : new int[]{1, -1}) {
            int y = startY + dy * sign;
            if (y >= level.method_31607() + 1 && y <= level.method_31600() - 2) {
               class_2338 ground = new class_2338(x, y - 1, z);
               class_2338 pos = new class_2338(x, y, z);
               if (level.method_8320(ground).method_51367() && level.method_8320(pos).method_26215() && level.method_8320(pos.method_10084()).method_26215()) {
                  return pos;
               }

               if (dy == 0) {
                  break;
               }
            }
         }
      }

      return null;
   }

   public static class_1606 spawnCripticShulker(class_3218 level, class_2338 pos) {
      class_1606 shulker = new class_1606(class_1299.field_6109, level);
      shulker.method_5808((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264(), (double)pos.method_10260() + (double)0.5F, 0.0F, 0.0F);
      class_2487 tag = new class_2487();
      shulker.method_5652(tag);
      tag.method_10567("Color", (byte)class_1767.field_7942.method_7789());
      shulker.method_5749(tag);
      shulker.method_5665(class_2561.method_43470("§2Shulker Críptico"));
      shulker.method_5880(false);
      shulker.method_5780("permadeath:criptic_shulker");
      shulker.method_5943(level, level.method_8404(pos), class_3730.field_16459, (class_1315)null);
      level.method_8649(shulker);
      return shulker;
   }
}
