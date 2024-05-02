package com.github.andrew0030.dakimakuramod.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;

public class TranslationHelper
{
    public static void getTooltipFromLang(List<Component> tooltip, String langPath)
    {
        Component textComponent = Component.translatable(langPath);
        String text = textComponent.getString();
        if(text.contains("\n")) {
            String[] textLines = text.split("\n");
            for(String line : textLines)
                tooltip.add(Component.literal(line));
        } else {
            tooltip.add(textComponent);
        }
    }

    public static void getToolTipWithArgs(List<Component> tooltip, String langPath, Object... args)
    {
        Component textComponent = Component.translatable(langPath, args);
        String text = textComponent.getString();
        if(text.contains("\n")) {
            String[] textLines = text.split("\n");
            for(String line : textLines)
                tooltip.add(Component.literal(line));
        } else {
            tooltip.add(textComponent);
        }
    }

    public static void addEnchantmentSeparationLine(List<Component> tooltip, ItemStack stack)
    {
        // Renders another "empty" line if the item is enchanted, so the enchantments are easier to read
        if(!EnchantmentHelper.getEnchantments(stack).isEmpty())
            tooltip.add(Component.literal(""));
    }
}