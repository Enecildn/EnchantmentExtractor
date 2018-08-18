package com.enecildn.enchantmentextractor;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryMenu implements Listener
{
	private static final String TITLE = ChatColor.GREEN + "EnchantmentExtractor";
	private static HashMap<Player, ItemStack> SelectedItem = new HashMap<Player, ItemStack>();
	
	public static void openInventory(Player player, ItemStack item)
	{
		SelectedItem.put(player, item);
		player.getInventory().removeItem(item);
		Inventory inventory = Bukkit.createInventory(player, 9, TITLE);
		inventory.setItem(8, item);
		int slot = 0;
		Map<Enchantment, Integer> enchantments = item.getEnchantments();
		for (Enchantment enchantment : enchantments.keySet())
		{
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta(); 
			meta.addStoredEnchant(enchantment, enchantments.get(enchantment), true);
			book.setItemMeta(meta);
			inventory.setItem(slot, book);
			slot++;
		}
		for (int i = 0; i < 9; i++)
		{
			if (inventory.getItem(i) == null)
			{
				ItemStack bedrock = new ItemStack(Material.BEDROCK, 1);
				ItemMeta meta = bedrock.getItemMeta();
				meta.setDisplayName(ChatColor.AQUA + "취소");
				bedrock.setItemMeta(meta);
				inventory.setItem(i, bedrock);
			}
		}
		player.openInventory(inventory);
	}
	
	@EventHandler
	public static void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if (event.getClickedInventory() == null)
		{
			return;
		}
		else if (event.getClickedInventory().getName() == TITLE)
		{
			if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK)
			{
				if (player.getLevel() >= 20)
				{
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) event.getCurrentItem().getItemMeta();
					for (Enchantment enchantment : meta.getStoredEnchants().keySet())
					{
						SelectedItem.get(player).removeEnchantment(enchantment);
						ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
						EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) book.getItemMeta();
						meta2.addStoredEnchant(enchantment, meta.getStoredEnchants().get(enchantment), true);
						book.setItemMeta(meta2);
						player.getInventory().addItem(book);
					}
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
					player.setLevel(((Player) event.getWhoClicked()).getLevel() - 20);
				}
				else
				{
					player.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "EnchantExtractor" + ChatColor.WHITE + "] " +
							   						  ChatColor.AQUA + "You need 20 levels to extract an enchantment.");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
				}
				
			}
			else if (event.getCurrentItem().getType() == Material.BEDROCK)
			{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
			}
			else
			{
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
			}
			event.setCancelled(true);
			player.getInventory().addItem(SelectedItem.get(player));
			SelectedItem.remove(player);
			player.closeInventory();
			player.updateInventory();
		}
	}
	
	@EventHandler
	public static void onInventoryClose(InventoryCloseEvent event)
	{
		if (event.getInventory().getName() == TITLE && SelectedItem.containsKey(event.getPlayer()))
		{
			event.getPlayer().getInventory().addItem(SelectedItem.get(event.getPlayer()));
		}
	}
}
