package dogged.koth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Koth.help")) {
                p.sendMessage("§cYou do not have permission to use this command!");
                return true;
            }
        }

        sender.sendMessage("");
        sender.sendMessage("§5§lHelp Menu");
        sender.sendMessage("§d§nCommands");
        sender.sendMessage("§d/kothwand §7§l» §eGrants the wand for opening koth settings and setting up the koth arena");
        sender.sendMessage("§d/startkoth §7§l» §eStarts the koth");
        sender.sendMessage("§d/stopkoth §7§l» §eStops the koth");
        sender.sendMessage("");
        sender.sendMessage("§d§nPermissions");
        sender.sendMessage("§dKoth.getwand §7§l» §ePermission to get the koth wand");
        sender.sendMessage("§dKoth.startcommand §7§l» §ePermission to start the koth");
        sender.sendMessage("§dKoth.stopcommand §7§l» §ePermission to stop the koth");
        sender.sendMessage("§dKoth.help §7§l» §ePermission to see the koth help menu");
        sender.sendMessage("");

        return true;
    }
}
