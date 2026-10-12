package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MechanicalPressBlockEntity.class)
public abstract class MechanicalPressBlockEntityMixin extends BasinOperatingBlockEntity {
    public MechanicalPressBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(method = "getRecipe", at = @At("RETURN"), cancellable = true)
    void filterRecipe(ItemStack item, CallbackInfoReturnable<Optional<PressingRecipe>> cir) {
        Optional<PressingRecipe> potentialRecipe = cir.getReturnValue();
        if (potentialRecipe.isEmpty()) return;
        PressingRecipe recipe = potentialRecipe.get();
        if (world == null) return;
        DynamicRegistryManager registryAccess = world.getRegistryManager();
        ItemStack output = recipe.getOutput(registryAccess);
        Optional<APMissingItem> potentialMissingItem = APItemAccessUtil.getMissingItemFor(output.getItem());
        ((IMissingAPItemAccessor)this).archipelago$changeMissingAPItem(potentialMissingItem.orElse(null));
        if (potentialMissingItem.isPresent()) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
