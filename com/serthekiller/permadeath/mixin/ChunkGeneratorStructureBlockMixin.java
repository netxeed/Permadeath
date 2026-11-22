package com.serthekiller.permadeath.mixin;

import com.serthekiller.permadeath.utils.DateManager;
import java.util.Set;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2791;
import net.minecraft.class_2794;
import net.minecraft.class_2960;
import net.minecraft.class_3195;
import net.minecraft.class_3485;
import net.minecraft.class_4076;
import net.minecraft.class_5138;
import net.minecraft.class_5321;
import net.minecraft.class_5455;
import net.minecraft.class_6880;
import net.minecraft.class_7059;
import net.minecraft.class_7138;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2794.class})
public class ChunkGeneratorStructureBlockMixin {
   @Unique
   private static final Set<class_2960> BLOCKED_STRUCTURES = Set.of(class_2960.method_60656("village_plains"), class_2960.method_60656("village_desert"), class_2960.method_60656("village_savanna"), class_2960.method_60656("village_taiga"), class_2960.method_60656("village_snowy"), class_2960.method_60656("desert_pyramid"), class_2960.method_60656("jungle_temple"), class_2960.method_60656("monument"), class_2960.method_60656("shipwreck"), class_2960.method_60656("shipwreck_beached"));

   @Inject(
      method = {"method_41044(Lnet/minecraft/class_7059$class_7060;Lnet/minecraft/class_5138;Lnet/minecraft/class_5455;Lnet/minecraft/class_7138;Lnet/minecraft/class_3485;JLnet/minecraft/class_2791;Lnet/minecraft/class_1923;Lnet/minecraft/class_4076;Lnet/minecraft/class_5321;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void permadeath$blockStructuresAfterDay40(class_7059.class_7060 structureSelectionEntry, class_5138 structureManager, class_5455 registryAccess, class_7138 randomState, class_3485 structureTemplateManager, long l, class_2791 chunkAccess, class_1923 chunkPos, class_4076 sectionPos, class_5321<class_1937> resourceKey, CallbackInfoReturnable<Boolean> cir) {
      if (DateManager.getCurrentRealDay() >= 40L && DateManager.getCurrentRealDay() <= 70L) {
         class_6880<class_3195> holder = structureSelectionEntry.comp_512();
         holder.method_40230().map(class_5321::method_29177).ifPresent((id) -> {
            if (BLOCKED_STRUCTURES.contains(id)) {
               cir.setReturnValue(false);
            }

         });
      }
   }
}
