package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.compat.create.IMissingAPItemAccessor;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(KineticBlockEntity.class)
public class KineticBlockEntityMixin {
    @Unique
    Optional<APItemAccessUtil.MissingItem> getMissingItem() {
        if ((Object) this instanceof BasinOperatingBlockEntity) {
            return ((IMissingAPItemAccessor)this).archipelago$getMissingAPItem();
        }
        return Optional.empty();
    }

    @Inject(method = "addToTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    void addMissingAPItemTooltip(List<Text> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        Optional<APItemAccessUtil.MissingItem> potentialMissingItem = getMissingItem();
        if (potentialMissingItem.isPresent()) {
            APItemAccessUtil.MissingItem missingItem = potentialMissingItem.get();
            MutableText heading = Text.translatable("archipelago.check.requires_check", missingItem.name).formatted(Formatting.GOLD);
            tooltip.add(heading);
            MutableText description;
            if (missingItem.amount == 1) {
                description = Text.translatable("archipelago.compat.create.crafting.requires_check", missingItem.name, missingItem.attemptedCraft);
            } else {
                description = Text.translatable("archipelago.compat.create.crafting.requires_check.multiple", missingItem.name, missingItem.attemptedCraft, missingItem.amount);
            }
            tooltip.addAll(TooltipHelper.cutTextComponent(description, FontHelper.Palette.GRAY_AND_WHITE));
            cir.setReturnValue(true);
        }
    }
}
