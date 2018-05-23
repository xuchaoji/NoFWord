package com.xuchaoji.craft.nofword;

import java.util.List;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class NoFWord extends JavaPlugin implements CommandExecutor, Listener {
	//for custom config file
	FileConfiguration config = getConfig();
	
	@Override
	public void onEnable() {
		//config.addDefault("ServerName", "233craft");
		//String holdItem = config.getString("ServerName");
		List<String> banWord = new ArrayList<String>();
		banWord.add("fk");
		banWord.add("fuck");
		config.addDefault("PASSWORD", "language");
		config.addDefault("BanWord", banWord);
		config.options().copyDefaults(true);
		saveConfig();
		this.getCommand("nfw").setExecutor(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN+"use \"/nfw PASSWOED banWords\" to add a ban word, password is set in config file.");
			}else if(!args[0].equals(config.getString("PASSWORD"))) {
				sender.sendMessage(ChatColor.DARK_RED+"Wrong PassWord");
			}else {
				List<String> l = config.getStringList("BanWord");
				l.add(args[1]);
				config.addDefault("BanWord", l);
				config.options().copyDefaults(true);
				saveConfig();
				reloadConfig();
			}
		}
		
		return true;
	}
	
	
	 
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String originMessage = e.getMessage();
		List<String> banlist = config.getStringList("BanWord");
		for(String l:banlist) {
			if(originMessage.contains(l)) {
				e.setMessage(ChatColor.DARK_RED+"[这个傻D想骂人，被屏蔽了]*********");
			}
		}
		
	}

}
