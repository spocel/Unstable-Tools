package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.block.BlockUnstableBlock;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

import static com.tfar.unstabletools.UnstableTools.MODID;

public class ObjectHolders {

  @ObjectHolder("unstabletools:unstable_ingot")
  public static ItemUnstableIngot unstableIngot;

  @ObjectHolder("unstabletools:unstable_block")
  public static BlockUnstableBlock unstableBlock;

  @ObjectHolder("unstabletools:division_sign")
  public static ItemDivisionSign divisionSign;

  @ObjectHolder("unstabletools:unstable_axe")
  public static ItemUnstableAxe unstableAxe;

  @ObjectHolder("unstabletools:unstable_pickaxe")
  public static ItemUnstablePickaxe unstablePickaxe;

  @ObjectHolder("unstabletools:unstable_spade")
  public static ItemUnstableSpade unstableSpade;

  @ObjectHolder("unstabletools:unstable_sword")
  public static ItemUnstableSword unstableSword;

  @ObjectHolder("unstabletools:unstable_hoe")
  public static ItemUnstableHoe unstableHoe;

  @ObjectHolder("unstabletools:unstable_shears")
  public static ItemUnstableShears unstableShears;

  @ObjectHolder("unstabletools:unstable_helmet")
  public static ItemUnstableArmor unstableHelmet;

  @ObjectHolder("unstabletools:unstable_chestplate")
  public static ItemUnstableArmor unstableChestplate;

  @ObjectHolder("unstabletools:unstable_leggings")
  public static ItemUnstableArmor unstableLeggings;

  @ObjectHolder("unstabletools:unstable_boots")
  public static ItemUnstableArmor unstableBoots;

  @ObjectHolder(MODID + ":compression")
  public static final IRecipeSerializer<?> RECIPE = null;

}
