package com.serthekiller.permadeath.recipes;

import com.serthekiller.permadeath.items.ModItems;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_1937;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_9694;

public class LifeOrb extends class_1852 {
   public LifeOrb(class_7710 category) {
      super(category);
   }

   public boolean method_8115(class_9694 input, class_1937 level) {
      if (input.method_59983() != 9) {
         return false;
      } else {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = input.method_59984(i);
            if (i != 4 && stack.method_7947() < 64) {
               return false;
            }

            if (i == 4) {
               if (!stack.method_31574(class_1802.field_8207)) {
                  return false;
               }
            } else if (i == 0) {
               if (!stack.method_31574(class_1802.field_8477)) {
                  return false;
               }
            } else if (i == 1) {
               if (!stack.method_31574(class_1802.field_8695)) {
                  return false;
               }
            } else if (i == 2) {
               if (!stack.method_31574(class_1802.field_8242)) {
                  return false;
               }
            } else if (i == 3) {
               if (!stack.method_31574(class_1802.field_8894)) {
                  return false;
               }
            } else if (i == 5) {
               if (!stack.method_31574(class_1802.field_20399)) {
                  return false;
               }
            } else if (i == 6) {
               if (!stack.method_31574(class_1802.field_20398)) {
                  return false;
               }
            } else if (i == 7) {
               if (!stack.method_31574(class_1802.field_8281)) {
                  return false;
               }
            } else if (!stack.method_31574(class_1802.field_8055)) {
               return false;
            }
         }

         return true;
      }
   }

   public class_1799 method_8116(class_9694 input, class_7225.class_7874 registries) {
      return new class_1799(ModItems.LIFE_ORB.method_8389());
   }

   public class_1865<? extends class_1852> method_8119() {
      return ModRecipes.LIFE_ORB_SERIALIZER;
   }
}
