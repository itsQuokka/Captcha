package com.captcha.commands;

import com.captcha.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CaptchaTimeCmd implements CommandExecutor {

    private Plugin plugin;

    CaptchaTimeCmd(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().log(Level.INFO, "You are not allowed to be using that command!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("captcha.settimeout")) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fYou are not allowed to use the captcha command!"));
            return false;
        }

        try {
            final int newTime = Integer.parseInt(args[1]);

            if (newTime > 60) {
                player.sendMessage(colorize("&7[&cCaptcha&7] &fYou must specify a time lower than 60 seconds!"));
                return false;
            }

            plugin.getConfig().set("captcha-delay", newTime);
            plugin.saveConfig();

            player.sendMessage(colorize("&7[&cCaptcha&7] &fYou have set the captcha time to &c" + newTime + "&f seconds!"));
        } catch (NumberFormatException e) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fYou must specify the number of seconds to change it too!"));
            return false;
        }
        return false;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
