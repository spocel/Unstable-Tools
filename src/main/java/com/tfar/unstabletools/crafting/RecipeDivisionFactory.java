package com.tfar.unstabletools.crafting;

import com.google.gson.JsonObject;
import com.tfar.unstabletools.UnstableTools;
import com.tfar.unstabletools.UnstableToolsConfig;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class RecipeDivisionFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(JsonContext context, JsonObject json) {
    ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

    CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
    primer.width = recipe.getRecipeWidth();
    primer.height = recipe.getRecipeHeight();
    primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
    primer.input = recipe.getIngredients();

    return new DivisionRecipe(new ResourceLocation(UnstableTools.MODID, "division_crafting"), recipe.getRecipeOutput(), primer);
  }

  public static class DivisionRecipe extends ShapedOreRecipe {
    public DivisionRecipe(ResourceLocation group, ItemStack result, CraftingHelper.ShapedPrimer primer) {
      super(group, result, primer);
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
      if (!super.matches(inv, world))return false;

      ItemStack divisionSign = ItemStack.EMPTY;
      for (int i = 0; i < inv.getSizeInventory(); ++i) {
        ItemStack stack = inv.getStackInSlot(i);
        if (stack.getItem() instanceof IDivisionItem) {
          divisionSign = stack;
        }
      }

      NBTTagCompound nbt = divisionSign.getTagCompound();

      if (nbt == null)return false;

      boolean activated = nbt.getBoolean("activated");

      Container c = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class,inv,"field_70465_c");
      return UnstableToolsConfig.allowed_container_classes.contains(c.getClass()) && activated;

    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
      ItemStack newOutput = super.getCraftingResult(inv);

      ItemStack divisionSign = ItemStack.EMPTY;
      for (int i = 0; i < inv.getSizeInventory(); ++i) {
        ItemStack stack = inv.getStackInSlot(i);
        if (stack.getItem() instanceof IDivisionItem) {
          divisionSign = stack;
        }
      }

      boolean stable = divisionSign.getTagCompound().getBoolean("stable");

      if (stable) return newOutput;

      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("timer", 200);
      newOutput.setTagCompound(nbt);
      return newOutput;
    }
  }
}

