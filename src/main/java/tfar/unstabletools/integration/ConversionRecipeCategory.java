package tfar.unstabletools.integration;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionRecipe;
import tfar.unstabletools.init.ModItems;

public class ConversionRecipeCategory implements IRecipeCategory<ConversionRecipe> {

    private final MutableComponent localizedName;
    private final IDrawableStatic background;
    private final IDrawable icon;

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(UnstableTools.MODID, "textures/gui/jei/block_conversion.png");


    public ConversionRecipeCategory(IGuiHelper guiHelper) {
        this.localizedName = Component.translatable("gui.jei.unstabletools.category.block_conversions");
        this.background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 0, 0, 101, 18).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.unstable_hoe));
    }

    @Override
    public RecipeType<ConversionRecipe> getRecipeType() {
        return JEIPlugin.TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, ConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).addItemStack(new ItemStack(recipe.getFrom().asItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 0).addItemStack(new ItemStack(recipe.getTo().asItem()));
    }
}
