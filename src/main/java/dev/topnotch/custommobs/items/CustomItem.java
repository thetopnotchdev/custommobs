package dev.topnotch.custommobs.items;

import org.bukkit.Material;

public record CustomItem(
        String id,
        String name,
        Material material,
        int customModelData,
        double attackDamage,
        String passive,
        String ability,
        long cooldownSeconds
) {}
