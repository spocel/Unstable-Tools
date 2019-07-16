package com.tfar.unstabletools.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemUnstableIngot extends Item implements IItemColored {

  public static final DamageSource DIVIDE_BY_DIAMOND = (new DamageSource("divide_by_diamond"));
  public static final DamageSource ESCAPE_DIVIDE_BY_DIAMOND = (new DamageSource("escape_divide_by_diamond"));

  public ItemUnstableIngot(Properties properties) {
    super(properties);
  }

  @Override
@OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()){
      tooltip.add(new StringTextComponent("The product of dividing iron by diamond,").applyTextStyle(TextFormatting.AQUA));
      tooltip.add(new StringTextComponent("handle with care").applyTextStyle(TextFormatting.AQUA));
    }
    if (!stack.hasTag()) {
      tooltip.add(new StringTextComponent("'Stable'"));
      return;
    }
    int timer = stack.getOrCreateTag().getInt("timer");
    tooltip.add(new StringTextComponent("Time left: " + timer));
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return (stack.hasTag()) ? 1 : 64;
  }

  @SubscribeEvent
  public static void playertick(TickEvent.PlayerTickEvent e) {

    if (e.phase == TickEvent.Phase.START) return;

    World world = e.player.world;
    Container container = e.player.openContainer;
    boolean explode = false;

    for (Slot slot : container.inventorySlots) {
      ItemStack stack = slot.getStack();
      if (!(stack.getItem() instanceof ItemUnstableIngot) || !stack.hasTag() || slot instanceof CraftingResultSlot)
        continue;
      int timer = stack.getTag().getInt("timer");
      if (timer == 0) {
        stack.shrink(1);
        explode = true;
        continue;
      }
      stack.getTag().putInt("timer", --timer);
    }

    container.detectAndSendChanges();

    if (!explode) return;
    PlayerEntity p = e.player;
    world.createExplosion(null, p.posX, p.posY, p.posZ, 1, Explosion.Mode.NONE);
    p.attackEntityFrom(DIVIDE_BY_DIAMOND, 100);
  }

  @SubscribeEvent
  public static void onContainerClose(PlayerContainerEvent.Close e) {
    PlayerEntity p = e.getEntityPlayer();
    Container c = e.getContainer();
    boolean explode = false;
    for (Slot slot : c.inventorySlots) {
      ItemStack stack = slot.getStack();
      if (!checkExplosion(stack) || slot instanceof CraftingResultSlot) continue;
      stack.shrink(1);
      explode = true;
    }
    if (!explode) return;
    p.world.createExplosion(null, p.posX, p.posY, p.posZ, 1, Explosion.Mode.NONE);
    p.attackEntityFrom(ESCAPE_DIVIDE_BY_DIAMOND, 100);
  }

  @SubscribeEvent
  public static void onItemDrop(ItemTossEvent e) {
    PlayerEntity p = e.getPlayer();
    ItemEntity entityItem = e.getEntityItem();
    ItemStack stack = entityItem.getItem();
    if (checkExplosion(stack)) {
      p.world.createExplosion(null, p.posX, p.posY, p.posZ, 1, Explosion.Mode.NONE);
      p.attackEntityFrom(ESCAPE_DIVIDE_BY_DIAMOND, 100);
      e.setCanceled(true);
    }
  }

  @Override
  public int getColor(ItemStack stack, int tintIndex) {
    if (!stack.hasTag()) {
      return 0xffffff;
    } else {
      CompoundNBT nbt = stack.getTag();
      int color = nbt.getInt("timer");
      double scale = color / 200d;

      int red, green, blue;

      if (scale >= .5) {
        red = green = 0xff;

        blue = (int) ((2 * scale - 1) * 0xff);
      } else if (scale >= .25) {
        red = 0xff;
        green = (int) (2 * scale * 0xff);
        blue = 0;
      } else {
        scale *= 256;
        scale = Math.floor(scale);
        scale %= 2;
        switch ((int) scale) {
          case 0: {
            red = 0xff;
            green = blue = 0;
            break;
          }
          case 1: {
            red = green = 0xff;
            blue = 0;
            break;
          }
          default:
            //this should never be anything other than 1 or 0
            throw new IllegalStateException("thonk");
        }
      }
      return (red << 16) + (green << 8) + blue;
    }
  }

  public static boolean checkExplosion(ItemStack stack) {
    return stack.hasTag() && stack.getItem() instanceof ItemUnstableIngot && stack.getTag().getInt("timer") > 0;
  }
}