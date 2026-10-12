package net.deadlydiamond98.archipelago.compat.create;

import net.deadlydiamond98.archipelago.util.APMissingItem;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IMissingAPItemAccessor {
    void archipelago$setMissingAPItem(@Nullable APMissingItem missingItem);
    Optional<APMissingItem> archipelago$getMissingAPItem();
    void archipelago$changeMissingAPItem(@Nullable APMissingItem item);
}
