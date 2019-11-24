package com.tfar.unstabletools.tools;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class UnstableBowItem extends BowItem {
  public UnstableBowItem(Properties p_i48522_1_) {
    super(p_i48522_1_);
    this.addPropertyOverride(new ResourceLocation("pull"), (stack, world, entity) -> {
      if (entity == null) {
        return 0.0F;
      } else {
        return !(entity.getActiveItemStack().getItem() instanceof BowItem) ? 0.0F : (float)(stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
      }
    });
    this.addPropertyOverride(new ResourceLocation("pulling"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   */
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity playerentity = (PlayerEntity)entityLiving;
      boolean flag = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
      ItemStack itemstack = playerentity.findAmmo(stack);

      int i = this.getUseDuration(stack) - timeLeft;
      i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
      if (i < 0) return;

      if (!itemstack.isEmpty() || flag) {
        if (itemstack.isEmpty()) {
          itemstack = new ItemStack(Items.ARROW);
        }

        double f = getArrowSpeed(i);
        if (f < 0.1D) {
          return;
        }
        if (!worldIn.isRemote) {
          ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
          AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
          abstractarrowentity = customeArrow(abstractarrowentity);
          abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0, (float) (f * 3.0F), 1);
          if (f >= 1) {
            abstractarrowentity.setIsCritical(true);
          }

          int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
          if (j > 0) {
            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double)j * 0.5D + 0.5D);
          }

          int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
          if (k > 0) {
            abstractarrowentity.setKnockbackStrength(k);
          }

          if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
            abstractarrowentity.setFire(100);
          }

          abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
          worldIn.addEntity(abstractarrowentity);
        }

        worldIn.playSound(null, playerentity.posX, playerentity.posY, playerentity.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, (float) (1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F));

        playerentity.addStat(Stats.ITEM_USED.get(this));
      }
    }
  }

  /**
   * Gets the velocity of the arrow entity from the bow's charge
   */
  public static double getArrowSpeed(int charge) {
    double speed = charge / 20d;
    speed = (speed * speed + speed * 2) / 3.0;
    if (speed > 3) {
      speed = 3;
    }

    return speed;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment != Enchantments.INFINITY && super.canApplyAtEnchantingTable(stack,enchantment);
  }
}
