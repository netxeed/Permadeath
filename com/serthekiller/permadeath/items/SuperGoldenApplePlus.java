package com.serthekiller.permadeath.items;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;

public class SuperGoldenApplePlus extends class_1792 {
   public SuperGoldenApplePlus(class_1792.class_1793 properties) {
      super(properties);
   }

   public boolean method_7886(class_1799 stack) {
      return false;
   }

   public class_1799 method_7861(class_1799 stack, class_1937 level, class_1309 entity) {
      if (!level.field_9236) {
         entity.method_6092(new class_1293(class_1294.field_5914, 6000, 0, false, true, true));
      }

      return super.method_7861(stack, level, entity);
   }
}
