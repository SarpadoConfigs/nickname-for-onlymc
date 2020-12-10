package com.garotinho.nickname;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Nickname extends JavaPlugin implements Listener
{  
  @Override
  public void onEnable() {
	  getConfig().options().copyDefaults(true);
	  PluginManager pm = Bukkit.getServer().getPluginManager();
      pm.registerEvents(this, this);
  }
  @Override
  public void onDisable() {
  }
  
  @EventHandler
  public void joinEvent(PlayerJoinEvent e)
  {
    Player p = e.getPlayer();
    String nick = getConfig().getString(p.getName());
    if (nick == null) 
      return;
    if (p.hasPermission("nickname.use")) {
      if (!nick.contains("&")) {
        p.setDisplayName(nick);
        return;
      }
      if (p.hasPermission("nickname.color")) {
        ChatColor.translateAlternateColorCodes('&', nick);
        p.setDisplayName(nick);
        return;
      }
      p.setDisplayName(nick);
      return;
    }
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
     if (cmd.getName().equalsIgnoreCase("nickname")) {
       if (args.length == 0) {
         sender.sendMessage(ChatColor.BLUE + "/Nickname [Nickname] <Player>");
      }
       if (args.length == 1) {
         if (!(sender instanceof Player)) {
           sender.sendMessage(ChatColor.BLUE + "Solo usuarios pueden ejecutar ese comando!!");
           return true;
        }

         Player p = (Player)sender;
         String nick = args[0];
         if (!p.hasPermission("nickname.use")) {
           p.sendMessage(ChatColor.BLUE + "Tu no tienes permiso de usar /Nickname!");
           return true;
        }
         if (nick.contains("&")) {
           if (p.hasPermission("nickname.color")) {
             ChatColor.translateAlternateColorCodes('&', nick);
             p.setDisplayName(nick);
             this.getConfig().set(p.getName(), nick);
             p.sendMessage(ChatColor.BLUE + "Tu nickname se cambio a '" + nick + "'.");
             return true;
          }

           p.sendMessage(ChatColor.BLUE + "Tu no tienes permiso de usar colores en tus nicknames!!");
           return true;
        }

         p.setDisplayName(nick);
         this.getConfig().set(p.getName(), nick);
         saveConfig();
         p.sendMessage(ChatColor.BLUE + "Tu nicknamme ah sido cambiado a '" + nick + "'.");
         return true;
      }

       if (args.length == 2) {
         if (Bukkit.getPlayer(args[1]) == null) {
           sender.sendMessage(ChatColor.BLUE + args[1] + " Doesn't Exist!");
           return true;
        }

         Player target = Bukkit.getPlayer(args[1]);
         Player p = (Player)sender;
         if (!p.hasPermission("nickname.use")) {
           p.sendMessage(ChatColor.BLUE + "Tu no tienes permiso de ejecutar el comando /Nickname!");
           return true;
        }
         if (!p.hasPermission("nickname.others")) {
           p.sendMessage(ChatColor.BLUE + "Tu no tienes permiso de cambiar otros' Nicknames!");
           return true;
        }
         String nick = args[0];
         if (nick.contains("&")) {
           if (p.hasPermission("nickname.color")) {
             ChatColor.translateAlternateColorCodes('&', nick);
             target.setDisplayName(nick);
             this.getConfig().set(target.getName(), nick);
             saveConfig();
             target.sendMessage(ChatColor.BLUE + "Tu nickname se cambio a '" + nick + "'.");
             p.sendMessage(ChatColor.BLUE + "Tu has cambiado " + target.getName() + "tu Nickname a '" + nick + "'.");
             return true;
          }

           p.sendMessage(ChatColor.BLUE + "Tu no tienes permiso de usar colores en tus nicknames!");
           return true;
        }

         target.setDisplayName(nick);
         this.getConfig().set(target.getName(), nick);
         saveConfig();
         target.sendMessage(ChatColor.BLUE + "Tu nick ah sido cambiado a'" + nick + "'.");
         p.sendMessage(ChatColor.BLUE + "Tu has cambiado" + target.getName() + "tu Nickname a '" + nick + "'.");
         return true;
      }

         sender.sendMessage(ChatColor.BLUE + "/Nickname [Nickname] <Player>");
    }

     return true;
  }
}