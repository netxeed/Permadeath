package com.serthekiller.permadeath.mixin;

import net.minecraft.class_3283;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftServer.class})
public interface MinecraftServerAccessor {
   @Accessor("field_4595")
   class_3283 getPackRepository();
}
