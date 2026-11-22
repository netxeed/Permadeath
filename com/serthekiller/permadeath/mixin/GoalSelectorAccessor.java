package com.serthekiller.permadeath.mixin;

import java.util.Set;
import net.minecraft.class_1355;
import net.minecraft.class_4135;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1355.class})
public interface GoalSelectorAccessor {
   @Accessor("field_6461")
   Set<class_4135> getAvailableGoals();
}
