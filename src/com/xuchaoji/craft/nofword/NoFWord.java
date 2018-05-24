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
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//if(sender instanceof Player) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN+"use \"/nfw PASSWOED banWords\" to add a ban word, password is set in config file.");
			}else if(!args[0].equals(config.getString("PASSWORD"))) {
				sender.sendMessage(ChatColor.DARK_RED+"Wrong PassWord");
			}else if(args.length == 1){
				sender.sendMessage(ChatColor.AQUA+"what's the ban word?");
			}else {
				System.out.println("Get ban word: " + args[1]);
				List<String> l = getConfig().getStringList("BanWord"); //此处用config变量也会出现奇怪的BUG
				if(l.contains(args[1])) {
					sender.sendMessage(args[1]+ "already in ban list!");
				}else {
				l.add((String)args[1]);
				System.out.println(args[1]+ " adding to ban list!");
				getConfig().set("BanWord", l); //此处若使用 config变量，只能生效一次，使用getConfig()方法可重新读取保存后的ban list
				System.out.println(args[1]+ " added!");
				saveConfig();
				System.out.println("save to config file.");
				
				reloadConfig();
				System.out.println("config file reloaded!");
			}
			}
		//}
		
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