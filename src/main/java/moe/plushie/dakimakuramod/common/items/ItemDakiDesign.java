package moe.plushie.dakimakuramod.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
        super("daki-design");
        setMaxStackSize(16);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            ArrayList<Daki> dakiList = DakimakuraMod.getProxy().getDakimakuraManager().getDakiList();
            for (int i = 0; i < dakiList.size(); i++) {
                ItemStack itemStack = new ItemStack(this, 1, 0);
                itemStack.setTagCompound(new NBTTagCompound());
                Daki daki = dakiList.get(i);
                DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
                items.add(itemStack);
            }
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
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound());
        if (daki != null) {
            addTooltip(stack, tooltip, "usage");
            daki.addInformation(stack, tooltip);
        } else {
            addTooltip(stack, tooltip, "unlock");
        }
    }
    
    private void addTooltip(ItemStack itemStack, List list, String tooltipName) {
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
