package com.tfar.unstabletools.tools;

import com.tfar.unstabletools.UnstableTools;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

//@Mod.EventBusSubscriber(modid = UnstableTools.MODID)
public class ItemUnstableShears extends ShearsItem {

  public ItemUnstableShears(Properties builder) {
    super(builder);
  }

  @Override
  public int getHarvestLevel(ItemStack stack,@Nonnull ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
    return 3;
  }

  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return ItemUnstablePickaxe.types;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    return 20;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  public boolean canHarvestBlock(BlockState block) {
    return true;
  }

  //doesn't work
  /*@SubscribeEvent
  @SuppressWarnings("unused")
  public static void itemDrops(BlockEvent.HarvestDropsEvent e) {
    PlayerEntity player = e.getHarvester();
    if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemUnstableShears)
      e.getDrops().removeIf(player::addItemStackToInventory);
  }*/
}