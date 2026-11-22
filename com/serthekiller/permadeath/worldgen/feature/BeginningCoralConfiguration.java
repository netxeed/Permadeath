package com.serthekiller.permadeath.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.class_2960;
import net.minecraft.class_3037;

public record BeginningCoralConfiguration(List<class_2960> structures) implements class_3037 {
   public static final Codec<BeginningCoralConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance.group(class_2960.field_25139.listOf().fieldOf("structures").forGetter(BeginningCoralConfiguration::structures)).apply(instance, BeginningCoralConfiguration::new));
}
