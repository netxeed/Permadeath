package com.serthekiller.permadeath.Cambios;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_1299;
import net.minecraft.class_1324;
import net.minecraft.class_1549;
import net.minecraft.class_1606;
import net.minecraft.class_1699;
import net.minecraft.class_1767;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_5134;

public class ModuloDeMuerte {
   public static void setupCaveSpiderStack(class_1549 spider, class_3218 level) {
      spider.method_5780("ProcessedStack");
      spider.method_5780("UltraCaveSpider");
      ((class_1324)Objects.requireNonNull(spider.method_5996(class_5134.field_23716))).method_6192((double)60.0F);
      spider.method_6033(60.0F);
      spider.method_5665(class_2561.method_43470("Araña Abismal").method_27692(class_124.field_1064));
      spider.method_5880(false);
      spider.method_5780("PotionImmune");
      class_1606 shulker = new class_1606(class_1299.field_6109, level);
      shulker.method_5780("ProcessedStack");
      shulker.method_5780("ShulkerRojo");
      shulker.method_47880(Optional.of(class_1767.field_7964));
      ((class_1324)Objects.requireNonNull(shulker.method_5996(class_5134.field_23716))).method_6192((double)150.0F);
      shulker.method_6033(150.0F);
      shulker.method_5665(class_2561.method_43470("Shulker Explosivo").method_27692(class_124.field_1061));
      shulker.method_5880(false);
      shulker.method_5814(spider.method_23317(), spider.method_23318() + (double)2.0F, spider.method_23321());
      level.method_8649(shulker);
      shulker.method_5875(true);
      shulker.method_5873(spider, true);
      shulker.method_5780("PotionImmune");
      class_1699 minecart = new class_1699(class_1299.field_6142, level);
      minecart.method_5814(spider.method_23317(), spider.method_23318(), spider.method_23321());
      minecart.method_5780("ProcessedStack");
      minecart.method_5780("PotionSpawnerMinecart");
      class_2487 spawnEntityTag = new class_2487();
      spawnEntityTag.method_10582("id", "minecraft:potion");
      class_2487 itemTag = new class_2487();
      itemTag.method_10582("id", "minecraft:splash_potion");
      itemTag.method_10569("count", 1);
      class_2487 componentsTag = new class_2487();
      class_2487 potionContents = new class_2487();
      potionContents.method_10582("potion", "minecraft:strong_harming");
      class_2499 customEffects = new class_2499();
      class_2487 instantDamage = new class_2487();
      instantDamage.method_10582("id", "minecraft:instant_damage");
      instantDamage.method_10569("amplifier", 3);
      instantDamage.method_10569("duration", 1);
      customEffects.add(instantDamage);
      potionContents.method_10566("custom_effects", customEffects);
      componentsTag.method_10566("minecraft:potion_contents", potionContents);
      itemTag.method_10566("components", componentsTag);
      spawnEntityTag.method_10566("Item", itemTag);
      class_2487 spawnDataTag = new class_2487();
      spawnDataTag.method_10566("entity", spawnEntityTag);
      class_2487 spawnerNbt = new class_2487();
      spawnerNbt.method_10566("SpawnData", spawnDataTag);
      spawnerNbt.method_10575("Delay", (short)40);
      spawnerNbt.method_10575("MinSpawnDelay", (short)30);
      spawnerNbt.method_10575("MaxSpawnDelay", (short)50);
      spawnerNbt.method_10575("SpawnCount", (short)1);
      spawnerNbt.method_10575("MaxNearbyEntities", (short)6);
      spawnerNbt.method_10575("RequiredPlayerRange", (short)16);
      spawnerNbt.method_10575("SpawnRange", (short)2);
      class_2487 minecartNbt = new class_2487();
      minecart.method_5662(minecartNbt);

      for(String key : spawnerNbt.method_10541()) {
         minecartNbt.method_10566(key, spawnerNbt.method_10580(key));
      }

      minecart.method_5651(minecartNbt);
      minecart.field_5960 = true;
      level.method_8649(minecart);
      minecart.method_5873(shulker, true);
   }
}
