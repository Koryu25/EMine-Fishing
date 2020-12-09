package com.github.koryu25.emf.eminefishing;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Timer extends BukkitRunnable {

	private int count;
	private Player player;
	private Location before;

	//コンストラクタ
	public Timer(int count, Player player, Location before) {
		this.count = count;
		this.player = player;
		this.before = before;
	}
	@Override
	public void run() {
		if (count > 0) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§e残り時間: §f"+count+"§e秒"));
		} else {
			cancel();
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§e終了！"));
			player.sendMessage("[EMF] §2EMine§7-§3Fishing§f 終了！");
			player.sendMessage("[EMF] §a現在の獲得済ポイント: §f"+Main.dbm.getPoint(player));
			player.getInventory().remove(Items.getFishigGameRod());
			//Mapから削除
			Main.timerMap.remove(this.player);
			//保存された(ゲーム以前の)座標にテレポート
			player.teleport(before);
		}
		count--;
	}

}
