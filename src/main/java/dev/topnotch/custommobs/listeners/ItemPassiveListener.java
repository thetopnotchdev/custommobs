package dev.topnotch.custommobs.listeners;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.items.CustomItem;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class ItemPassiveListener implements Listener {
    private final CustomMobsPlugin plugin;
    public ItemPassiveListener(CustomMobsPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta()) return;
        String id = itemStack.getItemMeta().getPersistentDataContainer().get(plugin.itemKey(), PersistentDataType.STRING);
        if (id == null) return;
        switch (id) {
            case "forest_fang" -> { if (chance(.20)) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0)); }
            case "brute_club" -> { if (event.getEntity() instanceof LivingEntity target && chance(.25)) target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)); }
            case "frost_bow" -> { if (event.getEntity() instanceof LivingEntity target && chance(.30)) target.setFreezeTicks(Math.max(target.getFreezeTicks(), 80)); }
            case "infernal_axe" -> { if (event.getEntity() instanceof LivingEntity target && chance(.30)) target.setFireTicks(100); }
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item == null || !item.hasItemMeta()) return;
        String id = item.getItemMeta().getPersistentDataContainer().get(plugin.itemKey(), PersistentDataType.STRING);
        if ("ancient_core".equals(id)) player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 0, true, false));
        if ("void_blade".equals(id)) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, true, false));
    }

    private boolean chance(double value) { return ThreadLocalRandom.current().nextDouble() < value; }
}
