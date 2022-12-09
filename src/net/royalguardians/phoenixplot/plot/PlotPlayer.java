package net.royalguardians.phoenixplot.plot;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.royalguardians.PhoenixID.PhoenixID;
import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class PlotPlayer {
    SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy HH:mm");
    /* OWN PLOTS
     The key is the plot world, to get the plot on the world
     the value is list of own plot coordinates "x:z"
     */
    Map<String, int[]> plots;
    Map<String, List<Integer>> partnerList = new HashMap<>();


    /* Plot Rating
    String = world + ":" + xz
    int[] =
    Landschaft 0
    Vegitation 1
    Detail 2
    Struktur 3
    Organic 4
    Innovation 5
    Atmosphäre 6
     */
    Map<String, int[]> rating;

    /* FRIENDS PLOT, who you added
     The key is the plot world to get the plot on the world
     the value is list of own plot coordinates "x:z"
     */
    Map<String, List<int[]>> partneredPlot;

    net.minecraft.server.v1_8_R3.Scoreboard board = new net.minecraft.server.v1_8_R3.Scoreboard();
    ScoreboardObjective obj = board.registerObjective("§3Phoenix§bPlot", IScoreboardCriteria.b);

    public PlotPlayer(Map<String, int[]> plots, Map<String, List<Integer>> partnerList, Map<String, List<int[]>> partneredPlot, Map<String, int[]> rating) {
        this.plots = plots;
        this.rating = rating;
        this.partneredPlot = partneredPlot;
    }

    public ScoreboardObjective getObj() {
        return obj;
    }


    public List<Integer> getPartnerList(String world) {
        return partnerList.get(world);
    }

    public String getRating(World world) {
        int[] xz = plots.get(world.getName());
        if(xz == null) {
            return "§0§b/";
        }
        int[] rating = this.rating.get(world.getName() + ":" + xz[0] + ":" + xz[1]);
        int allrate = rating[0] + rating[1] + rating[2] + rating[3] + rating[4] + rating[5] + rating[6]/7;
        int allrateCopy = allrate;

        String rate = "§c";
        boolean underZero = false;
        for(int lines = 20; lines > 0; lines++) {
            if(underZero) {
                rate += "|";
            } else {
                if(allrateCopy-5 >= 0) {
                    rate += "|";
                    continue;
                }
                rate += "§7|";
                underZero = true;
            }
        }
        return rate + " §c" + allrate + "%";
    }

    public Scoreboard getBoard() {
        return board;
    }

    public String getDate(World world) {
        if(!PhoenixPlot.getWorldData().containsKey(world.getName())) return "/";
        return df.format(PhoenixPlot.getWorldData().get(world.getName()).getDate());
    }
    public String getDate(Player player) {
        if(!PhoenixPlot.getWorldData().containsKey(player.getWorld().getName())) return "/";
        return df.format(PhoenixPlot.getWorldData().get(player.getWorld().getName()).getDate());
    }
    public String getPartner(Player player) {
        if(PhoenixPlot.getWorldData().containsKey(player.getWorld().getName())) return "" +PhoenixPlot.getWorldData().get(player.getWorld().getName()).getPartnerSize();
        return "/";
    }
    public String getPartner(World world) {
        if(PhoenixPlot.getWorldData().containsKey(world.getName())) return "" +PhoenixPlot.getWorldData().get(world.getName()).getPartnerSize();
        return "/";
    }
    public String getTheme(Player player) {
        if(!PhoenixPlot.getWorldData().containsKey(player.getWorld().getName())) return "/";
        return "" +PhoenixPlot.getWorldData().get(player.getWorld().getName()).getTheme();
    }
    public String getTheme(World world) {
        if(!PhoenixPlot.getWorldData().containsKey(world.getName())) return "/";
        return "" +PhoenixPlot.getWorldData().get(world.getName()).getTheme();
    }
    public Map<String, int[]> getPlots() {
        return plots;
    }

    public Map<String, List<int[]>> getPartneredPlot() {
        return partneredPlot;
    }

    public void teleportPlot(Player p) {
        if(!plots.containsKey(p.getWorld().getName())) {
            p.sendMessage(PhoenixPlot.getPrefix() + " §cIn dieser Welt besitzt du kein Plot.");
            return;
        }
        int size = PhoenixPlot.getWorldData().get(p.getWorld().getName()).getFullLength();
        double x = plots.get(p.getWorld().getName())[0]*size+(PhoenixPlot.getWorldData().get(p.getWorld().getName()).getPlotSize()/2);
        double z = plots.get(p.getWorld().getName())[1]*size-1.5;
        p.teleport(new Location(Bukkit.getWorld(p.getWorld().getName()), x, PhoenixPlot.getWorldData().get(p.getWorld().getName()).getPlotHeight(), z));
    }

    public void addPlot(Player player, String world, int[] xz, String uuid) {
        plots.put(player.getWorld().getName(), xz);
        int size = PhoenixPlot.getWorldData().get(player.getWorld().getName()).getFullLength();
        int x = xz[0]*size-1;
        int z = xz[1]*size-1;
        Block b = player.getWorld().getBlockAt(x, 0, z);
        b.setType(Material.WALL_SIGN);
        b.setData((byte) 1);
        Sign sign = (Sign) b.getState();

        sign.setLine(0, xz[0] + ":" + xz[1]);
        sign.update();
        PhoenixPlot.getDB().registerPlot(PhoenixID.ids.get(player.getUniqueId()).getOrdinal(), world, (xz[0] + ":" + xz[1]));
    }
    public void addPartnerPlot(String world, int[] xz, String uuid) {
        if(!partneredPlot.containsKey(world)) partneredPlot.put(world, new ArrayList<>());
        partneredPlot.get(world).add(xz);
        PhoenixID.db.getIdByUniqueID(uuid, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                PhoenixPlot.getDB().registerPartnerPlot(integer , world, xz[0] + ":" + xz[1]);
            }
        });
    }

    public void removePlot(String world, int[] xz, String uuid) {
        if(!plots.containsKey(world)) return;
        plots.remove(world);

        PhoenixID.db.getIdByUniqueID(uuid, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                PhoenixPlot.getDB().removePlayerPlot(integer, world, xz[0] + ":" + xz[1]);
            }
        });
    }
    public void removePlayerPlot(String world, int[] xz, String uuid) {
        if(!partneredPlot.containsKey(world)) return;
        partneredPlot.get(world).remove(xz);
        PhoenixID.db.getIdByUniqueID(uuid, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                PhoenixPlot.getDB().removePartnerPlot(integer, world, xz[0] + ":" + xz[1]);
            }
        });
    }

    public boolean isOwnPlot(String world, int[] xz) {
        if(!plots.containsKey(world)) return false;
        return plots.get(world) == xz;
    }

    public boolean isPartneredPlot(String world, int[] xz) {
        if(!partneredPlot.containsKey(world)) return false;
        return partneredPlot.get(world).contains(xz);
    }

    public boolean canBuild(String world, int[] xz) {
        if(plots.containsKey(world) && plots.get(world) == xz) return true;
        if(partneredPlot.containsKey(world) &&  partneredPlot.get(world).contains(xz)) return true;
        return false;
    }

    public boolean isOnPlot(int[] xz, Location loc) {
        WorldData data = PhoenixPlot.getWorldData().get(loc.getWorld().getName());
        int plotSize = data.getPlotSize();
        int x = xz[0]*data.getFullLength();
        int z = xz[1]*data.getFullLength();

        int plusX = x+plotSize;
        int plusZ = z+plotSize;
        if(loc.getBlockX() < x+1 || loc.getBlockX() > plusX) return false;
        if(loc.getBlockZ() < z+1|| loc.getBlockZ() > plusZ) return false;
        if(loc.getBlockY() < 1) return false;
        return true;
    }
    public boolean isOnPlotInteract(int[] xz, Location loc) {
        WorldData data = PhoenixPlot.getWorldData().get(loc.getWorld().getName());
        int plotSize = data.getPlotSize();
        int x = xz[0]*data.getFullLength();
        int z = xz[1]*data.getFullLength();
        int plusX = x+plotSize;
        int plusZ = z+plotSize;
        if(loc.getBlockX() < x+1 || loc.getBlockX() > plusX) return false;
        if(loc.getBlockZ() < z+1|| loc.getBlockZ() > plusZ) return false;
        return true;
    }
}
