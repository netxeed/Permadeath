package com.serthekiller.permadeath.worldgen.densityfunction;

import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public class ModDensityFunctions {
   public static void register() {
      class_2378.method_10230(class_7923.field_41160, class_2960.method_60655("permadeath", "uniform_islands"), UniformIslandsDensityFunction.CODEC);
   }
}
