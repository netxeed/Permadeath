package com.serthekiller.permadeath.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.serthekiller.permadeath.utils.SurvivalAchievementManager;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2170.class})
public class CommandMonitorMixin {
   private static final Set<String> BANNED = Set.of("gamemode", "defaultgamemode", "gamerule", "time", "weather", "difficulty", "effect", "give", "clear", "xp", "experience", "enchant", "attribute", "item", "loot", "summon", "setblock", "fill", "fillbiome", "clone", "place", "kill", "damage", "tp", "teleport", "spreadplayers", "data", "setworldspawn", "spawnpoint");

   @Inject(
      method = {"method_9249(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)V"},
      at = {@At("HEAD")}
   )
   private void permadeath$onCommand(ParseResults<class_2168> parseResults, String command, CallbackInfo ci) {
      CommandContextBuilder<class_2168> ctx = parseResults.getContext();
      class_1297 var6 = ((class_2168)ctx.getSource()).method_9228();
      if (var6 instanceof class_3222 player) {
         if (parseResults.getExceptions().isEmpty() && !parseResults.getReader().canRead()) {
            for(CommandContextBuilder<class_2168> c = ctx; c != null; c = c.getChild()) {
               if (!c.getNodes().isEmpty()) {
                  String root = ((ParsedCommandNode)c.getNodes().get(0)).getNode().getName();
                  if (BANNED.contains(root)) {
                     SurvivalAchievementManager.flagCheatDetected(player.method_5682(), player.method_5667(), "comando: /" + command);
                     return;
                  }
               }
            }

         }
      }
   }
}
