package moe.plushie.dakimakuramod.common.items.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockDakimakura extends ModItemBlock {

    public static final String TAG_DAKI_NAME = "dakiName";
    public static final String TAG_DAKI_DIR_NAME = "dakiDirName";
    public static final String TAG_DAKI_PACK_NAME = "dakiPackName";
    
    public ItemBlockDakimakura(Block block) {
        super(block);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        ArrayList<Daki> dakiList = DakimakuraMod.dakimakuraManager.getDakimakuraList();
        for (int i = 0; i < dakiList.size(); i++) {
            ItemStack itemStack = new ItemStack(item, 1, 0);
            itemStack.setTagCompound(new NBTTagCompound());
            Daki daki = dakiList.get(i);
            itemStack.getTagCompound().setString(TAG_DAKI_NAME, daki.getRomajiName());
            itemStack.getTagCompound().setString(TAG_DAKI_PACK_NAME, daki.getPackDirectoryName());
            itemStack.getTagCompound().setString(TAG_DAKI_DIR_NAME, daki.getDakiDirectoryName());
            list.add(itemStack);
        }
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (super.onItemUse(itemStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ)) {
            int rot = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            ForgeDirection dir = ((BlockDakimakura)field_150939_a).getRotation(rot);
            
            int offX = x;
            int offY = y;
            int offZ = z;
            ForgeDirection offset1 = ForgeDirection.getOrientation(side);
            offX += offset1.offsetX;
            offY += offset1.offsetY;
            offZ += offset1.offsetZ;
            super.placeBlockAt(itemStack, entityPlayer, world, offX, offY, offZ, side, hitX, hitY, hitZ, rot);
            world.markBlockForUpdate(offX, offY, offZ);
            
            offX += dir.offsetX;
            offY += dir.offsetY;
            offZ += dir.offsetZ;
            
            super.placeBlockAt(itemStack, entityPlayer, world, offX, offY, offZ, side, hitX, hitY, hitZ, rot + 4);
            world.markBlockForUpdate(offX, offY, offZ);
        }
        return true;
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, field_150939_a, metadata, 2)) {
            return false;
        }
        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(itemStack, player, list, par4);
        if (itemStack.hasTagCompound()) {
            if (itemStack.getTagCompound().hasKey(TAG_DAKI_NAME, NBT.TAG_STRING)) {
                list.add(itemStack.getTagCompound().getString(TAG_DAKI_NAME));
            }
        } else {
            list.add("Blank");
        }
    }
}
