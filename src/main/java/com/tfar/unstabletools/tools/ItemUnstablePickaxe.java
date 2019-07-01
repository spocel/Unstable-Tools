package com.tfar.unstabletools.tools;

import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class ItemUnstablePickaxe extends PickaxeItem {
  public ItemUnstablePickaxe(IItemTier material,int attackDamage,float attackSpeed,Properties properties) {
    super(material,attackDamage,attackSpeed,properties);
  }

  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    Set<ToolType> types = new HashSet<>();
    types.add(ToolType.PICKAXE);
    return types;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
