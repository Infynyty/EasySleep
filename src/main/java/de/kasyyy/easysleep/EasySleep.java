package de.kasyyy.easysleep;

import org.bukkit.Bukkit;
import org.bukkit.World;
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

    private final String RAIN_CLEARED_MESSAGE = Util.prefix  + "The sun is shining again!";
    private final String MORNING_MESSAGE = Util.prefix  + "Good morning!";

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
     private boolean addPlayer(World w) {
        playersInBed++;
        if(playersInBed >= (w.getPlayers().size()/2)) {
            playersInBed = 0;
            return true;
        }
        Bukkit.broadcastMessage(getPlayerMissingMessage(w));
        return false;
    }

    /**
     * Removes a player from the playerInBed Integer
     */
     private void removePlayer(World w) {
         playersInBed--;
         Bukkit.broadcastMessage(getPlayerMissingMessage(w));
    }

    //Checks if its night or if its raining and if so calls the addPlayer method
    @EventHandler
    void onSleep(PlayerBedEnterEvent e)  {

        if(!addPlayer(e.getPlayer().getWorld())) return;

        if(e.getPlayer().getWorld().hasStorm()) {
                e.getPlayer().getWorld().setStorm(false);
                Bukkit.broadcastMessage(RAIN_CLEARED_MESSAGE);
            }
        if(e.getPlayer().getWorld().getTime() < 12541) return;

        e.getPlayer().getWorld().setTime(0);
        Bukkit.broadcastMessage(MORNING_MESSAGE);
    }

    //Checks if its night or if its raining and if so calls the removePlayer method
    @EventHandler
    void onLeaveBed(PlayerBedLeaveEvent e) {
        if(e.getPlayer().getWorld().getTime() < 12541) return;
        removePlayer(e.getPlayer().getWorld());
    }

    /**
     * Returns the fitting message for missing players during the PlayerBedEnter and LeaveEvent depending on the number
     * of players.
     * @param w The player's world
     * @return The message string
     */
    private String getPlayerMissingMessage(World w) {
         if(playersInBed == 1) {
             return Util.prefix + playersInBed + " player is sleeping, at least "
                     + Math.ceil((float) w.getPlayers().size()/2) + " need to be " +
                     "in bed!";
         } else {
             return Util.prefix + playersInBed + " players are sleeping, at least "
                     + Math.ceil((float) w.getPlayers().size()/2) + " need to be " +
                     "in bed!";
         }
    }
}
