package com.serthekiller.permadeath.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.serthekiller.permadeath.utils.SpawnListRewriter;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1311;
import net.minecraft.class_1948;
import net.minecraft.class_1959;
import net.minecraft.class_2338;
import net.minecraft.class_2794;
import net.minecraft.class_3218;
import net.minecraft.class_5138;
import net.minecraft.class_5483;
import net.minecraft.class_6012;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1948.class})
public class NaturalSpawnerMixin {
   @Inject(
      method = {"method_29950(Lnet/minecraft/class_3218;Lnet/minecraft/class_5138;Lnet/minecraft/class_2794;Lnet/minecraft/class_1311;Lnet/minecraft/class_2338;Lnet/minecraft/class_6880;)Lnet/minecraft/class_6012;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void permadeath$rewriteSpawns(class_3218 level, class_5138 structureManager, class_2794 generator, class_1311 category, class_2338 pos, class_6880<class_1959> biome, CallbackInfoReturnable<class_6012<class_5483.class_1964>> cir) {
      cir.setReturnValue(SpawnListRewriter.rewrite(level, category, pos, (class_6012)cir.getReturnValue()));
   }

   @WrapOperation(
      method = {"method_27815(ILjava/lang/Iterable;Lnet/minecraft/class_1948$class_5260;Lnet/minecraft/class_6540;)Lnet/minecraft/class_1948$class_5262;"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1299;method_5891()Lnet/minecraft/class_1311;"
)}
   )
   private static class_1311 permadeath$capCategory(class_1299<?> type, Operation<class_1311> original, @Local class_1297 entity) {
      return SpawnListRewriter.capCategory(entity, (class_1311)original.call(new Object[]{type}));
   }
}
