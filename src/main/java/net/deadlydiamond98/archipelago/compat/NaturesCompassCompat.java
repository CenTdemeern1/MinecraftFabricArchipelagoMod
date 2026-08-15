package net.deadlydiamond98.archipelago.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.chaosthedude.naturescompass.config.NaturesCompassConfig;
import com.chaosthedude.naturescompass.utils.BiomeUtils;

public class NaturesCompassCompat {
    public static void restrictTo(World world, Set<String> unlockedBiomes) {
        Registry<Biome> registry = BiomeUtils.getBiomeRegistry(world);
        List<String> blacklist = new ArrayList<>();
        for (var entry : registry.getEntrySet()) {
            Identifier id = entry.getKey().getValue();
            if (!unlockedBiomes.contains(id.toString())) {
                blacklist.add(id.toString()); // exact match, no glob needed
            }
        }
        NaturesCompassConfig.biomeBlacklist = blacklist;
    }
}
