package net.royalguardians.phoenixplot.database;

import net.royalguardians.PhoenixID.PhoenixID;
import net.royalguardians.phoenixplot.PhoenixPlot;
import net.royalguardians.phoenixplot.plot.PlotPlayer;
import net.royalguardians.phoenixplot.scoreboard.PlotScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Database {
    public  ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Connection connection = null;
    public  Statement statement = null;
    public  PreparedStatement preparedStatement = null;
    public  String getPlots = "CREATE TABLE IF NOT EXISTS Plots(`ID` int(48), `World` varchar(32),`XZ` int(32));";
    public  String getPartnerPlots = "CREATE TABLE IF NOT EXISTS PartnerPlot(`ID` int(48), `World` varchar(32),`XZ` varchar(64));";
    public  String getPlotRating = "CREATE TABLE IF NOT EXISTS PlotRating(`ID` int(48), `World` varchar(32),`XZ` varchar(32),`Landscape` int(3), `Vegitation` int(3), `Detail` int(3), `Structur` int(3), `Organic` int(3), `Innovation` int(3), `Atmosphäre` int(3));";

    public synchronized void executeUpdate(PreparedStatement statement) {
        this.executorService.execute(() -> {
            try {
                statement.executeUpdate();
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void fetchAsynchronous(String syntax, Consumer<ResultSet> comsumer) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getConnection();
                    preparedStatement = connection.prepareStatement(syntax);
                    comsumer.accept(preparedStatement.executeQuery());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized void registerPlot(int id, String world, String xz) {
        try {
            getConnection();
            preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO Plots(ID, World, XZ) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, world);
            preparedStatement.setString(3, xz);
            executeUpdate(preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public synchronized void registerPartnerPlot(int id, String world, String xz) {
        try {
            getConnection();
            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO PartnerPlot(ID, World, XZ) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, world);
            preparedStatement.setString(3, xz);
            executeUpdate(preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public synchronized void registerPlotRating(int id, String world, String xz, int landscape, int vegitation, int organic, int structur, int detail, int innovation, int atmosphäre) {
        try {
            getConnection();
            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO PlotRating(ID, World, XZ, Landscape, Vegitation, Detail, Structur, Organic, Innovation, Atmosphäre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, world);
            preparedStatement.setString(3, xz);
            preparedStatement.setInt(4, landscape);
            preparedStatement.setInt(5, vegitation);
            preparedStatement.setInt(6, detail);
            preparedStatement.setInt(7, structur);
            preparedStatement.setInt(8, organic);
            preparedStatement.setInt(9, innovation);
            preparedStatement.setInt(10, atmosphäre);
            executeUpdate(preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public synchronized boolean loadPlayer(Player p) {
        Map<String, int[]> playerplots = new HashMap<>();
        Map<String, List<Integer>> partnerList = new HashMap<>();
        Map<String, List<int[]>> partnerplots = new HashMap<>();
        Map<String, int[]> rating = new HashMap<>();
            fetchAsynchronous("SELECT * FROM Plots WHERE ID = '" + PhoenixID.ids.get(p.getUniqueId()).getOrdinal() + "'", new Consumer<ResultSet>() {
                 @Override
                 public void accept(ResultSet resultSet) {
                     try {
                     while (resultSet.next()) {
                         String[] xz = resultSet.getString("XZ").split(":");
                         playerplots.put(resultSet.getString("World"), new int[] { Integer.parseInt(xz[0]), Integer.parseInt(xz[1])});
                     }
                         closeConnection();
                         resultSet.close();
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                 }
             });
        System.out.println("PartnerPlot");
            fetchAsynchronous("SELECT * FROM PartnerPlot WHERE ID = '" + p.getUniqueId().toString() + "'", new Consumer<ResultSet>() {
                @Override
                public void accept(ResultSet resultSet) {
                    try {
                        while (resultSet.next()) {
                            String world = resultSet.getString("World");
                            String[] xz = resultSet.getString("XZ").split(":");
                            if(!partnerplots.containsKey(world)) partnerplots.put(world, new ArrayList<>());
                            partnerplots.get(world).add(new int[] {Integer.parseInt(xz[0], Integer.parseInt(xz[1]))});
                        }
                        closeConnection();
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        System.out.println("PlotRating");
        fetchAsynchronous("SELECT * FROM PlotRating WHERE ID = '" + PhoenixID.ids.get(p.getUniqueId()).getOrdinal() + "'", new Consumer<ResultSet>() {
            @Override
            public void accept(ResultSet resultSet) {
                try {
                    while (resultSet.next()) {
                        String world = resultSet.getString("World");
                        String[] xz = resultSet.getString("XZ").split(":");
                        rating.put(world + ":" + xz[0] + ":" + xz[1], new int[] {
                           resultSet.getInt("Landscape"),
                                resultSet.getInt("Vegitation"),
                                resultSet.getInt("Detail"),
                                resultSet.getInt("Structur"),
                                resultSet.getInt("Organic"),
                                resultSet.getInt("Innovation"),
                                resultSet.getInt("Atmosphäre")
                        });
                    }
                    closeConnection();
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("PartnerPlot");
        for(String world : playerplots.keySet()) {
            fetchAsynchronous("SELECT * FROM PartnerPlot WHERE WORLD = " + world + " AND XZ = " + playerplots.get(world)[0] + ":" + playerplots.get(world)[1], new Consumer<ResultSet>() {
                @Override
                public void accept(ResultSet resultSet) {
                    List<Integer> ints = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            ints.add(resultSet.getInt("ID"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    partnerList.put(world, ints);
                }
            });
        }
            PlotPlayer plotPlayer = new PlotPlayer(playerplots, partnerList, partnerplots, rating);
            PhoenixPlot.getPlotPlayer().put(p, plotPlayer);
            PlotScoreboard.sendScoreboard(plotPlayer, p);
            return true;
    }

    public synchronized Map<String, String> getPlayerPlots(int id) {
            Map<String, String> plots = new HashMap<>();
            System.out.println("getPlayerPlots");
            fetchAsynchronous("SELECT * FROM Plots WHERE ID = " + id, new Consumer<ResultSet>() {
                @Override
                public void accept(ResultSet resultSet) {
                    try {
                        while (resultSet.next()) {
                            plots.put(resultSet.getString("World"), resultSet.getString("XZ"));
                        }
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            closeConnection();
        return plots;
    }
    public synchronized Map<String, String> getPartnerPlots(int id) {
        Map<String, String> plots = new HashMap<>();
        fetchAsynchronous("SELECT * FROM PartnerPlot WHERE ID = " + id, new Consumer<ResultSet>() {
            @Override
            public void accept(ResultSet resultSet) {
                try {
                    while (resultSet.next()) {
                        plots.put(resultSet.getString("World"), resultSet.getString("XZ"));
                    }
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        closeConnection();
        return plots;
    }

    public synchronized void removePlayerPlot(int id, String world, String xz) {
        Bukkit.getScheduler().runTaskAsynchronously(PhoenixPlot.getInstance(), new Runnable(){

            @Override
            public void run() {
                try {
                    getConnection();
                    preparedStatement = connection.prepareStatement("DELETE FROM Plots WHERE ID = ? AND World = ? AND XZ = ?");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, world);
                    preparedStatement.setString(3, xz);
                    executeUpdate(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized void removePartnerPlot(int id, String world, String xz) {
        Bukkit.getScheduler().runTaskAsynchronously(PhoenixPlot.getInstance(), new Runnable(){
            @Override
            public void run() {
                try {
                    getConnection();
                    preparedStatement = connection.prepareStatement("DELETE FROM PartnerPlot WHERE ID = ? AND World = ? AND XZ = ?");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, world);
                    preparedStatement.setString(3, xz);
                    executeUpdate(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createSQL() {
        File world = new File(PhoenixPlot.getInstance().getDataFolder(), "Worlds.db");
        File folder = new File(PhoenixPlot.getInstance().getDataFolder().getPath());
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("FOLDER SUCCESSFUL CREATED");
            } else {
                System.out.println("FOLDER FAILED TO CREATE");
            }
        }
        if (!world.exists()) {
            try {
                world.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + world.getAbsolutePath());
            closeConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("The Connection to the SQLite database is canceled.");
        }
    }

    public synchronized void closeConnection() {
        try {
            if(connection != null) if (!connection.isClosed()) connection.close();
            if(statement != null) statement.close();
            if(preparedStatement != null) preparedStatement.close();
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    public synchronized void getConnection() {
        File dataFolder = new File(PhoenixPlot.getInstance().getDataFolder(), "Worlds.db");
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder.getAbsolutePath());
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("The Connection to the SQLite database is canceled.");
        }
    }

    public void load() {
        try {
            getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(getPlots);
            statement.executeUpdate(getPartnerPlots);
            statement.executeUpdate(getPlotRating);
            closeConnection();
        }
        catch (SQLException e) {
        }
    }
}