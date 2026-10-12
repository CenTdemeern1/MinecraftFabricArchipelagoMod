package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemAccessor;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockBreakingKineticBlockEntity.class)
public class BlockBreakingKineticBlockEntityMixin {
    @Inject(method = "canBreak", at = @At("HEAD"), cancellable = true)
    void isAPBreakable(BlockState stateToBreak, float blockHardness, CallbackInfoReturnable<Boolean> cir) {
        Optional<APMissingItem> potentialMissingItem = APItemAccessUtil.getMissingItemToBreak(stateToBreak);
        ((IMissingAPItemAccessor)this).archipelago$changeMissingAPItem(potentialMissingItem.orElse(null));
        if (potentialMissingItem.isPresent()) {
            cir.setReturnValue(false);
        }
    }
}
