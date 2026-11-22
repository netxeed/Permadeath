package com.serthekiller.permadeath.Cambios;

import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1324;
import net.minecraft.class_1584;
import net.minecraft.class_1590;
import net.minecraft.class_1646;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_3218;
import net.minecraft.class_5134;

public class Ravagercreator {
   public static void setupUltraRavagerStack(class_1584 ravager, class_3218 level) {
      ravager.method_5780("ProcessedStack");
      ravager.method_5780("UltraRavager");
      ((class_1324)Objects.requireNonNull(ravager.method_5996(class_5134.field_23716))).method_6192((double)500.0F);
      ravager.method_6033(500.0F);
      ravager.method_5665(class_2561.method_43470("Ultra Ravager").method_27692(class_124.field_1060));
      ravager.method_5880(false);
      ravager.method_6092(new class_1293(class_1294.field_5904, -1, 1, false, false));
      ravager.method_6092(new class_1293(class_1294.field_5910, -1, 1, false, false));
      class_1590 pigman = new class_1590(class_1299.field_6050, level);
      if (pigman != null) {
         pigman.method_5780("ProcessedStack");
         pigman.method_5780("CarlosPigman");
         ((class_1324)Objects.requireNonNull(pigman.method_5996(class_5134.field_23716))).method_6192((double)150.0F);
         pigman.method_6033(150.0F);
         pigman.method_5665(class_2561.method_43470("Carlos el Esclavo").method_27692(class_124.field_1060));
         pigman.method_5880(false);
         ((class_1324)Objects.requireNonNull(pigman.method_5996(class_5134.field_23724))).method_6192((double)12.0F);
         ((class_1324)Objects.requireNonNull(pigman.method_5996(class_5134.field_23725))).method_6192((double)4.0F);
         ((class_1324)Objects.requireNonNull(pigman.method_5996(class_5134.field_23721))).method_6192((double)9.0F);
         ((class_1324)Objects.requireNonNull(pigman.method_5996(class_5134.field_23722))).method_6192((double)10.0F);
         pigman.method_6122(class_1268.field_5808, new class_1799(class_1802.field_8695));
         pigman.method_5946(class_1304.field_6173, 0.0F);
         pigman.method_5814(ravager.method_23317(), ravager.method_23318(), ravager.method_23321());
         level.method_8649(pigman);
         pigman.method_5873(ravager, true);
         class_1646 villager = new class_1646(class_1299.field_6077, level);
         if (villager != null) {
            villager.method_5780("ProcessedStack");
            villager.method_5780("JessVillager");
            ((class_1324)Objects.requireNonNull(villager.method_5996(class_5134.field_23716))).method_6192((double)240.0F);
            villager.method_6033(240.0F);
            villager.method_5665(class_2561.method_43470("Jess la Emperatriz").method_27692(class_124.field_1060));
            villager.method_5880(false);
            villager.method_6122(class_1268.field_5808, new class_1799(class_1802.field_8463));
            villager.method_5946(class_1304.field_6173, 0.0F);
            villager.method_5814(ravager.method_23317(), ravager.method_23318(), ravager.method_23321());
            level.method_8649(villager);
            villager.method_5873(pigman, true);
         }
      }

   }
}
