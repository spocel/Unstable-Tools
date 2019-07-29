package com.tfar.unstabletools;

import com.tfar.unstabletools.item.IItemColored;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class Client {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e) {
    UnstableTools.MOD_ITEMS.stream().filter(item -> item instanceof IItemColored).forEach(item -> e.getItemColors().registerItemColorHandler(((IItemColored) item)::getColor, item));
  }
}

