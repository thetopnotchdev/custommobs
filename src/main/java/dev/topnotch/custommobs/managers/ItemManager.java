package dev.topnotch.custommobs.managers;

import dev.topnotch.custommobs.items.CustomItem;
import org.bukkit.Material;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {
    private final Map<String, CustomItem> items = new LinkedHashMap<>();

    public ItemManager() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(new CustomItem("forest_fang", "Forest Fang", Material.IRON_SWORD, 0, 7.0,
                "Small chance to gain Speed I after hitting an enemy.", "Lunge forward and damage the target.", 12));
        register(new CustomItem("brute_club", "Brute Club", Material.IRON_AXE, 0, 9.0,
                "Hits have a chance to apply Slowness.", "Smash the ground and knock nearby enemies back.", 15));
        register(new CustomItem("frost_bow", "Frost Bow", Material.BOW, 0, 0.0,
                "Arrows have a chance to slow targets.", "Fire a frost arrow that slows the target heavily.", 10));
        register(new CustomItem("void_blade", "Void Blade", Material.DIAMOND_SWORD, 0, 12.0,
                "Kills have a chance to grant Speed II.", "Teleport a short distance toward where you are looking.", 15));
        register(new CustomItem("infernal_axe", "Infernal Axe", Material.NETHERITE_AXE, 0, 15.0,
                "Hits have a chance to ignite enemies.", "Create a short-range fire burst around you.", 20));
        register(new CustomItem("ancient_core", "Ancient Core", Material.NETHER_STAR, 0, 0.0,
                "Grants Resistance while held.", "Release an ancient shockwave that damages nearby enemies.", 30));
    }

    public void register(CustomItem item) {
        items.put(item.id(), item);
    }

    public CustomItem get(String id) {
        return items.get(id);
    }

    public Collection<CustomItem> all() {
        return items.values();
    }
}
