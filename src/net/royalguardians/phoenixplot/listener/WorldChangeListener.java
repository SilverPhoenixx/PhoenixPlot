package net.royalguardians.phoenixplot.listener;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import net.royalguardians.phoenixplot.scoreboard.PlotScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(event.getPlayer());
        if(plotPlayer == null) return;
        PlotScoreboard.updatePartner(plotPlayer, event.getPlayer(), plotPlayer.getPartner(event.getFrom()));
        PlotScoreboard.updateTheme(plotPlayer, event.getPlayer(), plotPlayer.getTheme(event.getFrom()));
        PlotScoreboard.updateDate(plotPlayer, event.getPlayer(), plotPlayer.getDate(event.getFrom()));
        PlotScoreboard.updateRating(plotPlayer, event.getPlayer(), plotPlayer.getRating(event.getFrom()));
    }
}
