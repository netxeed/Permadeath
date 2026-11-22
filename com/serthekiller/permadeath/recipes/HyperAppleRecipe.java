package com.serthekiller.permadeath.recipes;

import com.serthekiller.permadeath.items.ModItems;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_1937;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_9694;

public class HyperAppleRecipe extends class_1852 {
   public HyperAppleRecipe(class_7710 category) {
      super(category);
   }

   public boolean matches(class_9694 input, class_1937 level) {
      if (input.method_59991() == 3 && input.method_59992() == 3) {
         long day = DateManager.getCurrentRealDay();

         for(int i = 0; i < 9; ++i) {
            class_1799 stack = input.method_59984(i);
            if (i == 4) {
               if (!stack.method_31574(class_1802.field_8463)) {
                  return false;
               }

               long required = day >= 60L ? 64L : 1L;
               if ((long)stack.method_7947() != required) {
                  return false;
               }
            } else if (!stack.method_31574(class_1802.field_8494) || stack.method_7947() != 8) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public class_1799 assemble(class_9694 input, class_7225.class_7874 registries) {
      return new class_1799(ModItems.HYPER_GOLDEN_APPLE_PLUS.method_8389());
   }

   public class_1865<? extends class_1852> method_8119() {
      return ModRecipes.HYPER_APPLE_SERIALIZER;
   }
}
