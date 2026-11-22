package com.serthekiller.permadeath.dimension;

import net.minecraft.class_1937;
import net.minecraft.class_2874;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7924;

public class ModDimensions {
   public static final String MOD_ID = "permadeath";
   public static final class_5321<class_1937> THE_BEGINNING_LEVEL_KEY;
   public static final class_5321<class_2874> THE_BEGINNING_TYPE_KEY;

   public static void register() {
      System.out.println("[Permadeath] Dimensiones registradas.");
   }

   static {
      THE_BEGINNING_LEVEL_KEY = class_5321.method_29179(class_7924.field_41223, class_2960.method_60655("permadeath", "the_beginning"));
      THE_BEGINNING_TYPE_KEY = class_5321.method_29179(class_7924.field_41241, class_2960.method_60655("permadeath", "the_beginning_type"));
   }
}
