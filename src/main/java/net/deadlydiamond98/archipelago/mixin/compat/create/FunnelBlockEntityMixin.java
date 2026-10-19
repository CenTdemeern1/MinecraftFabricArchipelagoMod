package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.deadlydiamond98.archipelago.compat.create.CreateAPRequirementKind;
import net.deadlydiamond98.archipelago.compat.create.CreateAPRequirementUtil;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateDisplayableAPRequirement;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateItemTransportAPRequirement;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IDisplayableRequirementAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(FunnelBlockEntity.class)
public abstract class FunnelBlockEntityMixin extends SmartBlockEntity implements IDisplayableRequirementAccessor, IHaveHoveringInformation {
    @Unique
    @NotNull
    private CreateDisplayableAPRequirement archipelago$displayableRequirement = new CreateItemTransportAPRequirement(this);
    @Unique
    private final static Object PAUSED;

    @Unique
    private static <T extends Enum<T>> T getEnumEntry(String className, String name) {
        try {
            final var clazz = Class.forName(className);
            return Enum.valueOf((Class<T>) clazz, name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        if (FabricLoader.getInstance().isModLoaded("create")) {
            PAUSED = getEnumEntry("com.simibubi.create.content.logistics.funnel.FunnelBlockEntity$Mode", "PAUSED");
        } else {
            PAUSED = null;
        }
    }

    public FunnelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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

    @Coerce
    @Inject(method = "determineCurrentMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getOrEmpty(Lnet/minecraft/state/property/Property;)Ljava/util/Optional;"), cancellable = true)
    void determineMode(CallbackInfoReturnable<Object> cir) {
        if (world == null) return;
        BlockState blockState = this.getCachedState();
        Direction facing = AbstractFunnelBlock.getFunnelFacing(blockState);
        if (facing == null) return;
        Direction attachedDirection = facing.getOpposite();
        BlockPos attachedPos = pos.offset(attachedDirection);
        BlockState attachedState = world.getBlockState(attachedPos);
        Optional<APMissingItem> missingItem = APItemAccessUtil.getMissingItemToInteract(attachedState);
        CreateAPRequirementUtil.changeSingleNullable(
                this,
                missingItem.orElse(null),
                CreateAPRequirementKind.ITEM_TRANSPORT
        );
        if (missingItem.isPresent()) cir.setReturnValue(PAUSED);
    }
}
