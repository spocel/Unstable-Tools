package com.tfar.unstabletools.tools;

import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;

public class ItemUnstablePickaxe extends PickaxeItem {
  public ItemUnstablePickaxe(IItemTier material,int attackDamage,float attackSpeed,Properties properties) {
    super(material,attackDamage,attackSpeed,properties);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
