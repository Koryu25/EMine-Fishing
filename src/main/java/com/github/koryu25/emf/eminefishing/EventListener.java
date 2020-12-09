package com.github.koryu25.emf.eminefishing;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

	@EventHandler
	public void pje(PlayerJoinEvent e) {
		if (Main.dbm.confirmPlayer(e.getPlayer())) {
			Main.dbm.newPlayer(e.getPlayer());
			System.out.println("[EMF] 新規プレイヤー["+e.getPlayer().getName()+"]を確認。MySQLにレコードを追加しました。");
		}
	}

	@EventHandler
	public void pqe(PlayerQuitEvent e) {
		Main.gaming.remove(e.getPlayer());
		e.getPlayer().getInventory().remove(Items.getFishigGameRod());
	}

	@EventHandler
	public void pdie(PlayerDropItemEvent e) {
		if (Main.gaming.contains(e.getPlayer())) {
			if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("*Fishing Game Rod*")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void pfe(PlayerFishEvent e) {
		//イベントの状態確認
		if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
			//プレイヤーがゲーム中か確認
			if (Main.gaming.contains(e.getPlayer())) {
				if (!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("*Fishing Game Rod*")) {
					return;
				}
				//0から100までの乱数生成
				double random = new Random().nextDouble() * 100;
				double probability = 0;
				int index = 0;
				while (random > probability) {
					probability += Main.config.probabilities.get(index);
					index++;
				}
				String pname = e.getPlayer().getName();
				String item = Main.config.resultnames.get(index);
				int point = Main.config.points.get(index);
				e.getPlayer().sendMessage("[EMF] "+pname+"は §a"+item+"§f を手に入れた！ §e+"+point+"ポイント");
				Main.dbm.setPoint(e.getPlayer(), Main.dbm.getPoint(e.getPlayer()) + point);
				//漁獲不可化
				e.setCancelled(true);
				e.getHook().remove();
				e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
			}
		}
	}

	@EventHandler
	public void ice(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null) {
			int maxpage = Main.config.tradenames.size() / (TradeMenu.slotnum - 2);
			if (Main.config.tradenames.size() % (TradeMenu.slotnum - 2) != 0) {
				maxpage++;
			}
			for (int i = 1; i <= maxpage; i++) {
				if (e.getView().getTitle().equals("[EMF]ポイント交換画面 "+i+"/"+maxpage)) {
					e.setCancelled(true);
					int index = e.getSlot()+(TradeMenu.slotnum-2)*(i-1);
					//ページ変更
					if (e.getSlot() == 25) {
						if (e.getCurrentItem().getType() != Material.STONE_PICKAXE) {
							return;
						}
						new TradeMenu(player, i - 1);
					} else if (e.getSlot() == 26) {
						if (e.getCurrentItem().getType() != Material.IRON_PICKAXE) {
							return;
						}
						new TradeMenu(player, i + 1);
					} else {
						//所有ポイント確認
						int needpoint = Main.config.needpoints.get(index);
						if (Main.dbm.getPoint(player) >= needpoint) {
							Main.dbm.setPoint(player, Main.dbm.getPoint(player) - needpoint);
							player.getInventory().addItem(Items.getTradeItem(index));
							player.sendMessage("[EMF] "+needpoint+"ポイントを消費して"+Main.config.tradenames.get(index)+"を入手しました！");
						} else {
							player.sendMessage("[EMF] ポイントが足りません。");
						}
					}
				}
			}
		}
	}
}
