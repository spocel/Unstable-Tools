package tfar.unstabletools.item.tools;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class ItemUnstableAxe extends AxeItem {

  public ItemUnstableAxe(Tier materialIn, float damage, float attackSpeed, Properties properties) {
    super(materialIn, damage, attackSpeed, properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entity, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entity instanceof Player) || worldIn.isClientSide) return;
    ((Player) entity).getFoodData().eat(1, 0.2F);
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      if (livingEntity.getMobType() == MobType.UNDEAD)
        entity.hurt(DamageSource.playerAttack(player), 8);
      else livingEntity.heal(8);
      player.addEffect(new MobEffectInstance(MobEffects.HUNGER,20,4));
    }
    return true;
  }
}


