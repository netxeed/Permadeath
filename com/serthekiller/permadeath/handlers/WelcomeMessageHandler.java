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
      player.method_64398(class_2561.method_43470("¡Bienvenido a ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470("PERMADEATH").method_27695(new class_124[]{class_124.field_1061, class_124.field_1067})).method_10852(class_2561.method_43470("!").method_27692(class_124.field_1080)));
      player.method_64398(class_2561.method_43470("Creado por ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470("serthekiller").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067})));
      MinecraftServer server = player.method_5682();
      boolean restricted = server != null && ServerModeManager.get(server).isRestricted();
      boolean showFullCommands = player.method_64475(2) || !restricted;
      player.method_64398(class_2561.method_43473());
      if (showFullCommands) {
         player.method_64398(class_2561.method_43470("Comandos disponibles:").method_27695(new class_124[]{class_124.field_1054, class_124.field_1067}));
         player.method_64398(makeCommandLine("/permadeath changeday", "Cambiar de día/fase"));
         player.method_64398(makeCommandLine("/permadeath info", "Lista de comandos"));
      }

      player.method_64398(class_2561.method_43473());
      player.method_64398(class_2561.method_43470("Suscríbete a su canal: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470("youtube.com/@serthekiller").method_27694((style) -> style.method_10977(class_124.field_1075).method_30938(true).method_10958(new class_2558(class_2559.field_11749, "https://www.youtube.com/@serthekiller")).method_10949(new class_2568(class_5247.field_24342, class_2561.method_43470("Abrir canal"))))));
      player.method_64398(class_2561.method_43470("§8§m                                                    "));
      player.method_64398(class_2561.method_43473());
   }

   private static class_5250 makeCommandLine(String command, String description) {
      return class_2561.method_43470(" » ").method_27692(class_124.field_1063).method_10852(class_2561.method_43470(command).method_27694((style) -> style.method_10977(class_124.field_1060).method_10958(new class_2558(class_2559.field_11745, command)).method_10949(new class_2568(class_5247.field_24342, class_2561.method_43470("Click para usar"))))).method_10852(class_2561.method_43470(" - " + description).method_27692(class_124.field_1080));
   }
}
