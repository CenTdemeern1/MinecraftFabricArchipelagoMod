package net.deadlydiamond98.archipelago.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.chaosthedude.naturescompass.config.NaturesCompassConfig;
import com.chaosthedude.naturescompass.utils.BiomeUtils;

public class NaturesCompassCompat {
    private static final Identifier COMPASS_ID = Identifier.of("naturescompass", "naturescompass");

    public static void giveCompass(ServerPlayerEntity player) {
        Optional<Item> item = Registries.ITEM.getOrEmpty(COMPASS_ID);
        item.ifPresent(compassItem -> {
            if (player.getInventory().count(compassItem) == 0) {
                player.giveItemStack(new ItemStack(compassItem));
            }
        });
    }
    
    public static void restrictTo(World world, Set<String> unlockedBiomes) {
        Registry<Biome> registry = BiomeUtils.getBiomeRegistry(world);
        List<String> blacklist = new ArrayList<>();
        for (var entry : registry.getEntrySet()) {
            Identifier id = entry.getKey().getValue();
            if (!unlockedBiomes.contains(id.toString())) {
                blacklist.add(id.toString());
            }
        }
        NaturesCompassConfig.biomeBlacklist = blacklist;
    }

    public static void unrestrict() {
        NaturesCompassConfig.biomeBlacklist = new ArrayList<String>();
    }
}
