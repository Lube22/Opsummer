package com.opsummer.armor;

import org.bukkit.plugin.java.JavaPlugin;

public class OPSummerArmor extends JavaPlugin {

    private static OPSummerArmor instance;
    private ArmorManager armorManager;

    @Override
    public void onEnable() {
        instance = this;
        armorManager = new ArmorManager(this);

        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);

        GiveCommand cmd = new GiveCommand(this);
        getCommand("givesummer").setExecutor(cmd);
        getCommand("givesummer").setTabCompleter(cmd);

        getLogger().info("OPSummerArmor enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("OPSummerArmor disabled.");
    }

    public static OPSummerArmor getInstance() { return instance; }
    public ArmorManager getArmorManager()      { return armorManager; }
}
