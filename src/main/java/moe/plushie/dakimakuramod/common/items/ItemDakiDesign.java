package moe.plushie.dakimakuramod.common.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemDakiDesign extends AbstractModItem {
    
    @SideOnly(Side.CLIENT)
    private IIcon unlockedIcon;
    
    public ItemDakiDesign() {
        super("dakiDesign");
        setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        ArrayList<Daki> dakiList = DakimakuraMod.getProxy().getDakimakuraManager().getDakiList();
        for (int i = 0; i < dakiList.size(); i++) {
            ItemStack itemStack = new ItemStack(item, 1, 0);
            itemStack.setTagCompound(new NBTTagCompound());
            Daki daki = dakiList.get(i);
            DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
            list.add(itemStack);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        return getIcon(itemStack, 0);
    }
    
    @Override
    public IIcon getIcon(ItemStack itemStack, int pass) {
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (daki == null) {
            return itemIcon;
        } else {
            return unlockedIcon;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibModInfo.ID + ":" + "daki-design");
        unlockedIcon = register.registerIcon(LibModInfo.ID + ":" + "daki-design-unlock");
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (world.isRemote) {
            return itemStack;
        }
        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        if (dakiList.isEmpty()) {
            return itemStack;
        }
        int ranValue = world.rand.nextInt(dakiList.size());
        Daki daki = dakiList.get(ranValue);
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
        return itemStack;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        super.addInformation(itemStack, player, list, advancedItemTooltips);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (daki == null) {
            addTooltip(itemStack, player, list, advancedItemTooltips, "unlock");
        } else {
            addTooltip(itemStack, player, list, advancedItemTooltips, "usage");
            if (advancedItemTooltips) {
                list.add("PackDirectory: " + daki.getPackDirectoryName());
                list.add("DakiDirectory: " + daki.getDakiDirectoryName());
                list.add("Name: " + daki.getName());
                list.add("FlavourText: " + daki.getFlavourText());
            } else {
                String textPack = StatCollector.translateToLocal(ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.pack");
                String textName = StatCollector.translateToLocal(ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.name");
                list.add(StatCollector.translateToLocalFormatted(textPack, daki.getPackDirectoryName()));
                list.add(StatCollector.translateToLocalFormatted(textName, daki.getDisplayName()));
                if (!StringUtils.isNullOrEmpty(daki.getFlavourText())) {
                    String textFlavour = StatCollector.translateToLocal(ModBlocks.blockDakimakura.getUnlocalizedName() + ".tooltip.flavour");
                    list.add(StatCollector.translateToLocalFormatted(textFlavour, daki.getFlavourText()));
                }
            }
        }
    }
    
    private void addTooltip(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips, String tooltipName) {
        String unlocalized = itemStack.getUnlocalizedName() + ".tooltip." + tooltipName;
        String localized = StatCollector.translateToLocal(unlocalized);
        if (!unlocalized.equals(localized)) {
            if (localized.contains("%n")) {
                String[] split = localized.split("%n");
                for (int i = 0; i < split.length; i++) {
                    list.add(split[i]);
                }
            } else {
                list.add(localized);
            }
        }
    }
}
