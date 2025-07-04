package dogged.koth.commands;

import dogged.koth.Koth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements CommandExecutor {

    Koth plugin;

    public StopCommand(Koth p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("Koth.stopcommand")) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        sender.sendMessage("§dKoth Stopped");
        plugin.stop(true);
        return true;
    }
}
