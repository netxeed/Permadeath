package com.serthekiller.permadeath.worldgen.feature;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3031;
import net.minecraft.class_7923;

public class ModFeatures {
   public static final class_3031<BeginningCoralConfiguration> BEGINNING_CORAL;

   public static void register() {
   }

   static {
      BEGINNING_CORAL = (class_3031)class_2378.method_10230(class_7923.field_41144, class_2960.method_60655("permadeath", "beginning_coral"), new BeginningCoralFeature(BeginningCoralConfiguration.CODEC));
   }
}
