package com.tfar.unstabletools.crafting;

import com.google.common.collect.Sets;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;

import static com.tfar.unstabletools.UnstableTools.MODID;
import static com.tfar.unstabletools.UnstableTools.ObjectHolders.*;

public class RecipeDivision extends ShapedRecipe {
  public RecipeDivision(ResourceLocation idIn) {
super(idIn, MODID,1,3,NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromItems(division_sign), Ingredient.fromItems(Items.DIAMOND)),new ItemStack(unstable_ingot));
  }


  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    Container container = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class,inv,"field_70465_c");
    if (!Config.ServerConfig.allowed_containers.get().contains(container.getType().getRegistryName().toString()))return false;
    return super.matches(inv,worldIn);
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {

    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("timer",200);
    ItemStack output = new ItemStack(unstable_ingot);
    output.setTag(nbt);
    return output;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return division;
  }
}

