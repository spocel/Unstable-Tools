package tfar.unstabletools.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
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

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",at = @At("HEAD"))

    private static void interceptEntity(BlockState state, ServerLevel worldIn, BlockPos pos, BlockEntity tileEntityIn, Entity entityIn, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        entityThreadLocal.set(entityIn);
        itemStackThreadLocal.set(stack);
    }


    @Inject(method = "*(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"))
    private static void interceptDrops(Level worldIn, BlockPos pos, ItemStack stackToSpawn, CallbackInfo ci) {
        UnstableTools.onBlockDrops(worldIn,pos,stackToSpawn,entityThreadLocal.get(),itemStackThreadLocal.get());
    }
}
