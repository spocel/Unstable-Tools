package com.tfar.unstabletools.crafting;

import com.google.common.collect.Sets;
import com.tfar.unstabletools.item.ItemDivisionSign;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;

import static com.tfar.unstabletools.UnstableTools.ObjectHolders.*;

public class RecipeDivision extends SpecialRecipe {
  public RecipeDivision(ResourceLocation idIn) {
    super(idIn);
  }

  //fast workbench users will thank me
  public static final Set<Class<?>> classes = new HashSet<>(Sets.<Class<?>>newHashSet(WorkbenchContainer.class));

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    //iron(1), division sign(4), diamond(7)

    if (!inv.getStackInSlot(1).getItem().isIn(Tags.Items.INGOTS_IRON) || !(inv.getStackInSlot(4).getItem() instanceof IDivisionItem) || !inv.getStackInSlot(7).getItem().isIn(Tags.Items.GEMS_DIAMOND))
      return false;

    for (int i = 0; i < 9; i++)
      if (!inv.getStackInSlot(i).isEmpty() && (i == 0 || i == 2 || i == 3 || i == 5 || i == 6 || i == 8)) return false;
      return true;
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {

    ItemStack divisionSign = inv.getStackInSlot(4);
    boolean stable = divisionSign.getOrCreateTag().getBoolean("stable");
    if (stable)return new ItemStack(unstable_ingot);
    boolean activated = divisionSign.getOrCreateTag().getBoolean(ItemDivisionSign.active);
    if (!activated)return ItemStack.EMPTY;
    Container container = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class,inv,"field_70465_c");
    if (!classes.contains(container.getClass()))return ItemStack.EMPTY;
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("timer",200);
    ItemStack output = new ItemStack(unstable_ingot);
    output.setTag(nbt);
    return output;
  }

  @Nonnull
  @Override
  public ItemStack getRecipeOutput() {
    return new ItemStack(unstable_ingot);
  }

  @Override
  public boolean canFit(int width, int height) {
    return height == 3 && width == 3;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return division;
  }
}

