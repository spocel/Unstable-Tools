package com.tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemUnstablePickaxe extends PickaxeItem {
  public ItemUnstablePickaxe(IItemTier material,int attackDamage,float attackSpeed,Properties properties) {
    super(material,attackDamage,attackSpeed,properties);
  }

  static final Set<ToolType> pickaxe = Sets.newHashSet(ToolType.PICKAXE);

  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return pickaxe;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
