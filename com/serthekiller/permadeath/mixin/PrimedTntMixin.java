package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1540;
import net.minecraft.class_1541;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3218;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1541.class})
public abstract class PrimedTntMixin {
   @Shadow
   public abstract int method_6969();

   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void preExplosionFlyBlocks(CallbackInfo ci) {
      class_1541 tnt = (class_1541)this;
      if (!tnt.method_37908().field_9236 && tnt.method_5752().contains("PermadeathTNT") && tnt.method_6969() <= 1) {
         class_3218 world = (class_3218)tnt.method_37908();
         class_2338 center = tnt.method_24515();
         class_5819 rand = world.method_8409();
         int radius = 6;

         for(int x = -radius; x <= radius; ++x) {
            for(int y = -radius; y <= radius; ++y) {
               for(int z = -radius; z <= radius; ++z) {
                  if (Math.sqrt((double)(x * x + y * y + z * z)) <= (double)radius) {
                     class_2338 targetPos = center.method_10069(x, y, z);
                     class_2680 state = world.method_8320(targetPos);
                     if (!state.method_26215() && state.method_26214(world, targetPos) >= 0.0F) {
                        class_1540 fallingBlock = class_1540.method_40005(world, targetPos, state);
                        double diffX = (double)(targetPos.method_10263() - center.method_10263());
                        double diffZ = (double)(targetPos.method_10260() - center.method_10260());
                        fallingBlock.method_18800(diffX * 0.04 + (rand.method_43058() - (double)0.5F) * 0.15, 0.6 + rand.method_43058() * 0.8, diffZ * 0.04 + (rand.method_43058() - (double)0.5F) * 0.15);
                        fallingBlock.field_7193 = false;
                        world.method_8649(fallingBlock);
                        world.method_8652(targetPos, class_2246.field_10124.method_9564(), 3);
                     }
                  }
               }
            }
         }
      }

   }
}
