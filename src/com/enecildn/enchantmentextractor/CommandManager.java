package com.enecildn.enchantmentextractor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] data)
	{
		if (label.equalsIgnoreCase("ee"))
		{
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				if (!player.getInventory().getItemInMainHand().getEnchantments().isEmpty())
				{
					InventoryMenu.openInventory(player, player.getInventory().getItemInMainHand());
				}
				else
				{
					player.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "EnchantExtractor" + ChatColor.WHITE + "] " +
									   ChatColor.AQUA + "Enchantments cannot be extracted from this item.");
				}
			}
			return true;
		}
		return false;
	}
}
