package dogged.koth.listeners;

import dogged.koth.Koth;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static dogged.koth.listeners.KothSettings.menuPlayers;

public class ChatListener implements Listener {

    Koth plugin;

    public ChatListener(Koth p) {
        plugin = p;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        TextComponent message_tc = (TextComponent) e.message();
        String message = message_tc.content();

        if (message.toLowerCase().contains("cancel")) {
            if (!menuPlayers.containsKey(p)) return;
            menuPlayers.remove(p);
            p.sendMessage("§dCancelled");
            e.setCancelled(true);
            return;
        }

        if (!menuPlayers.containsKey(p)) return;

        if (menuPlayers.get(p).equals("clock")) {
            try {
                Double.parseDouble(message);
            } catch (NumberFormatException exception) {
                p.sendMessage("§dPlease only enter numbers");
                e.setCancelled(true);
                return;
            }

            plugin.getConfig().set("time_interval", message);
            p.sendMessage("§dTime interval set to " + message + "h");
        } else if (menuPlayers.get(p).equals("stayDuration")) {
            try {
                Double.parseDouble(message);
            } catch (NumberFormatException exception) {
                p.sendMessage("§dPlease only enter numbers");
                e.setCancelled(true);
                return;
            }

            if (message.contains(".")) plugin.getConfig().set("stay_duration", message.substring(0, message.indexOf(".")));
            else plugin.getConfig().set("stay_duration", message);
            p.sendMessage("§dStay duration set to " + message + "s");
        } else if (menuPlayers.get(p).equals("command")) {
            plugin.getConfig().set("command", message);
            p.sendMessage("§dCommand set to \"" + message + "\"");
        } else if (menuPlayers.get(p).equals("startMessage")) {
            plugin.getConfig().set("koth_start_message", message);
            p.sendMessage("§dKoth start message set to \"" + message.replace("&", "§") + "§d\"");
        } else if (menuPlayers.get(p).equals("winMessage")) {
            plugin.getConfig().set("koth_win_message", message);
            p.sendMessage("§dKoth win message set to \"" + message.replace("&", "§") + "§d\"");
        } else if (menuPlayers.get(p).equals("winningMessage")) {
            plugin.getConfig().set("koth_winning_message", message);
            p.sendMessage("§dKoth winning message set to \"" + message.replace("&", "§") + "§d\"");
        } else if (menuPlayers.get(p).equals("emptyMessage")) {
            plugin.getConfig().set("koth_empty_message", message);
            p.sendMessage("§dKoth empty message set to \"" + message.replace("&", "§") + "§d\"");
        }

        menuPlayers.remove(p);
        e.setCancelled(true);
        plugin.saveConfig();
    }
}
