package tfar.unstabletools.datagen;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public interface FinishedConversion  {
        void serialize(JsonObject json);

        /**
         * Gets the JSON for the recipe.
         */
        default JsonObject getRecipeJson() {
            JsonObject jsonobject = new JsonObject();
            this.serialize(jsonobject);
            return jsonobject;
        }

        /**
         * Gets the ID for the recipe.
         */
        ResourceLocation getID();
    }
