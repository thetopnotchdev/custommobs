package dev.topnotch.custommobs.managers;

import dev.topnotch.custommobs.mobs.CustomMob;
import dev.topnotch.custommobs.mobs.Rarity;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MobManager {
    private final Map<String, CustomMob> mobs = new LinkedHashMap<>();
    public MobManager() { registerDefaults(); }

    private void registerDefaults() {
        register(new CustomMob("forest_stalker", "Forest Stalker", EntityType.WOLF, 40, 6, Rarity.COMMON, "forest_fang"));
        register(new CustomMob("cave_brute", "Cave Brute", EntityType.ZOMBIE, 70, 8, Rarity.UNCOMMON, "brute_club"));
        register(new CustomMob("frostbite", "Frostbite", EntityType.POLAR_BEAR, 55, 7, Rarity.COMMON, "frost_bow"));
        register(new CustomMob("swamp_hag", "Swamp Hag", EntityType.WITCH, 65, 9, Rarity.UNCOMMON, "venom_vial"));
        register(new CustomMob("sand_reaver", "Sand Reaver", EntityType.HUSK, 85, 10, Rarity.UNCOMMON, "sand_cleaver"));
        register(new CustomMob("deepstalker", "Deepstalker", EntityType.DROWNED, 100, 11, Rarity.RARE, "tidal_trident"));
        register(new CustomMob("frozen_revenant", "Frozen Revenant", EntityType.STRAY, 110, 9, Rarity.RARE, "frost_bow"));
        register(new CustomMob("redcap", "Redcap", EntityType.ZOMBIE_VILLAGER, 120, 12, Rarity.RARE, "blood_axe"));
        register(new CustomMob("stormcaller", "Stormcaller", EntityType.EVOKER, 150, 13, Rarity.RARE, "storm_staff"));
        register(new CustomMob("graveborn", "Graveborn", EntityType.WITHER_SKELETON, 160, 14, Rarity.EPIC, "grave_scythe"));
        register(new CustomMob("void_hunter", "Void Hunter", EntityType.ENDERMAN, 180, 12, Rarity.EPIC, "void_blade"));
        register(new CustomMob("crimson_knight", "Crimson Knight", EntityType.PIGLIN, 210, 16, Rarity.EPIC, "crimson_saber"));
        register(new CustomMob("reef_tyrant", "Reef Tyrant", EntityType.ELDER_GUARDIAN, 260, 17, Rarity.EPIC, "tidal_trident"));
        register(new CustomMob("soul_eater", "Soul Eater", EntityType.PHANTOM, 240, 18, Rarity.LEGENDARY, "soul_bow"));
        register(new CustomMob("infernal_titan", "Infernal Titan", EntityType.PIGLIN_BRUTE, 300, 16, Rarity.LEGENDARY, "infernal_axe"));
        register(new CustomMob("iron_colossus", "Iron Colossus", EntityType.IRON_GOLEM, 450, 22, Rarity.LEGENDARY, "colossus_hammer"));
        register(new CustomMob("blight_drake", "Blight Drake", EntityType.RAVAGER, 500, 24, Rarity.LEGENDARY, "blight_mace"));
        register(new CustomMob("nether_warlord", "Nether Warlord", EntityType.WITHER_SKELETON, 550, 26, Rarity.LEGENDARY, "warlord_blade"));
        register(new CustomMob("end_reaper", "End Reaper", EntityType.ENDERMITE, 350, 20, Rarity.LEGENDARY, "reaper_sickle"));
        register(new CustomMob("ancient_warden", "Ancient Warden", EntityType.WARDEN, 700, 25, Rarity.MYTHIC, "ancient_core"));
        register(new CustomMob("starforged", "Starforged", EntityType.SNOW_GOLEM, 850, 30, Rarity.MYTHIC, "starforged_blade"));
        register(new CustomMob("abyssal_king", "Abyssal King", EntityType.WITHER, 1200, 32, Rarity.MYTHIC, "abyssal_crown"));
        register(new CustomMob("celestial_beast", "Celestial Beast", EntityType.RAVAGER, 1500, 38, Rarity.MYTHIC, "celestial_hammer"));
    }

    public void register(CustomMob mob) { mobs.put(mob.id(), mob); }
    public CustomMob get(String id) { return mobs.get(id); }
    public Collection<CustomMob> all() { return mobs.values(); }
    public CustomMob randomMob() { return mobs.isEmpty() ? null : mobs.values().stream().skip(ThreadLocalRandom.current().nextInt(mobs.size())).findFirst().orElse(null); }

    public Rarity randomRarity() {
        double total = 0;
        for (Rarity rarity : Rarity.values()) total += rarity.selectionWeight();
        double roll = ThreadLocalRandom.current().nextDouble(total), current = 0;
        for (Rarity rarity : Rarity.values()) {
            current += rarity.selectionWeight();
            if (roll < current) return rarity;
        }
        return Rarity.COMMON;
    }
}
