package dev.topnotch.custommobs.mobs;

import net.kyori.adventure.text.format.NamedTextColor;

public enum Rarity {
    COMMON("Common", NamedTextColor.WHITE, 40.0, 0.05, 1.0),
    UNCOMMON("Uncommon", NamedTextColor.GREEN, 25.0, 0.10, 1.25),
    RARE("Rare", NamedTextColor.BLUE, 18.0, 0.18, 1.6),
    EPIC("Epic", NamedTextColor.DARK_PURPLE, 10.0, 0.28, 2.1),
    LEGENDARY("Legendary", NamedTextColor.GOLD, 5.0, 0.45, 3.0),
    MYTHIC("Mythic", NamedTextColor.RED, 2.0, 0.70, 4.5);

    private final String displayName;
    private final NamedTextColor color;
    private final double selectionWeight;
    private final double itemDropChance;
    private final double powerMultiplier;

    Rarity(String displayName, NamedTextColor color, double selectionWeight, double itemDropChance, double powerMultiplier) {
        this.displayName = displayName;
        this.color = color;
        this.selectionWeight = selectionWeight;
        this.itemDropChance = itemDropChance;
        this.powerMultiplier = powerMultiplier;
    }

    public String displayName() { return displayName; }
    public NamedTextColor color() { return color; }
    public double selectionWeight() { return selectionWeight; }
    public double itemDropChance() { return itemDropChance; }
    public double powerMultiplier() { return powerMultiplier; }
}
