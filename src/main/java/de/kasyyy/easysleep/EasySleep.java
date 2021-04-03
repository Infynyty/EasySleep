package de.kasyyy.easysleep;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EasySleep extends JavaPlugin implements Listener {

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

    @EventHandler
    void onSleep(PlayerBedEnterEvent e)  {
        if (e.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) return;
        final SleepHandler sleepHandler = SleepHandler.getSleepHandler(e.getPlayer().getWorld());
        sleepHandler.addPlayer(e.getPlayer());
    }

    @EventHandler
    void onLeaveBed(PlayerBedLeaveEvent e) {
        final SleepHandler sleepHandler = SleepHandler.getSleepHandler(e.getPlayer().getWorld());
        sleepHandler.removePlayer(e.getPlayer());
    }
}
