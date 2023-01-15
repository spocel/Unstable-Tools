package tfar.unstabletools.crafting;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.unstabletools.JSONUtils2;

import java.util.HashMap;
import java.util.Map;

public class ConversionManager extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<Block, Block> conversionMap = new HashMap<>();
    private boolean someRecipesErrored;

    public static final String BLOCK_CONVS = "block_conversions";

    public ConversionManager() {
        super(GSON,BLOCK_CONVS);
    }

    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        this.someRecipesErrored = false;

        conversionMap.clear();

        for(Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_")) continue; //Forge: filter anything beginning with "_" as it's used for metadata.

            try {
                if (entry.getValue().isJsonObject() && !net.minecraftforge.common.crafting.CraftingHelper.processConditions(entry.getValue().getAsJsonObject(), "conditions")) {
                    LOGGER.debug("Skipping loading conversion {} as it's conditions were not met", resourcelocation);
                    continue;
                }
                Pair<Block,Block> blockPair = deserializeConversion(resourcelocation, JSONUtils.getJsonObject(entry.getValue(), "top element"));
                if (blockPair == null) {
                    LOGGER.info("Skipping loading conversion {} as it is empty", resourcelocation);
                    continue;
                }
                conversionMap.put(blockPair.getFirst(),blockPair.getSecond());
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading conversion {}", resourcelocation, jsonparseexception);
            }
        }

        LOGGER.info("Loaded {} block conversions", conversionMap.size());
    }

    /**
     * Deserializes a conversion object from json data.
     */
    public static Pair<Block,Block> deserializeConversion(ResourceLocation recipeId, JsonObject json) {
        if (json.size() == 0) {
            return null;
        }
        Block s1 = JSONUtils2.getBlock(json, "from");
        Block s2 = JSONUtils2.getBlock(json, "to");
        return Pair.of(s1,s2);
    }

    public Map<Block, Block> getConversionMap() {
        return conversionMap;
    }
}
