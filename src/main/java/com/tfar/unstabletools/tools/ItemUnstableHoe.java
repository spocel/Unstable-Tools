package com.tfar.unstabletools.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
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

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ItemUnstableHoe extends HoeItem {
  public ItemUnstableHoe(IItemTier material, float speed, Properties properties) {
    super(material,speed,properties);
  }

  protected static final Map<Block, BlockState> REVERSE_HOE_LOOKUP = new HashMap<>();

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  static {
    REVERSE_HOE_LOOKUP.put(Blocks.COBBLESTONE, Blocks.STONE.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.GRAVEL, Blocks.COBBLESTONE.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.SAND, Blocks.GRAVEL.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.MAGMA_BLOCK, Blocks.LAVA.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.OBSIDIAN, Blocks.LAVA.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.GLASS, Blocks.SAND.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.TERRACOTTA, Blocks.CLAY.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.STRIPPED_ACACIA_LOG,Blocks.ACACIA_LOG.getDefaultState());

    REVERSE_HOE_LOOKUP.put(Blocks.FARMLAND, Blocks.DIRT.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.GRASS_PATH, Blocks.GRASS_BLOCK.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.DIRT, Blocks.GRASS_BLOCK.getDefaultState());
    REVERSE_HOE_LOOKUP.put(Blocks.DEAD_BUSH, Blocks.OAK_SAPLING.getDefaultState());
  }

  /**
   * Called when this item is used when targetting a Block
   */
  @Nonnull
  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
    if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up())) {
      BlockState blockstate = REVERSE_HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
      if (blockstate != null) {
        PlayerEntity playerentity = context.getPlayer();
        world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isRemote) {
          world.setBlockState(blockpos, blockstate, 11);
          if (playerentity != null) {
            context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
              p_220043_1_.sendBreakAnimation(context.getHand());
            });
          }
        }

        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.PASS;
  }

  /**
   * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
   * the damage on the stack.
   */
  public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    return true;
  }
}