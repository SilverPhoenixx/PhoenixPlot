package net.royalguardians.phoenixplot.commands;

import net.royalguardians.PhoenixID.PhoenixID;
import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.api.UUIDCatcher;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class PlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(p);
            if(plotPlayer == null) {
                p.sendMessage(PhoenixPlot.getPrefix() + " §cBitte verlasse und betritt den Server neu.");
                return true;
            }
            if(!PhoenixPlot.getWorldData().containsKey(p.getWorld().getName())) {
                p.sendMessage(PhoenixPlot.getPrefix() + " §cDiese Welt ist keine Plot Welt!");
                return true;
            }
            if(plotPlayer.getPlots().containsKey(p.getWorld().getName())) {
                        p.sendMessage(PhoenixPlot.getPrefix() + " §cAuf dieser Welt kannst du keine weiteren Plots erstellen.");
                        return true;
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("auto")) {
                  int[] xz = PhoenixPlot.getWorldData().get(p.getWorld().getName()).getPlot();
                    plotPlayer.addPlot(p, p.getWorld().getName(), xz, p.getUniqueId().toString());
                    plotPlayer.teleportPlot(p);
                    p.sendMessage(PhoenixPlot.getPrefix() + " §eDas Plot mit der ID:§6 " + xz[0] + ":" + xz[1] + " §eist nun in deinem Besitz.");
                } else if(args[0].equalsIgnoreCase("claim")) {
                    WorldData worldData = PhoenixPlot.getWorldData().get(p.getWorld().getName());
                    int x = p.getLocation().getBlockX()/worldData.getFullLength();
                    int z = p.getLocation().getBlockZ()/worldData.getFullLength();
                    if(p.getLocation().getBlockX() < 0) x--;
                    if(p.getLocation().getBlockZ() < 0) z--;
                    if(worldData.isPlot(x, z)) {
                        p.sendMessage(PhoenixPlot.getPrefix() + " §cDas Plot gehört bereits einem Spieler.");
                        return true;
                    } else {
                        int[] xz = new int[]{x, z};
                        if (!plotPlayer.isOnPlot(xz, p.getLocation())) {
                            p.sendMessage(PhoenixPlot.getPrefix() + " §cDu befindest dich nicht auf einem Plot.");
                            return true;
                        }
                            worldData.getPlots().remove(xz);
                            worldData.getFile().setPlotList(worldData.getPlots());
                            plotPlayer.addPlot(p, p.getWorld().getName(), xz, p.getUniqueId().toString());
                            plotPlayer.teleportPlot(p);
                            p.sendMessage(PhoenixPlot.getPrefix() + " §eDas Plot mit der ID:§6 " + x + ":" + z + " §eist nun in deinem Besitz.");
                    }
                }
            } else if(args.length == 2) {
                Player t = Bukkit.getPlayer(args[1]);
                if(args[0].equalsIgnoreCase("add")) {
                    if(PhoenixPlot.getWorldData().get(p.getWorld().getName()).getPartnerSize() == 0) {
                        p.sendMessage(PhoenixPlot.getPrefix() + " §cIn der Welt kann man nicht mit einem Partner zusammen bauen!");
                        return true;
                    }
                    if(PhoenixPlot.getWorldData().get(p.getWorld().getName()).getPlotSize() <= plotPlayer.getPartnerList(p.getWorld().getName()).size()) {
                        p.sendMessage(PhoenixPlot.getPrefix() + " §cDie maximale Partneranzahl wurde erreicht!");
                        return true;
                    }
                        if (t == null) {
                            PhoenixID.db.getIdByName(args[1], new Consumer<Integer>() {
                                @Override
                                public void accept(Integer integer) {

                                }
                            });
                        } else {
                            UUID uuid = t.getUniqueId();

                        }
                } else if(args[0].equalsIgnoreCase("remove")) {

                }
            }
        }
        return false;
    }
}
