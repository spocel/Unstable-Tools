package tfar.unstabletools.item;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfar.unstabletools.UnstableTools;
import tfar.unstabletools.crafting.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class DivisionSignItem extends Item implements IItemColored {

    private final boolean stable;

    public DivisionSignItem(Properties properties, boolean stable) {
        super(properties);
        this.stable = stable;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(ItemStack stack) {
        return damage(stack.copy());
    }

    public ItemStack damage(ItemStack stack) {
        if (stable) return stack;
        CompoundTag nbt = stack.getOrCreateTag();
        int d = nbt.getInt("d");
        d--;
        if (d > 0) {
            nbt.putInt("d", d);
            return stack;
        } else {
            return new ItemStack(UnstableTools.ObjectHolders.inactive_division_sign);
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (13 * (Config.ServerConfig.uses.get() - stack.getOrCreateTag().getInt("d")) / (double)Config.ServerConfig.uses.get());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this == UnstableTools.ObjectHolders.division_sign;
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        InteractionHand hand = ctx.getHand();
        Level world = player.level;
        BlockPos pos = ctx.getClickedPos();
        if (hand == InteractionHand.OFF_HAND || world.isClientSide) return InteractionResult.FAIL;
        Block block = world.getBlockState(pos).getBlock();
        if (block != Blocks.ENCHANTING_TABLE) return InteractionResult.FAIL;
        long time = world.getLevelData().getDayTime() % 24000;

        boolean correctTime = false;
        if (time <= 17500) message(player, new TranslatableComponent("unstabletools.early"));
        else if (time <= 18500) {
            message(player, new TranslatableComponent("unstabletools.ontime"));
            correctTime = true;
        } else message(player, new TranslatableComponent("unstabletools.late"));
        boolean circle = true;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if (world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE) circle = false;
            }
        }

        if (!circle) message(player, new TranslatableComponent("unstabletools.incomplete"));
        boolean skyVisible = world.canSeeSkyFromBelowWater(pos.above());
        if (!skyVisible) message(player, new TranslatableComponent("unstabletools.nosky"));

        if (correctTime && circle && skyVisible) message(player, new TranslatableComponent("unstabletools.ready"));

        return InteractionResult.PASS;
    }

    private static void message(Player player, Component component) {
        player.sendMessage(component, Util.NIL_UUID);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown() && this == UnstableTools.ObjectHolders.inactive_division_sign)
            tooltip.add(new TextComponent("Drops from Wither").withStyle(ChatFormatting.AQUA));
        if (this != UnstableTools.ObjectHolders.division_sign || !stack.hasTag()) return;
        tooltip.add(new TextComponent("Uses Left: " + stack.getTag().getInt("d")));
    }

    @ObjectHolder("cursedearth:cursed_earth")
    public static final Block cursed_earth = null;

    @SubscribeEvent
    public static void onSacrifice(LivingDeathEvent e) {
        if (!(e.getSource().getEntity() instanceof Player player)) return;
        LivingEntity sacrifice = e.getEntityLiving();
        Level world = sacrifice.level;
        BlockPos pos = sacrifice.blockPosition();
        if (!world.canSeeSkyFromBelowWater(pos)) return;
        Block block = world.getBlockState(pos).getBlock();
        if (block != Blocks.ENCHANTING_TABLE) return;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if (world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE) return;
            }
        }

        long time = world.getLevelData().getDayTime() % 24000;
        if (time <= 17500 || time > 18500) return;

        NonNullList<ItemStack> mainInventory = player.getInventory().items;
        for (int i = 0; i < mainInventory.size(); i++) {
            final ItemStack stack = mainInventory.get(i);
            if (stack.getItem() != UnstableTools.ObjectHolders.inactive_division_sign && stack.getItem() != UnstableTools.ObjectHolders.division_sign) continue;
            ItemStack newStack = new ItemStack(UnstableTools.ObjectHolders.division_sign);
            newStack.getOrCreateTag().putInt("d", Config.ServerConfig.uses.get());
            mainInventory.set(i, newStack);
        }
        if (!world.isClientSide) {
            LightningBolt entity = EntityType.LIGHTNING_BOLT.create(world);
            entity.moveTo(sacrifice.getX(), sacrifice.getY(), sacrifice.getZ());
            world.addFreshEntity(entity);
        }
        if (ModList.get().isLoaded("cursedearth") && Config.ServerConfig.cursed_earth_integration.get()) {
            for (int x = pos.getX() - 7; x < pos.getX() + 8; x++)
                for (int z = pos.getZ() - 7; z < pos.getZ() + 8; z++) {
                    int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
                    for (int y1 = y + 7; y1 > y - 7; y1--) {
                        BlockPos pos1 = new BlockPos(x, y1, z);
                        Block block1 = world.getBlockState(pos1).getBlock();
                        if (block1 == Blocks.DIRT || block1 == Blocks.GRASS_BLOCK) {
                            world.setBlockAndUpdate(pos1, cursed_earth.defaultBlockState());
                            break;
                        }
                    }
                }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (this == UnstableTools.ObjectHolders.division_sign)
        if (group == this.getItemCategory() || group == CreativeModeTab.TAB_SEARCH) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("d",Config.ServerConfig.uses.get());
            items.add(stack);
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return this == UnstableTools.ObjectHolders.inactive_division_sign ? 0xff0000 : this == UnstableTools.ObjectHolders.division_sign ? 0xeedd00 : 0x00ff00;
    }

}
