package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.items.ModArmorMaterials;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1282;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1549;
import net.minecraft.class_1799;
import net.minecraft.class_3218;

public class day25Handler {
   public static void handleCaveSpiderHelmetDrop(class_1309 entity, class_1282 damageSource) {
      if (!entity.method_37908().field_9236) {
         long currentDay = DateManager.getCurrentRealDay();
         if (currentDay >= 25L && currentDay <= 30L) {
            if (entity instanceof class_1549) {
               float dropChance = 0.2F;
               int roll = entity.method_37908().field_9229.method_43048(100);
               if ((float)roll < dropChance * 100.0F) {
                  class_1799 helmet = new class_1799(ModArmorMaterials.NETHERITE_HELMET);
                  class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), helmet);
                  entity.method_37908().method_8649(itemEntity);
               }

            }
         }
      }
   }
}
