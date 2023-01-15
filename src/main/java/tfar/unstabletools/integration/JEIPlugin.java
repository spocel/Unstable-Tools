package tfar.unstabletools.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionManager;
import tfar.unstabletools.crafting.ConversionRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private IRecipeCategory<ConversionRecipe> conversionCategory;

    public static final ResourceLocation BLOCK_CONVERSIONS = new ResourceLocation(UnstableTools.MODID, ConversionManager.BLOCK_CONVS);

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(UnstableTools.ObjectHolders.unstable_hoe),BLOCK_CONVERSIONS);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        conversionCategory = new ConversionRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(conversionCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(getRecipes(), BLOCK_CONVERSIONS);
    }

    public static List<ConversionRecipe> getRecipes() {
        ConversionManager conversionManager = UnstableTools.instance.manager;
        List<ConversionRecipe> conversionRecipes = new ArrayList<>();
        for (Map.Entry<Block,Block> entry:conversionManager.getConversionMap().entrySet()) {
            conversionRecipes.add(new ConversionRecipe(entry.getKey(),entry.getValue()));
        }
        return conversionRecipes;
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(UnstableTools.MODID,UnstableTools.MODID);
    }

}
