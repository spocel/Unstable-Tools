package com.tfar.unstabletools.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemUnstableArmor extends ItemArmor {
  public ItemUnstableArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn) {
    super(materialIn, 0, equipmentSlotIn);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }
}
