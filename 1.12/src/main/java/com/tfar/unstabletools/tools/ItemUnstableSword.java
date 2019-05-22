package com.tfar.unstabletools.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemUnstableSword extends ItemSword {
  public ItemUnstableSword(ToolMaterial material) {
    super(material);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
