package dogged.koth.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class WandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Koth.getwand")){
                p.sendMessage("§cYou do not have permission to use this command!");
                return true;
            }

            ItemStack wand = new ItemStack(Material.GOLDEN_AXE);
            ItemMeta wandMeta = wand.getItemMeta();
            wandMeta.displayName(Component.text("§d§lKoth Wand"));
            wandMeta.lore(Collections.singletonList(Component.text("§7Right click to open the settings gui")));
            wand.setItemMeta(wandMeta);
            p.getInventory().addItem(wand);
        }

        else sender.sendMessage("§cYou must be a player to use this command");
        return true;
    }
}
