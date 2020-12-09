package com.github.koryu25.emf.eminefishing;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandManager implements CommandExecutor {

	private Plugin plugin;

	//コンストラクタ
	public CommandManager(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//ヘルプ用
		if (args.length == 0 || args.length > 1) {
			//ヘルプ表示
			sender.sendMessage("§7====== §2EMine§7-§3Fishing §f[Command help] §7======");
			sender.sendMessage("§3/emf start §f→ §2釣りを開始");
			sender.sendMessage("§3/emf stats §f→ §2現在の獲得ポイント表示");
			sender.sendMessage("§3/emf shop §f→ §2ポイント交換のためのショップを開く");
			sender.sendMessage("§eすべてプレイヤー用コマンドです。コンソールからは入力できません。");
			sender.sendMessage("§7======================================");
			return true;
		}
		//sender確認
		if (sender instanceof Player) {
			Player player = (Player) sender;
			//start
			if (args[0].equalsIgnoreCase("start")) {
				if (!player.hasPermission("emf.commands.start")) {
					player.sendMessage("[EMF] You don't have permission.");
					return true;
				}
				if (args.length != 1) {
					return false;
				}
				player.sendMessage("[EMF] §2EMine§7-§3Fishing§f スタート！");
				Main.gaming.add(player);
				player.getInventory().addItem(Items.getFishigGameRod());
				//現在座標を保存
				Location before = player.getLocation();
				//指定座標にテレポート
				player.teleport(Main.config.gamingLocation)
				new Timer(Main.config.time, player, before).runTaskTimer(plugin, 0, 20);//0tick待った後20tickごとにtime回繰り返す
				return true;
			}
			//stats
			else if (args[0].equalsIgnoreCase("stats")) {
				if (!player.hasPermission("emf.commands.stats")) {
					sender.sendMessage("[EMF] You don't have permission.");
					return true;
				}
				if (args.length != 1) {
					return false;
				}
				player.sendMessage("[EMF] §a現在の獲得済ポイント: §f"+Main.dbm.getPoint(player));
				return true;
			}
			//shop
			else if (args[0].equalsIgnoreCase("shop")) {
				if (!player.hasPermission("emf.commands.shop")) {
					sender.sendMessage("[EMF] You don't have permission.");
					return true;
				}
				if (args.length != 1) {
					return false;
				}
				new TradeMenu(player, 1);
				return true;
			}
		}
		return false;
	}
}
