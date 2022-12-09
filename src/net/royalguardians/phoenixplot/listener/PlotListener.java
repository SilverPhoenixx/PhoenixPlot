package net.royalguardians.phoenixplot.listener;

import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import net.royalguardians.phoenixplot.world.WorldAPI;
import net.royalguardians.phoenixplot.world.WorldData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlotListener implements Listener {

        @EventHandler
        public void onFade(BlockFadeEvent e) {
            e.setCancelled(true);
        }

        @EventHandler
        public void onUpdate(BlockPhysicsEvent e) {
            e.setCancelled(true);
        }

        @EventHandler
        public void onFloat(BlockFromToEvent e) {
            e.setCancelled(true);
        }
          @EventHandler
          public void onBreak(BlockBreakEvent e) {
            Player p = e.getPlayer();
            if(p.hasPermission("PhoenixPlot.Break")) return;
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getBlock().getX() < 0 ? e.getBlock().getX()/worlddata.getFullLength()-1 : e.getBlock().getX()/worlddata.getFullLength();
            int z = e.getBlock().getZ() < 0 ? e.getBlock().getZ()/worlddata.getFullLength()-1 : e.getBlock().getZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlot(xz, e.getBlock().getLocation())) e.setCancelled(true);
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent e) {
            Player p = e.getPlayer();
            if(p.hasPermission("PhoenixPlot.Place")) return;
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getBlock().getX()< 0 ? e.getBlock().getX()/worlddata.getFullLength()-1 : e.getBlock().getX()/worlddata.getFullLength();
            int z = e.getBlock().getZ() < 0 ? e.getBlock().getZ()/worlddata.getFullLength()-1 : e.getBlock().getZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlot(xz, e.getBlock().getLocation())) e.setCancelled(true);
        }

        @EventHandler
        public void onBucketEmptyEvent(PlayerBucketEmptyEvent e) {
            Player p = e.getPlayer();
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if (plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = p.getLocation().getBlockX() < 0 ? p.getLocation().getBlockX() / worlddata.getFullLength() - 1 : p.getLocation().getBlockX() / worlddata.getFullLength();
            int z = p.getLocation().getBlockZ() < 0 ? p.getLocation().getBlockZ() / worlddata.getFullLength() - 1 : p.getLocation().getBlockZ() / worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if (xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if (!plotPlayer.isOnPlot(xz, p.getLocation())) {
                e.setCancelled(true);
                return;
            }

            int xB = e.getBlockClicked().getX() < 0 ? e.getBlockClicked().getX() / worlddata.getFullLength() - 1 : e.getBlockClicked().getX() / worlddata.getFullLength();
            int zB = e.getBlockClicked().getZ() < 0 ? e.getBlockClicked().getZ() / worlddata.getFullLength() - 1 : e.getBlockClicked().getZ() / worlddata.getFullLength();
            int[] xzB = plotPlayer.getPlots().get(p.getWorld().getName());
            if (xzB == null || !(xzB[0] == xB && xzB[1] == zB)) {
                e.setCancelled(true);
                return;
            }
            if (!plotPlayer.isOnPlot(xz, p.getLocation())) {
                e.setCancelled(true);
                return;
            }
        }

        @EventHandler
        public void onBucketFillEvent(PlayerBucketFillEvent e) {
            Player p = e.getPlayer();
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getBlockClicked().getX() < 0 ? e.getBlockClicked().getX()/worlddata.getFullLength()-1 : e.getBlockClicked().getX()/worlddata.getFullLength();
            int z = e.getBlockClicked().getZ() < 0 ? e.getBlockClicked().getZ()/worlddata.getFullLength()-1 : e.getBlockClicked().getZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlot(xz, e.getBlockClicked().getLocation())) e.setCancelled(true);
        }

        @EventHandler
        public void onBedEnter(PlayerBedEnterEvent e) {
            Player p = e.getPlayer();
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getBed().getLocation().getBlockX() < 0 ? e.getBed().getLocation().getBlockX()/worlddata.getFullLength()-1 : e.getBed().getLocation().getBlockX()/worlddata.getFullLength();
            int z = e.getBed().getLocation().getBlockZ() < 0 ? e.getBed().getLocation().getBlockZ()/worlddata.getFullLength()-1 : e.getBed().getLocation().getBlockZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlot(xz, e.getBed().getLocation())) e.setCancelled(true);
        }

        @EventHandler
        public void onSpawn(PlayerSpawnLocationEvent event) {
            if(event.getSpawnLocation().getWorld() == null) {
                if (PhoenixPlot.getSpawn() == null) {
                    WorldAPI.loadWorld(PhoenixPlot.getSpawnWorldname());
                    PhoenixPlot.getConfigFile().loadSpawn();
                }
                if (!event.getPlayer().teleport(PhoenixPlot.getSpawn())) {
                    WorldAPI.loadWorld(PhoenixPlot.getSpawnWorldname());
                    event.getPlayer().teleport(PhoenixPlot.getSpawn());
                }
            }
        }
        @EventHandler
        public void onInteractEvent(PlayerInteractEvent e) {
            if(e.getClickedBlock() == null) return;
            if(e.getClickedBlock().getType() == Material.AIR) return;
            if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getPlayer().hasPermission("PhoenixPlot.Break")) {
                    return;
                }
            } else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getPlayer().hasPermission("PhoenixPlot.Place")) {
                    return;
                }
            }
            Player p = e.getPlayer();
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getClickedBlock().getX() < 0 ? e.getClickedBlock().getX()/worlddata.getFullLength()-1 : e.getClickedBlock().getX()/worlddata.getFullLength();
            int z = e.getClickedBlock().getZ() < 0 ? e.getClickedBlock().getZ()/worlddata.getFullLength()-1 : e.getClickedBlock().getZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlotInteract(xz, e.getClickedBlock().getLocation())) e.setCancelled(true);
        }

        @EventHandler
        public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
            Player p = e.getPlayer();
            PlotPlayer plotPlayer = PhoenixPlot.getPlotPlayer().get(e.getPlayer());
            WorldData worlddata = PhoenixPlot.getWorldData().get(e.getPlayer().getWorld().getName());
            if(plotPlayer == null || worlddata == null) {
                e.setCancelled(true);
                return;
            }
            int x = e.getRightClicked().getLocation().getBlockX() < 0 ? e.getRightClicked().getLocation().getBlockX()/worlddata.getFullLength()-1 : e.getRightClicked().getLocation().getBlockX()/worlddata.getFullLength();
            int z = e.getRightClicked().getLocation().getBlockZ() < 0 ? e.getRightClicked().getLocation().getBlockZ()/worlddata.getFullLength()-1 : e.getRightClicked().getLocation().getBlockZ()/worlddata.getFullLength();
            int[] xz = plotPlayer.getPlots().get(p.getWorld().getName());
            if(xz == null || !(xz[0] == x && xz[1] == z)) {
                e.setCancelled(true);
                return;
            }
            if(!plotPlayer.isOnPlot(xz, e.getRightClicked().getLocation())) e.setCancelled(true);
            }

        @EventHandler
        public void dropItem(PlayerDropItemEvent e) {
            e.setCancelled(true);
        }

    @EventHandler
    public void collectItem(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }
}
