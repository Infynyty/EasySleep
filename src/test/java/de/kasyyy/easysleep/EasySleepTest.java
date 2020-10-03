package de.kasyyy.easysleep;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Mock
    private Player playerMock;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(EasySleep.class);
        mockWorld = new WorldMock(Material.DIRT, 2);
    }


    @Test
    public void testAddPlayer() {
        server.setPlayers(2);

        when(playerMock.getWorld()).thenReturn(mockWorld);
        mockWorld.setTime(13000);
        mockWorld.setStorm(true);

        PlayerBedEnterEvent playerBedEnterEventMock = PowerMockito.mock(PlayerBedEnterEvent.class);
        PlayerBedLeaveEvent playerBedLeaveEvent = PowerMockito.mock(PlayerBedLeaveEvent.class);
        when(playerBedEnterEventMock.getPlayer()).thenReturn(playerMock);
        when(playerBedLeaveEvent.getPlayer()).thenReturn(playerMock);


        plugin.onSleep(playerBedEnterEventMock);

        assertEquals(0, mockWorld.getTime());
        assertFalse(mockWorld.hasStorm());

        plugin.onLeaveBed(playerBedLeaveEvent);

        server.setPlayers(3);

        plugin.onSleep(playerBedEnterEventMock);

        mockWorld.setTime(13000);
        mockWorld.setStorm(true);

        assertNotEquals(0, mockWorld.getTime());
        assertTrue(mockWorld.hasStorm());
    }

    @Test
    public void testRemovePlayer() {
        server.setPlayers(2);

        PlayerBedLeaveEvent playerBedLeaveEvent = PowerMockito.mock(PlayerBedLeaveEvent.class);
        when(playerBedLeaveEvent.getPlayer()).thenReturn(playerMock);
        when(playerMock.getWorld()).thenReturn(mockWorld);
        plugin.onLeaveBed(playerBedLeaveEvent);


    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}