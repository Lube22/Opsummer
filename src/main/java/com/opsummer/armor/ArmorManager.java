package com.opsummer.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ArmorManager {

    public static final String ITEM_KEY = "op_summer_piece";
    private final OPSummerArmor plugin;
    private final NamespacedKey nbtKey;
    private static final Color SUMMER_COLOR = Color.fromRGB(0xFF, 0xA0, 0x00);

    public ArmorManager(OPSummerArmor plugin) {
        this.plugin = plugin;
        this.nbtKey = new NamespacedKey(plugin, ITEM_KEY);
    }

    public NamespacedKey getNbtKey() { return nbtKey; }

    public ItemStack getHelmet()     { return build(Material.LEATHER_HELMET,     "OP Summer Helmet",     EquipmentSlotGroup.HEAD); }
    public ItemStack getChestplate() { return build(Material.LEATHER_CHESTPLATE, "OP Summer Chestplate", EquipmentSlotGroup.CHEST); }
    public ItemStack getLeggings()   { return build(Material.LEATHER_LEGGINGS,   "OP Summer Leggings",   EquipmentSlotGroup.LEGS); }
    public ItemStack getBoots()      { return build(Material.LEATHER_BOOTS,      "OP Summer Boots",      EquipmentSlotGroup.FEET); }

    private ItemStack build(Material mat, String name, EquipmentSlotGroup slot) {
        ItemStack item = new ItemStack(mat);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.displayName(Component.text(name)
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true));

        meta.setColor(SUMMER_COLOR);

        List<Component> lore = new ArrayList<>();
        lore.add(comp("Curse of Binding",        NamedTextColor.RED));
        lore.add(comp("Protection X",            NamedTextColor.GRAY));
        lore.add(comp("Blast Protection X",      NamedTextColor.GRAY));
        lore.add(comp("Fire Protection X",       NamedTextColor.GRAY));
        lore.add(comp("Projectile Protection X", NamedTextColor.GRAY));
        lore.add(comp("Unbreaking V",            NamedTextColor.GRAY));
        lore.add(comp("Mending",                 NamedTextColor.GRAY));
        lore.add(comp("Dyed",                    NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
        lore.add(Component.empty());
        lore.add(comp("Information", NamedTextColor.YELLOW));
        lore.add(comp("Chance: 2.222%", NamedTextColor.WHITE));
        lore.add(Component.empty());
        lore.add(comp("When " + slotLabel(slot) + ":", NamedTextColor.WHITE));
        lore.add(comp("+0.01 Speed",      NamedTextColor.BLUE));
        lore.add(comp("+2 Armor",         NamedTextColor.BLUE));
        lore.add(comp("+5 Attack Damage", NamedTextColor.BLUE));
        lore.add(comp("+5 Max Health",    NamedTextColor.BLUE));
        lore.add(Component.empty());
        lore.add(comp("When worn:", NamedTextColor.WHITE));
        lore.add(comp("+1.5 Explosion Knockback Resistance", NamedTextColor.BLUE));
        lore.add(comp("-150% Burning Time",                  NamedTextColor.BLUE));
        meta.lore(lore);

        meta.addEnchant(Enchantment.BINDING_CURSE,         1,  true);
        meta.addEnchant(Enchantment.PROTECTION,            10, true);
        meta.addEnchant(Enchantment.BLAST_PROTECTION,      10, true);
        meta.addEnchant(Enchantment.FIRE_PROTECTION,       10, true);
        meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING,            5,  true);
        meta.addEnchant(Enchantment.MENDING,               1,  true);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DYE);

        String s = slot.toString();

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
            new AttributeModifier(new NamespacedKey(plugin, "summer_speed_" + s), 0.01, Operation.ADD_NUMBER, slot));

        meta.addAttributeModifier(Attribute.GENERIC_ARMOR,
            new AttributeModifier(new NamespacedKey(plugin, "summer_armor_" + s), 2.0, Operation.ADD_NUMBER, slot));

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
            new AttributeModifier(new NamespacedKey(plugin, "summer_damage_" + s), 5.0, Operation.ADD_NUMBER, slot));

        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
            new AttributeModifier(new NamespacedKey(plugin, "summer_health_" + s), 5.0, Operation.ADD_NUMBER, slot));

        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE,
            new AttributeModifier(new NamespacedKey(plugin, "summer_ekr_" + s), 1.5, Operation.ADD_NUMBER, slot));

        meta.getPersistentDataContainer().set(nbtKey, PersistentDataType.STRING, s);

        item.setItemMeta(meta);
        return item;
    }

    private Component comp(String text, NamedTextColor color) {
        return Component.text(text).color(color).decoration(TextDecoration.ITALIC, false);
    }

    private String slotLabel(EquipmentSlotGroup slot) {
        if (slot == EquipmentSlotGroup.HEAD)  return "on Head";
        if (slot == EquipmentSlotGroup.CHEST) return "on Chest";
        if (slot == EquipmentSlotGroup.LEGS)  return "on Legs";
        if (slot == EquipmentSlotGroup.FEET)  return "on Feet";
        return "worn";
    }
}
