package com.tfar.unstabletools.item;

import com.tfar.unstabletools.crafting.IDivisionItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
@Mod.EventBusSubscriber
public class ItemDivisionSign extends Item implements IDivisionItem,IItemColored {

  public ItemDivisionSign(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return stack.hasTag() && stack.getTag().getBoolean("activated");
  }

  @Override
  @Nonnull
  public ItemStack getContainerItem(ItemStack itemStack) {
    return damage(itemStack.copy());
  }

  public static ItemStack damage(ItemStack stack) {
    CompoundNBT nbt = stack.getTag();
    boolean stable = stack.getTag().getBoolean("stable");
    if (stable)return stack;
    int d = nbt.getInt("d");
    d--;
    nbt.putInt("d", d);
    stack.setTag(nbt);
    return stack;
  }

  @Override
  @Nonnull
  public ActionResultType onItemUse(ItemUseContext ctx) {
    PlayerEntity player = ctx.getPlayer();
    Hand hand = ctx.getHand();
    World world = player.world;
    BlockPos pos = ctx.getPos();
    if (hand == Hand.OFF_HAND || world.isRemote)return ActionResultType.FAIL;
    Block block = world.getBlockState(pos).getBlock();
    if (block != Blocks.ENCHANTING_TABLE) return ActionResultType.FAIL;
    long time = world.getWorldInfo().getDayTime() % 24000;

    boolean correctTime = false;
    if (time <= 17500) player.sendMessage(new TranslationTextComponent("unstabletools.early"));
    else if (time <= 18500) {player.sendMessage(new TranslationTextComponent("unstabletools.ontime"));correctTime=true;}
    else player.sendMessage(new TranslationTextComponent("unstabletools.late"));
    boolean circle = true;
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) continue;
        BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
        if(world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE)circle = false;
      }
    }

    if (!circle)player.sendMessage(new TranslationTextComponent("unstabletools.incomplete"));
    boolean skyVisible = world.canBlockSeeSky(pos.up());
    if (!skyVisible)player.sendMessage(new TranslationTextComponent("unstabletools.nosky"));

    if(correctTime && circle && skyVisible)player.sendMessage(new TranslationTextComponent("unstabletools.ready"));

    return ActionResultType.PASS;
  }



  @Override
@OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown())tooltip.add(new StringTextComponent("Drops from Wither").applyTextStyle(TextFormatting.AQUA));
    if (!stack.hasTag()) return;
    boolean stable = stack.getTag().getBoolean("stable");
    if (stable){tooltip.add(new StringTextComponent("Stable"));return;}
    boolean activated = stack.getTag().getBoolean("activated");
    tooltip.add(new StringTextComponent("Activated: " + activated));
    if (!activated) return;
    tooltip.add(new StringTextComponent("Uses Left: " + stack.getTag().getInt("d")));
  }

  @SubscribeEvent
  public static void onSacrifice(LivingDeathEvent e){
    if (!(e.getSource().getTrueSource() instanceof PlayerEntity))return;
    PlayerEntity player = (PlayerEntity)e.getSource().getTrueSource();
    LivingEntity sacrifice = e.getEntityLiving();
    World world = sacrifice.world;
    BlockPos pos = sacrifice.getPosition();
    if (!world.canBlockSeeSky(pos))return;
    Block block = world.getBlockState(pos).getBlock();
    if (block != Blocks.ENCHANTING_TABLE)return;

    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) continue;
        BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
        if(world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE)return;
      }
    }

    long time = world.getWorldInfo().getDayTime() % 24000;
    if (time <= 17500 || time > 18500)return;

    for (ItemStack slot : player.inventory.mainInventory){
      if (!(slot.getItem() instanceof IDivisionItem))continue;
      slot.getOrCreateTag().putBoolean("activated",true);
      slot.getOrCreateTag().putInt("d",256);
    }
    if (!world.isRemote)
      ((ServerWorld)world).addLightningBolt(new LightningBoltEntity(sacrifice.world, sacrifice.posX, sacrifice.posY, sacrifice.posZ, false));
  }

  @Override
  public int getColor(ItemStack stack, int tintIndex) {
    return (!stack.hasTag()) ? 0xee0000 : (stack.getTag().getBoolean("stable")) ? 0x50dd00 : 0xee0000;
  }
}
