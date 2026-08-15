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

    public MobManager() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(new CustomMob("forest_stalker", "Forest Stalker", EntityType.WOLF, 40, 6, Rarity.COMMON, "forest_fang"));
        register(new CustomMob("cave_brute", "Cave Brute", EntityType.ZOMBIE, 70, 8, Rarity.UNCOMMON, "brute_club"));
        register(new CustomMob("frozen_revenant", "Frozen Revenant", EntityType.STRAY, 110, 9, Rarity.RARE, "frost_bow"));
        register(new CustomMob("void_hunter", "Void Hunter", EntityType.ENDERMAN, 180, 12, Rarity.EPIC, "void_blade"));
        register(new CustomMob("infernal_titan", "Infernal Titan", EntityType.PIGLIN_BRUTE, 300, 16, Rarity.LEGENDARY, "infernal_axe"));
        register(new CustomMob("ancient_warden", "Ancient Warden", EntityType.WARDEN, 700, 25, Rarity.MYTHIC, "ancient_core"));
    }

    public void register(CustomMob mob) {
        mobs.put(mob.id(), mob);
    }

    public CustomMob get(String id) {
        return mobs.get(id);
    }

    public Collection<CustomMob> all() {
        return mobs.values();
    }

    public CustomMob randomMob() {
        if (mobs.isEmpty()) return null;
        return mobs.values().stream().skip(ThreadLocalRandom.current().nextInt(mobs.size())).findFirst().orElse(null);
    }

    public Rarity randomRarity() {
        double total = 0;
        for (Rarity rarity : Rarity.values()) total += rarity.selectionWeight();
        double roll = ThreadLocalRandom.current().nextDouble(total);
        double current = 0;
        for (Rarity rarity : Rarity.values()) {
            current += rarity.selectionWeight();
            if (roll < current) return rarity;
        }
        return Rarity.COMMON;
    }
}
