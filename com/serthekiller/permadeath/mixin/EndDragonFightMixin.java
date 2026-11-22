package com.serthekiller.permadeath.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1299;
import net.minecraft.class_1533;
import net.minecraft.class_1686;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2881;
import net.minecraft.class_3218;
import net.minecraft.class_9334;
import net.minecraft.class_2350.class_2353;
import net.minecraft.class_2902.class_2903;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2881.class})
public class EndDragonFightMixin {
   @Shadow
   private class_3218 field_13108;
   @Shadow
   @Final
   private static Logger field_13112;
   @Unique
   private List<class_2338> altarPositions = new ArrayList();
   @Unique
   private int potionTimer = 0;

   @Inject(
      method = {"method_12515()V"},
      at = {@At("TAIL")}
   )
   private void modifyArenaFloor(CallbackInfo ci) {
      int radius = 120;
      int minY = 0;
      int maxY = 128;
      class_2338.class_2339 pos = new class_2338.class_2339();

      for(int x = -radius; x <= radius; ++x) {
         for(int z = -radius; z <= radius; ++z) {
            if (!(Math.sqrt((double)(x * x + z * z)) > (double)radius)) {
               int topY = -1;

               for(int y = maxY; y >= minY; --y) {
                  pos.method_10103(x, y, z);
                  if (this.field_13108.method_8320(pos).method_27852(class_2246.field_10471)) {
                     topY = y;
                     break;
                  }
               }

               if (topY != -1) {
                  for(int y = topY; y >= minY; --y) {
                     pos.method_10103(x, y, z);
                     if (!this.field_13108.method_8320(pos).method_27852(class_2246.field_10471)) {
                        break;
                     }

                     if (this.field_13108.field_9229.method_43057() < 0.35F) {
                        this.field_13108.method_8652(pos, class_2246.field_10462.method_9564(), 3);
                     }
                  }
               }
            }
         }
      }

      if (this.altarPositions.isEmpty()) {
         field_13112.info("Generando altares especiales...");
         this.generateSpecialAltars();
      }

   }

   @Inject(
      method = {"method_12538()V"},
      at = {@At("HEAD")}
   )
   private void onTick(CallbackInfo ci) {
      if (!this.altarPositions.isEmpty()) {
         ++this.potionTimer;
         if (this.potionTimer >= 60) {
            for(class_2338 altarPos : this.altarPositions) {
               field_13112.info("Generando poción en: " + String.valueOf(altarPos.method_10086(3)));
               this.spawnSplashPotion(altarPos.method_10086(3));
            }

            this.potionTimer = 0;
         }
      } else if (this.field_13108.method_8510() % 100L == 0L) {
         field_13112.warn("altarPositions está vacío, los altares no se han generado");
      }

   }

   @Unique
   private void generateSpecialAltars() {
      int r = 70;
      String[] names = new String[]{"Norte", "Sur", "Este", "Oeste"};
      int[][] offsets = new int[][]{{0, -r}, {0, r}, {r, 0}, {-r, 0}};

      for(int i = 0; i < 4; ++i) {
         int x = offsets[i][0];
         int z = offsets[i][1];
         int y = this.field_13108.method_8624(class_2903.field_13197, x, z);
         class_2338 center = new class_2338(x, y, z);
         field_13112.info("Altar " + names[i] + " generado en x: " + x + " y: " + y + " z: " + z);
         this.altarPositions.add(center);

         for(int dx = -2; dx <= 2; ++dx) {
            for(int dz = -2; dz <= 2; ++dz) {
               class_2338 current = center.method_10069(dx, 0, dz);
               if (Math.abs(dx) != 2 && Math.abs(dz) != 2) {
                  this.field_13108.method_8501(current, class_2246.field_10058.method_9564());
               } else {
                  this.field_13108.method_8501(current, class_2246.field_10383.method_9564());
               }
            }
         }

         this.field_13108.method_8501(center.method_10084(), class_2246.field_10536.method_9564());
         class_2338 lanternPos = center.method_10086(4);
         this.field_13108.method_8501(lanternPos, class_2246.field_10174.method_9564());

         for(class_2350 dir : class_2353.field_11062) {
            class_1533 frame = new class_1533(this.field_13108, lanternPos.method_10093(dir), dir);
            class_1799 waterBottle = new class_1799(class_1802.field_8574);
            waterBottle.method_57379(class_9334.field_49651, new class_1844(class_1847.field_8991));
            frame.method_6935(waterBottle);
            this.field_13108.method_8649(frame);
         }
      }

   }

   @Unique
   private void spawnSplashPotion(class_2338 pos) {
      class_1686 thrownPotion = new class_1686(class_1299.field_6045, this.field_13108);
      List<class_1293> customEffects = List.of(new class_1293(class_1294.field_5904, 400, 2), new class_1293(class_1294.field_5924, 400, 1), new class_1293(class_1294.field_5907, 400, 0));
      class_1844 contents = new class_1844(Optional.empty(), Optional.of(16711680), customEffects, Optional.empty());
      class_1799 splashStack = new class_1799(class_1802.field_8436);
      splashStack.method_57379(class_9334.field_49651, contents);
      thrownPotion.method_16940(splashStack);
      thrownPotion.method_5814((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264() + (double)1.0F, (double)pos.method_10260() + (double)0.5F);
      thrownPotion.method_7485((double)0.0F, (double)-0.5F, (double)0.0F, 0.5F, 1.0F);
      this.field_13108.method_8649(thrownPotion);
   }
}
