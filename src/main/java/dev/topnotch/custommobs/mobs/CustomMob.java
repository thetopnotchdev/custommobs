package dev.topnotch.custommobs.mobs;

import org.bukkit.entity.EntityType;

public record CustomMob(
        String id,
        String name,
        EntityType baseType,
        double health,
        double damage,
        Rarity rarity,
        String itemId
) {
    public double scaledHealth() {
        return health * rarity.powerMultiplier();
    }

    public double scaledDamage() {
        return damage * rarity.powerMultiplier();
    }
}
