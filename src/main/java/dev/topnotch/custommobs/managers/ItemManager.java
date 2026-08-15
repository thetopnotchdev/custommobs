package dev.topnotch.custommobs.managers;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.items.CustomItem;
import dev.topnotch.custommobs.mobs.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {
    private final Map<String, CustomItem> items = new LinkedHashMap<>();
    private CustomMobsPlugin plugin;

    public ItemManager() { registerDefaults(); }

    public void setPlugin(CustomMobsPlugin plugin) { this.plugin = plugin; }

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

    public void register(CustomItem item) { items.put(item.id(), item); }
    public CustomItem get(String id) { return items.get(id); }
    public Collection<CustomItem> all() { return items.values(); }

    public ItemStack createItemStack(CustomItem item, Rarity rarity) {
        ItemStack stack = new ItemStack(item.material());
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(rarity.displayName() + " " + item.name(), rarity.color())
                .decoration(TextDecoration.BOLD, true));
        if (item.customModelData() > 0) meta.setCustomModelData(item.customModelData());
        meta.lore(java.util.List.of(
                Component.text("Rarity: ").append(Component.text(rarity.displayName(), rarity.color())),
                Component.text("+" + item.attackDamage() + " Attack Damage"),
                Component.text("Passive: " + item.passive()),
                Component.text("Ability: " + item.ability()),
                Component.text("Cooldown: " + item.cooldownSeconds() + "s")
        ));
        if (plugin != null) meta.getPersistentDataContainer().set(plugin.itemKey(), PersistentDataType.STRING, item.id());
        stack.setItemMeta(meta);
        return stack;
    }
}
