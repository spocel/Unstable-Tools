package com.tfar.unstabletools;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Client {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e) {
    ItemColors itemColors = e.getItemColors();
    itemColors.register(ObjectHolders.unstableIngot::getColor, (Item) ObjectHolders.unstableIngot);
    itemColors.register(ObjectHolders.divisionSign::getColor, (Item) ObjectHolders.divisionSign);
  }
}
