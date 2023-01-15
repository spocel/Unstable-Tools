package tfar.unstabletools.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionRecipe;

public class ConversionRecipeCategory implements IRecipeCategory<ConversionRecipe> {

    private final TranslatableComponent localizedName;
    private final IDrawableStatic background;
    private final IDrawable icon;

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(UnstableTools.MODID, "textures/gui/jei/block_conversion.png");


    public ConversionRecipeCategory(IGuiHelper guiHelper) {
        this.localizedName = new TranslatableComponent("gui.jei.unstabletools.category.block_conversions");
        this.background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 0, 0, 101, 18).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(UnstableTools.ObjectHolders.unstable_hoe));
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.BLOCK_CONVERSIONS;
    }

    @Override
    public RecipeType<ConversionRecipe> getRecipeType() {
        return IRecipeCategory.super.getRecipeType();
    }

    @Override
    public Class<? extends ConversionRecipe> getRecipeClass() {
        return ConversionRecipe.class;
    }

    @Override
    public Component getTitle() {
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
