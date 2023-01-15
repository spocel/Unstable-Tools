package tfar.unstabletools.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.unstabletools.UnstableTools;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item.Properties;

public class ItemUnstableHoe extends HoeItem {
  public ItemUnstableHoe(IItemTier material, int attackDamage,float speed, Properties properties) {
    super(material,attackDamage,speed,properties);
  }

  /**
   * Called when this item is used when targetting a Block
   */
  @Nonnull
  @Override
  public ActionResultType useOn(ItemUseContext context) {
    World world = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
    if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
      Block block = UnstableTools.instance.manager.getConversionMap().get(world.getBlockState(blockpos).getBlock());
      if (block != null) {
        PlayerEntity playerentity = context.getPlayer();
        world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isClientSide) {
          world.setBlock(blockpos, block.defaultBlockState(), 11);
          if (playerentity != null) {
            context.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> p_220043_1_.broadcastBreakEvent(context.getHand()));
          }
        }
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.PASS;
  }
}