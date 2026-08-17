package net.deadlydiamond98.archipelago.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.chaosthedude.explorerscompass.config.ExplorersCompassConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

public class ExplorersCompassCompat {
    private static final Identifier COMPASS_ID = Identifier.of("explorerscompass", "explorerscompass");

    public static void giveCompass(ServerPlayerEntity player) {
        Optional<Item> item = Registries.ITEM.getOrEmpty(COMPASS_ID);
        item.ifPresent(compassItem -> {
            if (player.getInventory().count(compassItem) == 0) {
                player.giveItemStack(new ItemStack(compassItem));
            }
        });
    }

    public static void restrictTo(World world, Set<String> unlockedStructures) {
        Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
        List<String> blacklist = new ArrayList<>();
        for (var entry : registry.getEntrySet()) {
            Identifier id = entry.getKey().getValue();
            if (!unlockedStructures.contains(id.toString())) {
                blacklist.add(id.toString());
            }
        }
        ExplorersCompassConfig.structureBlacklist = blacklist;
    }

    public static void unrestrict() {
        ExplorersCompassConfig.structureBlacklist = new ArrayList<String>();
    }
}
