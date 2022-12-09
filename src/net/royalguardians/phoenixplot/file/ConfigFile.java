package net.royalguardians.phoenixplot.file;

import net.royalguardians.phoenixplot.PhoenixPlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    private File FileManager;
    private YamlConfiguration FileConfiguration;

    public ConfigFile(String FileName) {
        if(!PhoenixPlot.getInstance().getDataFolder().exists()) PhoenixPlot.getInstance().getDataFolder().mkdir();
        this.FileManager = new File(PhoenixPlot.getInstance().getDataFolder(), FileName);
        if (!FileManager.exists()) {
            try {
                FileManager.createNewFile();
                this.FileConfiguration = YamlConfiguration.loadConfiguration(this.FileManager);
                FileConfiguration.set("Prefix", "§7[§6Plot§7]");
                FileConfiguration.set("Spawn.World", "world");
                FileConfiguration.set("Spawn.X", 0.5);
                FileConfiguration.set("Spawn.Y", 100);
                FileConfiguration.set("Spawn.Z", 0.5);
                FileConfiguration.set("Spawn.Yaw", 90);
                FileConfiguration.set("Spawn.Pitch", 0);
                PhoenixPlot.setPrefix("§7[§6Plot§7]");
            } catch (IOException ex) {
            }
            save();
        } else {
            this.FileConfiguration = YamlConfiguration.loadConfiguration(this.FileManager);
            loadInformation();
        }
    }
    public void setSpawn(Location location) {
        FileConfiguration.set("Spawn.World", location.getWorld().getName());
        FileConfiguration.set("Spawn.X", location.getBlockX() + 0.5);
        FileConfiguration.set("Spawn.Y", location.getBlockY() + 0.5);
        FileConfiguration.set("Spawn.Z", location.getBlockZ() + 0.5);
        FileConfiguration.set("Spawn.Yaw", location.getYaw());
        FileConfiguration.set("Spawn.Pitch", location.getPitch());
        save();
    }
    public void loadSpawn() {
        Location loc = new Location(Bukkit.getWorld(FileConfiguration.getString("Spawn.World")), FileConfiguration.getDouble("Spawn.X"),FileConfiguration.getDouble("Spawn.Y"),FileConfiguration.getDouble("Spawn.Z"),(float) FileConfiguration.getDouble("Spawn.Yaw"), (float) FileConfiguration.getDouble("Spawn.Pitch"));
        PhoenixPlot.setSpawn(loc, FileConfiguration.getString("Spawn.World"));
    }

    public void loadInformation() {
        PhoenixPlot.setPrefix(FileConfiguration.getString("Prefix"));
        loadSpawn();
    }

    public void save() {
        try {
            this.FileConfiguration.save(this.FileManager);
        }
        catch (IOException ex) {}
    }
}
