package com.serthekiller.permadeath.worldgen;

import net.minecraft.class_2960;
import net.minecraft.class_3195;
import net.minecraft.class_5321;
import net.minecraft.class_7924;

public class ModStructures {
   public static final class_5321<class_3195> BEGINNING_PORTAL = key("begginningportal");
   public static final class_5321<class_3195> MINI_ISLAND = key("mini_island");
   public static final class_5321<class_3195> YTIC_BASE = key("ytic_base");

   private static class_5321<class_3195> key(String name) {
      return class_5321.method_29179(class_7924.field_41246, class_2960.method_60655("permadeath", name));
   }

   public static void register() {
   }
}
