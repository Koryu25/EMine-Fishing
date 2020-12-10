package com.github.koryu25.emf.eminefishing;

import com.github.koryu25.emf.eminefishing.game.Game;
import com.github.koryu25.emf.eminefishing.guiitem.TradeMenu;
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
			sender.sendMessage("§3/emf start §f→ §2ミニゲームを開始");
			sender.sendMessage("§3/emf stats §f→ §2現在の獲得ポイント表示");
			sender.sendMessage("§3/emf shop §f→ §2ポイント交換のためのショップを開く");
			sender.sendMessage("§3/emf stop §f→ §2ミニゲームを終了");
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
				new Game(plugin, player);
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
			//stop
			else if (args[0].equalsIgnoreCase("stop")) {
				if (!player.hasPermission("emf.commands.stop")) {
					sender.sendMessage("[EMF] You don't have permission.");
					return true;
				}
				if (args.length != 1) {
					return false;
				}
				Main.gaming.get(player).stop();
				return true;
			}
		}
		return false;
	}
}
