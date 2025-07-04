package dogged.koth.listeners;

import dogged.koth.Koth;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static dogged.koth.listeners.KothSettings.menuPlayers;

public class WandListener implements Listener {

    Koth plugin;

    public WandListener(Koth p) {
        plugin = p;
    }

    @EventHandler
    public void onWandClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!(e.getAction() == (Action.LEFT_CLICK_BLOCK))) return;
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§d§lKoth Wand")) return;
        if (!menuPlayers.containsKey(p)) return;
        if (!menuPlayers.get(p).contains("wand")) return;
        if (e.getClickedBlock() == null) return;

        Location loc = e.getClickedBlock().getLocation();

        if (menuPlayers.get(p).contains("1")) {
            plugin.getConfig().set("first_corner", loc);
            menuPlayers.replace(p, "wand 2");
            p.sendMessage("§dSelect the second corner");
        } else {
            plugin.getConfig().set("second_corner", loc.add(0, 1,0));
            p.sendMessage("§dKoth area set successfully");
            menuPlayers.remove(p);
        }

        e.setCancelled(true);
        plugin.saveConfig();
    }
}
