package net.deadlydiamond98.archipelago.compat;

import java.util.Set;

import net.deadlydiamond98.archipelago.archipelago.Archipelago;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.World;

public class ArchipelagoCompat {
    private static final boolean NATURES_COMPASS_LOADED =
        FabricLoader.getInstance().isModLoaded("naturescompass");

    public static void applyBiomeRestriction(World world, Set<String> unlockedBiomes) {
        if (NATURES_COMPASS_LOADED & Archipelago.hasLogicSetting("Natures Compass Logic")) {
            NaturesCompassCompat.restrictTo(world, unlockedBiomes);
        } else if (NATURES_COMPASS_LOADED & (!Archipelago.slotData() | !Archipelago.hasLogicSetting("Natures Compass Logic"))) {
            NaturesCompassCompat.unrestrict();
        }
    }
}
