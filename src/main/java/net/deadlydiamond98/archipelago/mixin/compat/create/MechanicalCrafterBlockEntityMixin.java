package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.RecipeGridHandler;
import net.deadlydiamond98.archipelago.networking.s2c.MissingAPItemForCreateS2CPacket;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemsAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(MechanicalCrafterBlockEntity.class)
public class MechanicalCrafterBlockEntityMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler;tryToApplyRecipe(Lnet/minecraft/world/World;Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler$GroupedItems;)Lnet/minecraft/item/ItemStack;"))
    ItemStack filterRecipeOutput(World world, RecipeGridHandler.GroupedItems items, Operation<ItemStack> original) {
        ItemStack output = original.call(world, items);
        if (!(world instanceof ServerWorld serverWorld)) return output;
        Optional<APMissingItem> potentialMissingItem = APItemAccessUtil.getMissingItemFor(output.getItem());
        if (potentialMissingItem.isPresent()) {
            APMissingItem missingItem = potentialMissingItem.get();
            BlockPos[] positions = RecipeGridHandler
                    .getAllCraftersOfChain((MechanicalCrafterBlockEntity) (Object) this)
                    .stream()
                    .map(BlockEntity::getPos)
                    .toArray(BlockPos[]::new);
            MissingAPItemForCreateS2CPacket.checkAndChangeMultiple(serverWorld, positions, new APMissingItem[] {missingItem});
            return null;
        }
        return output;
    }
}
