package dev.topnotch.custommobs.managers;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.mobs.CustomMob;
import dev.topnotch.custommobs.mobs.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnManager {
    private static final long TICKS_PER_MINECRAFT_DAY = 24000L;
    private final CustomMobsPlugin plugin;
    private final MobManager mobManager;
    private long lastSpawnDay = -1;

    public SpawnManager(CustomMobsPlugin plugin, MobManager mobManager) {
        this.plugin = plugin;
        this.mobManager = mobManager;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkDailySpawn();
            }
        }.runTaskTimer(plugin, 100L, 100L);
    }

    private void checkDailySpawn() {
        for (World world : plugin.getServer().getWorlds()) {
            long day = world.getFullTime() / TICKS_PER_MINECRAFT_DAY;
            if (day > 0 && day != lastSpawnDay) {
                lastSpawnDay = day;
                spawnForRandomPlayer();
                return;
            }
        }
    }

    public void spawnForRandomPlayer() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        if (players.isEmpty()) return;

        Player target = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        CustomMob template = mobManager.randomMob();
        if (template == null) return;

        Rarity rarity = mobManager.randomRarity();
        Location location = findSpawnLocation(target);
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, template.baseType());

        String displayName = rarity.displayName() + " " + template.name();
        entity.customName(Component.text(displayName, rarity.color()).decoration(TextDecoration.BOLD, true));
        entity.setCustomNameVisible(true);
        entity.getPersistentDataContainer().set(plugin.mobKey(), PersistentDataType.STRING, template.id());
        entity.getPersistentDataContainer().set(plugin.rarityKey(), PersistentDataType.STRING, rarity.name());

        if (entity.getAttribute(Attribute.MAX_HEALTH) != null) {
            double health = template.health() * rarity.powerMultiplier();
            entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
            entity.setHealth(Math.min(health, entity.getAttribute(Attribute.MAX_HEALTH).getValue()));
        }
        if (entity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
            entity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(template.damage() * rarity.powerMultiplier());
        }

        plugin.getServer().broadcast(Component.text("⚔ " + displayName + " has appeared!", rarity.color()));
    }

    private Location findSpawnLocation(Player player) {
        Location base = player.getLocation().clone();
        for (int i = 0; i < 20; i++) {
            double angle = ThreadLocalRandom.current().nextDouble(Math.PI * 2);
            double distance = ThreadLocalRandom.current().nextDouble(15, 35);
            Location candidate = base.clone().add(Math.cos(angle) * distance, 0, Math.sin(angle) * distance);
            int highest = candidate.getWorld().getHighestBlockYAt(candidate);
            candidate.setY(highest + 1);
            if (candidate.getBlock().isPassable()) return candidate;
        }
        return base.add(0, 1, 0);
    }
}
