package tfar.unstabletools.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static tfar.unstabletools.UnstableTools.MODID;
import static tfar.unstabletools.UnstableTools.ObjectHolders.*;

public class RecipeDivision extends ShapedRecipe {

  public static ItemStack ingot;
  public RecipeDivision(ResourceLocation idIn) {
super(idIn, MODID,1,3,NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.IRON_INGOT), Ingredient.of(division_sign), Ingredient.of(Items.DIAMOND)),createIngot());
  }

  public static ItemStack createIngot() {
    ingot = new ItemStack(unstable_ingot);
    ingot.getOrCreateTag().putInt("timer",Config.ServerConfig.timer.get());
    return ingot;
  }

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingContainer inv, Level worldIn) {
    try {
      AbstractContainerMenu container = inv.menu;
      MenuType<?> type = container.getType();//this will throw on certain inventories
      return Config.ServerConfig.allowed_containers.get().contains(type.getRegistryName().toString()) && super.matches(inv, worldIn);
      } catch (Exception ohno) {
        //ohno.printStackTrace();
        return false;
      }
    }

  @Nonnull
  @Override
  public RecipeSerializer<?> getSerializer() {
    return division;
  }
}

