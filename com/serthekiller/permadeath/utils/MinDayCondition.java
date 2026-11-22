package com.serthekiller.permadeath.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.class_2960;
import net.minecraft.class_6903;
import org.jetbrains.annotations.Nullable;

public record MinDayCondition(int minDay) implements ResourceCondition {
   public static final MapCodec<MinDayCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.INT.fieldOf("min_day").forGetter(MinDayCondition::minDay)).apply(instance, MinDayCondition::new));
   public static final ResourceConditionType<MinDayCondition> TYPE;

   public ResourceConditionType<?> getType() {
      return TYPE;
   }

   public boolean test(class_6903.@Nullable class_7863 registryInfoLookup) {
      return DateManager.getCurrentRealDay() >= (long)this.minDay;
   }

   public static void register() {
      ResourceConditions.register(TYPE);
   }

   static {
      TYPE = ResourceConditionType.create(class_2960.method_60655("permadeath", "min_day"), CODEC);
   }
}
