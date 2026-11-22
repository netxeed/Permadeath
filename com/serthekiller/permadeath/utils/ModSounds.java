package com.serthekiller.permadeath.utils;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public class ModSounds {
   public static final class_2960 PERMADEATH_ID = class_2960.method_60655("permadeath", "permadeath");
   public static final class_3414 PERMADEATH;

   public static void registerSounds() {
      class_2378.method_10230(class_7923.field_41172, PERMADEATH_ID, PERMADEATH);
   }

   static {
      PERMADEATH = class_3414.method_47908(PERMADEATH_ID);
   }
}
