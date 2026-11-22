package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1295;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1295.class})
public class AreaEffectCloudMixin {
   @Unique
   private int permadeath$damageTimer = 0;
   @Unique
   private int permadeath$lightningTimer = 0;

   @Inject(
      method = {"method_5773()V"},
      at = {@At("TAIL")}
   )
   private void customCloudLogic(CallbackInfo ci) {
      class_1295 cloud = (class_1295)this;
      if (!cloud.method_37908().field_9236) {
         if (cloud.method_5752().contains("LightningCloud")) {
            this.handleLightningCloud(cloud);
         } else {
            ++this.permadeath$damageTimer;
            if (this.permadeath$damageTimer >= 10) {
               this.permadeath$damageTimer = 0;

               for(class_1309 entity : cloud.method_37908().method_18467(class_1309.class, cloud.method_5829().method_1014((double)0.5F))) {
                  if (cloud.method_5600().method_10295() == class_2398.field_11204) {
                     if (cloud.method_5601() instanceof class_1309) {
                        entity.method_64419(cloud.method_48923().method_48815(cloud, cloud.method_5601()), 10.0F);
                     } else {
                        entity.method_64419(cloud.method_48923().method_48831(), 10.0F);
                     }
                  }

                  if (cloud.method_5600().method_10295() == class_2398.field_17430 && entity instanceof class_1657) {
                     entity.method_6012();
                  }

                  if (cloud.method_5752().contains("DarkHearts")) {
                     if (cloud.method_5601() instanceof class_1309) {
                        entity.method_64419(cloud.method_48923().method_48815(cloud, cloud.method_5601()), 12.0F);
                     } else {
                        entity.method_64419(cloud.method_48923().method_48831(), 12.0F);
                     }
                  }
               }
            }

         }
      }
   }

   @Unique
   private void handleLightningCloud(class_1295 cloud) {
      class_3218 world = (class_3218)cloud.method_37908();
      ++this.permadeath$lightningTimer;
      if (this.permadeath$lightningTimer >= 20) {
         this.permadeath$lightningTimer = 0;
         class_1538 lightning = new class_1538(class_1299.field_6112, world);
         if (lightning != null) {
            lightning.method_24203(cloud.method_23317(), cloud.method_23318(), cloud.method_23321());
            lightning.method_29498(false);
            world.method_8649(lightning);
         }
      }

   }
}
