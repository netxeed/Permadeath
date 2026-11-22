package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1308;
import net.minecraft.class_1355;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1308.class})
public interface MobAccessor {
   @Accessor("field_6201")
   class_1355 getGoalSelector();

   @Accessor("field_6185")
   class_1355 getTargetSelector();
}
