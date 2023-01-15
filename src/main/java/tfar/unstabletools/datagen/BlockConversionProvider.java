package tfar.unstabletools.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.unstabletools.crafting.ConversionManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class BlockConversionProvider implements DataProvider {


    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator.PathProvider recipePathProvider;

    public BlockConversionProvider(DataGenerator generator) {
        this.recipePathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "recipes");
    }

    @Override
    public void run(CachedOutput cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        registerRecipes((conversion) -> {
            if (!set.add(conversion.getID())) {
                throw new IllegalStateException("Duplicate recipe " + conversion.getID());
            } else {
                saveRecipe(cache, conversion.getRecipeJson(), this.recipePathProvider.json(conversion.getID()));
            }
        });

    }

    /**
     * Saves a recipe to a file.
     */
    private static void saveRecipe(CachedOutput cache, JsonObject json, Path path) {
        try {
            DataProvider.saveStable(cache, json, path);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save recipe {}", path, ioexception);
        }
    }

    @Override
    public String getName() {
        return "Block Conversions";
    }




    protected void registerRecipes(Consumer<FinishedConversion> consumer) {
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.COBBLESTONE,Blocks.STONE));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.GRAVEL, Blocks.COBBLESTONE));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.SAND, Blocks.GRAVEL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.MAGMA_BLOCK, Blocks.LAVA));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.OBSIDIAN, Blocks.MAGMA_BLOCK));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.GLASS, Blocks.SAND));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.TERRACOTTA, Blocks.CLAY));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DAMAGED_ANVIL, Blocks.CHIPPED_ANVIL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.CHIPPED_ANVIL, Blocks.ANVIL));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK));

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK));

        for (DyeColor dyeColor : DyeColor.values()) {
            String name = dyeColor.name().toLowerCase(Locale.ROOT);
            Block from = Registry.BLOCK.get(new ResourceLocation(name+"_stained_glass"));
            consumer.accept(BlockConversionBuilder.createBlockConversion(from,Blocks.GLASS,new ResourceLocation("stained_glass_"+name)));
        }

        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.FARMLAND, Blocks.DIRT));
        //consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.GRASS_PATH, Blocks.GRASS_BLOCK));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DIRT, Blocks.GRASS_BLOCK,new ResourceLocation("grass_block_2")));
        consumer.accept(BlockConversionBuilder.createBlockConversion(Blocks.DEAD_BUSH, Blocks.OAK_SAPLING));
    }
}