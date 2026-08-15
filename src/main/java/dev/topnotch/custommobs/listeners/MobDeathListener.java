package dev.topnotch.custommobs.listeners;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.items.CustomItem;
import dev.topnotch.custommobs.mobs.CustomMob;
import dev.topnotch.custommobs.mobs.Rarity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class MobDeathListener implements Listener {
    private final CustomMobsPlugin plugin;

    public MobDeathListener(CustomMobsPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        String mobId = entity.getPersistentDataContainer().get(plugin.mobKey(), PersistentDataType.STRING);
        String rarityName = entity.getPersistentDataContainer().get(plugin.rarityKey(), PersistentDataType.STRING);
        if (mobId == null || rarityName == null) return;

        event.getDrops().clear();
        CustomMob mob = plugin.mobManager().get(mobId);
        Rarity rarity;
        try { rarity = Rarity.valueOf(rarityName); }
        catch (IllegalArgumentException ignored) { return; }

        if (ThreadLocalRandom.current().nextDouble() <= rarity.itemDropChance() && mob != null) {
            CustomItem customItem = plugin.itemManager().get(mob.itemId());
            if (customItem != null) event.getDrops().add(plugin.itemManager().createItemStack(customItem, rarity));
        }

        if (entity.getKiller() instanceof Player player) {
            player.sendMessage("§7You defeated §" + rarity.color().asHexString() + rarity.displayName() + " " + (mob == null ? "Custom Mob" : mob.name()) + "§7.");
        }
    }
}
