package dogged.koth.listeners;

import dogged.koth.Koth;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KothSettings implements Listener {
    public static Map<Player, String> menuPlayers = new HashMap<>();

    Koth plugin;

    public KothSettings(Koth p) {
        plugin = p;
    }

    @EventHandler
    public void openSettings(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!(e.getAction() == (Action.RIGHT_CLICK_BLOCK) || e.getAction() == (Action.RIGHT_CLICK_AIR))) return;
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§d§lKoth Wand")) return;
        if (menuPlayers.containsKey(p) && menuPlayers.get(p).contains("wand")) return;

        Inventory settings = Bukkit.createInventory(p, 18, Component.text("§d§lKoth Settings"));

        ItemStack kothArena = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta kothArenaMeta = kothArena.getItemMeta();
        kothArenaMeta.displayName(Component.text("§bSet Arena Borders"));
        kothArena.setItemMeta(kothArenaMeta);

        if (plugin.getConfig().get("time_interval") == null) plugin.getConfig().set("time_interval", "1.0");
        if (plugin.getConfig().get("stay_duration") == null) plugin.getConfig().set("stay_duration", "15");
        if (plugin.getConfig().get("command") == null) plugin.getConfig().set("command", "");
        if (plugin.getConfig().get("koth_start_message") == null) plugin.getConfig().set("koth_start_message", "&c&lKOTH Has Started!");
        if (plugin.getConfig().get("koth_win_message") == null) plugin.getConfig().set("koth_win_message", "&d&l{p} won the KOTH!");
        if (plugin.getConfig().get("koth_winning_message") == null) plugin.getConfig().set("koth_winning_message", "&d{p} is winning the koth! [{time_left}]");
        if (plugin.getConfig().get("koth_empty_message") == null) plugin.getConfig().set("koth_empty_message", "&dNo one is winning the koth! Get to the top of the hill!");


        ItemStack timing = new ItemStack(Material.CLOCK);
        ItemMeta timingMeta = timing.getItemMeta();
        timingMeta.displayName(Component.text("§6Time Interval"));
        timingMeta.lore(Collections.singletonList(Component.text("§7Current time interval: " + plugin.getConfig().getString("time_interval") + "h")));
        timing.setItemMeta(timingMeta);

        ItemStack stayDuration = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta stayDurationMeta = stayDuration.getItemMeta();
        stayDurationMeta.displayName(Component.text("§bStay Duration"));
        stayDurationMeta.lore(Arrays.asList(
                Component.text("§7This is the amount of time a player must be in the koth for to win"),
                Component.text("§7Current Stay Duration: " + plugin.getConfig().getString("stay_duration") + "s")));
        stayDuration.setItemMeta(stayDurationMeta);

        ItemStack command = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta commandMeta = command.getItemMeta();
        commandMeta.displayName(Component.text("§eCommand"));
        commandMeta.lore(Arrays.asList(
                Component.text("§7This command will be run every time a player wins the koth"),
                Component.text("§7Current Command: " + plugin.getConfig().getString("command")),
                Component.text("§7Click to change the command")));
        command.setItemMeta(commandMeta);

        ItemStack kothStartMessage = new ItemStack(Material.PAPER);
        ItemMeta kothStartMessageMeta = kothStartMessage.getItemMeta();
        kothStartMessageMeta.displayName(Component.text("§fKoth Start Message"));
        kothStartMessageMeta.lore(Collections.singletonList(Component.text("§7Current koth start message: " + plugin.getConfig().getString("koth_start_message"))));
        kothStartMessage.setItemMeta(kothStartMessageMeta);

        ItemStack kothWinMessage = new ItemStack(Material.PAPER);
        ItemMeta kothWinMessageMeta = kothWinMessage.getItemMeta();
        kothWinMessageMeta.displayName(Component.text("§fKoth Win Message"));
        kothWinMessageMeta.lore(Collections.singletonList(Component.text("§7Current koth win message: " + plugin.getConfig().getString("koth_win_message"))));
        kothWinMessage.setItemMeta(kothWinMessageMeta);

        ItemStack kothWinningMessage = new ItemStack(Material.PAPER);
        ItemMeta kothWinningMessageMeta = kothWinningMessage.getItemMeta();
        kothWinningMessageMeta.displayName(Component.text("§fKoth Winning Message"));
        kothWinningMessageMeta.lore(Collections.singletonList(Component.text("§7Current koth winning message: " + plugin.getConfig().getString("koth_winning_message"))));
        kothWinningMessage.setItemMeta(kothWinningMessageMeta);

        ItemStack kothEmptyMessage = new ItemStack(Material.PAPER);
        ItemMeta kothEmptyMessageMeta = kothEmptyMessage.getItemMeta();
        kothEmptyMessageMeta.displayName(Component.text("§fKoth Empty Message"));
        kothEmptyMessageMeta.lore(Collections.singletonList(Component.text("§7Current koth empty message: " + plugin.getConfig().getString("koth_empty_message"))));
        kothEmptyMessage.setItemMeta(kothEmptyMessageMeta);

        settings.addItem(kothArena, timing, stayDuration, command, kothStartMessage, kothWinMessage, kothWinningMessage, kothEmptyMessage);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1, 0);
        p.openInventory(settings);
        plugin.saveConfig();
    }

    @EventHandler
    public void onSettingsClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals("§d§lKoth Settings")) {
            if (e.getCurrentItem() == null) return;

            if (!plugin.getStopped()) {
                p.sendMessage("§cYou cannot perform this action while the koth is running! Use /stopkoth to stop the koth");
                p.closeInventory();
                e.setCancelled(true);
                return;
            }

            switch (e.getCurrentItem().getType()) {
                case DIAMOND_BLOCK:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "wand 1");
                    else menuPlayers.put(p, "wand 1");
                    p.sendMessage("§dSelect the first corner");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case CLOCK:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "clock");
                    else menuPlayers.put(p, "clock");
                    p.sendMessage("§dEnter how often you want the koth to run");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case DIAMOND_SWORD:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "stayDuration");
                    else menuPlayers.put(p, "stayDuration");
                    p.sendMessage("§dEnter how long you want a player to stay in the koth for to win");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case COMMAND_BLOCK:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "command");
                    else menuPlayers.put(p, "command");
                    p.sendMessage("§dEnter the command you want to run after a player wins the koth");
                    p.sendMessage("§dUse {p} for the player who won the koth");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
            }

            switch (e.getSlot()) {
                case 4:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "startMessage");
                    else menuPlayers.put(p, "startMessage");
                    p.sendMessage("§dEnter the new koth start message");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case 5:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "winMessage");
                    else menuPlayers.put(p, "winMessage");
                    p.sendMessage("§dEnter the new koth win message");
                    p.sendMessage("§d{p} for the name of the winning player, {time_left} for the time left that the winning player must stay in the koth for");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case 6:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "winningMessage");
                    else menuPlayers.put(p, "winningMessage");
                    p.sendMessage("§dEnter the new koth winning message");
                    p.sendMessage("§d{p} for the name of the winning player, {time_left} for the time left that the winning player must stay in the koth for");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
                case 7:
                    if (menuPlayers.containsKey(p)) menuPlayers.replace(p, "emptyMessage");
                    else menuPlayers.put(p, "emptyMessage");
                    p.sendMessage("§dEnter the new koth empty message");
                    p.sendMessage("§dType \"cancel\" to cancel");
                    break;
            }

            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1, 0);
            e.setCancelled(true);
            p.closeInventory();
        }
    }
}
