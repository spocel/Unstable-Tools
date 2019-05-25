package com.tfar.unstabletools.crafting;

import com.google.gson.JsonObject;
import com.tfar.unstabletools.UnstableTools;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class RecipeAdvancedDivisionSignFactory implements IRecipeFactory {


    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
      ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

      CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
      primer.width = recipe.getRecipeWidth();
      primer.height = recipe.getRecipeHeight();
      primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
      primer.input = recipe.getIngredients();

      return new AdvancedDivisionSignRecipe(new ResourceLocation(UnstableTools.MODID, "advanced_division_sign_crafting"), recipe.getRecipeOutput(), primer);
    }

    public static class AdvancedDivisionSignRecipe extends ShapedOreRecipe {
      public AdvancedDivisionSignRecipe(ResourceLocation group, ItemStack result, CraftingHelper.ShapedPrimer primer) {
        super(group, result, primer);
      }

      @Override
      @Nonnull
      public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {

        ItemStack newOutput = this.output.copy();
        ItemStack itemstack = ItemStack.EMPTY;
        for (int i = 0; i < var1.getSizeInventory(); ++i) {
          ItemStack stack = var1.getStackInSlot(i);
          if (stack.getItem() instanceof IDivisionItem) {
            itemstack = stack;
          }
        }
        boolean activated = itemstack.getTagCompound().getBoolean("activated");

        if (!activated)return ItemStack.EMPTY;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("stable",true);
        newOutput.setTagCompound(nbt);
        return newOutput;
      }
    }
  }

