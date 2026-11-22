package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_10289;
import net.minecraft.class_1863;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import net.minecraft.class_8786;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1863.class})
public class RecipeManagerMixin {
   @Shadow
   private class_10289 field_54638;

   @Inject(
      method = {"method_20705*(Lnet/minecraft/class_10289;Lnet/minecraft/class_3300;Lnet/minecraft/class_3695;)V"},
      at = {@At("TAIL")}
   )
   private void onApply(class_10289 recipeMap, class_3300 resourceManager, class_3695 profiler, CallbackInfo ci) {
      if (DateManager.getCurrentRealDay() >= 40L) {
         List<class_8786<?>> allRecipes = new ArrayList(this.field_54638.method_64695());
         class_2960 torchId = class_2960.method_60656("torch");
         class_2960 torchredId = class_2960.method_60656("redstone_torch");
         class_2960 torchsoulId = class_2960.method_60656("soul_torch");
         boolean removed1 = allRecipes.removeIf((holder) -> holder.comp_1932().method_29177().equals(torchId));
         boolean removed2 = allRecipes.removeIf((holder) -> holder.comp_1932().method_29177().equals(torchredId));
         boolean removed3 = allRecipes.removeIf((holder) -> holder.comp_1932().method_29177().equals(torchsoulId));
         if (removed1 || removed2 || removed3) {
            this.field_54638 = class_10289.method_64700(allRecipes);
         }
      }

      if (DateManager.getCurrentRealDay() >= 50L) {
         List<class_8786<?>> allRecipes = new ArrayList(this.field_54638.method_64695());
         Set<class_2960> toRemove = Set.of(class_2960.method_60656("iron_ingot_from_smelting_iron_ore"), class_2960.method_60656("iron_ingot_from_smelting_deepslate_iron_ore"), class_2960.method_60656("iron_ingot_from_smelting_raw_iron"), class_2960.method_60656("iron_ingot_from_blasting_iron_ore"), class_2960.method_60656("iron_ingot_from_blasting_deepslate_iron_ore"), class_2960.method_60656("iron_ingot_from_blasting_raw_iron"), class_2960.method_60656("gold_ingot_from_smelting_gold_ore"), class_2960.method_60656("gold_ingot_from_smelting_deepslate_gold_ore"), class_2960.method_60656("gold_ingot_from_smelting_nether_gold_ore"), class_2960.method_60656("gold_ingot_from_smelting_raw_gold"), class_2960.method_60656("gold_ingot_from_blasting_gold_ore"), class_2960.method_60656("gold_ingot_from_blasting_deepslate_gold_ore"), class_2960.method_60656("gold_ingot_from_blasting_nether_gold_ore"), class_2960.method_60656("gold_ingot_from_blasting_raw_gold"));
         boolean anyRemoved = allRecipes.removeIf((holder) -> toRemove.contains(holder.comp_1932().method_29177()));
         if (anyRemoved) {
            this.field_54638 = class_10289.method_64700(allRecipes);
         }
      }

   }
}
