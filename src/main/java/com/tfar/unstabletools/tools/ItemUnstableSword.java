package com.tfar.unstabletools.tools;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ItemUnstableSword extends SwordItem {
  public ItemUnstableSword(IItemTier material,int attackDamage,float attackSpeed,Properties properties) {
    super(material,attackDamage,attackSpeed,properties);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
