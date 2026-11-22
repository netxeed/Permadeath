package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1308;
import net.minecraft.class_7;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1308.class})
public interface MobPathfindingAccessor {
   @Invoker("method_5941")
   void permadeath$setPathfindingMalus(class_7 var1, float var2);
}
