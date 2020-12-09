package com.github.koryu25.emf.eminefishing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Main extends JavaPlugin {

    //ClassField
    public static Config config;
    public static DBManager dbm;
    public static Map<Player, Timer> timerMap = new HashMap<>();

    @Override
    public void onEnable() {

        //コンフィグ読み込み
        config = new Config(this);

        //コマンドマネージャー呼び出し
        getCommand("emf").setExecutor(new CommandManager(this));

        //イベントリスナー呼び出し
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        //データベース接続
        dbm = new DBManager(config.host, config.database, config.username, config.password, config.port, config.tablename);
        if (!dbm.connectionTest()) getLogger().severe("[EMF] データベースへの接続テストに失敗しました。");
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().remove(Items.getFishigGameRod()));
    }
}
