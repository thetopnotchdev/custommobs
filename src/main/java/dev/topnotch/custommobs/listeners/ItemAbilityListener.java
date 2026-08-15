package dev.topnotch.custommobs.listeners;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.items.CustomItem;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class ItemAbilityListener implements Listener {
    private final CustomMobsPlugin plugin;
    public ItemAbilityListener(CustomMobsPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack stack = event.getItem();
        if (stack == null || !stack.hasItemMeta()) return;
        String itemId = stack.getItemMeta().getPersistentDataContainer().get(plugin.itemKey(), PersistentDataType.STRING);
        if (itemId == null) return;
        CustomItem item = plugin.itemManager().get(itemId);
        if (item == null) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        if (plugin.cooldownManager().isOnCooldown(player.getUniqueId(), item.id())) {
            player.sendActionBar("§cAbility cooldown: §f" + plugin.cooldownManager().remainingSeconds(player.getUniqueId(), item.id()) + "s");
            return;
        }

        switch (item.id()) {
            case "forest_fang" -> lunge(player, item);
            case "brute_club" -> smash(player, item);
            case "frost_bow" -> frost(player, item);
            case "void_blade" -> voidStep(player, item);
            case "infernal_axe" -> inferno(player, item);
            case "ancient_core" -> shockwave(player, item);
        }
    }

    private void lunge(Player p, CustomItem item) {
        p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(0.35));
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation(), 25, .5, .3, .5, .1);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1.4f);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private void smash(Player p, CustomItem item) {
        for (Entity e : p.getNearbyEntities(4, 2, 4)) {
            if (e instanceof LivingEntity living && e != p) {
                living.damage(8, p);
                Vector knock = living.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.2).setY(.5);
                living.setVelocity(knock);
            }
        }
        p.getWorld().spawnParticle(Particle.BLOCK, p.getLocation(), 50, 2, .2, 2, org.bukkit.Material.STONE.createBlockData());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, .8f, .8f);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private void frost(Player p, CustomItem item) {
        LivingEntity target = getTarget(p, 12);
        if (target != null) {
            target.damage(5, p);
            target.setFreezeTicks(Math.max(target.getFreezeTicks(), 120));
        }
        p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getEyeLocation(), 35, .5, .5, .5, .05);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private void voidStep(Player p, CustomItem item) {
        Vector direction = p.getLocation().getDirection().normalize();
        p.teleport(p.getLocation().add(direction.multiply(8)));
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 60, .5, 1, .5, .3);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.2f);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private void inferno(Player p, CustomItem item) {
        for (Entity e : p.getNearbyEntities(5, 3, 5)) {
            if (e instanceof LivingEntity living && e != p) {
                living.damage(10, p);
                living.setFireTicks(100);
            }
        }
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 100, 4, 1, 4, .05);
        p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, .8f);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private void shockwave(Player p, CustomItem item) {
        for (Entity e : p.getNearbyEntities(7, 4, 7)) {
            if (e instanceof LivingEntity living && e != p) {
                living.damage(18, p);
                Vector knock = living.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.5).setY(.8);
                living.setVelocity(knock);
            }
        }
        p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 150, 6, 1, 6, .15);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, .7f);
        plugin.cooldownManager().start(p.getUniqueId(), item.id(), item.cooldownSeconds());
    }

    private LivingEntity getTarget(Player player, double range) {
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity living && entity != player && player.hasLineOfSight(living)) return living;
        }
        return null;
    }
}
