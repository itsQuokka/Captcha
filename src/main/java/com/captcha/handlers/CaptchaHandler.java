package com.captcha.handlers;

import com.captcha.Plugin;
import com.captcha.objs.CaptchaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CaptchaHandler {

    private Plugin plugin;
    private List<CaptchaPlayer> captchaPlayers;

    public CaptchaHandler(Plugin plugin) {
        this.plugin = plugin;

        this.captchaPlayers = new ArrayList<>();
    }

    /**
     * Send the captcha to a player.
     *
     * @param sender The player who has issued the captcha commmand.
     * @param target The player who is being captcha'd.
     */
    public void sendCaptcha(Player sender, Player target) {
        captchaPlayers.add(new CaptchaPlayer(plugin, sender, target, this.getItem(null)));
    }

    /**
     * @param filter The itemstack to only populate the inventory once with.
     * @return the inventory to send to player
     */
    public Inventory getCaptchaInventory(ItemStack filter) {

        Inventory inventory = Bukkit.createInventory(null, 36, colorize("&fClick the &a&l" + filter.getType().toString().toUpperCase() + "!"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, getItem(filter));
        }

        int randomSlot = new Random().nextInt(inventory.getSize());
        inventory.setItem(randomSlot, filter);

        return inventory;
    }

    /**
     * Recursion method to fill inventory.
     *
     * @param filter The itemstack to only populate the inventory once with.
     * @return ItemStack.
     */
    private ItemStack getItem(ItemStack filter) {
        ItemStack[] itemStacks = new ItemStack[6];
        itemStacks[0] = new ItemStack(Material.REDSTONE_ORE, 1);
        itemStacks[1] = new ItemStack(Material.LAPIS_ORE, 1);
        itemStacks[2] = new ItemStack(Material.IRON_ORE, 1);
        itemStacks[3] = new ItemStack(Material.GOLD_ORE, 1);
        itemStacks[4] = new ItemStack(Material.DIAMOND_ORE, 1);
        itemStacks[5] = new ItemStack(Material.COAL_ORE, 1);

        if (filter == null) {
            return itemStacks[new Random().nextInt(itemStacks.length)];
        }

        ItemStack itemStack = itemStacks[new Random().nextInt(itemStacks.length)];
        if (!itemStack.getType().equals(filter.getType())) {
            return itemStack;
        }

        return getItem(filter);
    }

    /**
     * If the player exists, remove them from captcha list.
     * @param player the player to remove
     */
    public void remove(Player player) {
        Optional<CaptchaPlayer> oPlayer = captchaPlayers.stream().filter(c -> c.getUUID().equals(player.getUniqueId())).findFirst();
        oPlayer.ifPresent(captchaPlayer -> captchaPlayers.remove(captchaPlayer));
    }

    /**
     *
     * @param player the player we are searching for.
     * @return if the captcha'd players list contains the user.
     */
    public boolean has(Player player) {
        return captchaPlayers.stream().anyMatch(c -> c.getUUID().equals(player.getUniqueId()));
    }

    /**
     * @return The current list of players being captcha'd.
     */
    public List<CaptchaPlayer> getCaptchaPlayers() {
        return captchaPlayers;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
