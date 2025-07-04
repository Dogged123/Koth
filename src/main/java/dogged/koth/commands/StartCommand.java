package dogged.koth.commands;

import dogged.koth.Koth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    Koth plugin;

    public StartCommand(Koth p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("Koth.startcommand")) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        if (plugin.getConfig().getLocation("first_corner") == null || plugin.getConfig().getLocation("second_corner") == null) {
            sender.sendMessage("§cThe koth arena is not set up! Please set it up by running /kothwand");
            return true;
        }

        if (plugin.getConfig().getString("command") == null) {
            sender.sendMessage("§cThe command to execute after a player wins the koth is not set up! Please set it up by running /kothwand");
            return true;
        }

        if (plugin.getConfig().get("koth_start_message") == null) plugin.getConfig().set("koth_start_message", "&c&lKOTH Has Started!");
        if (plugin.getConfig().get("koth_win_message") == null) plugin.getConfig().set("koth_win_message", "&d&l{p} won the KOTH!");
        if (plugin.getConfig().get("koth_winning_message") == null) plugin.getConfig().set("koth_winning_message", "&d{p} is winning the koth! [{time_left}]");
        if (plugin.getConfig().get("koth_empty_message") == null) plugin.getConfig().set("koth_empty_message", "&dNo one is winning the koth! Get to the top of the hill!");

        sender.sendMessage("§dKoth Started");
        plugin.stop(true);
        plugin.stop(false);
        return true;
    }
}
