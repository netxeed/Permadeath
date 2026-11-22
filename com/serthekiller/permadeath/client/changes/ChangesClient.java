package com.serthekiller.permadeath.client.changes;

import com.serthekiller.permadeath.changes.DaySyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675.class_307;

@Environment(EnvType.CLIENT)
public class ChangesClient implements ClientModInitializer {
   private static class_304 openChanges;
   public static volatile long currentDay = 0L;

   public void onInitializeClient() {
      openChanges = KeyBindingHelper.registerKeyBinding(new class_304("key.permadeath.changes", class_307.field_1668, 80, "key.categories.permadeath"));
      ClientPlayNetworking.registerGlobalReceiver(DaySyncPayload.TYPE, (payload, context) -> currentDay = payload.day());
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         while(openChanges.method_1436()) {
            if (client.field_1724 != null && client.field_1755 == null) {
               class_310.method_1551().method_1507(new ChangesScreen(currentDay));
            }
         }

      });
   }
}
