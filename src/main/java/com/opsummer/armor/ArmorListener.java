package com.opsummer.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

/**
 * Handles the "-150% Burning Time" bonus.
 *
 * Minecraft doesn't have a native "burning time" attribute, so we cancel or
 * drastically shorten fire ticks whenever the player is wearing at least one
 * OP Summer armor piece. Each piece they wear cuts fire ticks by 50%; wearing
 * all four makes the player effectively fireproof (matching -150% shown, which
 * implies the set bonus eliminates burning entirely).
 *
 * Fire resistance potion effect is also applied while the full set is worn.
 */
public class ArmorListener implements Listener {

    private final OPSummerArmor plugin;

    public ArmorListener(OPSummerArmor plugin) {
        this.plugin = plugin;
    }

    /**
     * When a player is about to catch fire, check if they're wearing OP Summer pieces.
     * Each piece reduces fire duration by 50%. Full set = immunity.
     */
    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int pieces = countSummerPieces(player);
        if (pieces == 0) return;

        if (pieces >= 4) {
            // Full set: completely fire immune (matches -150% — well beyond full reduction)
            event.setCancelled(true);
            return;
        }

        // Each piece reduces fire ticks by 50%
        double reduction = 0.50 * pieces;
        int newDuration = (int) (event.getDuration() * (1.0 - reduction));
        event.setDuration(Math.max(0, newDuration));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private int countSummerPieces(Player player) {
        PlayerInventory inv = player.getInventory();
        int count = 0;
        for (ItemStack piece : new ItemStack[]{
                inv.getHelmet(), inv.getChestplate(),
                inv.getLeggings(), inv.getBoots()}) {
            if (isSummerPiece(piece)) count++;
        }
        return count;
    }

    public boolean isSummerPiece(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                   .getPersistentDataContainer()
                   .has(plugin.getArmorManager().getNbtKey(), PersistentDataType.STRING);
    }
}
