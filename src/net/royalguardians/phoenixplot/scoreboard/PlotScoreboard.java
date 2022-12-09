package net.royalguardians.phoenixplot.scoreboard;

import net.minecraft.server.v1_8_R3.*;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PlotScoreboard {

    public static void sendScoreboard(PlotPlayer plotplayer, Player p) {
        net.minecraft.server.v1_8_R3.Scoreboard board = plotplayer.getBoard();
        ScoreboardObjective obj = plotplayer.getObj();
        obj.setDisplayName("§3Anti§bPlot");
        PacketPlayOutScoreboardObjective create = new PacketPlayOutScoreboardObjective(obj, 0);
        sendPacket(p, create);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);
        sendPacket(p, display);

        ScoreboardScore s0 = new ScoreboardScore(board, obj, "§7» §3Rating");
        s0.setScore(11);
        ScoreboardScore s1 = new ScoreboardScore(board, obj, plotplayer.getRating(p.getWorld()));
        s1.setScore(10);
        ScoreboardScore s2 = new ScoreboardScore(board, obj, "§e");
        s2.setScore(9);
        ScoreboardScore s3 = new ScoreboardScore(board, obj, "§7» §3Theme");
        s3.setScore(8);
        ScoreboardScore s4 = new ScoreboardScore(board, obj, "§f§b" + plotplayer.getTheme(p));
        s4.setScore(7);
        ScoreboardScore s5 = new ScoreboardScore(board, obj, "§8 ");
        s5.setScore(6);
        ScoreboardScore s6 = new ScoreboardScore(board, obj, "§7» §3Max. Partner");
        s6.setScore(5);
        ScoreboardScore s7 = new ScoreboardScore(board, obj, "§b" + plotplayer.getPartner(p));
        s7.setScore(4);
        ScoreboardScore s8 = new ScoreboardScore(board, obj, "§9");
        s8.setScore(3);
        ScoreboardScore s9 = new ScoreboardScore(board, obj, "§7» §3Date");
        s9.setScore(2);
        ScoreboardScore s10 = new ScoreboardScore(board, obj, "§a§b" + plotplayer.getDate(p));
        s10.setScore(1);

        PacketPlayOutScoreboardScore ps0 = new PacketPlayOutScoreboardScore(s0);
        PacketPlayOutScoreboardScore ps1 = new PacketPlayOutScoreboardScore(s1);
        PacketPlayOutScoreboardScore ps2 = new PacketPlayOutScoreboardScore(s2);
        PacketPlayOutScoreboardScore ps3 = new PacketPlayOutScoreboardScore(s3);
        PacketPlayOutScoreboardScore ps4 = new PacketPlayOutScoreboardScore(s4);
        PacketPlayOutScoreboardScore ps5 = new PacketPlayOutScoreboardScore(s5);
        PacketPlayOutScoreboardScore ps6 = new PacketPlayOutScoreboardScore(s6);
        PacketPlayOutScoreboardScore ps7 = new PacketPlayOutScoreboardScore(s7);
        PacketPlayOutScoreboardScore ps8 = new PacketPlayOutScoreboardScore(s8);
        PacketPlayOutScoreboardScore ps9 = new PacketPlayOutScoreboardScore(s9);
        PacketPlayOutScoreboardScore ps10 = new PacketPlayOutScoreboardScore(s10);

        sendPacket(p, ps0);
        sendPacket(p, ps1);
        sendPacket(p, ps2);
        sendPacket(p, ps3);
        sendPacket(p, ps4);
        sendPacket(p, ps5);
        sendPacket(p, ps6);
        sendPacket(p, ps7);
        sendPacket(p, ps8);
        sendPacket(p, ps9);
        sendPacket(p, ps10);
        sendTabMessage(p);
        }

    public static void updateRating(PlotPlayer plotplayer, Player p, String oldRating) {
        net.minecraft.server.v1_8_R3.Scoreboard board = plotplayer.getBoard();
        ScoreboardObjective obj = plotplayer.getObj();
        ScoreboardScore s0 = new ScoreboardScore(board, obj, plotplayer.getRating(p.getWorld()));
        s0.setScore(10);
        PacketPlayOutScoreboardScore ps0 = new PacketPlayOutScoreboardScore(s0);
        removeScore(plotplayer, oldRating, 11, p);
        sendPacket(p, ps0);
    }

    public static void updateDate(PlotPlayer plotplayer, Player p, String oldDate) {
        net.minecraft.server.v1_8_R3.Scoreboard board = plotplayer.getBoard();
        ScoreboardObjective obj = plotplayer.getObj();
        ScoreboardScore s10 = new ScoreboardScore(board, obj, "§a§b" + plotplayer.getDate(p));
        s10.setScore(1);
        PacketPlayOutScoreboardScore ps10 = new PacketPlayOutScoreboardScore(s10);
        removeScore(plotplayer, "§a§b" + oldDate, 1, p);
        sendPacket(p, ps10);
    }

    public static void updateTheme(PlotPlayer plotplayer, Player p, String oldTheme) {
        net.minecraft.server.v1_8_R3.Scoreboard board = plotplayer.getBoard();
        ScoreboardObjective obj = plotplayer.getObj();
        ScoreboardScore s7 = new ScoreboardScore(board, obj, "§f§b" + plotplayer.getTheme(p));
        s7.setScore(7);
        PacketPlayOutScoreboardScore ps7 = new PacketPlayOutScoreboardScore(s7);
        removeScore(plotplayer, "§f§b" + oldTheme, 7, p);
        sendPacket(p, ps7);
    }
    public static void updatePartner(PlotPlayer plotplayer, Player p, String oldPartner) {
        net.minecraft.server.v1_8_R3.Scoreboard board = plotplayer.getBoard();
        ScoreboardObjective obj = plotplayer.getObj();
        ScoreboardScore s4 = new ScoreboardScore(board, obj, "§b" + plotplayer.getPartner(p));
        s4.setScore(4);
        PacketPlayOutScoreboardScore ps7 = new PacketPlayOutScoreboardScore(s4);
        removeScore(plotplayer,"§b" + oldPartner,4, p);
        sendPacket(p, ps7);
    }

    public static void removeScore(PlotPlayer plotplayer, String score, int scoreLine, Player p) {
        PacketPlayOutScoreboardScore remove = new PacketPlayOutScoreboardScore(score);
        sendPacket(p, remove);
    }

    public static void sendPacket(Player p, Packet<?> packet) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendTabMessage(Player p){
        String head = "§7» §3Phoenix§bPlot §7«" + "\n" + "§a";
        String foot = "\n" + "§bErbaue das Beste Bauwerk!";
        //  p.setPlayerListHeaderFooter(head, foot);
        ChatMessage header = new ChatMessage(head, new Object[0]);
        ChatMessage footer = new ChatMessage(foot, new Object[0]);
        PacketPlayOutPlayerListHeaderFooter tablist = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = tablist.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(tablist, header);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = tablist.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(tablist, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        CraftPlayer cp = (CraftPlayer) p;
        cp.getHandle().playerConnection.sendPacket(tablist);
    }
}
