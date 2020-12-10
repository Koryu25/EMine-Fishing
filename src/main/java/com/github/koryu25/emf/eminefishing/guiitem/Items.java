package com.github.koryu25.emf.eminefishing.guiitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.koryu25.emf.eminefishing.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

	public static ItemStack createItemStack(Material material, String name, String... lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
        return item;
	}
	//*Fishing Game Rod*
	public static ItemStack getFishigGameRod() {
		ItemStack itemstack = createItemStack(Material.FISHING_ROD, "*Fishing Game Rod*");
		//入れ食い3
		itemstack.addEnchantment(Enchantment.LURE, 3);
		//耐久力3
		itemstack.addEnchantment(Enchantment.DURABILITY, 3);
		//不可壊
		ItemMeta meta = itemstack.getItemMeta();
		meta.setUnbreakable(true);
		itemstack.setItemMeta(meta);
		return itemstack;
	}
	//交換アイテム
	public static ItemStack getGUITradeItem(int index) {
		ItemStack itemstack = new ItemStack(Main.config.tradematerials.get(index));
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName(Main.config.tradenames.get(index));
		List<String> lore = new ArrayList<String>();
		lore.add("§a必要ポイント: "+ Main.config.needpoints.get(index));
		if (Main.config.lores.containsKey(index)) {
			lore.addAll(Arrays.asList(Main.config.lores.get(index)));
		}
		meta.setLore(lore);
		itemstack.setItemMeta(meta);
		if (Main.config.enchants.containsKey(index)) {
			itemstack.addEnchantments(Main.config.enchants.get(index));
		}
		return itemstack;
	}
	public static ItemStack getTradeItem(int index) {
		ItemStack itemstack = new ItemStack(Main.config.tradematerials.get(index));
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName(Main.config.tradenames.get(index));
		if (Main.config.lores.containsKey(index)) {
			meta.setLore(Arrays.asList(Main.config.lores.get(index)));
			itemstack.setItemMeta(meta);
		}
		if (Main.config.enchants.containsKey(index)) {
			itemstack.addEnchantments(Main.config.enchants.get(index));
		}
		return itemstack;
	}
}
