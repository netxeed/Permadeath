package com.serthekiller.permadeath.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.serthekiller.permadeath.utils.SpawnListRewriter;
import net.minecraft.class_1299;
import net.minecraft.class_1308;
import net.minecraft.class_1311;
import net.minecraft.class_1948;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_1948.class_5262.class})
public class SpawnStateMixin {
   @WrapOperation(
      method = {"method_27825(Lnet/minecraft/class_1308;Lnet/minecraft/class_2791;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1299;method_5891()Lnet/minecraft/class_1311;"
)}
   )
   private class_1311 permadeath$capCategory(class_1299<?> type, Operation<class_1311> original, @Local(argsOnly = true) class_1308 mob) {
      return SpawnListRewriter.capCategory(mob, (class_1311)original.call(new Object[]{type}));
   }
}
