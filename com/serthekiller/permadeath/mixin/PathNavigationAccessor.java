package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1408;
import net.minecraft.class_8;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1408.class})
public interface PathNavigationAccessor {
   @Accessor("field_6678")
   class_8 getNodeEvaluator();

   @Accessor("field_6678")
   void setNodeEvaluator(class_8 var1);
}
