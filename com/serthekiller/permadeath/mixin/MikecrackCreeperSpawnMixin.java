package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.Cambios.mikecrack;
import net.minecraft.class_1299;
import net.minecraft.class_1588;
import net.minecraft.class_2338;
import net.minecraft.class_3730;
import net.minecraft.class_5425;
import net.minecraft.class_5819;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1588.class})
public class MikecrackCreeperSpawnMixin {
   private static final Logger LOGGER = LoggerFactory.getLogger("MikecrackCreeperSpawnMixin");
   private static int dbgBypassCount = 0;

   @Inject(
      method = {"method_20680(Lnet/minecraft/class_1299;Lnet/minecraft/class_5425;Lnet/minecraft/class_3730;Lnet/minecraft/class_2338;Lnet/minecraft/class_5819;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void mikecrack_bypassCreeperSpawnRules(class_1299<? extends class_1588> type, class_5425 level, class_3730 reason, class_2338 pos, class_5819 random, CallbackInfoReturnable<Boolean> cir) {
      if (type == class_1299.field_6046 && mikecrack.isEnabled()) {
         cir.setReturnValue(true);
         ++dbgBypassCount;
         if (dbgBypassCount % 20 == 1) {
            LOGGER.info("Mikecrack: checkMonsterSpawnRules bypass activo (llamadas: {})", dbgBypassCount);
         }
      }

   }
}
