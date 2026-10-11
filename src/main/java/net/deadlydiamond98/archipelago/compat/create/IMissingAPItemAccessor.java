package net.deadlydiamond98.archipelago.compat.create;

import net.deadlydiamond98.archipelago.util.APItemAccessUtil;

import java.util.Optional;

public interface IMissingAPItemAccessor {
    Optional<APItemAccessUtil.MissingItem> archipelago$getMissingAPItem();
}
