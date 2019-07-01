package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.block.BlockUnstableBlock;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ObjectHolders {

  @GameRegistry.ObjectHolder("unstabletools:unstable_ingot")
  public static ItemUnstableIngot unstableIngot;

  @GameRegistry.ObjectHolder("unstabletools:unstable_block")
  public static BlockUnstableBlock unstableBlock;

  @GameRegistry.ObjectHolder("unstabletools:division_sign")
  public static ItemDivisionSign divisionSign;

  @GameRegistry.ObjectHolder("unstabletools:unstable_axe")
  public static ItemUnstableAxe unstableAxe;

  @GameRegistry.ObjectHolder("unstabletools:unstable_pickaxe")
  public static ItemUnstablePickaxe unstablePickaxe;

  @GameRegistry.ObjectHolder("unstabletools:unstable_spade")
  public static ItemUnstableSpade unstableSpade;

  @GameRegistry.ObjectHolder("unstabletools:unstable_sword")
  public static ItemUnstableSword unstableSword;

  @GameRegistry.ObjectHolder("unstabletools:unstable_hoe")
  public static ItemUnstableHoe unstableHoe;

  @GameRegistry.ObjectHolder("unstabletools:unstable_shears")
  public static ItemUnstableShears unstableShears;

  @GameRegistry.ObjectHolder("unstabletools:unstable_helmet")
  public static ItemUnstableArmor unstableHelmet;

  @GameRegistry.ObjectHolder("unstabletools:unstable_chestplate")
  public static ItemUnstableArmor unstableChestplate;

  @GameRegistry.ObjectHolder("unstabletools:unstable_leggings")
  public static ItemUnstableArmor unstableLeggings;

  @GameRegistry.ObjectHolder("unstabletools:unstable_boots")
  public static ItemUnstableArmor unstableBoots;

}
