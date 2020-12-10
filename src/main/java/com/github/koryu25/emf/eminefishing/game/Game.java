package com.github.koryu25.emf.eminefishing.game;

import com.github.koryu25.emf.eminefishing.guiitem.Items;
import com.github.koryu25.emf.eminefishing.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Game {

    //InstanceField
    private Plugin plugin;
    private Player player;
    private Location before;
    private Timer timer;

    //Constructor
    public Game(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.before = player.getLocation();
        this.timer = new Timer(Main.config.time, player, before);

        Main.gaming.put(player, this);

        this.start();
    }

    private void start() {
        //ゲーム開始報告
        player.sendMessage("[EMF] §2EMine§7-§3Fishing§f スタート！");
        //つりざお配布
        player.getInventory().addItem(Items.getFishigGameRod());
        //指定座標にテレポート
        player.teleport(Main.config.gamingLocation);
        //タイマー起動
        timer.runTaskTimer(plugin, 0, 20);
    }

    public void stop() {
        timer.stop();
    }
}
