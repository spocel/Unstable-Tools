package tfar.unstabletools.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfar.unstabletools.crafting.Config;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemUnstableIngot extends Item implements IItemColored {

  public static final DamageSource DIVIDE_BY_DIAMOND = (new DamageSource("divide_by_diamond").bypassArmor());
  public static final DamageSource ESCAPE_DIVIDE_BY_DIAMOND = (new DamageSource("escape_divide_by_diamond").bypassArmor());

  public ItemUnstableIngot(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (Screen.hasShiftDown()){
      tooltip.add(Component.literal("The product of dividing iron by diamond,").withStyle(ChatFormatting.AQUA));
      tooltip.add(Component.literal("handle with care").withStyle(ChatFormatting.AQUA));
    }
    if (!stack.hasTag()) {
      tooltip.add(Component.literal("'Stable'"));
      return;
    }
    int timer = stack.getTag().getInt("timer");
    tooltip.add(Component.literal("Time left: " + timer));
  }

  @SubscribeEvent
  public static void playertick(TickEvent.PlayerTickEvent e) {

    if (e.phase == TickEvent.Phase.START) return;

    AbstractContainerMenu container = e.player.containerMenu;

    try {
      MenuType<?> type = container.getType();
      if (!Config.ServerConfig.allowed_containers.get().contains(Registry.MENU.getKey(type).toString()))
        return;
    } catch (Exception ex) {
      return;
    }

    Level world = e.player.level;

    if (world.isClientSide)return;
    boolean explode = false;

    List<Slot> inventorySlots = container.slots;
    for (Slot slot : inventorySlots) {
      ItemStack stack = slot.getItem();
      if (!(stack.getItem() instanceof ItemUnstableIngot) || !stack.hasTag() || slot instanceof ResultSlot)
        continue;
      int timer = stack.getTag().getInt("timer");
      if (timer <= 0) {
        slot.set(ItemStack.EMPTY);
        explode = true;
        continue;
      }
      stack.getTag().putInt("timer", --timer);
    }

    if (!explode) return;
    boom(e.player);
  }

  @SubscribeEvent
  public static void onContainerClose(PlayerContainerEvent.Close e) {
    AbstractContainerMenu c = e.getContainer();
    boolean explode = false;
    for (Slot slot : c.slots) {
      ItemStack stack = slot.getItem();
      if (!checkExplosion(stack) || slot instanceof ResultSlot) continue;
      slot.set(ItemStack.EMPTY);
      explode = true;
    }
    if (!explode) return;
    boom(e.getEntity());
  }

  @SubscribeEvent
  public static void onItemDrop(ItemTossEvent e) {
    Player p = e.getPlayer();
    ItemEntity entityItem = e.getEntity();
    ItemStack stack = entityItem.getItem();
    if (checkExplosion(stack)) {
      boom(p);
      e.setCanceled(true);
    }
  }

  public static void boom(Player player) {
    Level world = player.level;
    world.explode(null, player.getX(), player.getY(), player.getZ(), 1, Explosion.BlockInteraction.NONE);
    player.hurt(DIVIDE_BY_DIAMOND, 100);
  }

  @Override
  public int getColor(ItemStack stack, int tintIndex) {
    if (!stack.hasTag()) {
      return 0xffffff;
    } else {
      CompoundTag nbt = stack.getTag();
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
            throw new IllegalStateException("thonk"+scale);
        }
      }
      return (red << 16) + (green << 8) + blue;
    }
  }

  public static boolean checkExplosion(ItemStack stack) {
    return stack.hasTag() && stack.getItem() instanceof ItemUnstableIngot && stack.getTag().getInt("timer") > 0;
  }
}