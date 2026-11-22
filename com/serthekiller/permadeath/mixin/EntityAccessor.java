package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1297.class})
public interface EntityAccessor {
   @Accessor("field_6008")
   void setInvulnerableTime(int var1);
}
