package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.items.CustomArmorItem;
import com.serthekiller.permadeath.items.ModArmorMaterials;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_9279;
import net.minecraft.class_9334;
import net.minecraft.class_1304.class_1305;
import net.minecraft.class_1322.class_1323;

public class ArmorBonusHandler {
   private static final class_2960 HEALTH_MODIFIER_ID = class_2960.method_60655("permadeath", "armor_health_bonus");
   private static final int RESISTANCE_DURATION_TICKS = 40;
   private static final Map<UUID, Double> playerBonusState = new HashMap();

   public static void init() {
      ServerTickEvents.END_SERVER_TICK.register((ServerTickEvents.EndTick)(server) -> {
         for(class_3222 player : server.method_3760().method_14571()) {
            updateArmorBonus(player);
            updateInfernalResistance(player);
         }

      });
      ServerPlayerEvents.COPY_FROM.register((ServerPlayerEvents.CopyFrom)(oldPlayer, newPlayer, alive) -> cleanupPlayer(oldPlayer.method_5667()));
   }

   private static void updateInfernalResistance(class_3222 player) {
      if (hasFullInfernalSet(player)) {
         class_1293 res = player.method_6112(class_1294.field_5907);
         if (res == null || res.method_5584() < 20) {
            player.method_6092(new class_1293(class_1294.field_5907, 40, 0, false, false));
         }
      }

   }

   private static boolean hasFullInfernalSet(class_3222 player) {
      class_1799 helmet = player.method_6118(class_1304.field_6169);
      if (!helmet.method_31574(ModArmorMaterials.NETHERITE_INFERNAL_HELMET)) {
         return false;
      } else {
         class_1799 chest = player.method_6118(class_1304.field_6174);
         boolean validChest = chest.method_31574(ModArmorMaterials.NETHERITE_INFERNAL_CHESTPLATE) || isArmoredElytra(chest);
         if (!validChest) {
            return false;
         } else {
            class_1799 leggings = player.method_6118(class_1304.field_6172);
            if (!leggings.method_31574(ModArmorMaterials.NETHERITE_INFERNAL_LEGGINGS)) {
               return false;
            } else {
               class_1799 boots = player.method_6118(class_1304.field_6166);
               return boots.method_31574(ModArmorMaterials.NETHERITE_INFERNAL_BOOTS);
            }
         }
      }
   }

   private static boolean isArmoredElytra(class_1799 stack) {
      if (!stack.method_31574(class_1802.field_8833)) {
         return false;
      } else {
         class_9279 customData = (class_9279)stack.method_57824(class_9334.field_49628);
         if (customData == null) {
            return false;
         } else {
            class_2487 tag = customData.method_57461();
            return tag.method_10545("permadeath") && tag.method_10562("permadeath").method_10577("armored_elytra");
         }
      }
   }

   private static void updateArmorBonus(class_3222 player) {
      class_1324 healthAttribute = player.method_5996(class_5134.field_23716);
      if (healthAttribute != null) {
         double bonusHealth = (double)0.0F;
         int customPieces = 0;
         double firstBonus = (double)0.0F;

         for(class_1304 slot : class_1304.values()) {
            if (slot.method_5925() == class_1305.field_6178) {
               class_1799 stack = player.method_6118(slot);
               class_1792 var16 = stack.method_7909();
               double pieceBonus;
               boolean counts;
               if (var16 instanceof CustomArmorItem) {
                  CustomArmorItem customArmor = (CustomArmorItem)var16;
                  counts = true;
                  pieceBonus = customArmor.getBonusHealth();
               } else if (slot == class_1304.field_6174 && isArmoredElytra(stack)) {
                  counts = true;
                  pieceBonus = (double)10.0F;
               } else {
                  counts = false;
                  pieceBonus = (double)0.0F;
               }

               if (counts) {
                  ++customPieces;
                  if (firstBonus == (double)0.0F) {
                     firstBonus = pieceBonus;
                  } else if (firstBonus != pieceBonus) {
                     customPieces = 0;
                     break;
                  }
               }
            }
         }

         if (customPieces == 4) {
            bonusHealth = firstBonus;
         }

         UUID playerId = player.method_5667();
         Double previousBonus = (Double)playerBonusState.get(playerId);
         if (previousBonus == null || !previousBonus.equals(bonusHealth)) {
            if (healthAttribute.method_6199(HEALTH_MODIFIER_ID) != null) {
               healthAttribute.method_6200(HEALTH_MODIFIER_ID);
            }

            if (bonusHealth > (double)0.0F) {
               class_1322 modifier = new class_1322(HEALTH_MODIFIER_ID, bonusHealth, class_1323.field_6328);
               healthAttribute.method_26837(modifier);
            } else if (previousBonus != null && previousBonus > (double)0.0F) {
               float newMaxHealth = (float)healthAttribute.method_6194();
               if (player.method_6032() > newMaxHealth) {
                  player.method_6033(newMaxHealth);
               }
            }

            playerBonusState.put(playerId, bonusHealth);
         }

      }
   }

   public static void cleanupPlayer(UUID playerId) {
      playerBonusState.remove(playerId);
   }
}
