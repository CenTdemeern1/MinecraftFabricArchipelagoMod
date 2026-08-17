package net.deadlydiamond98.archipelago.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chaosthedude.explorerscompass.config.ExplorersCompassConfig;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

public class ExplorersCompassCompat {
    public static void restrictTo(World world, Set<String> unlockedStructures) {
        Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
        List<String> blacklist = new ArrayList<>();
        for (var entry : registry.getEntrySet()) {
            Identifier id = entry.getKey().getValue();
            if (!unlockedStructures.contains(id.toString())) {
                blacklist.add(id.toString()); // exact match, no glob needed
            }
        }
        ExplorersCompassConfig.structureBlacklist = blacklist;
    }

    public static void unrestrict() {
        ExplorersCompassConfig.structureBlacklist = new ArrayList<String>();
    }
}
