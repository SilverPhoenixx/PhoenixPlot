package net.royalguardians.phoenixplot.commands;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.world.WorldAPI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                    if(PhoenixPlot.getSpawn() == null) {
                        WorldAPI.loadWorld(PhoenixPlot.getSpawnWorldname());
                        PhoenixPlot.getConfigFile().loadSpawn();
                    }
                    if(PhoenixPlot.getSpawn() == null) {
                        player.sendMessage("Welt ist nicht geladen.");
                        WorldAPI.loadWorld(PhoenixPlot.getSpawn().getWorld().getName());
                        return  true;
                    }
                if (!player.teleport(PhoenixPlot.getSpawn())) {
                    WorldAPI.loadWorld(PhoenixPlot.getSpawnWorldname());
                    player.teleport(PhoenixPlot.getSpawn());
                }
                player.sendMessage(PhoenixPlot.getPrefix() + " §eDu wurdest zum Spawn teleportiert.");
            } else {
                if(!player.hasPermission("PhoenixPlot.setSpawn")) {
                    player.sendMessage(PhoenixPlot.getPrefix() + " §cDer Befehl lautet: /Spawn");
                    return true;
                }
                Location loc = player.getLocation();
                PhoenixPlot.getConfigFile().setSpawn(loc);
                player.sendMessage(PhoenixPlot.getPrefix() + " §eDer Spawm wurde zu X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + " Yaw: " + loc.getYaw() +  " Pitch: " + loc.getPitch() + " in der Welt: " + loc.getWorld().getName() + " gesetzt");
            }
        }
        return false;
    }
}
