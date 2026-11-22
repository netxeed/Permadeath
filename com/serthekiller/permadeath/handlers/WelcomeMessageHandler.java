package com.serthekiller.permadeath.handlers;

import com.serthekiller.permadeath.utils.ServerModeManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.class_124;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_3222;
import net.minecraft.class_5250;
import net.minecraft.class_2558.class_2559;
import net.minecraft.class_2568.class_5247;
import net.minecraft.server.MinecraftServer;

public class WelcomeMessageHandler {
   public static void init() {
      ServerPlayConnectionEvents.JOIN.register((ServerPlayConnectionEvents.Join)(handler, sender, server) -> {
         class_3222 player = handler.method_32311();
         sendWelcomeMessage(player);
      });
   }

   private static void sendWelcomeMessage(class_3222 player) {
      player.method_64398(class_2561.method_43473());
      player.method_64398(class_2561.method_43470("§8§m                                                    "));
      player.method_64398(class_2561.method_43470("¡Bienvenido a ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470("PERMADEATHCORE").method_27695(new class_124[]{class_124.field_1061, class_124.field_1067})).method_10852(class_2561.method_43470("!").method_27692(class_124.field_1080)));
      player.method_64398(class_2561.method_43470("Mod Creado por ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470("serthekiller (youtube.com/@serthekiller)").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067})));
   }