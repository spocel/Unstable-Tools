package tfar.unstabletools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class JSONUtils2 {

    public static Block getBlock(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            return Registry.BLOCK.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
                return new JsonSyntaxException("Expected " + memberName + " to be a block, was unknown string '" + s + "'");
            });
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a block, was " + JSONUtils.toString(json));
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
