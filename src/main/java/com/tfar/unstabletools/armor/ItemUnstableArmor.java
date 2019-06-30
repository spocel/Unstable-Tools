package com.tfar.unstabletools.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemUnstableArmor extends ArmorItem {
  public ItemUnstableArmor(Properties properties, IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn) {
    super(materialIn, equipmentSlotIn, properties);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair,@Nonnull ItemStack repair) {
    return false;
  }
}
