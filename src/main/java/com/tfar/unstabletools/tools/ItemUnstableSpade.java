package com.tfar.unstabletools.tools;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ItemUnstableSpade extends ItemSpade {
  public ItemUnstableSpade(ToolMaterial material) {
    super(material);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
