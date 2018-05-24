package com.xuchaoji.craft.nofword;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoFWord extends JavaPlugin implements CommandExecutor, Listener {
	// for custom config file
	FileConfiguration config = getConfig();

	@Override
	public void onEnable() {
		// config.addDefault("ServerName", "233craft");
		// String holdItem = config.getString("ServerName");
		List<String> banWord = new ArrayList<String>();
		banWord.add("fk");
		banWord.add("fuck");
		config.addDefault("PASSWORD", "language");
		config.addDefault("ReplaceChatMessage", "******");
		config.addDefault("BanWord", banWord);
		config.options().copyDefaults(true);
		saveConfig();
		this.getCommand("nfw").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		System.out.println(ChatColor.GREEN + "[NFW]enabled!");
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(
					ChatColor.DARK_GREEN + "[nfw]use \"/nfw PASSWOED banWord1 banWord2 ...\" to add a ban word."
							+ ChatColor.RED + "\n PASSWORD is set in config file.");
			reloadConfig();
		} else if (!args[0].equals(getConfig().getString("PASSWORD"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[nfw]Wrong PassWord");
		} else if (args.length == 1) {
			sender.sendMessage(ChatColor.AQUA + "[nfw]what's the ban word?");
		} else {
			for (int i = 1; i < args.length; i++) {
				System.out.println("Get ban word: " + args[i]);
				List<String> l = getConfig().getStringList("BanWord"); // 此处用config变量也会出现奇怪的BUG
				if (l.contains(args[i])) {
					sender.sendMessage("[nfw]" + args[1] + "already in ban list!");
				} else {
					l.add((String) args[i]);
					System.out.println("[nfw]" + args[1] + " adding to ban list!");
					getConfig().set("BanWord", l); // 此处若使用 config变量，只能生效一次，使用getConfig()方法可重新读取保存后的ban list
					System.out.println(args[1] + " added!");
					saveConfig();
					System.out.println("[nfw]save to config file.");
					reloadConfig();
					System.out.println("[nfw]config file reloaded!");
					sender.sendMessage(ChatColor.RED + args[i] + ChatColor.GREEN + " added to ban list!");
				}
			}
		}
		return true;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String originMessage = e.getMessage();
		List<String> banlist = getConfig().getStringList("BanWord");//
		for (String l : banlist) {
			if (originMessage.contains(l)) {
				e.setMessage(ChatColor.DARK_RED + getConfig().getString("ReplaceChatMessage"));
			}
		}

	}

}