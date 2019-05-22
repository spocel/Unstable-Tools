package com.tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber
public class ItemUnstableAxe extends ItemAxe {

  private static Set<String> toolclass = Sets.newHashSet("axe");

  public ItemUnstableAxe(Item.ToolMaterial materialIn,float damage, float attackSpeed) {
    super(materialIn,damage,attackSpeed);
  }

  public ItemUnstableAxe(Item.ToolMaterial materialIn) {
    this(materialIn, 9, -3);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  @Nonnull
  public Set<String> getToolClasses(ItemStack stack) {
    return toolclass;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    return 4;
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entityIn instanceof EntityPlayer) || worldIn.isRemote)return;
    if (((EntityPlayer) entityIn).getRNG().nextFloat()>.05)return;
    ((EntityPlayer)entityIn).getFoodStats().addStats(1, 0.2F);
  }
}


