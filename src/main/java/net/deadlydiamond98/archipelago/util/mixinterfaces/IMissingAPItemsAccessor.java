package net.deadlydiamond98.archipelago.util.mixinterfaces;

import net.deadlydiamond98.archipelago.networking.s2c.MissingAPItemForCreateS2CPacket;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public interface IMissingAPItemsAccessor {
    /// This method is primarily called by the packet deserializer;
    /// calling it on the server will not send it to the client.
    void archipelago$setMissingAPItems(@NotNull APMissingItem[] missingItems);
    APMissingItem[] archipelago$getMissingAPItems();
    /// Change the missing AP item to either no item (null) or a single item
    default void archipelago$changeMissingAPItem(@Nullable APMissingItem item) {
        if (item == null) {
            archipelago$changeMissingAPItems(new APMissingItem[0]);
        } else {
            archipelago$changeMissingAPItems(new APMissingItem[] { item });
        }
    }
    BlockEntity archipelago$getBlockEntity();
    /// Changes the list of missing AP items, if it is different from the current one.
    /// This will also notify clients of change if called on the server.
    default void archipelago$changeMissingAPItems(@NotNull APMissingItem[] items) {
        if (Arrays.equals(items, archipelago$getMissingAPItems())) return;
        archipelago$setMissingAPItems(items);
        BlockEntity blockEntity = archipelago$getBlockEntity();
        if (blockEntity.getWorld() instanceof ServerWorld serverWorld) {
            MissingAPItemForCreateS2CPacket.send(
                    serverWorld.getPlayers(),
                    new BlockPos[] { blockEntity.getPos() },
                    items
            );
        }
    }
}
