package net.deadlydiamond98.archipelago.archipelago.items;


import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.archipelago.items.type.progression.BiomeUnlockAPItem;
import net.deadlydiamond98.archipelago.archipelago.items.type.progression.PersistantStateAPItem;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;

import java.util.*;

/**
 * Creates Checks that are saved to World Data and Checks that load things from datapacks
 */
public class SavedArchipelagoItems {
    public static final Map<String, String> ID_TO_NAME_MAP = new HashMap<>();
    public static final List<String> PERSISTENT_STATE_PROGRESSIVES = new ArrayList<>();
    public static final List<String> PERSISTENT_STATE_BOOLEANS = new ArrayList<>();
    
    public static void register() {

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VANILLA /////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Abilities
        register("Swim", "swim", true);
        register("Sprint", "sprint", true);
        register("Jump", "jump", true);

        registerWithJson("Chests & Barrels", "chests", true);
        registerWithJson("Sleeping", "spawn_point", true);
        register("Wither Summoning", "wither_summoning", true);
        register("Villager Trading", "trading", true);
        register("Piglin Bartering", "bartering", true);

        // Crafting Stations
        registerWithJson("Brewing", "brewing", true);
        registerWithJson("Enchanting", "enchanting", true);
        registerWithJson("Smithing", "smithing", true);
        registerWithJson("Other Crafting Stations", "misc_stations", true);

        // Single Use Recipes
        registerWithJson("Bucket Recipes", "bucket", true);
        registerWithJson("Flint and Steel Recipes", "igniter", true);
        registerWithJson("Minecart Recipes", "minecarts", true);
        registerWithJson("Brush Recipes", "brush", true);
        registerWithJson("Spyglass Recipes", "spyglass", true);
        registerWithJson("Shear Recipes", "shears", true);
        registerWithJson("Eye of Ender Recipes", "ender_eye", true);
        registerWithJson("Fishing Rod Recipes", "fishing", true);
        registerWithJson("Glass Bottle Recipes", "bottles", true);
        registerWithJson("Resource Compacting Recipes", "compacting", true);
        registerWithJson("Shield Recipes", "shield", true);
        registerWithJson("Bundle Recipes", "bundles", true);
        registerWithJson("TNT Recipes", "tnt", true);

        // Progressive Crafting
        registerWithJson("Progressive Tools", "tools", false);
        registerWithJson("Progressive Weapons", "weapons", false);
        registerWithJson("Progressive Archery", "archery", false);
        registerWithJson("Progressive Armor", "armor", false);
        registerWithJson("Progressive Smelting", "smelting", false);
        registerWithJson("Progressive Dye Recipes", "dyes", false);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // CREATE //////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Crafting Stations
        if (APMod.isModLoaded("create")) {
            registerWithJson("Water Wheels", "water_wheel", true);
            registerWithJson("Windmills", "windmill", true);
            registerWithJson("Steam Engines", "steam_engine", true);
            registerWithJson("Mechanical Mixer Recipes", "mechanical_mixer", true);
            registerWithJson("Mechanical Press Recipes", "mechanical_press", true);
            registerWithJson("Cogwheels", "cogwheel", true);
            registerWithJson("Sand Paper", "sand_paper", true);
            registerWithJson("Blaze Burners", "blaze_burner", true);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // GENERIC /////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        if (hasAdvancedStorage()) {
//            registerWithJson("Advanced Storage Recipes", "advanced_storage_recipes", true);
//        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // BIOMES //////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        Set<String> ocean = Set.of(
            "minecraft:warm_ocean",
            "minecraft:lukewarm_ocean", 
            "minecraft:deep_lukewarm_ocean",
            "minecraft:ocean",
            "minecraft:deep_ocean",
            "minecraft:cold_ocean",
            "minecraft:deep_cold_ocean",
            "minecraft:frozen_ocean",
            "minecraft:deep_frozen_ocean"
        );
        ArchipelagoItems.ITEMS.put("Ocean Biomes", new BiomeUnlockAPItem(ocean));

        Set<String> plains = Set.of(
            "minecraft:plains",
            "minecraft:sunflower_plains",
            "minecraft:snowy_plains",
            "minecraft:ice_spikes",
            "minecraft:mushroom_fields"
        );
        ArchipelagoItems.ITEMS.put("Plains Biomes", new BiomeUnlockAPItem(plains));

        Set<String> savanna = Set.of(
            "minecraft:savanna",
            "minecraft:savanna_plateau",
            "minecraft:windswept_savanna"
        );
        ArchipelagoItems.ITEMS.put("Savanna Biomes", new BiomeUnlockAPItem(savanna));

        Set<String> forest = Set.of(
            "minecraft:forest",
            "minecraft:flower_forest",
            "minecraft:birch_forest",
            "minecraft:dark_forest",
            "minecraft:old_growth_birch_forest",
            "minecraft:old_growth_pine_taiga",
            "minecraft:old_growth_spruce_taiga",
            "minecraft:taiga",
            "minecraft:snowy_taiga",
            "minecraft:windswept_forest"
        );
        ArchipelagoItems.ITEMS.put("Forest Biomes", new BiomeUnlockAPItem(forest));

        Set<String> jungle = Set.of(
            "minecraft:jungle",
            "minecraft:sparse_jungle",
            "minecraft:bamboo_jungle"
        );
        ArchipelagoItems.ITEMS.put("Jungle Biomes", new BiomeUnlockAPItem(jungle));

        Set<String> wetland = Set.of(
            "minecraft:river",
            "minecraft:frozen_river",
            "minecraft:beach",
            "minecraft:stony_beach",
            "minecraft:stony_shore",
            "minecraft:swamp",
            "minecraft:mangrove_swamp"
        );
        ArchipelagoItems.ITEMS.put("Wetland Biomes", new BiomeUnlockAPItem(wetland));

        Set<String> mountain = Set.of(
            "minecraft:frozen_peaks",
            "minecraft:jagged_peaks",
            "minecraft:stony_peaks"
        );
        ArchipelagoItems.ITEMS.put("Mountain Biomes", new BiomeUnlockAPItem(mountain));

        Set<String> highland = Set.of(
            "minecraft:meadow",
            "minecraft:cherry_grove",
            "minecraft:grove",
            "minecraft:snowy_slopes",
            "minecraft:windswept_hills",
            "minecraft:windswept_gravelly_hills"
        );
        ArchipelagoItems.ITEMS.put("Highland Biomes", new BiomeUnlockAPItem(highland));

        Set<String> arid = Set.of(
            "minecraft:desert",
            "minecraft:badlands",
            "minecraft:eroded_badlands",
            "minecraft:wooded_badlands"
        );
        ArchipelagoItems.ITEMS.put("Arid Biomes", new BiomeUnlockAPItem(arid));

        Set<String> underground = Set.of(
            "minecraft:dripstone_caves",
            "minecraft:lush_caves",
            "minecraft:deep_dark"
        );
        ArchipelagoItems.ITEMS.put("Underground Biomes", new BiomeUnlockAPItem(underground));

        Set<String> nether = Set.of(
            "minecraft:nether_wastes",
            "minecraft:warped_forest",
            "minecraft:crimson_forest",
            "minecraft:soul_sand_valley",
            "minecraft:basalt_deltas"
        );
        ArchipelagoItems.ITEMS.put("Nether Biomes", new BiomeUnlockAPItem(nether));

        Set<String> end = Set.of(
            "minecraft:the_end",
            "minecraft:end_highlands",
            "minecraft:end_midlands",
            "minecraft:small_end_islands",
            "minecraft:end_barrens"
        );
        ArchipelagoItems.ITEMS.put("End Biomes", new BiomeUnlockAPItem(end));
    }

    public static boolean hasAdvancedStorage() {
        return APMod.isModLoaded("ironchests");
    }

    public static void registerWithJson(String name, String key, boolean isBool) {
        // Add Check to Progressive Items list for loading json data
        if (isBool) {
            APItemAccessUtil.BOOLEAN_ITEM_IDS.add(key);
            APItemAccessUtil.BOOLEAN_ITEMS.put(key, new HashSet<>());
        } else {
            APItemAccessUtil.PROGRESSIVE_ITEM_IDS.add(key);
            APItemAccessUtil.PROGRESSIVE_ITEMS.put(key, new HashMap<>());
        }
        register(name, key, isBool);
    }

    public static void register(String name, String key, boolean isBool) {
        // Add Check to Items
        ArchipelagoItems.ITEMS.put(name, new PersistantStateAPItem(key));
        ID_TO_NAME_MAP.put(key, name);

        if (isBool) {
            PERSISTENT_STATE_BOOLEANS.add(key);
        } else {
            PERSISTENT_STATE_PROGRESSIVES.add(key);
        }
    }
}
