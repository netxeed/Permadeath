package com.serthekiller.permadeath.changes;

import com.serthekiller.permadeath.utils.DateManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record DaySyncPayload(long day) implements class_8710 {
   public static final class_8710.class_9154<DaySyncPayload> TYPE = new class_8710.class_9154(class_2960.method_60655("permadeath", "day_sync"));
   public static final class_9139<class_9129, DaySyncPayload> CODEC;

   public class_8710.class_9154<? extends class_8710> method_56479() {
      return TYPE;
   }

   public static void register() {
      PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
      ServerPlayConnectionEvents.JOIN.register((ServerPlayConnectionEvents.Join)(handler, sender, server) -> send(handler.field_14140, currentDay()));
   }

   public static void send(class_3222 player, long day) {
      ServerPlayNetworking.send(player, new DaySyncPayload(day));
   }

   private static long currentDay() {
      return DateManager.getCurrentRealDay();
   }

   static {
      CODEC = class_9139.method_56434(class_9135.field_48551, DaySyncPayload::day, DaySyncPayload::new);
   }
}
