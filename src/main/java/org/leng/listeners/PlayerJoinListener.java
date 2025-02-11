package org.leng.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leng.LengMOTD;

public class PlayerJoinListener implements Listener {
    private final LengMOTD plugin;

    public PlayerJoinListener(LengMOTD plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isMaintenanceMode() && !plugin.isStaff(event.getPlayer())) {
            event.getPlayer().kickPlayer(plugin.getMaintenanceMessage());
        }
    }
}