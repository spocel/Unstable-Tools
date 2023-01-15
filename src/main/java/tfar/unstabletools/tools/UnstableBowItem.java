package tfar.unstabletools.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

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
  public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
    if (entityLiving instanceof Player) {
      Player player = (Player)entityLiving;
      boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
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
          AbstractArrow abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, player);
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

          abstractarrowentity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
          worldIn.addFreshEntity(abstractarrowentity);
        }

        worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, (float) (1.0F / 1.2f + f * 0.5F));

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
