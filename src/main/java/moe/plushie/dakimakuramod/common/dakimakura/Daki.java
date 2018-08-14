package moe.plushie.dakimakuramod.common.dakimakura;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;

public class Daki implements Comparable<Daki> {
    
    private final String packDirectoryName;
    private final String dakiDirectoryName;
    
    private String name = "";
    private String author = "";
    private String imageFront = null;
    private String imageBack = null;
    private String flavourText = "";
    
    public Daki(String packDirectoryName, String dakiDirectoryName) {
        this.packDirectoryName = packDirectoryName;
        this.dakiDirectoryName = dakiDirectoryName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPackDirectoryName() {
        return packDirectoryName;
    }
    
    public String getDakiDirectoryName() {
        return dakiDirectoryName;
    }
    
    public String getImageFront() {
        return imageFront;
    }
    
    public void setImageFront(String imageFront) {
        this.imageFront = imageFront;
    }
    
    public String getImageBack() {
        return imageBack;
    }
    
    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }
    
    public String getFlavourText() {
        return flavourText;
    }
    
    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }
    
    public String getDisplayName() {
        if (!StringUtils.isNullOrEmpty(name)) {
            return name;
        } else {
            return dakiDirectoryName;
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dakiDirectoryName == null) ? 0 : dakiDirectoryName.hashCode());
        result = prime * result + ((packDirectoryName == null) ? 0 : packDirectoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Daki other = (Daki) obj;
        if (dakiDirectoryName == null) {
            if (other.dakiDirectoryName != null)
                return false;
        } else if (!dakiDirectoryName.equals(other.dakiDirectoryName))
            return false;
        if (packDirectoryName == null) {
            if (other.packDirectoryName != null)
                return false;
        } else if (!packDirectoryName.equals(other.packDirectoryName))
            return false;
        return true;
    }

    @Override
    public int compareTo(Daki o) {
        int result = packDirectoryName.compareTo(o.packDirectoryName) * 1000;
        return result + dakiDirectoryName.compareTo(o.dakiDirectoryName);
    }

    @Override
    public String toString() {
        return "Daki [packDirectoryName=" + packDirectoryName + ", dakiDirectoryName=" + dakiDirectoryName + "]";
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
        int index = dakiManager.getDakiIndexInPack(this) + 1;
        int total = dakiManager.getNumberOfDakisInPack(packDirectoryName);
        String textPack = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.pack";
        String textName = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.name";
        list.add(StatCollector.translateToLocalFormatted(textPack, getPackDirectoryName(), index, total));
        list.add(StatCollector.translateToLocalFormatted(textName, getDisplayName()));
        if (!StringUtils.isNullOrEmpty(getFlavourText())) {
            String textFlavour = ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.flavour";
            list.add(StatCollector.translateToLocalFormatted(textFlavour, getFlavourText()));
        }
    }
}
