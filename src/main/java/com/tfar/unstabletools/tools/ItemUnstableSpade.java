package com.tfar.unstabletools.tools;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ItemStack;

public class ItemUnstableSpade extends ShovelItem {
  public ItemUnstableSpade(IItemTier material, int attackDamage, float attackSpeed, Properties properties) {
    super(material,attackDamage,attackSpeed,properties);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
