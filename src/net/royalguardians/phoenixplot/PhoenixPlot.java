package net.royalguardians.phoenixplot;

import com.avaje.ebean.EbeanServer;
import net.royalguardians.phoenixplot.commands.BuildEventCommand;
import net.royalguardians.phoenixplot.commands.PlotCommand;
import net.royalguardians.phoenixplot.commands.SpawnCommand;
import net.royalguardians.phoenixplot.database.Database;
import net.royalguardians.phoenixplot.file.ConfigFile;
import net.royalguardians.phoenixplot.generator.PhoenixGenerator;
import net.royalguardians.phoenixplot.listener.JoinListener;
import net.royalguardians.phoenixplot.listener.PlotListener;
import net.royalguardians.phoenixplot.listener.WorldChangeListener;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PhoenixPlot extends JavaPlugin {

    private static String prefix;
    private static PhoenixPlot instance;

    private static String spawnWorldname;
    private static Location spawn;

    private static ConfigFile file;
    private static Database database;

   private static HashMap<String, WorldData> worldData = new HashMap<>();
   private static HashMap<Player, PlotPlayer> plotplayer = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        file = new ConfigFile("config.yml");
        file.loadInformation();
        database = new Database();
        database.createSQL();
        database.load();
        loadCommands();
        loadListener();

        /* for(File file : Bukkit.getWorldContainer().listFiles()) {
            if(!file.isDirectory()) continue;
            File configfile = new File(file.getPath() + "/config.yml");
            if(!configfile.exists()) continue;
            WorldFile worldFile = new WorldFile(file);
            WorldData data = worldFile.loadWorldData(file.getName());
            worldData.put(file.getName(), data);
        } */
    }

    @Override
    public void onDisable() {
        for(Player p : Bukkit.getOnlinePlayers()) p.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        for(World world : Bukkit.getWorlds()) {
            Bukkit.unloadWorld(world, true);
        }
    }

    public static PhoenixPlot getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        PhoenixPlot.prefix = prefix;
    }

    public static Location getSpawn() {
        return spawn;
    }

    public static String getSpawnWorldname() {
        return spawnWorldname;
    }

    public static void setSpawn(Location spawn, String spawnWorldname) {
        PhoenixPlot.spawnWorldname = spawnWorldname;
        PhoenixPlot.spawn = spawn;
    }
    public static ConfigFile getConfigFile() {
        return file;
    }

    public void loadCommands() {
        getCommand("Buildevent").setExecutor(new BuildEventCommand());
        getCommand("Plot").setExecutor(new PlotCommand());
        getCommand("Spawn").setExecutor(new SpawnCommand());
    }

    public void loadListener() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlotListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldChangeListener(), this);
    }


    public static HashMap<String, WorldData> getWorldData() {
        return worldData;
    }

    public static HashMap<Player, PlotPlayer> getPlotPlayer() {
        return plotplayer;
    }

    public static void addWorldData(String string, WorldData data) {
        worldData.put(string, data);
    }

    public static Database getDB() {
        return database;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new PhoenixGenerator();
    }
}
