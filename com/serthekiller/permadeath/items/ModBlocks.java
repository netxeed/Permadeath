package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.PermadeathMod;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_2378;
import net.minecraft.class_2498;
import net.minecraft.class_2582;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_4970.class_2251;

public class ModBlocks {
   public static class_2248 INFERNAL_NETHERITE_BLOCK;
   public static final class_5321<class_2582> WITHER_EMPEROR;

   public static void register() {
      class_2960 id = class_2960.method_60655("permadeath", "infernal_netherite_block");
      class_5321<class_2248> blockKey = class_5321.method_29179(class_7924.field_41254, id);
      class_5321<class_1792> itemKey = class_5321.method_29179(class_7924.field_41197, id);
      INFERNAL_NETHERITE_BLOCK = new class_2248(class_2251.method_9637().method_63500(blockKey).method_9629(5.0F, 5.0F).method_9626(class_2498.field_22150).method_29292());
      class_2378.method_39197(class_7923.field_41175, blockKey, INFERNAL_NETHERITE_BLOCK);
      class_1792.class_1793 itemProps = (new class_1792.class_1793()).method_63686(itemKey).method_7889(1);
      class_2378.method_39197(class_7923.field_41178, itemKey, new class_1747(INFERNAL_NETHERITE_BLOCK, itemProps));
      PermadeathMod.LOGGER.info("Bloques de permadeath registrados");
   }

   static {
      WITHER_EMPEROR = class_5321.method_29179(class_7924.field_41252, class_2960.method_60655("permadeath", "wither_emperor"));
   }
}
