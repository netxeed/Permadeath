package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.recipes.BeginningRelicRecipe;
import com.serthekiller.permadeath.recipes.HyperAppleRecipe;
import com.serthekiller.permadeath.recipes.LifeOrb;
import com.serthekiller.permadeath.recipes.SuperAppleRecipe;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.Optional;
import net.minecraft.class_1657;
import net.minecraft.class_1734;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_3218;
import net.minecraft.class_3955;
import net.minecraft.class_3956;
import net.minecraft.class_8566;
import net.minecraft.class_8786;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1734.class})
public class ResultSlotMixin {
   @Shadow
   @Final
   private class_8566 field_7870;

   @Inject(
      method = {"method_7667(Lnet/minecraft/class_1657;Lnet/minecraft/class_1799;)V"},
      at = {@At("HEAD")}
   )
   private void permadeath$consumeStacks(class_1657 player, class_1799 stack, CallbackInfo ci) {
      class_1937 var5 = player.method_37908();
      if (var5 instanceof class_3218 serverLevel) {
         Optional<class_8786<class_3955>> recipeHolder = serverLevel.method_8503().method_3772().method_8132(class_3956.field_17545, this.field_7870.method_59961(), serverLevel);
         if (recipeHolder.isPresent()) {
            class_3955 recipe = (class_3955)((class_8786)recipeHolder.get()).comp_1933();
            if (recipe instanceof HyperAppleRecipe) {
               long day = DateManager.getCurrentRealDay();

               for(int i = 0; i < this.field_7870.method_5439(); ++i) {
                  if (i != 4) {
                     this.field_7870.method_5438(i).method_7934(7);
                  } else if (day >= 60L) {
                     this.field_7870.method_5438(i).method_7934(63);
                  }
               }
            }

            if (recipe instanceof SuperAppleRecipe) {
               for(int i = 0; i < this.field_7870.method_5439(); ++i) {
                  if (i != 4) {
                     this.field_7870.method_5438(i).method_7934(7);
                  }
               }
            } else if (recipe instanceof BeginningRelicRecipe) {
               int[] edgeSlots = new int[]{1, 3, 5, 7};

               for(int slot : edgeSlots) {
                  this.field_7870.method_5438(slot).method_7934(31);
               }
            } else if (recipe instanceof LifeOrb) {
               int[] edgeSlots = new int[]{0, 1, 2, 3, 5, 6, 7, 8};

               for(int slot : edgeSlots) {
                  this.field_7870.method_5438(slot).method_7934(63);
               }
            }

         }
      }
   }
}
