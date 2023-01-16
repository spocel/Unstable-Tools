package tfar.unstabletools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class JSONUtils2 {

    public static Block getBlock(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            return BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(s)).orElseThrow(() -> new JsonSyntaxException("Expected " + memberName + " to be a block, was unknown string '" + s + "'"));
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a block, was " + GsonHelper.getType(json));
        }
    }

    public static Block getBlock(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return getBlock(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a block");
        }
    }

}
