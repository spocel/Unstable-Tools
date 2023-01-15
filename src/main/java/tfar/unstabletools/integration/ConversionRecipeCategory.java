package tfar.unstabletools.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionRecipe;

public class ConversionRecipeCategory implements IRecipeCategory<ConversionRecipe> {

    private final String localizedName;
    private final IDrawableStatic background;
    private final IDrawable icon;

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(UnstableTools.MODID, "textures/gui/jei/block_conversion.png");


    public ConversionRecipeCategory(IGuiHelper guiHelper) {
        this.localizedName = I18n.format("gui.jei.unstabletools.category.block_conversions");
        this.background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 0, 0, 101, 18).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(UnstableTools.ObjectHolders.unstable_hoe));
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.BLOCK_CONVERSIONS;
    }

    @Override
    public Class<? extends ConversionRecipe> getRecipeClass() {
        return ConversionRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ConversionRecipe recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(recipe.getFrom().asItem()));
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.getTo().asItem()));
    }

    public void setRecipe(IRecipeLayout recipeLayout, ConversionRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 0, 0);
        itemStacks.init(1, false, 83, 0);

        itemStacks.set(ingredients);
    }
}
