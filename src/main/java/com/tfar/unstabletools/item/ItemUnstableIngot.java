package com.tfar.unstabletools.item;

import com.tfar.unstabletools.UnstableToolsConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemUnstableIngot extends Item implements IItemColored {

  public static final DamageSource DIVIDE_BY_DIAMOND = (new DamageSource("divide_by_diamond"));
  public static final DamageSource ESCAPE_DIVIDE_BY_DIAMOND = (new DamageSource("escape_divide_by_diamond"));

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (worldIn == null) return;
    if (!stack.hasTagCompound()) {
      tooltip.add("'Stable'");
      return;
    }
    int timer = stack.getTagCompound().getInteger("timer");
    tooltip.add("Time left: " + timer);
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return (stack.hasTagCompound()) ? 1 : 64;
  }

  @SubscribeEvent
  public static void playertick(TickEvent.PlayerTickEvent e) {

    if (e.phase == TickEvent.Phase.START) return;

    World world = e.player.world;
    Container container = e.player.openContainer;
    if (UnstableToolsConfig.allowed_container_classes.contains(container.getClass())) {
      boolean explode = false;

      for (Slot slot : container.inventorySlots) {
        ItemStack stack = slot.getStack();
        if (!(stack.getItem() instanceof ItemUnstableIngot) || !stack.hasTagCompound() || slot instanceof SlotCrafting)
          continue;
        int timer = stack.getTagCompound().getInteger("timer");
        if (timer == 0) {
          stack.shrink(1);
          explode = true;
          continue;
        }
        stack.getTagCompound().setInteger("timer", --timer);
      }
      if (!world.isRemote)
        container.detectAndSendChanges();

      if (!explode) return;
      EntityPlayer p = e.player;
      world.createExplosion(null, p.posX, p.posY, p.posZ, 1, false);
      p.attackEntityFrom(DIVIDE_BY_DIAMOND, 100);
    }
  }

  @SubscribeEvent
  public static void onContainerClose(PlayerContainerEvent.Close e) {
    EntityPlayer p = e.getEntityPlayer();
    Container c = e.getContainer();
    boolean explode = false;
    for (Slot slot : c.inventorySlots) {
      ItemStack stack = slot.getStack();
      if (!checkExplosion(stack) || slot instanceof SlotCrafting) continue;
      stack.shrink(1);
      explode = true;
    }
    if (!explode) return;
    p.world.createExplosion(null, p.posX, p.posY, p.posZ, 1, false);
    p.attackEntityFrom(ESCAPE_DIVIDE_BY_DIAMOND, 100);
  }

  @SubscribeEvent
  public static void onItemDrop(ItemTossEvent e) {
    EntityPlayer p = e.getPlayer();
    EntityItem entityItem = e.getEntityItem();
    ItemStack stack = entityItem.getItem();
    if (checkExplosion(stack)) {
      p.world.createExplosion(null, p.posX, p.posY, p.posZ, 1, false);
      p.attackEntityFrom(ESCAPE_DIVIDE_BY_DIAMOND, 100);
      e.setCanceled(true);
    }
  }

  @Override
  public int getColor(ItemStack stack, int tintIndex) {
    if (!stack.hasTagCompound()) {
      return 0xffffff;
    } else {
      NBTTagCompound nbt = stack.getTagCompound();
      int color = nbt.getInteger("timer");
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
            throw new IllegalStateException();
        }
      }
      return (red << 16) + (green << 8) + blue;
    }
  }

  public static boolean checkExplosion(ItemStack stack) {
    return stack.hasTagCompound() && stack.getItem() instanceof ItemUnstableIngot && stack.getTagCompound().getInteger("timer") > 0;
  }
}