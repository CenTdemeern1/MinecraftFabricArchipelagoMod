package net.deadlydiamond98.archipelago.compat.create;

import net.deadlydiamond98.archipelago.util.APItemAccessUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IMissingAPItemAccessor {
    void archipelago$setMissingAPItem(@Nullable APItemAccessUtil.MissingItem missingItem);
    Optional<APItemAccessUtil.MissingItem> archipelago$getMissingAPItem();
}
