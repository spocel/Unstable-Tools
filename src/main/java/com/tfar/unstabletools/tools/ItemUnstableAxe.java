package com.tfar.unstabletools.tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.potion.Effects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemUnstableAxe extends AxeItem {

  public ItemUnstableAxe(IItemTier materialIn, float damage, float attackSpeed, Properties properties) {
    super(materialIn, damage, attackSpeed, properties);
  }

  public ItemUnstableAxe(IItemTier materialIn,Properties properties) {
    this(materialIn, 9, -3,properties);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return 0;
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entityIn instanceof PlayerEntity) || worldIn.isRemote) return;
    if (((PlayerEntity) entityIn).getRNG().nextFloat() > .05) return;
    ((PlayerEntity) entityIn).getFoodStats().addStats(1, 0.2F);  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    if (entity instanceof LivingEntity) {
      if (((LivingEntity) entity).getCreatureAttribute() == CreatureAttribute.UNDEAD)
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 8);
      else ((LivingEntity) entity).heal(8);
    }
    player.addPotionEffect(new EffectInstance(Effects.HUNGER,20,4));
    return true;
  }
}


