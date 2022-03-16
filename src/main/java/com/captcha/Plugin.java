package com.captcha;

import com.captcha.commands.CaptchaCmd;
import com.captcha.events.ClickEvent;
import com.captcha.events.QuitEvent;
import com.captcha.handlers.CaptchaHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private CaptchaHandler captchaHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        captchaHandler = new CaptchaHandler(this);

        getCommand("captcha").setExecutor(new CaptchaCmd(this));

        getServer().getPluginManager().registerEvents(new ClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

    /**
     * @return the handler the manages all currently captcha'd users.
     */
    public CaptchaHandler getCaptchaHandler() {
        return captchaHandler;
    }
}
