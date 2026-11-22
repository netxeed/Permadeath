package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.CrystalRegenManager;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1511;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1511.class})
public abstract class EndCrystalMixin extends class_1297 {
   @Unique
   private boolean hasRegeneratedOnce = false;

   public EndCrystalMixin(class_1299<?> entityType, class_1937 level) {
      super(entityType, level);
   }

   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void onTick(CallbackInfo ci) {
      if (!this.method_37908().field_9236) {
         class_1937 var3 = this.method_37908();
         if (var3 instanceof class_3218) {
            class_3218 serverLevel = (class_3218)var3;
            CrystalRegenManager.register(serverLevel, this.method_24515());
         }
      }

   }

   @Inject(
      method = {"method_5652(Lnet/minecraft/class_2487;)V"},
      at = {@At("TAIL")}
   )
   private void saveRegenerationData(class_2487 compound, CallbackInfo ci) {
      compound.method_10556("PermadeathHasRegenerated", this.hasRegeneratedOnce);
   }

   @Inject(
      method = {"method_5749(Lnet/minecraft/class_2487;)V"},
      at = {@At("TAIL")}
   )
   private void loadRegenerationData(class_2487 compound, CallbackInfo ci) {
      if (compound.method_10545("PermadeathHasRegenerated")) {
         this.hasRegeneratedOnce = compound.method_10577("PermadeathHasRegenerated");
      }

   }
}
