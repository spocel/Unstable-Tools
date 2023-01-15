package tfar.unstabletools.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;

import static tfar.unstabletools.UnstableTools.MODID;
import static tfar.unstabletools.UnstableTools.ObjectHolders.*;

public class RecipeDivision extends ShapedRecipe {

  public static ItemStack ingot;
  public RecipeDivision(ResourceLocation idIn) {
super(idIn, MODID,1,3,NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromItems(division_sign), Ingredient.fromItems(Items.DIAMOND)),createIngot());
  }

  public static ItemStack createIngot() {
    ingot = new ItemStack(unstable_ingot);
    ingot.getOrCreateTag().putInt("timer",Config.ServerConfig.timer.get());
    return ingot;
  }

  public static final Field field1 = ObfuscationReflectionHelper.findField(CraftingInventory.class,"field_70465_c");
  public static final Field field2 = ObfuscationReflectionHelper.findField(Container.class,"field_216965_e");

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    try {
      Container container = (Container) field1.get(inv);
      if (container == null) return false;
      ContainerType<?> type = (ContainerType<?>) field2.get(container);
      return type != null && Config.ServerConfig.allowed_containers.get().contains(type.getRegistryName().toString()) && super.matches(inv, worldIn);
    } catch (Exception ohno) {
      ohno.printStackTrace();
    }
    return false;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return division;
  }
}

