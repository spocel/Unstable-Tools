package com.tfar.unstabletools;

import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.tfar.unstabletools.UnstableTools.ObjectHolders.division_sign;
import static com.tfar.unstabletools.UnstableTools.ObjectHolders.unstable_ingot;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Client {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e) {
    ItemColors itemColors = e.getItemColors();
    itemColors.register(((ItemUnstableIngot)unstable_ingot)::getColor, (Item) unstable_ingot);
    itemColors.register(((ItemDivisionSign)division_sign)::getColor, (Item) division_sign);
  }
}
