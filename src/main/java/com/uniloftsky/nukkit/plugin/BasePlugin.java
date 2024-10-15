package com.uniloftsky.nukkit.plugin;

import cn.nukkit.plugin.PluginBase;

public class BasePlugin extends PluginBase {

    @Override
    public void onEnable() {
        this.getLogger().info("BasePlugin enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("BasePlugin disabled!");
    }
}
