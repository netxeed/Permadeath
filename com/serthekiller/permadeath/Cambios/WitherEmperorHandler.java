package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1542;
import net.minecraft.class_1639;
import net.minecraft.class_1767;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_2378;
import net.minecraft.class_2561;
import net.minecraft.class_2582;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_6908;
import net.minecraft.class_7924;
import net.minecraft.class_9304;
import net.minecraft.class_9307;
import net.minecraft.class_9334;

public class WitherEmperorHandler {
   public static void SpawnEmperor(class_1309 entity, class_3218 world) {
      class_1639 emperor = (class_1639)entity;
      emperor.method_5665(class_2561.method_43470("§eWither Emperador"));
      emperor.method_5880(false);
      emperor.method_5780("wither_emperor");
      class_1324 maxHealth = emperor.method_5996(class_5134.field_23716);
      class_1324 attackDamage = emperor.method_5996(class_5134.field_23721);
      class_1324 movementSpeed = emperor.method_5996(class_5134.field_23719);
      if (maxHealth != null) {
         maxHealth.method_6192((double)80.0F);
         emperor.method_6033(80.0F);
      }

      if (attackDamage != null) {
         attackDamage.method_6192((double)18.0F);
      }

      if (movementSpeed != null) {
         movementSpeed.method_6192(0.28);
      }

      class_1799 bow = new class_1799(class_1802.field_8102);
      class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
      class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
      registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 100));
      registry.method_46746(class_1893.field_9116).ifPresent((holder) -> enchantments.method_57547(holder, 5));
      bow.method_57379(class_9334.field_49633, enchantments.method_57549());
      emperor.method_5673(class_1304.field_6173, bow);
      emperor.method_5946(class_1304.field_6173, 0.0F);
      emperor.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8678));
      emperor.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8416));
      emperor.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8753));
      emperor.method_5946(class_1304.field_6174, 0.0F);
      emperor.method_5946(class_1304.field_6172, 0.0F);
      emperor.method_5946(class_1304.field_6166, 0.0F);
      class_1799 banner = createEmperorBanner(world);
      emperor.method_5673(class_1304.field_6169, banner);
      emperor.method_5946(class_1304.field_6169, 0.0F);
   }

   public static boolean trySpawnEmperor(class_1309 entity, class_3218 world) {
      if (!(entity instanceof class_1639)) {
         return false;
      } else if (entity.method_5752().contains("permadeath:processed")) {
         return false;
      } else if (!world.method_23753(entity.method_24515()).method_40220(class_6908.field_36518)) {
         return false;
      } else {
         long currentDay = DateManager.getCurrentRealDay();
         if (currentDay < 60L) {
            if (world.field_9229.method_43057() > 0.05F) {
               return false;
            }
         } else if (world.field_9229.method_43057() > 0.2F) {
            return false;
         }

         class_1639 emperor = (class_1639)entity;
         emperor.method_5665(class_2561.method_43470("§eWither Emperador"));
         emperor.method_5880(false);
         emperor.method_5780("wither_emperor");
         class_1324 maxHealth = emperor.method_5996(class_5134.field_23716);
         class_1324 attackDamage = emperor.method_5996(class_5134.field_23721);
         class_1324 movementSpeed = emperor.method_5996(class_5134.field_23719);
         if (maxHealth != null) {
            maxHealth.method_6192((double)80.0F);
            emperor.method_6033(80.0F);
         }

         if (attackDamage != null) {
            attackDamage.method_6192((double)18.0F);
         }

         if (movementSpeed != null) {
            movementSpeed.method_6192(0.28);
         }

         class_1799 bow = new class_1799(class_1802.field_8102);
         class_9304.class_9305 enchantments = new class_9304.class_9305(class_9304.field_49385);
         class_2378<class_1887> registry = world.method_30349().method_30530(class_7924.field_41265);
         registry.method_46746(class_1893.field_9103).ifPresent((holder) -> enchantments.method_57547(holder, 100));
         registry.method_46746(class_1893.field_9116).ifPresent((holder) -> enchantments.method_57547(holder, 5));
         bow.method_57379(class_9334.field_49633, enchantments.method_57549());
         emperor.method_5673(class_1304.field_6173, bow);
         emperor.method_5946(class_1304.field_6173, 0.0F);
         emperor.method_5673(class_1304.field_6174, new class_1799(class_1802.field_8678));
         emperor.method_5673(class_1304.field_6172, new class_1799(class_1802.field_8416));
         emperor.method_5673(class_1304.field_6166, new class_1799(class_1802.field_8753));
         emperor.method_5946(class_1304.field_6174, 0.0F);
         emperor.method_5946(class_1304.field_6172, 0.0F);
         emperor.method_5946(class_1304.field_6166, 0.0F);
         class_1799 banner = createEmperorBanner(world);
         emperor.method_5673(class_1304.field_6169, banner);
         emperor.method_5946(class_1304.field_6169, 0.0F);
         return true;
      }
   }

   private static class_1799 createEmperorBanner(class_3218 world) {
      class_1799 banner = new class_1799(class_1802.field_8572);
      banner.method_57379(class_9334.field_49631, class_2561.method_43470("§4Estandarte del Wither Emperador"));
      class_2378<class_2582> patternRegistry = world.method_30349().method_30530(class_7924.field_41252);
      class_9307.class_3750 builder = new class_9307.class_3750();
      class_5321<class_2582> emperorKey = class_5321.method_29179(class_7924.field_41252, class_2960.method_60655("permadeath", "wither_emperor"));
      patternRegistry.method_46746(emperorKey).ifPresent((holder) -> builder.method_16376(holder, class_1767.field_7964));
      banner.method_57379(class_9334.field_49619, builder.method_57573());
      return banner;
   }

   private static void addPattern(class_9307.class_3750 builder, class_2378<class_2582> registry, class_5321<class_2582> patternKey, class_1767 color) {
      registry.method_46746(patternKey).ifPresent((holder) -> builder.method_16376(holder, color));
   }

   public static void handleDropsE(class_1309 entity, class_3218 world) {
      long currentDay = DateManager.getCurrentRealDay();
      if (entity instanceof class_1639 && entity.method_5752().contains("wither_emperor") && currentDay < 60L && world.field_9229.method_43057() < 0.5F) {
         class_1799 sword = new class_1799(class_1802.field_22022);
         class_1542 itemEntity = new class_1542(world, entity.method_23317(), entity.method_23318(), entity.method_23321(), sword);
         itemEntity.method_6976();
         world.method_8649(itemEntity);
      }

   }
}
