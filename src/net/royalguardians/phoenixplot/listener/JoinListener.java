package net.royalguardians.phoenixplot.listener;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.world.WorldAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.CompletableFuture;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            if(!event.getPlayer().teleport(PhoenixPlot.getSpawn())) {
                WorldAPI.loadWorld(PhoenixPlot.getSpawnWorldname());
                PhoenixPlot.getConfigFile().loadSpawn();
                event.getPlayer().teleport(PhoenixPlot.getSpawn());
            }
        }
        event.setJoinMessage(PhoenixPlot.getPrefix() + " §e" + event.getPlayer().getName() + " ist dem Server beigetreten.");
        CompletableFuture.supplyAsync(() -> PhoenixPlot.getDB().loadPlayer(event.getPlayer()));
    }
}
