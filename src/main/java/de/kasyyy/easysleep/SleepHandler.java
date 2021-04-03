package de.kasyyy.easysleep;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A class that contains all logic for handling sleeping players and setting the time and / or weather. For every world
 * with sleeping player exists a SleepHandler. SleepHandlers should only be created and accessed via the {@link
 * SleepHandler#getSleepHandler(World)} method. All data is transient.
 */
public class SleepHandler {
    /** The percentage of players that need to be asleep for the time / weather to be changed. */
    private static final float PERCENTAGE_OF_NEEDED_SLEEPING_PLAYERS = 0.7f;
    /** The message that is displayed after the weather was changed. */
    private static final String RAIN_CLEARED_MESSAGE = Util.prefix + "The sun is shining again!";
    /** The message that is displayed after the time was changed. */
    private static final String MORNING_MESSAGE = Util.prefix + "Good morning!";

    /**
     * A HashMap containing all worlds and their corresponding SleepHandler. SleepHandlers are only created after they
     * are first accessed, so for non-null objects the {@link SleepHandler#getSleepHandler(World)} method should be
     * used.
     */
    private static final HashMap<World, SleepHandler> worldToSleepHandler = new HashMap<>();

    /** The world that this instance of the SleepHandler is responsible for. */
    private final World world;
    /** All players that are currently in bed.*/
    private final Set<UUID> playersInBed = new HashSet<>();

    private SleepHandler(final World world) {
        this.world = world;
        worldToSleepHandler.put(world, this);
    }

    /**
     * Returns a {@link SleepHandler} for the given world if it exists. Otherwise a new one is created.
     *
     * @param world The world for which the {@link SleepHandler} should be returned.
     *
     * @return The {@link SleepHandler} for the given world.
     */
    public static SleepHandler getSleepHandler(final World world) {
        if (worldToSleepHandler.get(world) != null) {
            return worldToSleepHandler.get(world);
        } else {
            return new SleepHandler(world);
        }
    }

    /**
     * Adds a player to the list of sleeping players.
     *
     * @param player The player that should be added.
     */
    public void addPlayer(final Player player) {
        playersInBed.add(player.getUniqueId());
        if (isSufficientAmountOfPlayersSleeping(player.getWorld())) {
            manageWeatherAndTime();
            playersInBed.clear();
            return;
        }
        Bukkit.broadcastMessage(getPlayerMissingMessage(player.getWorld()));
    }

    /**
     * Removes a player from the list of sleeping players.
     *
     * @param player The player to be removed.
     */
    public void removePlayer(final Player player) {
        playersInBed.remove(player.getUniqueId());
        if (!(world.getTime() >= 12541 || world.hasStorm())) return;
        Bukkit.broadcastMessage(getPlayerMissingMessage(player.getWorld()));
    }

    /**
     * Checks whether enough players are sleeping for the plugin to set to day or clear the weather.
     *
     * @param world The world that should be checked.
     *
     * @return {@code True}, if enough players are sleeping, otherwise {@code false}.
     */
    private boolean isSufficientAmountOfPlayersSleeping(final World world) {
        return ((float) playersInBed.size()) >= ((float) world.getPlayers().size() * PERCENTAGE_OF_NEEDED_SLEEPING_PLAYERS);
    }

    /**
     * Clears the weather if it is stormy and sets the time to be day if it is night.
     */
    private void manageWeatherAndTime() {
        if(world.hasStorm()) {
            world.setStorm(false);
            Bukkit.broadcastMessage(RAIN_CLEARED_MESSAGE);
        }
        if(world.getTime() >= 12541) {
            world.setTime(0);
            Bukkit.broadcastMessage(MORNING_MESSAGE);
        }
        playersInBed.clear();
    }

    /**
     * Returns the fitting message for missing players during the PlayerBedEnter and LeaveEvent depending on the number
     * of players.
     *
     * @param world The player's world
     *
     * @return The message string
     */
    private String getPlayerMissingMessage(final World world) {
        if (playersInBed.size() == 1) {
            return Util.prefix + playersInBed.size() + " player is sleeping, at least "
                + ((int) Math.ceil((float) world.getPlayers().size() * PERCENTAGE_OF_NEEDED_SLEEPING_PLAYERS))
                + " need to be in bed!";
        } else {
            return Util.prefix + playersInBed.size() + " players are sleeping, at least "
                + ((int) Math.ceil((float) world.getPlayers().size() * PERCENTAGE_OF_NEEDED_SLEEPING_PLAYERS))
                + " need to be in bed!";
        }
    }
}
