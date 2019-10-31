package net.tererun.plugin.prone.prone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class Prone extends JavaPlugin implements Listener, CommandExecutor {

    static FileConfiguration config;
    static Map<Player, Integer> enable = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        getCommand("prone").setExecutor(this);
        config = getConfig();

        for (Player player : Bukkit.getOnlinePlayers()) {
            enable.put(player, 0);
        }
    }

    @EventHandler
    public void onElytraDetect(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            if (enable.get(e.getEntity()) == 1) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        enable.put(e.getPlayer(), 0);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("prone")) {
            if (sender.hasPermission("prone")) {
                Player player = (Player)sender;
                if (enable.get(player) == 0) {
                    enable.put(player, 1);
                    sender.sendMessage(ChatColor.AQUA + "匍匐を有効にしました");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2));
                    player.setGliding(true);
                    return true;
                } else {
                    enable.put(player, 0);
                    sender.sendMessage(ChatColor.AQUA + "匍匐を無効にしました");
                    player.removePotionEffect(PotionEffectType.SLOW);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "パーミッションを持っていません！");
            }
        }
        return false;
    }

}