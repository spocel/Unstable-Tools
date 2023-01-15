package tfar.unstabletools.init;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModBlocks {
    public static final Block unstable_block = new Block(Block.Properties.of(Material.METAL).strength(5, 6000).requiresCorrectToolForDrops()) {


        @Override
        @OnlyIn(Dist.CLIENT)
        public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
            return adjacentBlockState.getBlock() == this;
        }

    };
}
