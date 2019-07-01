package com.tfar.unstabletools.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class ItemUnstableAxe extends ItemAxe {

  public ItemUnstableAxe(Item.ToolMaterial materialIn, float damage, float attackSpeed) {
    super(materialIn, damage, attackSpeed);
  }

  public ItemUnstableAxe(Item.ToolMaterial materialIn) {
    this(materialIn, 9, -3);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    return 4;
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entityIn instanceof EntityPlayer) || worldIn.isRemote) return;
    if (((EntityPlayer) entityIn).getRNG().nextFloat() > .05) return;
    ((EntityPlayer) entityIn).getFoodStats().addStats(1, 0.2F);
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    if (entity instanceof EntityLivingBase) {
      if (((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 8);
      else ((EntityLivingBase) entity).heal(8);
    }
    player.addPotionEffect(new PotionEffect(MobEffects.HUNGER,20,4));
    return true;
  }
}


