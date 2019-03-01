package gq.devmc.zhd;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main13
  extends JavaPlugin
  implements Listener
{
  public void onDisable()
  {
    Bukkit.getConsoleSender().sendMessage("§8[§6§lZ§8] §7ZHD Disabled");
  }
  
  public void onEnable()
  {
    Bukkit.getConsoleSender().sendMessage("§8[§6§lZ§8] §7ZHD Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    File f = new File(getDataFolder() + "/config.yml");
    if (!f.exists())
    {
      saveResource("config.yml", true);
      saveConfig();
    }
    setup(false);
  }
  
  void setup(boolean deepSearch)
  {
    if (deepSearch) {
      reloadConfig();
    }
    try
    {
      for (Player p : Bukkit.getOnlinePlayers()) {
        p.setMaximumNoDamageTicks(Integer.parseInt(getConfig().getString("zhd")));
      }
    }
    catch (Exception e)
    {
      Bukkit.getConsoleSender().sendMessage("§4[Z] ZHD has been reset.");
      getConfig().set("zhd", Integer.valueOf(7));
      saveConfig();
      for (Player p : Bukkit.getOnlinePlayers()) {
        p.setMaximumNoDamageTicks(Integer.parseInt(getConfig().getString("zhd")));
      }
    }
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (command.getName().equalsIgnoreCase("zhd"))
    {
      if (!sender.hasPermission("znetwork.zhd"))
      {
        sender.sendMessage("§8[§6§lZ§8] §7You don't have permission for this command.");
        return true;
      }
      if (args.length >= 1)
      {
        if ((args.length == 2) && (args[0].equalsIgnoreCase("sethd")))
        {
          try
          {
            Integer.parseInt(args[1]);
          }
          catch (Exception e)
          {
            sender.sendMessage("§c" + args[1] + " §7is not a valid.");return false;
          }
          int i = Integer.parseInt(args[1]);
          getConfig().set("zhd", Integer.valueOf(i));
          saveConfig();
          sender.sendMessage("§8[§6§lZ§8] §7ZHD has been set to §c" + i);
          setup(true);
        }
        else if ((args.length == 2) && (args[0].equalsIgnoreCase("setmsg")))
        {
          try
          {
            Boolean.parseBoolean(args[1].toLowerCase());
          }
          catch (Exception e)
          {
            sender.sendMessage("§c" + args[1] + " is not 'true' or 'false'");return false;
          }
          boolean b = Boolean.parseBoolean(args[1].toLowerCase());
          getConfig().set("msg", Boolean.valueOf(b));
          saveConfig();
          
          sender.sendMessage("§8[§6§lZ§2] §7MSG has been set to " + b);
        }
        else if ((args.length == 1) && (args[0].equalsIgnoreCase("reload")))
        {
          sender.sendMessage("§8[§6§lZ§2] §7Config reloaded");
          setup(true);
        }
        else if ((args.length == 1) && (args[0].equalsIgnoreCase("rl")))
        {
          setup(true);
          sender.sendMessage("§8[§6§lZ§8] §7Plugin reloaded");
        }
        else
        {
          sender.sendMessage("§8§m------------------------------------");
          sender.sendMessage("§8» /§7zhd sethd §8<§7delay§8>");
          sender.sendMessage("§8» /§7zhd setmsg §8<§7true§8/§7false§8>");
          sender.sendMessage("§8» /§7zhd rl");
          sender.sendMessage("§8§m------------------------------------");
        }
      }
      else
      {
        sender.sendMessage("§8§m------------------------------------");
        sender.sendMessage("§8» /§7zhd sethd §8<§7delay§8>");
        sender.sendMessage("§8» /§7zhd setmsg §8<§7true§8/§7false§8>");
        sender.sendMessage("§8» /§7zhd rl");
        sender.sendMessage("§8§m------------------------------------");
      }
    }
    return true;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e)
  {
    e.getPlayer().setMaximumNoDamageTicks(Integer.parseInt(getConfig().getString("zhd")));
    if (getConfig().getBoolean("msg"))
    {
      final Player p = e.getPlayer();
      getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
      {
        public void run()
        {
          p.getPlayer().sendMessage("§8[§6§lZ§8] ZNetwork");
        }
      }, 3L);
    }
  }
}
