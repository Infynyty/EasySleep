package de.kasyyy.easysleep;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class EasySleep extends JavaPlugin implements Listener {
    private int playersInBed;

    public EasySleep() {
        super();
    }

    protected EasySleep(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage(Util.prefix + "Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.prefix + "Disabled!");
    }

    /**
     * Adds a player to the playersInBed Integer
     * @return Returns true if the number of players in bed exceeds 50% of total players.
     */
     boolean addPlayer() {
        playersInBed++;
        if(playersInBed >= (Bukkit.getOnlinePlayers().size()/2)) {
            playersInBed = 0;
            return true;
        }
        if(playersInBed == 1) {
            Bukkit.broadcastMessage(Util.prefix + playersInBed + " player is sleeping, at least "
                    + Math.ceil((float) Bukkit.getOnlinePlayers().size()/2) + " need to be in bed!");
            return false;
        } else {
            Bukkit.broadcastMessage(Util.prefix + playersInBed + " players are sleeping, at least "
                    + Math.ceil((float) Bukkit.getOnlinePlayers().size()/2) + " need to be in bed!");
            return false;
        }
    }

    /**
     * Removes a player from the playerInBed Integer
     */
     void removePlayer() {
        playersInBed--;
        if(playersInBed == 1) {
            Bukkit.broadcastMessage(Util.prefix + playersInBed + " player is sleeping, at least "
                    + ((int)Math.ceil((float) Bukkit.getOnlinePlayers().size()/2)) + " need to be in bed!");
        } else {
            Bukkit.broadcastMessage(Util.prefix + playersInBed + " players are sleeping, at least "
                    + ((int)Math.ceil((float) Bukkit.getOnlinePlayers().size()/2)) + " need to be in bed!");
        }
    }

    //Checks if its night or if its raining and if so calls the addPlayer method
    @EventHandler
    public void onSleep(PlayerBedEnterEvent e)  {
         if(e.getPlayer().getWorld().getTime() < 12541) return;
            if(addPlayer()) { //TODO: Is this called every time?
                if(e.getPlayer().getWorld().hasStorm()) {
                    e.getPlayer().getLocation().getWorld().setStorm(false);
                    Bukkit.broadcastMessage(Util.prefix  + "The sun is shining again!");
                }
                e.getPlayer().getLocation().getWorld().setTime(0);
                Bukkit.broadcastMessage(Util.prefix  + "Good morning!");
            }
    }

    //Checks if its night or if its raining and if so calls the removePlayer method
    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent e) {
        if(e.getPlayer().getWorld().getTime() < 12541) return;
        removePlayer();
    }
}
