package com.tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber
public class ItemUnstableShears extends ItemShears {

  @Override
  @Nonnull
  public Set<String> getToolClasses(ItemStack stack) {
    return Sets.newHashSet("pickaxe");
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    return 3;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {
    return 20;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  public boolean canHarvestBlock(IBlockState block) {
    return true;
  }

  @SubscribeEvent
  public static void itemDrops(BlockEvent.HarvestDropsEvent e) {
    EntityPlayer player = e.getHarvester();
    if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemUnstableShears)
      e.getDrops().removeIf(player::addItemStackToInventory);
  }
}