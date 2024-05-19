package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.IDakiPack;
import com.github.andrew0030.dakimakuramod.util.TranslationHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.i18nformatter.qual.I18nChecksFormat;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Daki implements Comparable<Daki>
{
    private final String packDirectoryName;
    private final String dakiDirectoryName;

    private String name = "";
    private String author = "";
    private String imageFront = null;
    private String imageBack = null;
    private String flavourText = "";
    private boolean smooth = true;

    public Daki(String packDirectoryName, String dakiDirectoryName)
    {
        this.packDirectoryName = packDirectoryName;
        this.dakiDirectoryName = dakiDirectoryName;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackDirectoryName()
    {
        return this.packDirectoryName;
    }

    public String getDakiDirectoryName()
    {
        return this.dakiDirectoryName;
    }

    public String getImageFront()
    {
        return this.imageFront;
    }

    public void setImageFront(String imageFront)
    {
        this.imageFront = imageFront;
    }

    public String getImageBack()
    {
        return this.imageBack;
    }

    public void setImageBack(String imageBack)
    {
        this.imageBack = imageBack;
    }

    public String getFlavourText()
    {
        return this.flavourText;
    }

    public void setFlavourText(String flavourText)
    {
        this.flavourText = flavourText;
    }

    public boolean isSmooth()
    {
        return this.smooth;
    }

    public void setSmooth(boolean smooth)
    {
        this.smooth = smooth;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dakiDirectoryName == null) ? 0 : dakiDirectoryName.hashCode());
        result = prime * result + ((packDirectoryName == null) ? 0 : packDirectoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Daki other = (Daki) obj;
        return Objects.equals(dakiDirectoryName, other.dakiDirectoryName) &&
                Objects.equals(packDirectoryName, other.packDirectoryName);
    }

    @Override
    public int compareTo(Daki o)
    {
        int result = packDirectoryName.compareTo(o.packDirectoryName) * 1000;
        return result + dakiDirectoryName.compareTo(o.dakiDirectoryName);
    }

    @Override
    public String toString()
    {
        return "Daki [packDirectoryName=" + packDirectoryName + ", dakiDirectoryName=" + dakiDirectoryName + "]";
    }

    public String getDisplayName()
    {
        if (!StringUtil.isNullOrEmpty(this.name))
            return this.name;
        int lastSlashIndex = this.dakiDirectoryName.lastIndexOf('/');
        if (lastSlashIndex != -1)
            return dakiDirectoryName.substring(lastSlashIndex + 1);
        return dakiDirectoryName;
    }

    public void addInformation(ItemStack itemStack, List<Component> tooltip)
    {
        DakiManager dakiManager = DakimakuraMod.getDakimakuraManager();
        IDakiPack dakiPack = dakiManager.getDakiPack(this.packDirectoryName);
        if (dakiPack != null)
        {
            int index =  dakiManager.getDakiIndexInPack(this) + 1;
            int total = dakiPack.getDakiCount();
            tooltip.add(Component.translatable("tooltip.dakimakuramod.dakimakura.pack", dakiPack.getName(), index, total));
            tooltip.add(Component.translatable("tooltip.dakimakuramod.dakimakura.name", this.getDisplayName()));
        }
        // Flavour Text
        if (!StringUtil.isNullOrEmpty(this.getFlavourText()))
            tooltip.add(Component.translatable("tooltip.dakimakuramod.dakimakura.flavour", this.getFlavourText()));
    }
}