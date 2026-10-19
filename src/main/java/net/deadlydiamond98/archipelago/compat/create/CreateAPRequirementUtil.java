package net.deadlydiamond98.archipelago.compat.create;

import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.compat.create.requirements.CreateDisplayableAPRequirement;
import net.deadlydiamond98.archipelago.networking.s2c.MissingAPItemForCreateS2CPacket;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IDisplayableRequirementAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@MethodsReturnNonnullByDefault
public class CreateAPRequirementUtil {
    public static void changeSingleNullable(
            BlockEntity blockEntity,
            @Nullable APMissingItem missingItem,
            CreateAPRequirementKind kind
    ) {
        if (!(blockEntity.getWorld() instanceof ServerWorld serverWorld)) return;
        APMissingItem[] array = arrayFromNullable(missingItem);
        if (!setMissingItems(blockEntity, array, kind)) return;
        MissingAPItemForCreateS2CPacket.send(serverWorld, new BlockPos[] { blockEntity.getPos() }, array);
    }

    public static void changeMultipleNullable(
            BlockEntity[] blockEntities,
            @Nullable APMissingItem missingItem,
            CreateAPRequirementKind kind
    ) {
        APMissingItem[] array = arrayFromNullable(missingItem);
        changeMultiple(blockEntities, array, kind);
    }

    public static void changeMultiple(
            BlockEntity[] blockEntities,
            APMissingItem[] missingItems,
            CreateAPRequirementKind kind
    ) {
        if (blockEntities.length == 0) return;
        if (!(blockEntities[0].getWorld() instanceof ServerWorld serverWorld)) return;
        boolean needsNotify = false;
        for (BlockEntity blockEntity : blockEntities) {
            needsNotify |= setMissingItems(blockEntity, missingItems, kind);
        }
        if (!needsNotify) return;
        BlockPos[] positions = Arrays.stream(blockEntities).map(BlockEntity::getPos).toArray(BlockPos[]::new);
        MissingAPItemForCreateS2CPacket.send(serverWorld, positions, missingItems);
    }

    private static boolean setMissingItems(
            BlockEntity blockEntity,
            APMissingItem[] missingItems,
            CreateAPRequirementKind kind
    ) {
        if (!(blockEntity instanceof IDisplayableRequirementAccessor accessor)) {
            APMod.LOGGER.error(
                    "Just tried accessing the Displayable Requirement of {} " +
                            "which doesn't implement IDisplayableRequirementAccessor",
                    blockEntity.getClass()
            );
            return false;
        }
        CreateDisplayableAPRequirement requirement = accessor.archipelago$getDisplayableRequirement();
        if (requirement.getKind() == kind) {
            if (Arrays.equals(requirement.missingAPItems, missingItems)) return false;
            requirement.missingAPItems = missingItems;
        } else {
            accessor.archipelago$setDisplayableRequirement(kind.instantiate(blockEntity, missingItems));
        }
        return true;
    }

    private static APMissingItem[] arrayFromNullable(@Nullable APMissingItem missingItem) {
        if (missingItem == null)
            return new APMissingItem[0];
        else
            return new APMissingItem[] { missingItem };
    }
}
