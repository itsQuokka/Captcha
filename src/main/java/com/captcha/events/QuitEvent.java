package com.captcha.events;

import com.captcha.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    private Plugin plugin;

    public QuitEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getCaptchaHandler().remove(event.getPlayer());
    }
}
