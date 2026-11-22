package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.utils.DateManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.class_124;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_5134;
import net.minecraft.class_1322.class_1323;
import net.minecraft.server.MinecraftServer;

public class HyperGoldenApplePlus extends class_1792 {
   private static final class_2960 HEALTH_MODIFIER_ID = class_2960.method_60655("permadeath", "permanent_health_boost");
   private static final double HP_PER_APPLE = (double)4.0F;
   private static final Map<UUID, Integer> playersConsumedCount = new HashMap();

   public HyperGoldenApplePlus(class_1792.class_1793 properties) {
      super(properties);
   }

   public static void registerRespawnHandler() {
      ServerPlayerEvents.AFTER_RESPAWN.register((ServerPlayerEvents.AfterRespawn)(oldPlayer, newPlayer, alive) -> {
         int consumed = (Integer)playersConsumedCount.getOrDefault(newPlayer.method_5667(), 0);
         if (consumed > 0) {
            class_1324 healthAttr = newPlayer.method_5996(class_5134.field_23716);
            if (healthAttr != null) {
               healthAttr.method_6200(HEALTH_MODIFIER_ID);
               healthAttr.method_26837(new class_1322(HEALTH_MODIFIER_ID, (double)consumed * (double)4.0F, class_1323.field_6328));
               newPlayer.method_6033(newPlayer.method_6063());
            }
         }
      });
   }

   public boolean method_7886(class_1799 stack) {
      return false;
   }

   public class_1799 method_7861(class_1799 stack, class_1937 level, class_1309 entity) {
      if (!level.field_9236 && entity instanceof class_3222 player) {
         UUID playerUUID = player.method_5667();
         long day = DateManager.getCurrentRealDay();
         int consumed = (Integer)playersConsumedCount.getOrDefault(playerUUID, 0);
         if (day < 40L) {
            player.method_64398(class_2561.method_43470("Esta manzana es demasiado poderosa para ser consumida antes del día 40.").method_27692(class_124.field_1061));
            return stack;
         }

         if (day < 60L && consumed >= 1) {
            player.method_64398(class_2561.method_43470("Ya has alcanzado el límite de 1 manzana antes del día 60.").method_27692(class_124.field_1061));
            this.playFailSound(level, player);
            return stack;
         }

         if (consumed >= 2) {
            player.method_64398(class_2561.method_43470("Ya has consumido el máximo permitido (2 manzanas).").method_27692(class_124.field_1061));
            this.playFailSound(level, player);
            return stack;
         }

         class_1324 healthAttribute = player.method_5996(class_5134.field_23716);
         if (healthAttribute != null) {
            int newCount = consumed + 1;
            double totalExtraHP = (double)newCount * (double)4.0F;
            healthAttribute.method_6200(HEALTH_MODIFIER_ID);
            class_1322 modifier = new class_1322(HEALTH_MODIFIER_ID, totalExtraHP, class_1323.field_6328);
            healthAttribute.method_26837(modifier);
            player.method_6033(player.method_6032() + 4.0F);
            playersConsumedCount.put(playerUUID, newCount);
            MinecraftServer server = player.method_5682();
            if (server != null) {
               HyperAppleDataManager.saveData(server);
            }

            player.method_64398(class_2561.method_43470("¡Han aumentado tus contenedores!").method_27695(new class_124[]{class_124.field_1060, class_124.field_1067}));
            level.method_43128((class_1657)null, player.method_23317(), player.method_23318(), player.method_23321(), class_3417.field_14709, class_3419.field_15248, 1.0F, 1.0F);
         }
      }

      return super.method_7861(stack, level, entity);
   }

   private void playFailSound(class_1937 level, class_3222 player) {
      level.method_43128((class_1657)null, player.method_23317(), player.method_23318(), player.method_23321(), class_3417.field_15102, class_3419.field_15248, 0.5F, 1.0F);
   }

   public static Map<UUID, Integer> getConsumedPlayersMap() {
      return playersConsumedCount;
   }
}
