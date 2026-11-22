package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1309.class})
public abstract class MixinPlayerFoodEffects {
   long currentDay = DateManager.getCurrentRealDay();
   @Shadow
   protected class_1799 field_6277;

   @Inject(
      method = {"method_6040()V"},
      at = {@At("HEAD")}
   )
   private void permadeath$onCompleteUsingItem(CallbackInfo ci) {
      class_1309 self = (class_1309)this;
      if (!self.method_37908().field_9236) {
         if (self instanceof class_1657) {
            class_1657 player = (class_1657)self;
            if (!this.field_6277.method_7960()) {
               int PERMANENT = -1;
               if (this.currentDay < 70L) {
                  if (this.field_6277.method_31574(class_1802.field_8741)) {
                     if (this.currentDay >= 60L) {
                        player.method_6092(new class_1293(class_1294.field_5921, 1, 3));
                     } else if (this.currentDay >= 50L) {
                        player.method_6092(new class_1293(class_1294.field_5922, 100, 0, false, true));
                     }
                  }

                  if (this.field_6277.method_31574(class_1802.field_8680) && this.currentDay >= 50L) {
                     player.method_6092(new class_1293(class_1294.field_5899, -1, 0, false, true));
                     player.method_6092(new class_1293(class_1294.field_5916, -1, 0, false, true));
                  }

                  if (this.field_6277.method_31574(class_1802.field_8511) && this.currentDay >= 50L) {
                     player.method_6092(new class_1293(class_1294.field_5903, -1, 0, false, true));
                     player.method_6092(new class_1293(class_1294.field_5911, -1, 0, false, true));
                  }

                  if (this.field_6277.method_31574(class_1802.field_8635) && this.currentDay >= 50L) {
                     player.method_6092(new class_1293(class_1294.field_5899, -1, 1, false, true));
                     player.method_6092(new class_1293(class_1294.field_5909, -1, 0, false, true));
                  }

                  if (this.field_6277.method_31574(class_1802.field_8323) && this.currentDay >= 50L) {
                     player.method_6092(new class_1293(class_1294.field_5916, -1, 0, false, true));
                     player.method_6092(new class_1293(class_1294.field_5899, -1, 1, false, true));
                     player.method_6092(new class_1293(class_1294.field_5903, -1, 1, false, true));
                  }

                  if ((this.field_6277.method_31574(class_1802.field_8389) || this.field_6277.method_31574(class_1802.field_8726) || this.field_6277.method_31574(class_1802.field_8046) || this.field_6277.method_31574(class_1802.field_8748) || this.field_6277.method_31574(class_1802.field_8504) || this.field_6277.method_31574(class_1802.field_8429) || this.field_6277.method_31574(class_1802.field_8209)) && this.currentDay >= 80L) {
                     player.method_6092(new class_1293(class_1294.field_5903, -1, 0, false, true));
                  }

               }
            }
         }
      }
   }
}
