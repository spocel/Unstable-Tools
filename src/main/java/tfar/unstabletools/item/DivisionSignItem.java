package tfar.unstabletools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
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

import net.minecraft.item.Item.Properties;

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
        CompoundNBT nbt = stack.getOrCreateTag();
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
    public double getDurabilityForDisplay(ItemStack stack) {
        return (Config.ServerConfig.uses.get() - stack.getOrCreateTag().getInt("d")) / (double)Config.ServerConfig.uses.get();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return this == UnstableTools.ObjectHolders.division_sign;
    }

    @Override
    @Nonnull
    public ActionResultType useOn(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        Hand hand = ctx.getHand();
        World world = player.level;
        BlockPos pos = ctx.getClickedPos();
        if (hand == Hand.OFF_HAND || world.isClientSide) return ActionResultType.FAIL;
        Block block = world.getBlockState(pos).getBlock();
        if (block != Blocks.ENCHANTING_TABLE) return ActionResultType.FAIL;
        long time = world.getLevelData().getDayTime() % 24000;

        boolean correctTime = false;
        if (time <= 17500) message(player, new TranslationTextComponent("unstabletools.early"));
        else if (time <= 18500) {
            message(player, new TranslationTextComponent("unstabletools.ontime"));
            correctTime = true;
        } else message(player, new TranslationTextComponent("unstabletools.late"));
        boolean circle = true;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                if (world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE) circle = false;
            }
        }

        if (!circle) message(player, new TranslationTextComponent("unstabletools.incomplete"));
        boolean skyVisible = world.canSeeSkyFromBelowWater(pos.above());
        if (!skyVisible) message(player, new TranslationTextComponent("unstabletools.nosky"));

        if (correctTime && circle && skyVisible) message(player, new TranslationTextComponent("unstabletools.ready"));

        return ActionResultType.PASS;
    }

    private static void message(PlayerEntity player, ITextComponent component) {
        player.sendMessage(component, Util.NIL_UUID);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown() && this == UnstableTools.ObjectHolders.inactive_division_sign)
            tooltip.add(new StringTextComponent("Drops from Wither").withStyle(TextFormatting.AQUA));
        if (this != UnstableTools.ObjectHolders.division_sign || !stack.hasTag()) return;
        tooltip.add(new StringTextComponent("Uses Left: " + stack.getTag().getInt("d")));
    }

    @ObjectHolder("cursedearth:cursed_earth")
    public static final Block cursed_earth = null;

    @SubscribeEvent
    public static void onSacrifice(LivingDeathEvent e) {
        if (!(e.getSource().getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) e.getSource().getEntity();
        LivingEntity sacrifice = e.getEntityLiving();
        World world = sacrifice.level;
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

        NonNullList<ItemStack> mainInventory = player.inventory.items;
        for (int i = 0; i < mainInventory.size(); i++) {
            final ItemStack stack = mainInventory.get(i);
            if (stack.getItem() != UnstableTools.ObjectHolders.inactive_division_sign && stack.getItem() != UnstableTools.ObjectHolders.division_sign) continue;
            ItemStack newStack = new ItemStack(UnstableTools.ObjectHolders.division_sign);
            newStack.getOrCreateTag().putInt("d", Config.ServerConfig.uses.get());
            mainInventory.set(i, newStack);
        }
        if (!world.isClientSide) {
            LightningBoltEntity entity = EntityType.LIGHTNING_BOLT.create(world);
            entity.moveTo(sacrifice.getX(), sacrifice.getY(), sacrifice.getZ());
            world.addFreshEntity(entity);
        }
        if (ModList.get().isLoaded("cursedearth") && Config.ServerConfig.cursed_earth_integration.get()) {
            for (int x = pos.getX() - 7; x < pos.getX() + 8; x++)
                for (int z = pos.getZ() - 7; z < pos.getZ() + 8; z++) {
                    int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) - 1;
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
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (this == UnstableTools.ObjectHolders.division_sign)
        if (group == this.getItemCategory() || group == ItemGroup.TAB_SEARCH) {
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
