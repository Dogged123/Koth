package dogged.koth;

import dogged.koth.commands.HelpCommand;
import dogged.koth.commands.StartCommand;
import dogged.koth.commands.StopCommand;
import dogged.koth.commands.WandCommand;
import dogged.koth.listeners.WandListener;
import dogged.koth.listeners.ChatListener;
import dogged.koth.listeners.KothSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Koth extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("kothwand").setExecutor(new WandCommand());
        getCommand("stopkoth").setExecutor(new StopCommand(this));
        getCommand("startkoth").setExecutor(new StartCommand(this));
        getCommand("help").setExecutor(new HelpCommand());

        getServer().getPluginManager().registerEvents(new KothSettings(this), this);
        getServer().getPluginManager().registerEvents(new WandListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        kothChecker();
        saveDefaultConfig();
    }

    boolean stop = false;
    int kothSchedulerTaskID;
    public void kothScheduler() {
        long timeInterval;

        if (getConfig().getString("time_interval") == null) timeInterval = 60*60*20;
        else timeInterval = (long) (Double.parseDouble(getConfig().getString("time_interval"))*60*60*20);

        new BukkitRunnable() {
            @Override
            public void run() {
                kothSchedulerTaskID = getTaskId();
                if (winningPlayer != null && winningPlayer.isOnline()) {
                    Bukkit.broadcast(Component.text("§d§l" + winningPlayer.getName() + " won the KOTH!"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
                    }
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = getConfig().getString("command").replace("{p}", winningPlayer.getName());
                    Bukkit.dispatchCommand(console, command);
                    stopKothChecker = true;
                }
                if (stop) cancel();
                else {
                    stopKothChecker = false;
                    startTime = System.currentTimeMillis();
                    Bukkit.broadcast(Component.text(getConfig().getString("koth_start_message").replace("&", "§")));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    }
                }
            }
        }.runTaskTimer(this, 0, timeInterval);
    }

    Player winningPlayer;
    long startTime = System.currentTimeMillis();
    boolean stopKothChecker;
    boolean anyPlayers = false;

    public void kothChecker() {
        stopKothChecker = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!stopKothChecker) {
                    anyPlayers = false;
                    Location firstCorner = getConfig().getLocation("first_corner");
                    Location secondCorner = getConfig().getLocation("second_corner");

                    double minX = Math.min(firstCorner.getX(), secondCorner.getX());
                    double minY = Math.min(firstCorner.getY(), secondCorner.getY());
                    double minZ = Math.min(firstCorner.getZ(), secondCorner.getZ());

                    double maxX = Math.max(firstCorner.getX(), secondCorner.getX());
                    double maxY = Math.max(firstCorner.getY(), secondCorner.getY());
                    double maxZ = Math.max(firstCorner.getZ(), secondCorner.getZ());

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Location loc = p.getLocation();

                        if (loc.getX() >= minX && loc.getX() <= maxX && loc.getY() >= minY && loc.getY() <= maxY && loc.getZ() >= minZ && loc.getZ() <= maxZ) {
                            anyPlayers = true;
                            if (winningPlayer != null && winningPlayer.isOnline()) {
                                loc = winningPlayer.getLocation();
                                if (!(loc.getX() >= minX && loc.getX() <= maxX && loc.getY() >= minY && loc.getY() <= maxY && loc.getZ() >= minZ && loc.getZ() <= maxZ)) {
                                    winningPlayer = p;
                                    startTime = System.currentTimeMillis(); // reset the counter every time a new player is winning
                                }
                            } else winningPlayer = p;
                        }

                        long timeLeft = Long.parseLong(getConfig().getString("stay_duration")) - ((System.currentTimeMillis() - startTime) / 1000);
                        if (winningPlayer == null) {
                            p.sendActionBar(Component.text(getConfig().getString("koth_empty_message").replace("&", "§")));
                            startTime = System.currentTimeMillis();
                        } else if (timeLeft > 0) p.sendActionBar(Component.text(getConfig().getString("koth_winning_message").replace("&", "§").replace("{p}", winningPlayer.getName()).replace("{time_left}", String.valueOf(timeLeft))));
                    }

                    if (!anyPlayers) {
                        winningPlayer = null;
                    }

                    if ((System.currentTimeMillis() - startTime) / 1000 >= Long.parseLong(getConfig().getString("stay_duration"))) {
                        Bukkit.broadcast(Component.text(getConfig().getString("koth_win_message").replace("&", "§").replace("{p}", winningPlayer.getName())));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
                        }
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = getConfig().getString("command").replace("{p}", winningPlayer.getName());
                        Bukkit.dispatchCommand(console, command);
                        stopKothChecker = true;
                        winningPlayer = null;
                    }
                } else {
                    startTime = System.currentTimeMillis();
                    winningPlayer = null;
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    public void stop(boolean stopKoth) {
        stop = stopKoth;

        if (stop) {
            Bukkit.getScheduler().cancelTask(kothSchedulerTaskID);
            stopKothChecker = true;
        } else kothScheduler();
    }

    public boolean getStopped() {
        return stopKothChecker;
    }
}
