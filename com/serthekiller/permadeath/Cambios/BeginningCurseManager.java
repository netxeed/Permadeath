package com.serthekiller.permadeath.Cambios;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_18;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_26;
import net.minecraft.class_4284;
import net.minecraft.class_7225;
import net.minecraft.server.MinecraftServer;

public class BeginningCurseManager extends class_18 {
   private static final String DATA_NAME = "permadeath_beginning_curse";
   private boolean firstEntryClaimed = false;
   private final Map<UUID, Long> cursedUntilMillis = new HashMap();

   public static BeginningCurseManager get(MinecraftServer server) {
      class_26 storage = server.method_30002().method_17983();
      return (BeginningCurseManager)storage.method_17924(new class_18.class_8645<>(BeginningCurseManager::new, BeginningCurseManager::load, (class_4284)null), "permadeath_beginning_curse");
   }

   public boolean isFirstEntryClaimed() {
      return this.firstEntryClaimed;
   }

   public void markFirstEntryClaimed() {
      this.firstEntryClaimed = true;
      this.method_80();
   }

   public void resetFirstEntry() {
      this.firstEntryClaimed = false;
      this.method_80();
   }

   public void curse(UUID uuid, long durationMillis) {
      this.cursedUntilMillis.put(uuid, System.currentTimeMillis() + durationMillis);
      this.method_80();
   }

   public boolean isCursed(UUID uuid) {
      Long until = (Long)this.cursedUntilMillis.get(uuid);
      return until != null && until > System.currentTimeMillis();
   }

   public void clearExpired() {
      boolean changed = this.cursedUntilMillis.values().removeIf((until) -> until <= System.currentTimeMillis());
      if (changed) {
         this.method_80();
      }

   }

   private static BeginningCurseManager load(class_2487 tag, class_7225.class_7874 registries) {
      BeginningCurseManager manager = new BeginningCurseManager();
      manager.firstEntryClaimed = tag.method_10577("FirstEntryClaimed");
      class_2499 list = tag.method_10554("Cursed", 10);

      for(int i = 0; i < list.size(); ++i) {
         class_2487 entry = list.method_10602(i);
         manager.cursedUntilMillis.put(entry.method_25926("UUID"), entry.method_10537("Until"));
      }

      return manager;
   }

   public class_2487 method_75(class_2487 tag, class_7225.class_7874 registries) {
      tag.method_10556("FirstEntryClaimed", this.firstEntryClaimed);
      class_2499 list = new class_2499();

      for(Map.Entry<UUID, Long> entry : this.cursedUntilMillis.entrySet()) {
         class_2487 entryTag = new class_2487();
         entryTag.method_25927("UUID", (UUID)entry.getKey());
         entryTag.method_10544("Until", (Long)entry.getValue());
         list.add(entryTag);
      }

      tag.method_10566("Cursed", list);
      return tag;
   }
}
