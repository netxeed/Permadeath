package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1533;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1533.class})
public class ItemFrameMixin {
   @Inject(
      method = {"method_6933(Lnet/minecraft/class_1799;Z)V"},
      at = {@At("HEAD")}
   )
   private void onSetItem(class_1799 stack, boolean update, CallbackInfo ci) {
      class_1533 frame = (class_1533)(Object)this;
      long currentDay = DateManager.getCurrentRealDay();
      if (frame.method_37908().method_27983() == class_1937.field_25181 && stack.method_31574(class_1802.field_8833) && currentDay >= 40L && DateManager.getCurrentRealDay() < 70L) {
         stack.method_7974(stack.method_7936() - 1);
      }

   }
}
