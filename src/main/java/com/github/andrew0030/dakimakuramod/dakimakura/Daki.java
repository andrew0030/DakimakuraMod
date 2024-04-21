package com.github.andrew0030.dakimakuramod.dakimakura;

import net.minecraft.util.StringUtil;

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

    public String getDisplayName()
    {
        if (!StringUtil.isNullOrEmpty(name))
            return name;
        int lastSlashIndex = dakiDirectoryName.lastIndexOf('/');
        if (lastSlashIndex != -1)
            return dakiDirectoryName.substring(lastSlashIndex + 1);
        return dakiDirectoryName;
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

//    public void addInformation(ItemStack itemStack, List list)
//    {
//        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
//        IDakiPack dakiPack = dakiManager.getDakiPack(packDirectoryName);
//        if (dakiPack != null) {
//            int index =  dakiManager.getDakiIndexInPack(this) + 1;
//            int total = dakiPack.getDakiCount();
//            String textPack = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.pack";
//            String textName = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.name";
//            list.add(I18n.format(textPack, dakiPack.getName(), index, total));
//            list.add(I18n.format(textName, getDisplayName()));
//        }
//        if (!StringUtils.isNullOrEmpty(getFlavourText())) {
//            String textFlavour = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.flavour";
//            list.add(I18n.format(textFlavour, getFlavourText()));
//        }
//    }
}