package com.tfar.unstabletools.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ItemUnstableHoe extends ItemHoe {
  public ItemUnstableHoe(ToolMaterial material) {
    super(material);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  private static Map<Block, Block> till = new HashMap<>();

  private static Map<Block, Block> metatill = new HashMap<>();


  static {
    till.put(Blocks.COBBLESTONE, Blocks.STONE);
    till.put(Blocks.GRAVEL, Blocks.COBBLESTONE);
    till.put(Blocks.SAND, Blocks.GRAVEL);
    till.put(Blocks.MAGMA, Blocks.LAVA);
    till.put(Blocks.OBSIDIAN, Blocks.LAVA);
    till.put(Blocks.GLASS, Blocks.SAND);
    till.put(Blocks.HARDENED_CLAY, Blocks.CLAY);

    metatill.put(Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.STAINED_HARDENED_CLAY);
    metatill.put(Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.STAINED_HARDENED_CLAY);
    metatill.put(Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.STAINED_HARDENED_CLAY);
    metatill.put(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.STAINED_HARDENED_CLAY);


  }

  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  @Nonnull
  public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World worldIn, BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);

    if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
      return EnumActionResult.FAIL;
    } else {
      //  int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
      //  if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

      IBlockState iblockstate = worldIn.getBlockState(pos);
      Block block = iblockstate.getBlock();

      if (metatill.containsKey(block)) {
        this.setBlock(itemstack, player, worldIn, pos, till.get(block).getDefaultState());
        return EnumActionResult.SUCCESS;
      }

      if (till.containsKey(block)) {
        this.setBlock(itemstack, player, worldIn, pos, till.get(block).getDefaultState());
        return EnumActionResult.SUCCESS;
      }

      if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()) && block == Blocks.DIRT)
        switch (iblockstate.getValue(BlockDirt.VARIANT)) {
          case DIRT:
            this.setBlock(itemstack, player, worldIn, pos, Blocks.GRASS.getDefaultState());
            return EnumActionResult.SUCCESS;
          case COARSE_DIRT:
            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
            return EnumActionResult.SUCCESS;
        }
      return EnumActionResult.PASS;
    }
  }

  @Override
  protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
    worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

    if (!worldIn.isRemote)
      worldIn.setBlockState(pos, state, 11);
  }
}