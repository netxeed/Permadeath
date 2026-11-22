package com.serthekiller.permadeath.mixin;

import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public class SleepCounter {
   @Mixin({class_1657.class})
   public interface PlayerAccessor {
      @Accessor("field_7487")
      void setSleepCounter(int var1);
   }
}
