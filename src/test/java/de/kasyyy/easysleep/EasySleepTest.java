package de.kasyyy.easysleep;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerBedEnterEvent.class)
public class EasySleepTest {

    private ServerMock server;
    private EasySleep plugin;
    private World mockWorld;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(EasySleep.class);
        mockWorld = new WorldMock(Material.DIRT, 2);
    }


    @Test
    public void testAddPlayer() {
        server.setPlayers(2);
        PlayerMock p = server.getPlayer(0);

        PlayerBedEnterEvent playerBedEnterEvent = PowerMockito.mock(PlayerBedEnterEvent.class);
        when(playerBedEnterEvent.getPlayer()).thenReturn(p);
        plugin.onSleep(playerBedEnterEvent);

        assertTrue(plugin.addPlayer());
        assertEquals(0, mockWorld.getTime());
        assertFalse(mockWorld.hasStorm());

        server.setPlayers(3);
        plugin.removePlayer();

        assertFalse(plugin.addPlayer());
    }

    @Test
    public void testRemovePlayer() {
        server.setPlayers(2);
        PlayerMock p = server.getPlayer(0);

        PlayerBedLeaveEvent playerBedLeaveEvent = PowerMockito.mock(PlayerBedLeaveEvent.class);
        when(playerBedLeaveEvent.getPlayer()).thenReturn(p);
        plugin.onLeaveBed(playerBedLeaveEvent);


    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}