package com.serthekiller.permadeath.mixin;

import java.util.Map;
import net.minecraft.class_10289;
import net.minecraft.class_2960;
import net.minecraft.class_8786;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_10289.class})
public interface RecipeMapAccessor {
   @Accessor("field_54645")
   Map<class_2960, class_8786<?>> getByKey();

   @Mutable
   @Accessor("field_54645")
   void setByKey(Map<class_2960, class_8786<?>> var1);
}
