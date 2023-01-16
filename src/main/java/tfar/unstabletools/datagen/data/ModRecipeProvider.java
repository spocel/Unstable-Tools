package tfar.unstabletools.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import tfar.unstabletools.init.ModBlocks;
import tfar.unstabletools.init.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ethereal_glass,8)
                .define('G', ModItems.unstable_ingot)
                .define('P', Tags.Items.GLASS)
                .pattern("PPP")
                .pattern("PGP")
                .pattern("PPP")
                .unlockedBy("has_glass", has(Tags.Items.GLASS))
                .save(consumer);
    }

}
