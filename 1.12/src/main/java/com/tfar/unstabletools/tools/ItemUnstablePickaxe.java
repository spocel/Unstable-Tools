package com.tfar.unstabletools.tools;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemUnstablePickaxe extends ItemPickaxe {
  public ItemUnstablePickaxe(ToolMaterial material) {
    super(material);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
