package com.github.koryu25.emf.eminefishing;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

public class Config {

	private final Plugin plugin;
	private FileConfiguration config = null;

	//MySQL
	public String host;
	public String username;
	public String password;
	public String database;
	public int port;
	public String tablename;
	//制限時間
	public int time;
	//ゲーミングロケーション
	public Location gamingLocation;
	//釣果アイテム
	public ArrayList<String> resultnames = new ArrayList<String>();
	public ArrayList<Material> resultmaterials = new ArrayList<Material>();
	public ArrayList<Double> probabilities = new ArrayList<Double>();
	public ArrayList<Integer> points = new ArrayList<Integer>();
	//交換アイテム
	public ArrayList<String> tradenames = new ArrayList<String>();
	public ArrayList<Material> tradematerials = new ArrayList<Material>();
	public ArrayList<Integer> needpoints = new ArrayList<Integer>();
	public HashMap<Integer, String[]> lores = new HashMap<Integer, String[]>();
	public HashMap<Integer, HashMap<Enchantment, Integer>> enchants = new HashMap<Integer, HashMap<Enchantment, Integer>>();

	//コンストラクタ
	public Config (Plugin plugin) {
		this.plugin = plugin;
		this.load();
	}

	@SuppressWarnings("deprecation")
	private void load() {
		plugin.saveDefaultConfig();
		if (config != null) {
			plugin.reloadConfig();
		}
		config = plugin.getConfig();
		//MySQL
		this.host = config.getString("MySQL.host");
		this.username = config.getString("MySQL.username");
		this.password = config.getString("MySQL.password");
		this.database = config.getString("MySQL.database");
		this.port = config.getInt("MySQL.port");
		this.tablename = config.getString("MySQL.tablename");
		//制限時間
		this.time = config.getInt("Time", 600);
		//ゲーミングロケーション
		this.gamingLocation = new Location(
				Bukkit.getWorld(config.getString("GamingLocation.World")),
				config.getInt("GamingLocation.X"),
				config.getInt("GamingLocation.Y"),
				config.getInt("GamingLocation.Z")
		);
		//釣果アイテム
		for (String key : config.getConfigurationSection("result-items").getKeys(false)) {
			resultnames.add(key);
			resultmaterials.add(Material.getMaterial(config.getString("result-items."+key+".material-id")));
			probabilities.add(config.getDouble("result-items."+key+".probability"));
			points.add(config.getInt("result-items."+key+".point"));
		}
		//確率計算
		double d = 0;
		for (double p : probabilities) {
			d += p;
		}
		d = Math.round(d * 10000) / 10000;
		if (d != 100) {
			System.out.println("[EMF] configエラー：確率の合計が100になっていません！("+d+")");
		}
		//交換アイテム
		int index = 0;
		//ここから
		CustomConfig tradeitem = new CustomConfig(this.plugin, "tradeitem.yml");
		tradeitem.saveDefaultConfig();
		if (config != null) {
			tradeitem.reloadConfig();
		}
		config = tradeitem.getConfig();
		//ここまで
		for (String key : config.getConfigurationSection("trade-items").getKeys(false)) {
			tradenames.add(key);
			tradematerials.add(Material.getMaterial(config.getString("trade-items."+key+".material-id")));
			needpoints.add(config.getInt("trade-items."+key+".need-point"));
			//Lore
			if (config.contains("trade-items."+key+".lore")) {
				lores.put(index, config.getStringList("trade-items."+key+".lore").toArray(new String[config.getStringList("trade-items."+key+".lore").size() - 1]));
			}
			//Enchant
			if (config.contains("trade-items."+key+".enchant")) {
				HashMap<Enchantment, Integer> enchant = new HashMap<Enchantment, Integer>();
				for (String key2 : config.getConfigurationSection("trade-items."+key+".enchant").getKeys(false)) {
					enchant.put(Enchantment.getByName(key2), config.getInt("trade-items."+key+".enchant."+key2));
				}
				enchants.put(index, enchant);
			}
			index++;
		}
	}

	public void reload() {
		load();
	}
}
