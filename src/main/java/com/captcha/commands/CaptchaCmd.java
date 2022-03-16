package com.captcha.commands;

import com.captcha.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CaptchaCmd implements CommandExecutor {

    private Plugin plugin;

    public CaptchaCmd(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            plugin.getLogger().log(Level.INFO, "[!] You are not allowed to captcha the user from here!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("captcha.push")) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fYou are not allowed to use the captcha command!"));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fYou must specify a player by their name!"));
            return false;
        }

        if (args[0].equalsIgnoreCase("settimeout")) {
            new CaptchaTimeCmd(plugin).onCommand(sender, command, label, args);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fThere seemed to be an issue finding that player!"));
            return false;
        }

        if (!target.isOnline()) {
            player.sendMessage(colorize("&7[&cCaptcha&7] &fThe player must be online to send a captcha!"));
            return false;
        }

//        if (target.hasPermission("captcha.*") || target.hasPermission("captcha.bypass")) {
//            player.sendMessage(colorize("&7[&cCaptcha&7] &fSorry, you are not allowed to captcha that user!"));
//            return false;
//        }

        plugin.getCaptchaHandler().sendCaptcha(player, target);
        player.sendMessage(colorize("&7[&cCaptcha&7] &fYou have sent a captcha request to &c" + target.getName() + "&f!"));
        player.sendMessage(colorize("&7[&cCaptcha&7] &fPlease wait &c" + plugin.getConfig().getInt("captcha-delay") + "&f seconds for the captcha result!"));

        return true;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
