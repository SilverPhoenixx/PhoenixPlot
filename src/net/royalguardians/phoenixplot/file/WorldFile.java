package net.royalguardians.phoenixplot.file;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.generator.PhoenixGenerator;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WorldFile {

    private File FileManager;
    private YamlConfiguration FileConfiguration;

    public WorldFile(File worldFolder) {
        if(!worldFolder.exists()) worldFolder.mkdir();
        this.FileManager = new File(worldFolder, "config.yml");
        this.FileConfiguration = YamlConfiguration.loadConfiguration(this.FileManager);
        if (!FileManager.exists()) {
            try {
                FileManager.createNewFile();
                FileConfiguration.set("Theme", "PLACEHOLDER");
                FileConfiguration.set("PlotHeight", 0);
                FileConfiguration.set("PartnerSize", 0);
                FileConfiguration.set("PlotRadius", 0);
                FileConfiguration.set("SchematicLength", 0);
                FileConfiguration.set("Date", "20.08.2001 12:00");
                FileConfiguration.set("Plots", "Plot");
                FileConfiguration.set("Schematic", "Test");
                FileConfiguration.set("Auto.X", 0);
                FileConfiguration.set("Auto.Z", 0);
                FileConfiguration.set("Auto.Rotation", 0);
                FileConfiguration.set("Auto.Position", 0);
                FileConfiguration.set("Auto.Length", 1);
                FileConfiguration.set("Auto.Repeat", 0);
            } catch (IOException ex) {
            }
            save();
        }
    }


    public void setWorldData(Player p, int plotheight, String theme, int partnerSize, int plotradius, int schematicLength, int height, String date) {
        try {
            List<Block> blocks = new ArrayList<>();
            for(int bX = p.getLocation().getBlockX(); bX < p.getLocation().getBlockX()+schematicLength+1; bX++) {
                for(int bY = p.getLocation().getBlockY()-1; bY < p.getLocation().getBlockY()+height+1; bY++) {
                    for(int bZ = p.getLocation().getBlockZ(); bZ < p.getLocation().getBlockZ()+schematicLength+1; bZ++) {
                        if(p.getWorld().getBlockAt(bX, bY, bZ).getType() == Material.AIR) continue;
                        blocks.add(p.getWorld().getBlockAt(bX, bY, bZ));
                    }
                }
            }
            SimpleDateFormat df = new SimpleDateFormat( "dd:MM:yyyy:HH:mm");
            Date newDate = df.parse(date);

            df.applyPattern("dd.MM.yyyy HH:mm");
            FileConfiguration.set("PlotHeight", plotheight);
            FileConfiguration.set("Theme", theme);
            FileConfiguration.set("PartnerSize", partnerSize);
            FileConfiguration.set("PlotRadius", plotradius);
            FileConfiguration.set("SchematicLength", schematicLength);
            FileConfiguration.set("Height", height);
            FileConfiguration.set("Date", df.format(newDate));

            FileConfiguration.set("Auto.X", 0);
            FileConfiguration.set("Auto.Z", 0);
            FileConfiguration.set("Auto.Rotation", 0);
            FileConfiguration.set("Auto.Position", 0);
            FileConfiguration.set("Auto.Length", 1);
            FileConfiguration.set("Auto.Repeat", 0);

            for(Block b : blocks) {

                int x = b.getX()-p.getLocation().getBlockX();
                int y = b.getY()-p.getLocation().getBlockY();
                int z = b.getZ()-p.getLocation().getBlockZ();
                FileConfiguration.set("Schematic." + x + ":" + y + ":" + z, b.getTypeId() + ":" + b.getData());
            }
            save();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, String> getBlocks() {
        HashMap<String, String> blockMap = new HashMap<>();
        for(String key : FileConfiguration.getConfigurationSection("Schematic").getKeys(true)) {
            blockMap.put(key, FileConfiguration.getString("Schematic." + key));
        }
        return blockMap;
    }

    public List<int[]> avaiblePlots() {
        if(FileConfiguration.getString("Plots").equalsIgnoreCase("Plot")) return null;
        List<int[]> avaiblePlot = new ArrayList<>();
        for(String key : FileConfiguration.getConfigurationSection("Plots").getKeys(true)) {
            String[] plot = FileConfiguration.getString("Plots." + key).split(":");
            avaiblePlot.add(new int[] {Integer.parseInt(plot[0]), Integer.parseInt(plot[1])});
        }
        return avaiblePlot;
    }

    public void setPlotList(List<int[]> plots) {
        FileConfiguration.set("Plots", plots);
        save();
    }

    public WorldData loadWorldData(String world) {
        try {
            int plotheihgt = FileConfiguration.getInt("PlotHeight");
            String theme = FileConfiguration.getString("Theme");
            int plotradius = FileConfiguration.getInt("PlotRadius");
            int partnerSize = FileConfiguration.getInt("PartnerSize");
            int schematicLength = FileConfiguration.getInt("SchematicLength");
            int height = FileConfiguration.getInt("Height");

            int x = FileConfiguration.getInt("Auto.X");
            int z = FileConfiguration.getInt("Auto.Z");
            int rotation = FileConfiguration.getInt("Auto.Rotation");
            int position = FileConfiguration.getInt("Auto.Position");
            int length = FileConfiguration.getInt("Auto.Length");
            int repeat = FileConfiguration.getInt("Auto.Repeat");

            List<int[]> avaiblePlots = avaiblePlots();

            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = f.parse(FileConfiguration.getString("Date"));
            HashMap<String, String> blocks = getBlocks();


            WorldData worldData = new WorldData(world, plotheihgt, blocks, theme, partnerSize, plotradius, schematicLength, height, date,x, z, rotation, length, position, repeat, avaiblePlots,this);
            return worldData;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void updateData(int x, int z, int repeat, int rotation, int length, int position) {
        FileConfiguration.set("Auto.X", x);
        FileConfiguration.set("Auto.Z", z);
        FileConfiguration.set("Auto.Rotation", rotation);
        FileConfiguration.set("Auto.Position", position);
        FileConfiguration.set("Auto.Length", length);
        FileConfiguration.set("Auto.Repeat", repeat);
        save();
    }


    public void save() {
        try {
            this.FileConfiguration.save(this.FileManager);
        }
        catch (IOException ex) {}
    }
}
