package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1320;
import net.minecraft.class_1324;
import net.minecraft.class_1542;
import net.minecraft.class_1642;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_2378;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_7924;
import net.minecraft.class_9304;
import net.minecraft.class_9334;

public class GiantZombieHandler {
   public static final String TAG = "permadeath:giant_zombie";
   public static final double SCALE = (double)6.0F;
   public static final double GIANT_WIDTH = 3.5999999999999996;
   public static final double GIANT_HEIGHT = 11.7;
   private static final double MAX_HEALTH = (double)600.0F;
   private static final double ATTACK_DAMAGE = (double)30.0F;
   private static final double MOVEMENT_SPEED = 0.35;
   private static final double FOLLOW_RANGE = (double)32.0F;
   private static final double STEP_HEIGHT = (double)3.5F;

   public static boolean isGiant(class_1297 entity) {
      return entity.method_5752().contains("permadeath:giant_zombie");
   }

   public static void applyGiantStats(class_1642 zombie) {
      zombie.method_7217(false);
      zombie.method_5848();
      zombie.method_5772();
      setBase(zombie, class_5134.field_47760, (double)6.0F);
      setBase(zombie, class_5134.field_23716, (double)600.0F);
      zombie.method_6033(600.0F);
      setBase(zombie, class_5134.field_23721, (double)30.0F);
      setBase(zombie, class_5134.field_23719, 0.35);
      setBase(zombie, class_5134.field_23717, (double)32.0F);
      setBase(zombie, class_5134.field_47761, (double)3.5F);
      setBase(zombie, class_5134.field_23727, (double)0.0F);
      zombie.method_5665(class_2561.method_43470("§eZombie Gigante"));
      zombie.method_5880(false);
      zombie.method_5780("permadeath:giant_zombie");
   }

   private static void setBase(class_1309 entity, class_6880<class_1320> attribute, double value) {
      class_1324 instance = entity.method_5996(attribute);
      if (instance != null) {
         instance.method_6192(value);
      }

   }

   public static void summonGiant(class_1309 entity, class_3218 world) {
      class_1642 giant = new class_1642(world);
      giant.method_5814(entity.method_23317(), entity.method_23318(), entity.method_23321());
      giant.method_36456(entity.method_36454());
      applyGiantStats(giant);
      world.method_8649(giant);
      entity.method_31472();
   }

   public static void handleDrops(class_1309 entity, class_3218 world) {
      if (isGiant(entity) && DateManager.getCurrentRealDay() < 60L) {
         class_1799 bow = new class_1799(class_1802.field_8102);
         bow.method_57379(class_9334.field_49631, class_2561.method_43470("Arco de Gigante"));
         class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
         class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
         registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 10));
         bow.method_57379(class_9334.field_49633, enchantments.method_57549());
         class_1542 itemEntity = new class_1542(world, entity.method_23317(), entity.method_23318(), entity.method_23321(), bow);
         itemEntity.method_6976();
         world.method_8649(itemEntity);
      }
   }
}
