package tfar.unstabletools.datagen.assets;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public class ModBlockStateProvider extends BlockStateProvider {
    @Override
    protected BlockStateProviderType<?> type() {
        return null;
    }

    @Override
    public BlockState getState(RandomSource pRandom, BlockPos pState) {
        return null;
    }
}
