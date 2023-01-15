package tfar.unstabletools.tools;

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

import net.minecraft.item.Item.Properties;

public class UnstableBowItem extends BowItem {
  public UnstableBowItem(Properties p_i48522_1_) {
    super(p_i48522_1_);
 //   this.addPropertyOverride(new ResourceLocation("pull"), (stack, world, entity) -> {
  //    if (entity == null) {
 //       return 0.0F;
 //     } else {
   //     return !(entity.getActiveItemStack().getItem() instanceof BowItem) ? 0.0F : (float)(stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
//      }
  //  });
   // this.addPropertyOverride(new ResourceLocation("pulling"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   */
  public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity)entityLiving;
      boolean flag = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
      ItemStack itemstack = player.getProjectile(stack);

      int i = this.getUseDuration(stack) - timeLeft;
      i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, !itemstack.isEmpty() || flag);
      if (i < 0) return;

      if (!itemstack.isEmpty() || flag) {
        if (itemstack.isEmpty()) {
          itemstack = new ItemStack(Items.ARROW);
        }

        double f = getArrowSpeed(i);
        if (f < 0.1D) {
          return;
        }
        if (!worldIn.isClientSide) {
          ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
          AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, player);
          abstractarrowentity = customArrow(abstractarrowentity);
      //    abstractarrowentity.shoot(player, player.rotationPitch, player.rotationYaw, 0, (float) (f * 3.0F), 1);
          if (f >= 1) {
            abstractarrowentity.setCritArrow(true);
          }

          int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
          if (j > 0) {
            abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double)j * 0.5D + 0.5D);
          }

          int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
          if (k > 0) {
            abstractarrowentity.setKnockback(k);
          }

          if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
            abstractarrowentity.setSecondsOnFire(100);
          }

          abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
          worldIn.addFreshEntity(abstractarrowentity);
        }

        worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, (float) (1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F));

        player.awardStat(Stats.ITEM_USED.get(this));
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
    return enchantment != Enchantments.INFINITY_ARROWS && super.canApplyAtEnchantingTable(stack,enchantment);
  }
}
