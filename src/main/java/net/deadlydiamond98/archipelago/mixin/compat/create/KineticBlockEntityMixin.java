package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateCraftingAPRequirement;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateDisplayableAPRequirement;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IDisplayableRequirementAccessor;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IDisplaysMissingAPItemsForCreate;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemsAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity implements IMissingAPItemsAccessor, IDisplaysMissingAPItemsForCreate, IDisplayableRequirementAccessor {
    @Unique
    @NotNull
    private APMissingItem[] archipelago$missingAPItems = new APMissingItem[0];
    @Unique
    @NotNull
    private CreateDisplayableAPRequirement archipelago$dispRequirement = new CreateCraftingAPRequirement(this);

    @Override
    public @NotNull CreateDisplayableAPRequirement archipelago$getDisplayableRequirement() {
        return archipelago$dispRequirement;
    }

    @Override
    public void archipelago$setDisplayableRequirement(@NotNull CreateDisplayableAPRequirement requirement) {
        archipelago$dispRequirement = requirement;
    }

    public KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void archipelago$setMissingAPItems(@NotNull APMissingItem[] missingItems) {
        archipelago$missingAPItems = missingItems;
    }

    public APMissingItem[] archipelago$getMissingAPItems() {
        return archipelago$missingAPItems;
    }

    public BlockEntity archipelago$getBlockEntity() {
        return this;
    }

    @WrapMethod(method = "addToTooltip", remap = false)
    boolean addMissingAPItemTooltip(List<Text> tooltip, boolean isPlayerSneaking, Operation<Boolean> original) {
        boolean originalOutput = original.call(tooltip, isPlayerSneaking);
        return archipelago$addMissingAPItemTooltip(tooltip, originalOutput);
    }
}
