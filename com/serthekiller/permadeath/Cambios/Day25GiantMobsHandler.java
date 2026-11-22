package com.serthekiller.permadeath.Cambios;

import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.items.ModArmorMaterials;
import com.serthekiller.permadeath.utils.DateManager;
import net.minecraft.class_1282;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1542;
import net.minecraft.class_1571;
import net.minecraft.class_1589;
import net.minecraft.class_1621;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_5134;

public class Day25GiantMobsHandler {
   public static void transformGiantMobs(class_1309 entity, class_3218 serverLevel) {
      if (!serverLevel.field_9236) {
         long currentDay = DateManager.getCurrentRealDay();
         if (currentDay >= 25L) {
            if (entity instanceof class_1621) {
               class_1621 slime = (class_1621)entity;
               if (!(entity instanceof class_1589)) {
                  int currentSize = slime.method_7152();
                  if ((currentSize == 1 || currentSize == 2 || currentSize == 3 || currentSize == 4) && !slime.method_16914()) {
                     slime.method_5665(class_2561.method_43470("§6GIGA Slime"));
                     slime.method_5880(false);
                     slime.method_7161(15, true);
                     class_1324 maxHealthAttribute = slime.method_5996(class_5134.field_23716);
                     if (maxHealthAttribute != null) {
                        double currentMaxHealth = maxHealthAttribute.method_6201();
                        maxHealthAttribute.method_6192(currentMaxHealth * (double)2.0F);
                        slime.method_6033(slime.method_6063());
                     }
                  }
               }
            }

            if (entity instanceof class_1589) {
               class_1589 magmaCube = (class_1589)entity;
               int currentSize = magmaCube.method_7152();
               if ((currentSize == 1 || currentSize == 2 || currentSize == 3 || currentSize == 4) && !magmaCube.method_16914()) {
                  magmaCube.method_5665(class_2561.method_43470("§6GIGA MagmaCube"));
                  magmaCube.method_7161(16, true);
                  magmaCube.method_5880(false);
               }
            }

            if (entity instanceof class_1571) {
               class_1571 ghast = (class_1571)entity;
               if (entity.method_37908().method_27983() != class_1937.field_25181 && entity.method_37908().method_27983() != ModDimensions.THE_BEGINNING_LEVEL_KEY) {
                  if (currentDay < 40L) {
                     if (!ghast.method_16914() || !ghast.method_5797().getString().contains("Demoníaco") && !ghast.method_5797().getString().contains("Ender")) {
                        class_1324 maxHealthAttribute = ghast.method_5996(class_5134.field_23716);
                        if (maxHealthAttribute != null) {
                           int randomHealth = 40 + serverLevel.field_9229.method_43048(21);
                           maxHealthAttribute.method_6192((double)randomHealth);
                           ghast.method_6033((float)randomHealth);
                        }

                        ghast.method_5665(class_2561.method_43470("§6Ghast Demoníaco"));
                        ghast.method_5880(false);
                     }
                  } else if (!ghast.method_16914()) {
                     int chance = serverLevel.field_9229.method_43048(100);
                     if (chance > 75) {
                        class_1324 maxHealthAttribute = ghast.method_5996(class_5134.field_23716);
                        if (maxHealthAttribute != null) {
                           int randomHealth = 40 + serverLevel.field_9229.method_43048(21);
                           maxHealthAttribute.method_6192((double)randomHealth);
                           ghast.method_6033((float)randomHealth);
                        }

                        ghast.method_5665(class_2561.method_43470("§6Ghast Demoníaco"));
                        ghast.method_5880(false);
                     } else {
                        class_1324 maxHealthAttribute = ghast.method_5996(class_5134.field_23716);
                        if (maxHealthAttribute != null) {
                           maxHealthAttribute.method_6192((double)60.0F);
                           ghast.method_6033(60.0F);
                        }

                        ghast.method_5665(class_2561.method_43470("§6Demonio Flotante"));
                        ghast.method_5880(false);
                     }
                  }
               }
            }

         }
      }
   }

   private static boolean isDemonicGhast(class_1571 ghast) {
      return ghast.method_16914() && ghast.method_5797().getString().contains("Demoníaco");
   }

   public static void handleGiantMobArmorDrops(class_1309 entity, class_1282 damageSource) {
      if (!entity.method_37908().field_9236) {
         long currentDay = DateManager.getCurrentRealDay() + 1L;
         if (currentDay >= 25L) {
            if (entity.method_16914()) {
               String customName = entity.method_5797().getString();
               float dropChance = 0.1F;
               int roll = entity.method_37908().field_9229.method_43048(100);
               class_1799 armorPiece = null;
               String mobType = "";
               if (entity instanceof class_1621 && customName.contains("GIGA Slime")) {
                  mobType = "GIGAslime";
                  armorPiece = new class_1799(ModArmorMaterials.NETHERITE_CHESTPLATE);
               } else if (entity instanceof class_1589 && customName.contains("GIGA MagmaCube")) {
                  mobType = "GIGA MagmaCube";
                  armorPiece = new class_1799(ModArmorMaterials.NETHERITE_LEGGINGS);
               } else if (entity instanceof class_1571 && customName.contains("Demoníaco")) {
                  mobType = "Ghast Demoníaco";
                  armorPiece = new class_1799(ModArmorMaterials.NETHERITE_BOOTS);
                  dropChance = 0.2F;
               }

               if (armorPiece != null && (float)roll < dropChance * 100.0F) {
                  class_1542 itemEntity = new class_1542((class_3218)entity.method_37908(), entity.method_23317(), entity.method_23318(), entity.method_23321(), armorPiece);
                  entity.method_37908().method_8649(itemEntity);
               } else if (armorPiece != null) {
               }

            }
         }
      }
   }
}
