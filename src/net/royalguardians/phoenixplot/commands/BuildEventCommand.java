package net.royalguardians.phoenixplot.commands;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.file.WorldFile;
import net.royalguardians.phoenixplot.world.WorldAPI;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class BuildEventCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("PhoenixPlot.Buildevent")) return true;
            if(args.length == 9) {
            if(args[0].equalsIgnoreCase("create")) {
                    try {
                        List<String> fileName = Arrays.asList(Bukkit.getWorldContainer().list());
                        if(fileName.contains(args[1])) {
                            p.sendMessage(PhoenixPlot.getPrefix() + " §cWähle einen anderen Namen, der Ordner existiert bereits.");
                            return true;
                        }
                        WorldFile f = new WorldFile(new File(Bukkit.getWorldContainer().getPath() + "/" + args[1]));
                        try {
                            f.setWorldData(p, Integer.parseInt(args[2]), args[3], Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), args[8]);
                            WorldData data = f.loadWorldData(args[1]);
                            PhoenixPlot.addWorldData(args[1], data);
                            WorldAPI.loadWorld(args[1]);
                            p.teleport(new Location(Bukkit.getWorld(args[1]), 0, 128, 0));
                            p.sendMessage(PhoenixPlot.getPrefix() + " §eDas Bauevent: " + args[1] + " bis zum: " + new SimpleDateFormat("dd.MM.yyy HH:mm").format(data.getDate()) + " wurde erstellt.");
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    } catch (NumberFormatException ex) {
                        p.sendMessage(PhoenixPlot.getPrefix() + " §eDer Befehl lautet: /Buildevent create <Name> <SpawnHeight> <Thema> <Partneranzahl> <PlotLength> <SchematicLength> <SchematicHeight> <Enddatum: dd:MM:yyyy:HH:mm>");
                    }
                } else {
                    p.sendMessage(PhoenixPlot.getPrefix() + " §eDer Befehl lautet: /Buildevent create <Name> <SpawnHeight> <Thema> <Partneranzahl> <PlotLength> <SchematicLength> <SchematicHeight> <Enddatum: dd:MM:yyyy:HH:mm>");
                }
            } else {
                p.sendMessage(PhoenixPlot.getPrefix() + " §eDer Befehl lautet: /Buildevent create <Name> <SpawnHeight> <Thema> <Partneranzahl> <PlotLength> <SchematicLength> <SchematicHeight> <Enddatum: dd:MM:yyyy:HH:mm>");
            }
        }
        return false;
    }
}
