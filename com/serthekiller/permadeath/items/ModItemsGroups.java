package com.serthekiller.permadeath.items;

import com.serthekiller.permadeath.PermadeathMod;
import com.serthekiller.permadeath.Cambios.TotemManager;
import java.util.Collections;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_7706;
import net.minecraft.class_1761.class_7705;

public class ModItemsGroups {
   public static void register() {
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(class_1802.field_22030, new class_1935[]{ModArmorMaterials.NETHERITE_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(ModArmorMaterials.NETHERITE_BOOTS, new class_1935[]{ModArmorMaterials.NETHERITE_INFERNAL_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_INFERNAL_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_INFERNAL_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_INFERNAL_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_INFERNAL_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_INFERNAL_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_INFERNAL_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(ModArmorMaterials.NETHERITE_INFERNAL_BOOTS, new class_1935[]{ModArmorMaterials.NETHERITE_DEMONIACA_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_DEMONIACA_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_DEMONIACA_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_DEMONIACA_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_DEMONIACA_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_DEMONIACA_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_DEMONIACA_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(ModArmorMaterials.NETHERITE_DEMONIACA_BOOTS, new class_1935[]{ModArmorMaterials.NETHERITE_CIENTIFICA_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_CIENTIFICA_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_CIENTIFICA_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_CIENTIFICA_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_CIENTIFICA_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_CIENTIFICA_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_CIENTIFICA_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(ModArmorMaterials.NETHERITE_CIENTIFICA_BOOTS, new class_1935[]{ModArmorMaterials.NETHERITE_BOMB_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_BOMB_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_BOMB_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_BOMB_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_BOMB_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_BOMB_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_BOMB_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(ModArmorMaterials.NETHERITE_BOMB_BOOTS, new class_1935[]{ModArmorMaterials.NETHERITE_PINK_HELMET});
         entries.addAfter(ModArmorMaterials.NETHERITE_PINK_HELMET, new class_1935[]{ModArmorMaterials.NETHERITE_PINK_CHESTPLATE});
         entries.addAfter(ModArmorMaterials.NETHERITE_PINK_CHESTPLATE, new class_1935[]{ModArmorMaterials.NETHERITE_PINK_LEGGINGS});
         entries.addAfter(ModArmorMaterials.NETHERITE_PINK_LEGGINGS, new class_1935[]{ModArmorMaterials.NETHERITE_PINK_BOOTS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.addAfter(class_1802.field_8288, Collections.singleton(TotemManager.createSurvivorMedal()), class_7705.field_40191));
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_41061).register((ItemGroupEvents.ModifyEntries)(entries) -> {
         entries.addAfter(class_1802.field_8367, new class_1935[]{ModItems.SUPER_GOLDEN_APPLE_PLUS});
         entries.addAfter(ModItems.SUPER_GOLDEN_APPLE_PLUS, new class_1935[]{ModItems.HYPER_GOLDEN_APPLE_PLUS});
      });
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.addAfter(class_1802.field_8288, new class_1935[]{ModItems.END_RELIC}));
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.addAfter(ModItems.END_RELIC, new class_1935[]{ModItems.BEGINNING_RELIC}));
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.addAfter(class_1802.field_8626, new class_1935[]{ModBlocks.INFERNAL_NETHERITE_BLOCK}));
      ItemGroupEvents.modifyEntriesEvent(class_7706.field_40202).register((ItemGroupEvents.ModifyEntries)(entries) -> entries.addAfter(ModItems.BEGINNING_RELIC, new class_1799[]{ModArmorMaterials.createArmoredElytraStack(), new class_1799(ModItems.LIFE_ORB)}));
      PermadeathMod.LOGGER.info("Items agregados a grupos creativos");
   }
}
