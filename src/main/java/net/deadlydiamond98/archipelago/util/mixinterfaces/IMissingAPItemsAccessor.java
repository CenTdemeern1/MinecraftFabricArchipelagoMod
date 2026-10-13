package net.deadlydiamond98.archipelago.util.mixinterfaces;

import net.deadlydiamond98.archipelago.util.APMissingItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMissingAPItemsAccessor {
    /// This method is primarily called by the packet deserializer;
    /// calling it on the server will not send it to the client.
    void archipelago$setMissingAPItems(@NotNull APMissingItem[] missingItems);
    APMissingItem[] archipelago$getMissingAPItems();
    /// Change the missing AP item to either no item (null) or a single item
    void archipelago$changeMissingAPItem(@Nullable APMissingItem items);
    /// Changes the list of missing AP items, if it is different from the current one.
    /// This will also notify clients of change if called on the server.
    void archipelago$changeMissingAPItems(@NotNull APMissingItem[] items);
}
