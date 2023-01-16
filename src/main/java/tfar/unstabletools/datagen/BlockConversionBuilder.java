package tfar.unstabletools.datagen;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class BlockConversionBuilder {

    public static Result createBlockConversion(Block from,Block to) {
        return createBlockConversion(from,to, BuiltInRegistries.BLOCK.getKey(to));
    }

    public static Result createBlockConversion(Block from,Block to,ResourceLocation location) {
        return new Result(location,from,to);
    }

    public static class Result implements FinishedConversion {
        private final ResourceLocation id;

        private final Block from;
        private final Block to;

        public Result(ResourceLocation idIn, Block from, Block resultIn) {
            this.id = idIn;
            this.from = from;
            this.to = resultIn;
        }

        public void serialize(JsonObject json) {
            json.addProperty("from", BuiltInRegistries.BLOCK.getKey(this.from).toString());

            json.addProperty("to", BuiltInRegistries.BLOCK.getKey(this.to).toString());
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getID() {
            return this.id;
        }
    }
}
