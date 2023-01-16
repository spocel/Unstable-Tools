package tfar.unstabletools.integration;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.client.DefaultClientPlugin;
import me.shedaniel.rei.plugin.common.displays.DefaultStrippingDisplay;
import net.minecraft.core.registries.BuiltInRegistries;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.ConversionRecipe;
import tfar.unstabletools.init.ModItems;

import java.util.Comparator;

@REIPluginClient
public class ReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(ConversionRecipe.class, ConversionRecipeDisplay::new);


        UnstableTools.instance.manager.getConversionMap().entrySet().stream().sorted(Comparator.comparing(b -> BuiltInRegistries.BLOCK.getKey(b.getKey()))).forEach(set -> {
            registry.add(new ConversionRecipeDisplay(EntryStacks.of(set.getKey()), EntryStacks.of(set.getValue())));
        });

    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ConversionRecipeCategory());
        registry.addWorkstations(REIPlugin.TYPE, EntryStacks.of(ModItems.unstable_hoe));
    }
}
