package com.serthekiller.permadeath.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.class_2919;
import net.minecraft.class_3541;
import net.minecraft.class_5820;
import net.minecraft.class_6910;
import net.minecraft.class_7243;

public class UniformIslandsDensityFunction implements class_6910.class_6913 {
   public static final UniformIslandsDensityFunction INSTANCE = new UniformIslandsDensityFunction();
   public static final MapCodec<UniformIslandsDensityFunction> CODEC;
   private static final class_3541 NOISE_1;
   private static final class_3541 NOISE_2;
   private static final class_3541 NOISE_3;
   private static final class_3541 NOISE_4;
   private static final double MID_Y = (double)200.0F;
   private static final double TOP_Y = (double)207.0F;
   private static final double BOTTOM_Y = (double)180.0F;
   private static final double MEDIUM_WEIGHT = 0.45;
   private static final double SMALL_WEIGHT = 0.2;
   private static final double DETAIL_WEIGHT = 0.08;
   private static final double TOTAL_WEIGHT = 1.73;
   private static final double TOP_MEDIUM_WEIGHT = 0.45;
   private static final double TOP_SMALL_WEIGHT = 0.07;
   private static final double TOP_DETAIL_WEIGHT = 0.02;
   private static final double TOP_TOTAL_WEIGHT = 1.54;
   private static final double BASE_THRESHOLD = 0.03;
   private static final double[] TOP_LAYER_STEP_DISTANCE;
   private static final double NOISE_GRADIENT_FACTOR = 2.6;
   private static final double[] TOP_LAYER_THRESHOLDS;
   private static final double EROSION_PER_LAYER_BOTTOM = 0.018;
   private static final int BOTTOM_FLATTEN_LAYERS = 5;
   private static final double EROSION_PER_LAYER_BOTTOM_FLATTEN = 0.05;

   private UniformIslandsDensityFunction() {
   }

   public double method_40464(class_6910.class_6912 ctx) {
      double y = (double)ctx.comp_372();
      if (!(y > (double)207.0F) && !(y < (double)180.0F)) {
         double scaleXZ = 0.006;
         double x = (double)ctx.comp_371() * scaleXZ;
         double z = (double)ctx.comp_373() * scaleXZ;
         double large = NOISE_1.method_22416(x, (double)0.0F, z);
         double medium = NOISE_2.method_22416(x * (double)2.0F, (double)0.0F, z * (double)2.0F);
         double small = NOISE_3.method_22416(x * (double)4.0F, (double)0.0F, z * (double)4.0F);
         double detail = NOISE_4.method_22416(x * (double)8.0F, (double)0.0F, z * (double)8.0F);
         double n = large + medium * 0.45 + small * 0.07 + detail * 0.02;
         n /= 1.54;
         double threshold = thresholdForLayer(y);
         return n - threshold;
      } else {
         return (double)-1.0F;
      }
   }

   private static double thresholdForLayer(double y) {
      if (y >= (double)200.0F) {
         int layer = (int)Math.round(y - (double)200.0F);
         layer = Math.max(0, Math.min(layer, TOP_LAYER_THRESHOLDS.length - 1));
         return TOP_LAYER_THRESHOLDS[layer];
      } else {
         int layer = (int)Math.round((double)200.0F - y);
         int flattenStart = 16;
         if (layer < flattenStart) {
            return 0.03 + (double)layer * 0.018;
         } else {
            double thresholdAtFlattenStart = 0.03 + (double)(flattenStart - 1) * 0.018;
            int flattenLayer = layer - flattenStart + 1;
            return thresholdAtFlattenStart + (double)flattenLayer * 0.05;
         }
      }
   }

   public double comp_377() {
      return (double)-1.0F;
   }

   public double comp_378() {
      return (double)1.0F;
   }

   public class_7243<? extends class_6910> method_41062() {
      return class_7243.method_42116(CODEC);
   }

   static {
      CODEC = MapCodec.unit(INSTANCE);
      class_2919 r1 = new class_2919(new class_5820(12345L));
      r1.method_33650(17292);
      NOISE_1 = new class_3541(r1);
      class_2919 r2 = new class_2919(new class_5820(67890L));
      r2.method_33650(17292);
      NOISE_2 = new class_3541(r2);
      class_2919 r3 = new class_2919(new class_5820(11223L));
      r3.method_33650(17292);
      NOISE_3 = new class_3541(r3);
      class_2919 r4 = new class_2919(new class_5820(54321L));
      r4.method_33650(17292);
      NOISE_4 = new class_3541(r4);
      TOP_LAYER_STEP_DISTANCE = new double[]{(double)3.5F, (double)3.5F, (double)3.5F, (double)3.5F, (double)5.0F, (double)5.0F, (double)5.0F};
      double scaleXZ = 0.006;
      TOP_LAYER_THRESHOLDS = new double[TOP_LAYER_STEP_DISTANCE.length + 1];
      TOP_LAYER_THRESHOLDS[0] = 0.03;

      for(int i = 0; i < TOP_LAYER_STEP_DISTANCE.length; ++i) {
         TOP_LAYER_THRESHOLDS[i + 1] = TOP_LAYER_THRESHOLDS[i] + TOP_LAYER_STEP_DISTANCE[i] * scaleXZ * 2.6;
      }

   }
}
