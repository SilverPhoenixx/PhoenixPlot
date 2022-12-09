package net.royalguardians.phoenixplot.world;

import net.royalguardians.phoenixplot.file.WorldFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WorldData {

    String name;
    int plotHeight;

    Map<String, String> blocks;
    int plotSize, schematicLength, schematicheight, partnerSize, fullLength;
    String theme;
    Date date;
    WorldFile file;

    List<int[]> plots = new ArrayList<>();
    int x;
    int z;

    int rotation;
    int length;
    int position;
    int repeat;

    public WorldData(String name, int plotHeight, Map<String, String> blocks, String theme, int partnerSize, int plotSize, int schematicLength, int height, Date date, int x, int z, int rotation, int length, int position, int repeat, List<int[]> plots, WorldFile file) {
        this.name = name;

        this.blocks = blocks;

        this.plotHeight = plotHeight;

        this.theme = theme;
        this.partnerSize = partnerSize;

        this.schematicLength = schematicLength;
        this.schematicheight = height;
        this.plotSize = plotSize;

        this.fullLength = schematicLength;
        this.date = date;
        this.file = file;

    /* Variables for getting next Free Plot ID */
        this.rotation = rotation;
        this.length = length;
        this.position = position;
        this.repeat = repeat;

        this.x = x;
        this.z = z;
    }

    public WorldFile getFile() {
        return file;
    }

    public List<int[]> getPlots() {
        return plots;
    }

    public int getPlotSize() {
        return plotSize;
    }

    public int getSchematicLength() {
        return schematicLength;
    }


    public Date getDate() {
        return date;
    }

    public Map<String, String> getBlocks() {
        return blocks;
    }

    public int getPlotHeight() {
        return plotHeight;
    }

    public int getSchematicHeight() {
        return schematicheight;
    }

    public int getFullLength() {
        return fullLength;
    }

    public int getPartnerSize() {
        return partnerSize;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isPlot(int x, int z) {
        x = x*fullLength;
        z = z*fullLength;
        return Bukkit.getWorld(name).getBlockAt(x-1, 0, z-1).getType() == Material.WALL_SIGN;
    }
    public int[] getPlot() {
            int[] plot;
            if(!plots.isEmpty()) {
                while(true) {
                    int x = plots.get(0)[0];
                    int z = plots.get(0)[1];
                    if(isPlot(x, z)) {
                        plots.remove(0);
                        plots.add(getFreePlot());
                        continue;
                    }
                    plot = plots.get(0);
                    break;
                }
                plots.add(getFreePlot());
                plots.remove(plot);
            } else {
                for (int i = 0; i < 5; i++) {
                    plots.add(getFreePlot());
                }
                return getPlot();
            }
            file.setPlotList(plots);
            file.updateData(x, z, repeat, rotation, length, position);
            return plot;
        }
        public int[] getFreePlot() {
            switch(rotation) {
                case 0:{
                    x++; break;
                }
                case 1:{
                    z--; break;
                }
                case 2:{
                    x--; break;
                }
                case 3:{
                    z++; break;
                }
            }
            position++;
            if(position >= length) {
                repeat++;
                rotation++;
                if(rotation == 4) rotation = 0;
                position = 0;
                if(repeat == 2) {
                    repeat = 0;
                    length++;
                }
            }
            return new int[] {x, z};
        }
}
