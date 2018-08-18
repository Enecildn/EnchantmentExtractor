package com.enecildn.enchantmentextractor;

import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentExtractor extends JavaPlugin
{
	public void onEnable()
	{
		getCommand("ee").setExecutor(new CommandManager());
		getServer().getPluginManager().registerEvents(new InventoryMenu(), this);
	}
}
