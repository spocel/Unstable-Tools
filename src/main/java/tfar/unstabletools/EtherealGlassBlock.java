package tfar.unstabletools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EtherealGlassBlock extends GlassBlock {
    public EtherealGlassBlock(Properties p_53640_) {
        super(p_53640_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pContext instanceof EntityCollisionContext entityCollisionContext &&
                entityCollisionContext.getEntity() instanceof Player player ? Shapes.empty(): super.getCollisionShape(pState, pLevel, pPos, pContext);
    }
}
