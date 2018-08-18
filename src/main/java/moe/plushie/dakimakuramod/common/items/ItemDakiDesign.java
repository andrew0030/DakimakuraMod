package moe.plushie.dakimakuramod.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDakiDesign extends AbstractModItem {
    
    public ItemDakiDesign() {
        super("dakiDesign");
        setMaxStackSize(16);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
    	subItems.add(new ItemStack(itemIn, 1, 0));
        ArrayList<Daki> dakiList = DakimakuraMod.getProxy().getDakimakuraManager().getDakiList();
        for (int i = 0; i < dakiList.size(); i++) {
            ItemStack itemStack = new ItemStack(itemIn, 1, 0);
            itemStack.setTagCompound(new NBTTagCompound());
            Daki daki = dakiList.get(i);
            DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
            subItems.add(itemStack);
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	ItemStack itemStack = playerIn.getHeldItem(handIn);
        DakiManager dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
        Daki checkDaki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (checkDaki != null) {
            return new ActionResult(EnumActionResult.PASS, itemStack);
        }
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        if (dakiList.isEmpty()) {
            return new ActionResult(EnumActionResult.PASS, itemStack);
        }
        
        if (!worldIn.isRemote) {
            Random random = new Random(System.nanoTime());
            int ranValue = random.nextInt(dakiList.size());
            Daki daki = dakiList.get(ranValue);
            giveDesignToPlayer(worldIn, playerIn, daki);
            itemStack.shrink(1);
            return new ActionResult(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }
    
    private void giveDesignToPlayer(World world, EntityPlayer entityPlayer, Daki daki) {
        ItemStack itemStack = new ItemStack(ModItems.dakiDesign);
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
        InventoryPlayer inv = entityPlayer.inventory;
        if (!inv.addItemStackToInventory(itemStack)) {
            entityPlayer.entityDropItem(itemStack, entityPlayer.getEyeHeight());
        }
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
