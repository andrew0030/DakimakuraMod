package com.github.andrew0030.dakimakuramod.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationHelper
{
    private static final Pattern FORMATTING_REGEX;

    static
    {
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("(§[");
        for (ChatFormatting formatting : ChatFormatting.values())
            patternBuilder.append(formatting.getChar());
        patternBuilder.append("])");
        FORMATTING_REGEX = Pattern.compile(patternBuilder.toString());
    }

    public static void formatTooltips(List<Component> tooltip)
    {
        // Holds the components which have been split/styled if needed
        List<Component> styledLines = new ArrayList<>();
        for (Component component : tooltip) // We loop over every tooltip entry
        {
            String rawText = component.getString(); // The raw text of the component (also already has %d and %s replaced with given values)
            MutableComponent line = null; // A mutable component so we can append elements with different styles if needed
            // We get a List of Pairs, which will be appended to each other, to achieve multiple styles per component
            List<Pair<List<ChatFormatting>, String>> formattedParts = TranslationHelper.splitFormattedString(rawText);

            for (Pair<List<ChatFormatting>, String> pair : formattedParts)
            {
                String pairText = pair.getSecond();
                ChatFormatting[] styles = pair.getFirst().toArray(new ChatFormatting[0]);

                int lastIndex = 0;
                while (lastIndex < pairText.length())
                {
                    int newIndex = pairText.indexOf("%n", lastIndex);
                    if (newIndex == -1)
                    {
                        String remainingText = pairText.substring(lastIndex);
                        line = TranslationHelper.appendOrInit(line, remainingText, styles);
                        lastIndex = pairText.length();
                    }
                    else
                    {
                        String textBeforeBreak = pairText.substring(lastIndex, newIndex);
                        line = TranslationHelper.appendOrInit(line, textBeforeBreak, styles);
                        styledLines.add(line);
                        line = null;

                        // Handles Styling Codes after a new line
                        lastIndex = newIndex + 2;
                        if (lastIndex < pairText.length() && pairText.charAt(lastIndex) == '§')
                            lastIndex += 2; // Skips over the '§' and the following style code character
                    }
                }
            }

            if (line != null)
                styledLines.add(line);
        }

        tooltip.clear();
        tooltip.addAll(styledLines);
    }

    private static List<Pair<List<ChatFormatting>, String>> splitFormattedString(String text)
    {
        List<Pair<List<ChatFormatting>, String>> resultList = new ArrayList<>();
        List<ChatFormatting> currentFormats = new ArrayList<>();
        Matcher matcher = FORMATTING_REGEX.matcher(text);
        int lastEnd = 0;
        while (matcher.find())
        {
            // Add the portion of the string before the match with the current formats
            if (matcher.start() > lastEnd)
                resultList.add(new Pair<>(new ArrayList<>(currentFormats), text.substring(lastEnd, matcher.start())));

            // Update the current formats based on the match
            ChatFormatting format = ChatFormatting.getByCode(matcher.group().charAt(1));
            if (format != null)
                currentFormats.add(format);
            lastEnd = matcher.end();
        }

        // Add the remainder of the string after the last match
        if (lastEnd < text.length())
            resultList.add(new Pair<>(new ArrayList<>(currentFormats), text.substring(lastEnd)));

        return resultList;
    }

    /** Appends the given values as a component to the end of the line, or initializes it with them. */
    private static MutableComponent appendOrInit(MutableComponent line, String text, ChatFormatting[] styles)
    {
        if (line == null)
            line = Component.literal(text).withStyle(styles);
        else
            line.append(Component.literal(text).withStyle(styles));
        return line;
    }
}