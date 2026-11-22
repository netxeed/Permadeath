package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.dimension.TheBeginningEventHandler;
import com.serthekiller.permadeath.storm.StormHandler;
import com.serthekiller.permadeath.utils.DateManager;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2329;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_5454;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2329.class})
public abstract class EndGatewayMixin {
   @Inject(
      method = {"method_60770(Lnet/minecraft/class_3218;Lnet/minecraft/class_1297;Lnet/minecraft/class_2338;)Lnet/minecraft/class_5454;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGatewayTeleport(class_3218 serverLevel, class_1297 entity, class_2338 blockPos, CallbackInfoReturnable<class_5454> cir) {
      if (entity instanceof class_3222 serverPlayer) {
         long currentDay = DateManager.getCurrentRealDay();
         class_1937 level = serverPlayer.method_37908();
         if (level.method_27983() == class_1937.field_25179 && currentDay >= 40L && currentDay < 50L) {
            if (serverPlayer.field_13974.method_14257().method_8388() || serverPlayer.field_13974.method_14257().method_8386()) {
               serverPlayer.method_5859(serverPlayer.method_23317(), (double)-100.0F, serverPlayer.method_23321());
               cir.cancel();
            }

         } else if (level.method_27983() == class_1937.field_25179 && currentDay >= 50L) {
            class_3218 destination = ((MinecraftServer)Objects.requireNonNull(serverPlayer.method_5682())).method_3847(ModDimensions.THE_BEGINNING_LEVEL_KEY);
            if (destination != null) {
               if (!StormHandler.isStormActive()) {
                  class_243 targetPos = TheBeginningEventHandler.ensurePortalAndGetSpawn(destination);
                  cir.cancel();
                  cir.setReturnValue(new class_5454(destination, targetPos, serverPlayer.method_18798(), serverPlayer.method_36454(), serverPlayer.method_36455() + 1.0F, class_5454.field_52247));
               } else {
                  class_3218 overworld = ((MinecraftServer)Objects.requireNonNull(serverPlayer.method_5682())).method_3847(class_1937.field_25179);
                  if (overworld == null) {
                     return;
                  }

                  class_243 spawnPos = TheBeginningEventHandler.getPlayerSpawnInOverworld(serverPlayer, overworld);
                  cir.cancel();
                  cir.setReturnValue(new class_5454(overworld, spawnPos, class_243.field_1353, serverPlayer.method_36454(), serverPlayer.method_36455(), class_5454.field_52247));
               }

            }
         } else {
            if (level.method_27983() == ModDimensions.THE_BEGINNING_LEVEL_KEY && currentDay >= 50L) {
               class_3218 overworld = ((MinecraftServer)Objects.requireNonNull(serverPlayer.method_5682())).method_3847(class_1937.field_25179);
               if (overworld == null) {
                  return;
               }

               class_243 spawnPos = TheBeginningEventHandler.getPlayerSpawnInOverworld(serverPlayer, overworld);
               cir.cancel();
               cir.setReturnValue(new class_5454(overworld, spawnPos, class_243.field_1353, serverPlayer.method_36454(), serverPlayer.method_36455(), class_5454.field_52247));
            }

         }
      }
   }
}
