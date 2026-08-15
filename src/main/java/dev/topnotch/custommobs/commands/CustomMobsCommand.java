package dev.topnotch.custommobs.commands;

import dev.topnotch.custommobs.CustomMobsPlugin;
import dev.topnotch.custommobs.mobs.CustomMob;
import dev.topnotch.custommobs.mobs.Rarity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomMobsCommand implements CommandExecutor {
    private final CustomMobsPlugin plugin;
    public CustomMobsCommand(CustomMobsPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("custommobs.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§6CustomMobs §7commands:");
            sender.sendMessage("§e/custommobs spawn [mob] [rarity]");
            sender.sendMessage("§e/custommobs list");
            sender.sendMessage("§e/custommobs reload");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "spawn" -> {
                if (!(sender instanceof Player player)) { sender.sendMessage("§cThis command requires a player."); return true; }
                CustomMob mob = args.length >= 2 ? plugin.mobManager().get(args[1].toLowerCase()) : plugin.mobManager().randomMob();
                if (mob == null) { sender.sendMessage("§cUnknown mob."); return true; }
                Rarity rarity = args.length >= 3 ? parseRarity(args[2]) : plugin.mobManager().randomRarity();
                if (rarity == null) { sender.sendMessage("§cUnknown rarity."); return true; }
                plugin.spawnManager().spawn(player.getLocation(), mob, rarity);
                sender.sendMessage("§aSpawned §f" + rarity.displayName() + " " + mob.name() + "§a.");
            }
            case "list" -> {
                sender.sendMessage("§6Custom Mobs:");
                for (CustomMob mob : plugin.mobManager().all()) sender.sendMessage("§7- §f" + mob.id() + " §8(" + mob.rarity().displayName() + ")");
            }
            case "reload" -> {
                plugin.reloadConfig();
                sender.sendMessage("§aCustomMobs configuration reloaded.");
            }
            default -> sender.sendMessage("§cUnknown subcommand.");
        }
        return true;
    }

    private Rarity parseRarity(String value) {
        try { return Rarity.valueOf(value.toUpperCase()); }
        catch (IllegalArgumentException ignored) { return null; }
    }
}
