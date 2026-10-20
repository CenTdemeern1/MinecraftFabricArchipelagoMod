package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import com.simibubi.create.content.logistics.packager.PackagingRequest;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.deadlydiamond98.archipelago.compat.create.CreateAPRequirementKind;
import net.deadlydiamond98.archipelago.compat.create.CreateAPRequirementUtil;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateDisplayableAPRequirement;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateItemTransportAPRequirement;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IDisplayableRequirementAccessor;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(PackagerBlockEntity.class)
public abstract class PackagerBlockEntityMixin extends SmartBlockEntity implements IDisplayableRequirementAccessor, IHaveHoveringInformation {
    @Unique
    @NotNull
    private CreateDisplayableAPRequirement archipelago$displayableRequirement = new CreateItemTransportAPRequirement(this);

    public PackagerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @NotNull CreateDisplayableAPRequirement archipelago$getDisplayableRequirement() {
        return archipelago$displayableRequirement;
    }

    @Override
    public void archipelago$setDisplayableRequirement(@NotNull CreateDisplayableAPRequirement requirement) {
        archipelago$displayableRequirement = requirement;
    }

    @Override
    public boolean addToTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        return archipelago$displayableRequirement.addToTooltip(tooltip, isPlayerSneaking);
    }

    @Unique
    boolean isAttachedBlockLocked() {
        if (world == null) return true;
        BlockState state = getCachedState();
        Direction direction = state.get(FacingBlock.FACING);
        BlockState attachedBlock = world.getBlockState(pos.offset(direction.getOpposite()));
        Optional<APMissingItem> missingItem = APItemAccessUtil.getMissingItemToInteract(attachedBlock);
        CreateAPRequirementUtil.changeSingleNullable(
                this,
                missingItem.orElse(null),
                CreateAPRequirementKind.ITEM_TRANSPORT
        );
        return missingItem.isPresent();
    }

    @Inject(method = "attemptToSend", at = @At("HEAD"), cancellable = true, remap = false)
    void interceptSend(List<PackagingRequest> queuedRequests, CallbackInfo ci) {
        if (isAttachedBlockLocked())
            ci.cancel();
    }

    @Inject(method = "unwrapBox", at = @At("HEAD"), cancellable = true, remap = false)
    void interceptUnwrap(ItemStack box, TransactionContext ctx, CallbackInfoReturnable<Boolean> cir) {
        if (isAttachedBlockLocked())
            cir.setReturnValue(false);
    }
}
