package dev.topnotch.custommobs;

import dev.topnotch.custommobs.commands.CustomMobsCommand;
import dev.topnotch.custommobs.listeners.ItemAbilityListener;
import dev.topnotch.custommobs.listeners.ItemPassiveListener;
import dev.topnotch.custommobs.listeners.MobDeathListener;
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
    private NamespacedKey itemKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        mobKey = new NamespacedKey(this, "custom_mob");
        rarityKey = new NamespacedKey(this, "rarity");
        itemKey = new NamespacedKey(this, "custom_item");
        mobManager = new MobManager();
        itemManager = new ItemManager();
        itemManager.setPlugin(this);
        cooldownManager = new CooldownManager();
        spawnManager = new SpawnManager(this, mobManager);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemAbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemPassiveListener(this), this);
        if (getCommand("custommobs") != null) getCommand("custommobs").setExecutor(new CustomMobsCommand(this));
        spawnManager.start();
        getLogger().info("CustomMobs enabled - daily mob spawning started.");
    }

    @Override
    public void onDisable() { getLogger().info("CustomMobs disabled."); }
    public MobManager mobManager() { return mobManager; }
    public ItemManager itemManager() { return itemManager; }
    public CooldownManager cooldownManager() { return cooldownManager; }
    public SpawnManager spawnManager() { return spawnManager; }
    public NamespacedKey mobKey() { return mobKey; }
    public NamespacedKey rarityKey() { return rarityKey; }
    public NamespacedKey itemKey() { return itemKey; }
}
