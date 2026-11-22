package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.PermadeathMod;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_10191;
import net.minecraft.class_10394;
import net.minecraft.class_124;
import net.minecraft.class_1322;
import net.minecraft.class_1741;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3417;
import net.minecraft.class_3489;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_8051;
import net.minecraft.class_9274;
import net.minecraft.class_9279;
import net.minecraft.class_9285;
import net.minecraft.class_9300;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;

public class ModArmorMaterials {
   private static final class_1741 NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "netherite");
   private static final class_1741 INFERNAL_NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "infernal_netherite");
   private static final class_1741 DEMONIC_NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "demonic_netherite");
   private static final class_1741 CIENTIFIC_NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "scientific_netherite");
   private static final class_1741 BOMB_NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "bomb_netherite");
   private static final class_1741 PINK_NETHERITE_MATERIAL = createMaterial(3, 6, 8, 3, 15, 3.0F, "pink_netherite");
   private static final double NETHERITE_BONUS_HEALTH = (double)8.0F;
   public static final double INFERNAL_NETHERITE_BONUS_HEALTH = (double)10.0F;
   private static final double CELESTIAL_NETHERITE_BONUS_HEALTH = (double)14.0F;
   private static final double OTHER_NETHERITE_BONUS_HEALTH = (double)0.0F;
   public static final class_1792 NETHERITE_HELMET = register("netherite_helmet", (settings) -> new CustomArmorItem(NETHERITE_MATERIAL, class_8051.field_41934, (double)8.0F, settings));
   public static final class_1792 NETHERITE_CHESTPLATE = register("netherite_chestplate", (settings) -> new CustomArmorItem(NETHERITE_MATERIAL, class_8051.field_41935, (double)8.0F, settings));
   public static final class_1792 NETHERITE_LEGGINGS = register("netherite_leggings", (settings) -> new CustomArmorItem(NETHERITE_MATERIAL, class_8051.field_41936, (double)8.0F, settings));
   public static final class_1792 NETHERITE_BOOTS = register("netherite_boots", (settings) -> new CustomArmorItem(NETHERITE_MATERIAL, class_8051.field_41937, (double)8.0F, settings));
   public static final class_1792 NETHERITE_INFERNAL_HELMET = register("infernal_netherite_helmet", (settings) -> new CustomArmorItem(INFERNAL_NETHERITE_MATERIAL, class_8051.field_41934, (double)10.0F, settings));
   public static final class_1792 NETHERITE_INFERNAL_CHESTPLATE = register("infernal_netherite_chestplate", (settings) -> new CustomArmorItem(INFERNAL_NETHERITE_MATERIAL, class_8051.field_41935, (double)10.0F, settings));
   public static final class_1792 NETHERITE_INFERNAL_LEGGINGS = register("infernal_netherite_leggings", (settings) -> new CustomArmorItem(INFERNAL_NETHERITE_MATERIAL, class_8051.field_41936, (double)10.0F, settings));
   public static final class_1792 NETHERITE_INFERNAL_BOOTS = register("infernal_netherite_boots", (settings) -> new CustomArmorItem(INFERNAL_NETHERITE_MATERIAL, class_8051.field_41937, (double)10.0F, settings));
   public static final class_1792 NETHERITE_DEMONIACA_HELMET = register("demonic_netherite_helmet", (settings) -> new CustomArmorItem(DEMONIC_NETHERITE_MATERIAL, class_8051.field_41934, (double)0.0F, settings));
   public static final class_1792 NETHERITE_DEMONIACA_CHESTPLATE = register("demonic_netherite_chestplate", (settings) -> new CustomArmorItem(DEMONIC_NETHERITE_MATERIAL, class_8051.field_41935, (double)0.0F, settings));
   public static final class_1792 NETHERITE_DEMONIACA_LEGGINGS = register("demonic_netherite_leggings", (settings) -> new CustomArmorItem(DEMONIC_NETHERITE_MATERIAL, class_8051.field_41936, (double)0.0F, settings));
   public static final class_1792 NETHERITE_DEMONIACA_BOOTS = register("demonic_netherite_boots", (settings) -> new CustomArmorItem(DEMONIC_NETHERITE_MATERIAL, class_8051.field_41937, (double)0.0F, settings));
   public static final class_1792 NETHERITE_CIENTIFICA_HELMET = register("scientific_netherite_helmet", (settings) -> new CustomArmorItem(CIENTIFIC_NETHERITE_MATERIAL, class_8051.field_41934, (double)0.0F, settings));
   public static final class_1792 NETHERITE_CIENTIFICA_CHESTPLATE = register("scientific_netherite_chestplate", (settings) -> new CustomArmorItem(CIENTIFIC_NETHERITE_MATERIAL, class_8051.field_41935, (double)0.0F, settings));
   public static final class_1792 NETHERITE_CIENTIFICA_LEGGINGS = register("scientific_netherite_leggings", (settings) -> new CustomArmorItem(CIENTIFIC_NETHERITE_MATERIAL, class_8051.field_41936, (double)0.0F, settings));
   public static final class_1792 NETHERITE_CIENTIFICA_BOOTS = register("scientific_netherite_boots", (settings) -> new CustomArmorItem(CIENTIFIC_NETHERITE_MATERIAL, class_8051.field_41937, (double)0.0F, settings));
   public static final class_1792 NETHERITE_BOMB_HELMET = register("bomb_netherite_helmet", (settings) -> new CustomArmorItem(BOMB_NETHERITE_MATERIAL, class_8051.field_41934, (double)0.0F, settings));
   public static final class_1792 NETHERITE_BOMB_CHESTPLATE = register("bomb_netherite_chestplate", (settings) -> new CustomArmorItem(BOMB_NETHERITE_MATERIAL, class_8051.field_41935, (double)0.0F, settings));
   public static final class_1792 NETHERITE_BOMB_LEGGINGS = register("bomb_netherite_leggings", (settings) -> new CustomArmorItem(BOMB_NETHERITE_MATERIAL, class_8051.field_41936, (double)0.0F, settings));
   public static final class_1792 NETHERITE_BOMB_BOOTS = register("bomb_netherite_boots", (settings) -> new CustomArmorItem(BOMB_NETHERITE_MATERIAL, class_8051.field_41937, (double)0.0F, settings));
   public static final class_1792 NETHERITE_PINK_HELMET = register("pink_netherite_helmet", (settings) -> new CustomArmorItem(PINK_NETHERITE_MATERIAL, class_8051.field_41934, (double)0.0F, settings));
   public static final class_1792 NETHERITE_PINK_CHESTPLATE = register("pink_netherite_chestplate", (settings) -> new CustomArmorItem(PINK_NETHERITE_MATERIAL, class_8051.field_41935, (double)0.0F, settings));
   public static final class_1792 NETHERITE_PINK_LEGGINGS = register("pink_netherite_leggings", (settings) -> new CustomArmorItem(PINK_NETHERITE_MATERIAL, class_8051.field_41936, (double)0.0F, settings));
   public static final class_1792 NETHERITE_PINK_BOOTS = register("pink_netherite_boots", (settings) -> new CustomArmorItem(PINK_NETHERITE_MATERIAL, class_8051.field_41937, (double)0.0F, settings));

   public static class_1799 createArmoredElytraStack() {
      class_1799 stack = new class_1799(class_1802.field_8833);
      class_9285 modifiers = class_9285.method_57480().method_57487(class_5134.field_23724, new class_1322(class_2960.method_60655("permadeath", "armored_elytra_armor"), (double)8.0F, class_1323.field_6328), class_9274.field_49222).method_57487(class_5134.field_23725, new class_1322(class_2960.method_60655("permadeath", "armored_elytra_toughness"), (double)3.0F, class_1323.field_6328), class_9274.field_49222).method_57487(class_5134.field_23718, new class_1322(class_2960.method_60655("permadeath", "armored_elytra_kb"), 0.1, class_1323.field_6328), class_9274.field_49222).method_57486();
      stack.method_57379(class_9334.field_49636, modifiers);
      class_2487 inner = new class_2487();
      inner.method_10556("armored_elytra", true);
      class_2487 tag = new class_2487();
      tag.method_10566("permadeath", inner);
      stack.method_57379(class_9334.field_49628, class_9279.method_57456(tag));
      stack.method_57379(class_9334.field_50239, class_2561.method_43470("Elytra de Netherite Infernal").method_27694((style) -> style.method_10977(class_124.field_1065).method_10978(false)));
      return stack;
   }

   public static void init() {
      PermadeathMod.LOGGER.info("Armaduras personalizadas registradas");
   }

   private static class_1741 createMaterial(int helmet, int leggings, int chestplate, int boots, int enchantability, float toughness, String material) {
      Map<class_8051, Integer> defenseMap = new EnumMap(class_8051.class);
      defenseMap.put(class_8051.field_41937, boots);
      defenseMap.put(class_8051.field_41936, leggings);
      defenseMap.put(class_8051.field_41935, chestplate);
      defenseMap.put(class_8051.field_41934, helmet);
      defenseMap.put(class_8051.field_48838, chestplate);
      class_5321<class_10394> assetKey = class_5321.method_29179(class_10191.field_55214, class_2960.method_60655("permadeath", material));
      return new class_1741(27, defenseMap, enchantability, class_3417.field_21866, toughness, 0.1F, class_3489.field_54064, assetKey);
   }

   private static <T extends class_1792> T register(String name, Function<class_1792.class_1793, T> factory) {
      class_2960 id = class_2960.method_60655("permadeath", name);
      class_1792.class_1793 props = (new class_1792.class_1793()).method_63686(class_5321.method_29179(class_7924.field_41197, id)).method_57349(class_9334.field_49630, new class_9300(true)).method_24359();
      return (T)(class_2378.method_10230(class_7923.field_41178, id, (class_1792)factory.apply(props)));
   }
}
