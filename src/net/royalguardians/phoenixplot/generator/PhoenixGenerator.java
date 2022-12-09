package net.royalguardians.phoenixplot.generator;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

import java.util.Random;

public class PhoenixGenerator extends ChunkGenerator {

    int x = 0;
    int z = 0;


    WorldData worldData;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData data = createChunkData(world);
        world.setDifficulty(Difficulty.PEACEFUL);

        if(PhoenixPlot.getWorldData().containsKey(world.getName())) {
            worldData = PhoenixPlot.getWorldData().get(world.getName());
            int fullLength = worldData.getFullLength();
        for(int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                for (int blockY = 0; blockY < worldData.getSchematicHeight(); blockY++) {
                    if(blockY == 0) {
                        biome.setBiome(blockX, blockZ, Biome.PLAINS);
                        data.setBlock(blockX, 0, blockZ, Material.DIRT);
                    }
                    if (chunkX < 0 && chunkZ >= 0) {
                        generateSchematic(0, 0, blockX, blockY, blockZ, chunkX, chunkZ, fullLength, data);
                        generateSchematic(0, 1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(-1, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(-1, 1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                    } else if (chunkZ < 0 && chunkX >= 0) {
                        /* Finished */
                        generateSchematic(0, 0, blockX, blockY, blockZ, chunkX, chunkZ, fullLength, data);
                        generateSchematic(1, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(0, -1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(1, -1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                    } else if (chunkX < 0 && chunkZ < 0) {
                        /* Finished */
                        generateSchematic(0, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(-1, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(0, -1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(-1, -1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                    } else {
                        /* Finished */
                        generateSchematic(0, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(1, 0, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(0, 1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                        generateSchematic(1, 1, blockX, blockY, blockZ, chunkX, chunkZ,fullLength, data);
                    }
                }
            }
        }
        }
        return data;
    }

    public void generateSchematic(int addX, int addZ, int blockX, int blockY, int blockZ, int chunkX, int chunkZ, int fullLength, ChunkData data) {
        x = (chunkX*16+blockX) - ((chunkX*16+blockX)/fullLength + addX)*fullLength;
        z = (chunkZ*16+blockZ) - ((chunkZ*16+blockZ)/fullLength + addZ)*fullLength;
        if(worldData.getBlocks().get(x + ":" + blockY + ":" + z) == null) return;
        String[] block = worldData.getBlocks().get(x + ":" + blockY + ":" + z).split(":");
        if(worldData.getBlocks().containsKey(x + ":" + blockY + ":" + z)) { data.setBlock(blockX, blockY+1, blockZ, new MaterialData(Integer.parseInt(block[0]), (byte) Integer.parseInt(block[1])));
        }
    }
}
