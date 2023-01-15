package tfar.unstabletools.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import tfar.unstabletools.UnstableTools;

import javax.annotation.Nonnull;

public class ItemUnstableHoe extends HoeItem {
  public ItemUnstableHoe(Tier material, int attackDamage,float speed, Properties properties) {
    super(material,attackDamage,speed,properties);
  }

  /**
   * Called when this item is used when targetting a Block
   */
  @Nonnull
  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level world = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
    if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
      Block block = UnstableTools.instance.manager.getConversionMap().get(world.getBlockState(blockpos).getBlock());
      if (block != null) {
        Player playerentity = context.getPlayer();
        world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!world.isClientSide) {
          world.setBlock(blockpos, block.defaultBlockState(), 11);
          if (playerentity != null) {
            context.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> p_220043_1_.broadcastBreakEvent(context.getHand()));
          }
        }
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }
}