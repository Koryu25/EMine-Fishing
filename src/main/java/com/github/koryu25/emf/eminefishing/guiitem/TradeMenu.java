package com.github.koryu25.emf.eminefishing.guiitem;

import com.github.koryu25.emf.eminefishing.Main;
import com.github.koryu25.emf.eminefishing.guiitem.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TradeMenu {

	public static final int slotnum = 27;

	public TradeMenu(Player player, int page) {
		int itemnum = Main.config.tradenames.size();
		int maxpage = Main.config.tradenames.size() / (slotnum - 2);
		if (itemnum % (slotnum - 2) != 0) {
			maxpage++;
		}
		Inventory inv = Bukkit.createInventory(null, slotnum, "[EMF]ポイント交換画面 "+page+"/"+maxpage);
		int num = slotnum - 2;
		if (itemnum < slotnum - 2) {
			num = itemnum;
		} else if (itemnum <= page * (slotnum - 2)) {
			num = itemnum - (page - 1) * (slotnum - 2);
		}
		for (int i = 0; i < num; i++) {
			inv.setItem(i, Items.getGUITradeItem(i + (page - 1) * (slotnum - 2)));
		}
		if (page == 1) {
			inv.setItem(slotnum - 2, Items.createItemStack(Material.WOODEN_PICKAXE, "§m前のページ"));
		} else {
			inv.setItem(slotnum - 2, Items.createItemStack(Material.STONE_PICKAXE, "前のページ"));
		}
		if (page == maxpage) {
			inv.setItem(slotnum - 1, Items.createItemStack(Material.WOODEN_PICKAXE, "§m次のページ"));
		} else {
			inv.setItem(slotnum - 1, Items.createItemStack(Material.IRON_PICKAXE, "次のページ"));
		}
		player.openInventory(inv);
	}
}
