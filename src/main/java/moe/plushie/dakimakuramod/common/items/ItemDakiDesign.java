package moe.plushie.dakimakuramod.common.items;

import java.util.ArrayList;
import java.util.List;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDakiDesign extends AbstractModItem {
    
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
    
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack containerItem = super.getContainerItem(itemStack);
        if (containerItem != null) {
            return itemStack.copy();
        } else {
            return null;
        }
    }
    
    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        if (getContainerItem() != null) {
            Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
            return daki != null;
        } else {
            return false;
        }
    }
    /*
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
        return false;
    }*/
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer playerIn, EnumHand hand) {
        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        if (dakiList.isEmpty()) {
            return new ActionResult(EnumActionResult.PASS, itemStack);
        }
        if (!world.isRemote) {
            int ranValue = world.rand.nextInt(dakiList.size());
            Daki daki = dakiList.get(ranValue);
            if (!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        super.addInformation(itemStack, player, list, advancedItemTooltips);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (daki != null) {
            addTooltip(itemStack, player, list, advancedItemTooltips, "usage");
            daki.addInformation(itemStack, player, list, advancedItemTooltips);
        } else {
            addTooltip(itemStack, player, list, advancedItemTooltips, "unlock");
        }
    }
    
    private void addTooltip(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips, String tooltipName) {
        String unlocalized = itemStack.getUnlocalizedName() + ".tooltip." + tooltipName;
        String localized = I18n.format(unlocalized);
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
