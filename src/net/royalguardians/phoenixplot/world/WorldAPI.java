package net.royalguardians.phoenixplot.world;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.file.WorldFile;
import net.royalguardians.phoenixplot.generator.PhoenixGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldAPI {

    public static void loadWorld(String name) {
        File file = new File(Bukkit.getWorldContainer() + "/" + name);
        File configfile = new File(file.getPath() + "/config.yml");
        if (file.isDirectory() && configfile.exists()) {
            WorldFile worldFile = new WorldFile(file);
            WorldData data = worldFile.loadWorldData(file.getName());
            PhoenixPlot.getWorldData().put(file.getName(), data);
            WorldCreator worldCreator = new WorldCreator(name);
            worldCreator.generator(new PhoenixGenerator());
            worldCreator.generateStructures(false);
            worldCreator.createWorld().setKeepSpawnInMemory(false);
        } else {
            WorldCreator worldCreator = new WorldCreator(name);
            worldCreator.generator();
            worldCreator.generateStructures(false);
            worldCreator.createWorld().setKeepSpawnInMemory(false);
        }
    }
}
