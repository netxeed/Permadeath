package com.serthekiller.permadeath.client.changes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ChangesData {
   private static final Map<Long, Long> PREVIEWS = Map.of(35L, 40L, 55L, 60L);
   private static final List<DayPage> PAGES = new ArrayList();

   public static String titleFor(long day) {
      Long target = (Long)PREVIEWS.get(day);
      return target != null ? "Previa día " + target : "Día " + day;
   }

   private static void add(DayPage page) {
      PAGES.add(page);
   }

   public static List<DayPage> unlockedPages(long currentDay) {
      return PAGES.stream().filter((p) -> p.day() <= currentDay).sorted(Comparator.comparingLong(DayPage::day)).toList();
   }

   private static void img(long day, String name, int w, int h) {
      add(ChangesData.DayPage.image(day, class_2960.method_60655("permadeath", "textures/gui/changes/" + name + ".png"), w, h));
   }

   private ChangesData() {
   }

   static {
      img(0L, "dia0_1", 1920, 1080);
      img(0L, "dia0_2", 1920, 1080);
      img(10L, "dia10_1", 680, 385);
      img(20L, "dia20_0", 680, 385);
      img(20L, "dia20_1", 680, 385);
      img(20L, "dia20_2", 680, 385);
      img(25L, "dia25_1", 680, 680);
      img(25L, "dia25_2", 2420, 1370);
      img(25L, "dia25_3", 680, 385);
      img(30L, "dia30_1", 680, 385);
      img(30L, "dia30_2", 2420, 1370);
      img(35L, "dia35_1", 2000, 2000);
      img(35L, "dia35_2", 2000, 2000);
      img(40L, "dia40_1", 2420, 1370);
      img(40L, "dia40_2", 680, 385);
      img(40L, "dia40_4", 2420, 1370);
      img(40L, "dia40_5", 2420, 1370);
      img(40L, "dia40_6", 2420, 1370);
      img(40L, "dia40_7", 1000, 566);
      img(40L, "dia40_8", 2420, 1370);
      img(40L, "dia40_9", 2420, 1370);
      img(40L, "dia40_10", 2420, 1370);
      img(40L, "dia40_11", 2420, 1370);
      img(40L, "dia40_12", 2420, 1370);
      img(50L, "dia50_1", 680, 385);
      img(50L, "dia50_2", 680, 385);
      img(50L, "dia50_3", 2420, 1370);
      img(50L, "dia50_4", 2420, 1370);
      img(50L, "dia50_5", 680, 385);
      img(50L, "dia50_6", 680, 680);
      img(50L, "dia50_7", 2420, 1370);
      img(50L, "dia50_8", 680, 385);
      img(50L, "dia50_9", 680, 385);
      img(50L, "dia50_10", 680, 385);
      img(50L, "dia50_11", 1000, 566);
      img(55L, "dia55_1", 2420, 1370);
      img(55L, "dia55_2", 680, 385);
      img(55L, "dia55_3", 680, 385);
      img(55L, "dia55_4", 680, 385);
      img(55L, "dia55_5", 680, 385);
      img(60L, "dia60_1", 680, 385);
      img(60L, "dia60_2", 680, 385);
      img(60L, "dia60_3", 680, 385);
      img(60L, "dia60_4", 680, 385);
      img(60L, "dia60_5", 680, 385);
      img(60L, "dia60_6", 680, 385);
      img(60L, "dia60_7", 680, 385);
      img(60L, "dia60_8", 680, 385);
   }

   @Environment(EnvType.CLIENT)
   public static record DayPage(long day, List<String> lines, @Nullable class_2960 image, int imgW, int imgH) {
      public static DayPage text(long day, String... lines) {
         return new DayPage(day, List.of(lines), (class_2960)null, 0, 0);
      }

      public static DayPage image(long day, class_2960 image, int w, int h) {
         return new DayPage(day, List.of(), image, w, h);
      }
   }
}
