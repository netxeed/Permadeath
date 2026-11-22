package com.serthekiller.permadeath.client.changes;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;

@Environment(EnvType.CLIENT)
public class ChangesScreen extends class_437 {
   private static final int PADDING = 8;
   private static final int TITLE_H = 14;
   private static final int MIN_W = 220;
   private static final int MIN_H = 100;
   private final List<ChangesData.DayPage> pages;
   private int index;
   private class_4185 prevButton;
   private class_4185 nextButton;

   public ChangesScreen(long currentDay) {
      super(class_2561.method_43470("Cambios"));
      this.pages = ChangesData.unlockedPages(currentDay);
      this.index = Math.max(0, this.pages.size() - 1);
   }

   private float imageScale() {
      ChangesData.DayPage p = this.current();
      if (p != null && p.image() != null) {
         float maxW = (float)(this.field_22789 - 120);
         float maxH = (float)(this.field_22790 - 90);
         return Math.min(1.0F, Math.min(maxW / (float)p.imgW(), maxH / (float)p.imgH()));
      } else {
         return 1.0F;
      }
   }

   private int contentW() {
      ChangesData.DayPage p = this.current();
      if (p == null) {
         return 220;
      } else if (p.image() != null) {
         return Math.round((float)p.imgW() * this.imageScale());
      } else {
         int w = 0;

         for(String s : p.lines()) {
            w = Math.max(w, this.field_22793.method_1727(s));
         }

         return Math.max(220, w);
      }
   }

   private int contentH() {
      ChangesData.DayPage p = this.current();
      if (p == null) {
         return 100;
      } else {
         return p.image() != null ? Math.round((float)p.imgH() * this.imageScale()) : Math.max(100, p.lines().size() * 12 + 4);
      }
   }

   private ChangesData.DayPage current() {
      return this.pages.isEmpty() ? null : (ChangesData.DayPage)this.pages.get(this.index);
   }

   private int panelW() {
      return this.contentW() + 16;
   }

   private int panelH() {
      return this.contentH() + 14 + 16;
   }

   private int panelX() {
      return (this.field_22789 - this.panelW()) / 2;
   }

   private int panelY() {
      return (this.field_22790 - this.panelH()) / 2 - 10;
   }

   protected void method_25426() {
      this.prevButton = class_4185.method_46430(class_2561.method_43470("<"), (b) -> this.go(-1)).method_46434(this.panelX() - 30, this.field_22790 / 2 - 20, 24, 40).method_46431();
      this.nextButton = class_4185.method_46430(class_2561.method_43470(">"), (b) -> this.go(1)).method_46434(this.panelX() + this.panelW() + 6, this.field_22790 / 2 - 20, 24, 40).method_46431();
      this.method_37063(this.prevButton);
      this.method_37063(this.nextButton);
      this.method_37063(class_4185.method_46430(class_2561.method_43471("gui.done"), (b) -> this.method_25419()).method_46434(this.field_22789 / 2 - 100, this.panelY() + this.panelH() + 12, 200, 20).method_46431());
      this.updateArrows();
   }

   private void go(int delta) {
      int n = this.index + delta;
      if (n >= 0 && n < this.pages.size()) {
         this.index = n;
         this.method_41843();
      }
   }

   private void updateArrows() {
      this.prevButton.field_22763 = this.index > 0;
      this.nextButton.field_22763 = this.index < this.pages.size() - 1;
   }

   public void method_25420(class_332 g, int mouseX, int mouseY, float partialTick) {
      super.method_25420(g, mouseX, mouseY, partialTick);
      int x = this.panelX();
      int y = this.panelY();
      int w = this.panelW();
      int h = this.panelH();
      g.method_25294(x - 2, y - 2, x + w + 2, y + h + 2, -16777216);
      g.method_25294(x, y, x + w, y + h, -3750202);
      g.method_25294(x + 8 - 1, y + 14 + 8 - 1, x + w - 8 + 1, y + h - 8 + 1, -15070706);
      ChangesData.DayPage p = this.current();
      if (p == null) {
         g.method_51433(this.field_22793, "Aún no hay cambios", x + 8, y + 4, -12566464, false);
      } else {
         int sameDay = 0;
         int pos = 0;

         for(int i = 0; i < this.pages.size(); ++i) {
            if (((ChangesData.DayPage)this.pages.get(i)).day() == p.day()) {
               ++sameDay;
               if (i <= this.index) {
                  ++pos;
               }
            }
         }

         String var10000 = ChangesData.titleFor(p.day());
         String title = var10000 + (sameDay > 1 ? "  (" + pos + "/" + sameDay + ")" : "");
         g.method_51433(this.field_22793, title, x + 8, y + 4, -12566464, false);
         int cx = x + 8;
         int cy = y + 14 + 8;
         if (p.image() != null) {
            g.method_25290(class_1921::method_62277, p.image(), cx, cy, 0.0F, 0.0F, this.contentW(), this.contentH(), this.contentW(), this.contentH());
         } else {
            int ly = cy + 4;

            for(String line : p.lines()) {
               g.method_51433(this.field_22793, line, cx + 4, ly, -1, false);
               ly += 12;
            }
         }

      }
   }

   public void method_25394(class_332 g, int mouseX, int mouseY, float partialTick) {
      super.method_25394(g, mouseX, mouseY, partialTick);
      this.updateArrows();
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 263) {
         this.go(-1);
         return true;
      } else if (keyCode == 262) {
         this.go(1);
         return true;
      } else {
         return super.method_25404(keyCode, scanCode, modifiers);
      }
   }

   public boolean method_25421() {
      return false;
   }
}
