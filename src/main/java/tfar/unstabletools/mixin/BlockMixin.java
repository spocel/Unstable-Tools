package tfar.unstabletools.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.unstabletools.UnstableTools;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    private static final ThreadLocal<Entity> entityThreadLocal = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<ItemStack> itemStackThreadLocal = ThreadLocal.withInitial(() -> ItemStack.EMPTY);

    @Inject(remap = false,method = "getDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",at = @At("HEAD"))

    private static void interceptEntity(BlockState state, ServerWorld worldIn, BlockPos pos, TileEntity tileEntityIn, Entity entityIn, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        entityThreadLocal.set(entityIn);
        itemStackThreadLocal.set(stack);
    }


    @Inject(method = "*(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"))
    private static void interceptDrops(World worldIn, BlockPos pos, ItemStack stackToSpawn, CallbackInfo ci) {
        UnstableTools.onBlockDrops(worldIn,pos,stackToSpawn,entityThreadLocal.get(),itemStackThreadLocal.get());
    }
}
