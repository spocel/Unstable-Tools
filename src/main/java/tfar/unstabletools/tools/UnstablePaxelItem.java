package tfar.unstabletools.tools;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Set;

public class UnstablePaxelItem extends DiggerItem {
  public UnstablePaxelItem(float attackDamageIn, float attackSpeedIn, Tier tier, Set<Block> effectiveBlocksIn, Properties builder) {
    super(attackDamageIn, attackSpeedIn, tier,null, builder);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entityIn instanceof Player) || worldIn.isClientSide) return;
    ((Player) entityIn).getFoodData().eat(1, 0.2F);  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Material material = state.getMaterial();
    return material != Material.WOOD && material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.BAMBOO ? super.getDestroySpeed(stack, state) : this.speed;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
    if (entity instanceof LivingEntity) {
      if (((LivingEntity) entity).getMobType() == MobType.UNDEAD)
        entity.hurt(DamageSource.playerAttack(player), 8);
      else ((LivingEntity) entity).heal(8);
      player.addEffect(new MobEffectInstance(MobEffects.HUNGER,20,4));
    }
    return true;
  }
}
