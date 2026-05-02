package com.opsummer.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /givesummer <helmet|chestplate|leggings|boots|all> [player]
 */
public class GiveCommand implements CommandExecutor, TabCompleter {

    private final OPSummerArmor plugin;
    private static final List<String> PIECES = Arrays.asList("helmet", "chestplate", "leggings", "boots", "all");

    public GiveCommand(OPSummerArmor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("opsummer.give")) {
            sender.sendMessage(Component.text("You don't have permission to use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /givesummer <helmet|chestplate|leggings|boots|all> [player]").color(NamedTextColor.YELLOW));
            return true;
        }

        // Determine target player
        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Player '" + args[1] + "' not found or offline.").color(NamedTextColor.RED));
                return true;
            }
        } else if (sender instanceof Player p) {
            target = p;
        } else {
            sender.sendMessage(Component.text("Console must specify a player: /givesummer <piece> <player>").color(NamedTextColor.RED));
            return true;
        }

        ArmorManager am = plugin.getArmorManager();
        String piece = args[0].toLowerCase();

        switch (piece) {
            case "helmet"     -> give(target, am.getHelmet(),     sender);
            case "chestplate" -> give(target, am.getChestplate(), sender);
            case "leggings"   -> give(target, am.getLeggings(),   sender);
            case "boots"      -> give(target, am.getBoots(),      sender);
            case "all" -> {
                give(target, am.getHelmet(),     null);
                give(target, am.getChestplate(), null);
                give(target, am.getLeggings(),   null);
                give(target, am.getBoots(),      null);
                sender.sendMessage(Component.text("Gave the full OP Summer set to " + target.getName() + "!").color(NamedTextColor.GREEN));
                if (!sender.equals(target)) {
                    target.sendMessage(Component.text("You received the full OP Summer armor set!").color(NamedTextColor.GOLD));
                }
            }
            default -> sender.sendMessage(Component.text("Unknown piece '" + piece + "'. Use: helmet, chestplate, leggings, boots, all").color(NamedTextColor.RED));
        }

        return true;
    }

    private void give(Player target, ItemStack item, CommandSender notifySender) {
        target.getInventory().addItem(item);
        if (notifySender != null) {
            String itemName = item.getItemMeta().displayName() != null
                ? net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                      .serialize(item.getItemMeta().displayName())
                : item.getType().name();
            notifySender.sendMessage(Component.text("Gave " + itemName + " to " + target.getName() + "!").color(NamedTextColor.GREEN));
            if (!notifySender.equals(target)) {
                target.sendMessage(Component.text("You received: " + itemName + "!").color(NamedTextColor.GOLD));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return PIECES.stream()
                .filter(p -> p.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        return List.of();
    }
}
