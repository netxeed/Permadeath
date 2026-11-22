package com.serthekiller.permadeath.recipes;

import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public class ModRecipes {
   public static final class_1865<HyperAppleRecipe> HYPER_APPLE_SERIALIZER = new class_1852.class_1866(HyperAppleRecipe::new);
   public static final class_1865<SuperAppleRecipe> SUPER_APPLE_RECIPE_RECIPE_SERIALIZER = new class_1852.class_1866(SuperAppleRecipe::new);
   public static final class_1865<BeginningRelicRecipe> BEGINNING_RELIC_SERIALIZER = new class_1852.class_1866(BeginningRelicRecipe::new);
   public static final class_1865<LifeOrb> LIFE_ORB_SERIALIZER = new class_1852.class_1866(LifeOrb::new);

   public static void register() {
      System.out.println("[Permadeath] Registrando receta HyperApple...");
      class_2378.method_10230(class_7923.field_41189, class_2960.method_60655("permadeath", "hyper_apple"), HYPER_APPLE_SERIALIZER);
      System.out.println("[Permadeath] Registrando receta SuperApple...");
      class_2378.method_10230(class_7923.field_41189, class_2960.method_60655("permadeath", "super_apple"), SUPER_APPLE_RECIPE_RECIPE_SERIALIZER);
      class_2378.method_10230(class_7923.field_41189, class_2960.method_60655("permadeath", "beginning_relic"), BEGINNING_RELIC_SERIALIZER);
      class_2378.method_10230(class_7923.field_41189, class_2960.method_60655("permadeath", "life_orb"), LIFE_ORB_SERIALIZER);
      System.out.println("[Permadeath] Recetas registradas exitosamente!");
   }
}
