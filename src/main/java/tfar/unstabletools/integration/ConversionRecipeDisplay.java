package tfar.unstabletools.integration;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.DefaultStrippingDisplay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionRecipe;

import java.util.Collections;
import java.util.List;

public class ConversionRecipeDisplay extends BasicDisplay {

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(UnstableTools.MODID, "textures/gui/jei/block_conversion.png");

    public ConversionRecipeDisplay(ConversionRecipe recipe) {
        this(recipe.getFrom(),recipe.getTo());
    }

    public ConversionRecipeDisplay(Block from, Block to) {
       this(EntryStacks.of(from),EntryStacks.of(to));
    }

    public ConversionRecipeDisplay(EntryStack<?> in, EntryStack<?> out) {
        this(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)));
    }

    public ConversionRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIPlugin.TYPE;
    }

    public static BasicDisplay.Serializer<ConversionRecipeDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimpleRecipeLess(ConversionRecipeDisplay::new);
    }
}
