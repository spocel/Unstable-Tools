package tfar.unstabletools.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import tfar.unstabletools.crafting.RecipeDivision;

public class ModRecipeSerializer {
    public static final RecipeSerializer<?> division = new SimpleRecipeSerializer<>(RecipeDivision::new);
}
