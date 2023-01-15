package tfar.unstabletools.item.tools;

import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.level.block.state.BlockState;
import tfar.unstabletools.UnstableTools;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    Level level = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, net.minecraftforge.common.ToolActions.HOE_TILL, false);
    Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
    if (pair == null) {
      return InteractionResult.PASS;
    } else {
      if (context.getClickedFace() != Direction.DOWN && level.isEmptyBlock(blockpos.above())) {
        Block block = UnstableTools.instance.manager.getConversionMap().get(level.getBlockState(blockpos).getBlock());
        if (block != null) {
          Player playerentity = context.getPlayer();
          level.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
          if (!level.isClientSide) {
            level.setBlock(blockpos, block.defaultBlockState(), 11);
            if (playerentity != null) {
              context.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> p_220043_1_.broadcastBreakEvent(context.getHand()));
            }
          }
          return InteractionResult.SUCCESS;
        }
      }
    }
    return InteractionResult.PASS;
  }
}