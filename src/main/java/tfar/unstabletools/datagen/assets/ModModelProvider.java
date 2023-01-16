package tfar.unstabletools.datagen.assets;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tfar.unstabletools.init.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Map<Block, BlockStateGenerator> map = Maps.newHashMap();
        Consumer<BlockStateGenerator> consumer = (p_125120_) -> {
            Block block = p_125120_.getBlock();
            BlockStateGenerator blockstategenerator = map.put(block, p_125120_);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> map1 = Maps.newHashMap();
        Set<Item> set = Sets.newHashSet();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> biconsumer = (p_125123_, p_125124_) -> {
            Supplier<JsonElement> supplier = map1.put(p_125123_, p_125124_);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + p_125123_);
            }
        };
        Consumer<Item> consumer1 = set::add;
        new ModBlockModelGenerators(consumer, biconsumer, consumer1).run();
        //new ItemModelGenerators(biconsumer).run();
        List<Block> list = getBlocks().stream().filter((p_125117_) -> !map.containsKey(p_125117_)).toList();
        if (!list.isEmpty()) {
            throw new IllegalStateException("Missing blockstate definitions for: " + list);
        } else {
            getBlocks().forEach((p_125128_) -> {
                Item item = Item.BY_BLOCK.get(p_125128_);
                if (item != null) {
                    if (set.contains(item)) {
                        return;
                    }

                    ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(item);
                    if (!map1.containsKey(resourcelocation)) {
                        map1.put(resourcelocation, new DelegatedModel(ModelLocationUtils.getModelLocation(p_125128_)));
                    }
                }

            });
            return CompletableFuture.allOf(this.saveCollection(pOutput, map,
                    block -> this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location())), this.saveCollection(pOutput, map1, this.modelPathProvider::json));
        }
    }

    public List<Block> getBlocks() {
        return List.of(ModBlocks.ethereal_glass,ModBlocks.unstable_block);
    }
}
