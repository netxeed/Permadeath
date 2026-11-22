package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1606;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1606.class})
public interface ShulkerAccessor {
   @Invoker("method_7122")
   void invokeSetRawPeekAmount(int var1);
}
