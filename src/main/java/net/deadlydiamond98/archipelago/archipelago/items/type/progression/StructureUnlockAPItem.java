package net.deadlydiamond98.archipelago.archipelago.items.type.progression;

import java.util.Set;

import io.github.archipelagomw.parts.NetworkItem;
import net.deadlydiamond98.archipelago.archipelago.items.type.AbstractAPItem;
import net.deadlydiamond98.archipelago.common.world.APPersistentState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class StructureUnlockAPItem extends AbstractAPItem{
    private final Set<String> structures;

    public StructureUnlockAPItem(Set<String> structures) {
        this.structures = structures;
    }

    @Override
    protected void triggerOneTimeEffect(NetworkItem item, MinecraftServer server) {
        APPersistentState state = APPersistentState.get();
        ServerWorld world = server.getOverworld();
        state.unlockStructures(structures, world);
    }
}
