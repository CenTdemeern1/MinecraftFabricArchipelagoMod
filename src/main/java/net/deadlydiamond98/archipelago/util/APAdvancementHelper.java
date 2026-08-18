package net.deadlydiamond98.archipelago.util;

import net.deadlydiamond98.archipelago.archipelago.Archipelago;
import net.deadlydiamond98.archipelago.archipelago.ArchipelagoGoalHelper;
import net.deadlydiamond98.archipelago.archipelago.locations.APLocations;
import net.deadlydiamond98.archipelago.common.world.APPersistentState;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class APAdvancementHelper {

    /**
     * Grants all advancements that are checked from Archipelago to all Players
     */
    public static void resyncAdvancements() {
        // Sends all Found Advancements
        Set<Long> foundAdvancementIds = APPersistentState.get().getAdvancementIds();
        Archipelago.run(archipelago -> archipelago.checkLocations(
            foundAdvancementIds.stream()
                               .filter(id -> APLocations.ADVANCEMENT_LOCATIONS.inverse().get(id) != null)
                               .filter(id -> archipelago.getLocationManager().getMissingLocations().contains(id))
                               .collect(Collectors.toList())
        ));
        new ArrayList<>(foundAdvancementIds).forEach(APAdvancementHelper::grantAdvancement);
        // Grant Root Advancements
        grantRootAdvancements();
        // Attempts to trigger goal
        ArchipelagoGoalHelper.tryTriggerGoal();
    }

    /**
     * Grants All Root Advancements to players
     */
    private static void grantRootAdvancements() {
        MinecraftServer server = APServerUtil.server;

        if (server != null) {
            server.getAdvancementLoader().getAdvancements().forEach(advancement -> {
                if (advancement.getRoot() == advancement) {
                    grantAdvancement(advancement.getId());
                }
            });
        }
    }

    /**
     * Grants an Advancement for all players on the server
     * @param id the location id corresponding to the advancement
     */
    public static void grantAdvancement(long id) {
        Identifier advancementID = APLocations.ADVANCEMENT_LOCATIONS.inverse().get(id);
        if (advancementID == null) {
            return;
        }

        grantAdvancement(advancementID);
    }

    /**
     * Grants an Advancement for all players on the server
     * @param id the advancement ID
     */
    public static void grantAdvancement(Identifier id) {
        MinecraftServer server = APServerUtil.server;

        if (server != null) {
            List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
            Advancement advancement = server.getAdvancementLoader().get(id);
            if (advancement == null) {
                return;
            }
            
            for (ServerPlayerEntity player : players) {
                AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
                if (!progress.isDone()) {
                    progress.getUnobtainedCriteria().forEach(s -> {
                        player.getAdvancementTracker().grantCriterion(advancement, s);
                    });
                }
            }
        }
    }

    public static boolean isValidAdvancement(Identifier id) {
        if (APLocations.ADVANCEMENT_LOCATIONS.containsKey(id)) {
            Archipelago archipelago = Archipelago.archipelago;
            if (archipelago != null) {
                Set<Long> locations = archipelago.getLocationManager().getMissingLocations();
                return locations.contains(APLocations.ADVANCEMENT_LOCATIONS.get(id));
            }
        }
        return false;
    }
}
