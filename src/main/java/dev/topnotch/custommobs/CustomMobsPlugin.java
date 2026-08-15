package dev.topnotch.custommobs;

import dev.topnotch.custommobs.managers.CooldownManager;
import dev.topnotch.custommobs.managers.ItemManager;
import dev.topnotch.custommobs.managers.MobManager;
import dev.topnotch.custommobs.managers.SpawnManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomMobsPlugin extends JavaPlugin {
    private MobManager mobManager;
    private ItemManager itemManager;
    private CooldownManager cooldownManager;
    private SpawnManager spawnManager;
    private NamespacedKey mobKey;
    private NamespacedKey rarityKey;

    @Override
    public void onEnable() {
        mobKey = new NamespacedKey(this, "custom_mob");
        rarityKey = new NamespacedKey(this, "rarity");
        mobManager = new MobManager();
        itemManager = new ItemManager();
        cooldownManager = new CooldownManager();
        spawnManager = new SpawnManager(this, mobManager);
        spawnManager.start();
        getLogger().info("CustomMobs enabled - daily mob spawning started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomMobs disabled.");
    }

    public MobManager mobManager() { return mobManager; }
    public ItemManager itemManager() { return itemManager; }
    public CooldownManager cooldownManager() { return cooldownManager; }
    public SpawnManager spawnManager() { return spawnManager; }
    public NamespacedKey mobKey() { return mobKey; }
    public NamespacedKey rarityKey() { return rarityKey; }
}
