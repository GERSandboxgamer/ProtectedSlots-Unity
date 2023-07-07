package de.sbg.unity.protectedslots;

import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.objects.Player;

public class EventListener implements Listener {

    private ProtectedSlots plugin;

    public EventListener(ProtectedSlots plugin) {
        this.plugin = plugin;
    }

    @EventMethod
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        int rest = plugin.MaxSlots - Server.getPlayerCount() - plugin.Config.ProtectedSlotsAmonth;
        int Admin = plugin.Config.ProtectedSlotsAmonth;
        Player player = event.getPlayer();

        for (Player player2 : Server.getAllPlayers()) {
            if (player2.isAdmin()) {
                Admin -= 1;
            }
        }

        if (player.isAdmin() || plugin.Config.NoAdminPlayers.contains(player.getUID())) {
            if (rest < 0 && Admin < 0) {
                player.kick("Server is full!");
            }
        } else {
            if (rest < 0) {
                player.kick("Server is full!");
            }
        }

    }

}
