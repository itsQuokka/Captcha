package com.captcha.objs;

import com.captcha.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CaptchaPlayer {

    private Plugin plugin;
    private Player sender;
    private UUID uuid;
    private ItemStack filterItem;
    private Integer runnableID;

    /**
     *
     * @param plugin Plugin instance.
     * @param sender The user who has issued the captcha command.
     * @param player The user who is being forced to enter a captcha.
     * @param filterItem The item that they must click.
     */
    public CaptchaPlayer(Plugin plugin, Player sender, Player player, ItemStack filterItem) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        this.filterItem = filterItem;
        this.sender = sender;

        startCaptcha(player);
    }

    /**
     * Start the schedulers to listen to player activity.
     * @param player the player to captcha.
     */
    private void startCaptcha(Player player) {
        this.runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            Inventory captchaInventory = plugin.getCaptchaHandler().getCaptchaInventory(filterItem);
            player.openInventory(captchaInventory);
            player.updateInventory();
        }, 0, 2 * 20);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getCaptchaHandler().has(player)) {
                if (plugin.getConfig().getBoolean("auto-kick")) {
                    if (player.isOnline()) {
                        player.kickPlayer(colorize("&cYou've failed to enter the captcha in time, you may login again!"));
                    }
                    return;
                }

                if (this.sender.isOnline()) {
                    this.sender.sendMessage(colorize("&7[&aCaptcha&7] &f" + player.getName() + " has failed to enter the captcha in time!"));
                }
            }
        }, plugin.getConfig().getInt("captcha-delay") * 20);
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemStack getFilterItem() {
        return filterItem;
    }

    public Integer getRunnableID() {
        return runnableID;
    }

    public Player getSender() {
        return sender;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
