package tfar.unstabletools.crafting;

import net.minecraft.block.Block;

public class ConversionRecipe {

    private final Block from;
    private final Block to;

    public ConversionRecipe(Block from, Block to) {
        this.from = from;
        this.to = to;
    }

    public Block getFrom() {
        return from;
    }

    public Block getTo() {
        return to;
    }
}
