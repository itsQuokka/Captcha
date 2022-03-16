package com.captcha.events;

import com.captcha.Plugin;
import com.captcha.objs.CaptchaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class ClickEvent implements Listener {

    private Plugin plugin;

    public ClickEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        Optional<CaptchaPlayer> oPlayer = plugin.getCaptchaHandler().getCaptchaPlayers().stream().filter(c -> c.getUUID().equals(player.getUniqueId())).findFirst();
        if (!oPlayer.isPresent()) return;

        event.setCancelled(true);

        Material clicked = event.getCurrentItem().getType();
        Material comparator = oPlayer.get().getFilterItem().getType();

        if (clicked.equals(comparator)) {
            player.closeInventory();

            Bukkit.getScheduler().cancelTask(oPlayer.get().getRunnableID());
            plugin.getCaptchaHandler().remove(player);

            oPlayer.get().getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cCaptcha&7] &f" + player.getName() + " has successfully verified their captcha!"));
        }
    }
}
