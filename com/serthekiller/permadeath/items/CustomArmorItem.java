package com.serthekiller.permadeath.items;

import java.util.List;
import net.minecraft.class_1738;
import net.minecraft.class_1741;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import net.minecraft.class_8051;

public class CustomArmorItem extends class_1738 {
   private final double bonusHealth;
   private final class_1741 armorMaterial;

   public CustomArmorItem(class_1741 material, class_8051 type, double bonusHealth, class_1792.class_1793 properties) {
      super(material, type, properties);
      this.bonusHealth = bonusHealth;
      this.armorMaterial = material;
   }

   public double getBonusHealth() {
      return this.bonusHealth;
   }

   public void method_7851(class_1799 stack, class_1792.class_9635 context, List<class_2561> tooltipComponents, class_1836 tooltipFlag) {
      super.method_7851(stack, context, tooltipComponents, tooltipFlag);
   }
}
